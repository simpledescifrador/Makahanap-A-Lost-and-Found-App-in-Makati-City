package com.makatizen.makahanap.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.RegisterTokenResponse;
import com.makatizen.makahanap.ui.chat.ChatItemAdapter;
import com.makatizen.makahanap.ui.chat_convo.ChatConvoActivity;
import com.makatizen.makahanap.ui.item_details.ItemDetailsActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RequestCodes;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFCM";

    @Inject
    DataManager mDataManager;

    private CompositeDisposable bag = new CompositeDisposable();

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bag.dispose();
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            /*Email Verification*/
            if (remoteMessage.getData().containsKey("email")) {
                String email = remoteMessage.getData().get("email");
                String code = remoteMessage.getData().get("code");
                Intent intent = new Intent("com.makatizen.makahanap.email_verification");
                intent.putExtra("email", email);
                intent.putExtra("code", code);

                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);

                localBroadcastManager.sendBroadcast(intent);
                return;
            }

            String currentAccountId = String.valueOf(mDataManager.getCurrentAccount().getId());

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (remoteMessage.getData().containsKey("message_id")) {
                if (!remoteMessage.getData().get("message_id").isEmpty()) {
                    /* New Message */
                    String receiverId = remoteMessage.getData().get("receiver_id");
                    if (currentAccountId.equals(receiverId)) {

                        /*  Prepare the data needed */
                        String messageId = remoteMessage.getData().get("message_id");
                        String senderId = remoteMessage.getData().get("sender_id");
                        String messageTime = remoteMessage.getData().get("message_time");
                        String message = remoteMessage.getData().get("message");
                        String chatRoomId = remoteMessage.getData().get("chat_room_id");

                        Intent intent = new Intent("com.makatizen.makahanap.messages");
                        intent.putExtra("message_id", messageId);
                        intent.putExtra("sender_id", senderId);
                        intent.putExtra("receiver_id", receiverId);
                        intent.putExtra("message_time", messageTime);
                        intent.putExtra("message", message);
                        intent.putExtra("chat_room_id", chatRoomId);

                        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);

                        localBroadcastManager.sendBroadcast(intent);
                    }
                }
            }

            String accountId = remoteMessage.getData().get("account_id");
            if (currentAccountId.equals(accountId)) {

                int id = Integer.parseInt(remoteMessage.getData().get("id"));
                String title = remoteMessage.getData().get("title");
                String content = remoteMessage.getData().get("content");
                String imageUrl = remoteMessage.getData().get("image_url");
                String createdAt = remoteMessage.getData().get("created_at");
                String viewed = remoteMessage.getData().get("viewed");
                String type = remoteMessage.getData().get("type");
                int refId = Integer.parseInt(remoteMessage.getData().get("ref_id"));
                int totalNotifications = Integer.parseInt(remoteMessage.getData().get("total_notifications"));
                int totalUnviewedNotifications = Integer.parseInt(remoteMessage.getData().get("total_unviewed_notifications"));

                Intent intent = new Intent("com.makatizen.makahanap.notification");
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("image_url", imageUrl);
                intent.putExtra("created_at", createdAt);
                intent.putExtra("viewed", viewed.equalsIgnoreCase("Yes"));
                intent.putExtra("account_id", accountId);
                intent.putExtra("total_notification", totalNotifications);
                intent.putExtra("total_unviewed_notification", totalUnviewedNotifications);
                intent.putExtra("type", type);
                intent.putExtra("ref_id", refId);

                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);

                localBroadcastManager.sendBroadcast(intent);
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            if (remoteMessage.getNotification().getClickAction().equals("ChatMessage")) {
                showMessageNotification(remoteMessage);
            } else {
                showNotification(remoteMessage);
            }
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(final String token) {
        Log.d(TAG, "onNewToken: " + token);
        sendTokenToServer(token);
    }

    private void handleNow() {
    }

    private void scheduleJob() {
    }

    private void sendTokenToServer(final String token) {
        int currentAccountId = mDataManager.getCurrentAccount().getId();
        mDataManager.registerTokenToServer(token, currentAccountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<RegisterTokenResponse>() {
                    @Override
                    public void onError(final Throwable e) {

                    }

                    @Override
                    public void onSubscribe(final Disposable d) {
                        bag.add(d);
                    }

                    @Override
                    public void onSuccess(final RegisterTokenResponse registerTokenResponse) {
                    }
                });
    }

    private void showMessageNotification(final RemoteMessage remoteMessage) {
        String currentAccountId = String.valueOf(mDataManager.getCurrentAccount().getId());
        String title = remoteMessage.getNotification().getTitle();
        String content = StringEscapeUtils.unescapeEcmaScript(remoteMessage.getNotification().getBody());
        String clickAction = remoteMessage.getNotification().getClickAction();
        final NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        /*Android Oreo and Above Notification */
        {
            if (VERSION.SDK_INT >= VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("Maka-Hanap",
                        "Maka-Hanap Notification",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("Maka-Hanap Channel Test");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        // TODO: 2/27/19 Intent for click action for message and reply action
        Intent intent = new Intent(this, ChatConvoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        MakahanapAccount senderAccount = new MakahanapAccount();
        senderAccount.setProfileImageUrl(remoteMessage.getData().get("account_image"));
        senderAccount.setId(Integer.parseInt(remoteMessage.getData().get("sender_id")));
        senderAccount.setFirstName(remoteMessage.getData().get("sender_name"));
        senderAccount.setLastName("");

        String chatId = remoteMessage.getData().get("chat_room_id");
        int itemId = Integer.parseInt(remoteMessage.getData().get("item_id"));

        intent.putExtra(IntentExtraKeys.ACCOUNT, senderAccount);
        intent.putExtra(IntentExtraKeys.ITEM_ID, itemId);
        intent.putExtra(IntentExtraKeys.CHAT_ID, chatId);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, RequestCodes.MESSAGE_CONVO, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultBoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        /*Android Below Oreo Notification*/
        if (currentAccountId.equals(remoteMessage.getData().get("receiver_id"))) {
            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Maka-Hanap")
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setSound(defaultBoundUri)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setContentInfo("Message");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .apply(new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                            .load(ApiConstants.MAKATIZEN_API_BASE_URL + remoteMessage.getData().get("account_image"))
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull final Bitmap resource,
                                                            @Nullable final Transition<? super Bitmap> transition) {
                                    notificationBuilder.setLargeIcon(resource);
                                    notificationManager.notify(1, notificationBuilder.build());

                                }
                            });
                }
            });
        } else {
            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Maka-Hanap")
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setSound(defaultBoundUri)
                    .setContentText("from " + remoteMessage.getData().get("sender_name"))
                    .setContentTitle(remoteMessage.getData().get("receiver_name") + " " + "have new messages")
                    .setContentInfo("Message");

            notificationManager.notify(1, notificationBuilder.build());

        }

    }

    private void showNotification(RemoteMessage remoteMessage) {
        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.custom_notification_collapsed);
        final RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.custom_notification_expanded);

        String title = remoteMessage.getNotification().getTitle();
        String content = StringEscapeUtils.unescapeEcmaScript(remoteMessage.getNotification().getBody());
        String clickAction = remoteMessage.getNotification().getClickAction();
        final NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        /*Android Oreo and Above Notification */
        {
            if (VERSION.SDK_INT >= VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("Maka-Hanap",
                        "Maka-Hanap Notification",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("Maka-Hanap Channel");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        collapsedView.setTextViewText(R.id.collapsed_title, title);
        collapsedView.setTextViewText(R.id.collapsed_subtitle, content);

        expandedView.setTextViewText(R.id.expanded_title, title);
        expandedView.setTextViewText(R.id.expanded_location_tv, content);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher_round);
        Uri defaultBoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        /*Android Below Oreo Notification*/
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Maka-Hanap")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setSound(defaultBoundUri)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setContentTitle(title);

        int itemId = Integer.parseInt(remoteMessage.getData().get("ref_id"));
        /* Notification Type Mak */
        if (clickAction.equals("Item Detail")) {
            Intent intent = new Intent(this, ItemDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(IntentExtraKeys.ITEM_ID, itemId);
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(this, RequestCodes.ITEM_DETAILS, intent, PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentIntent(pendingIntent);
        }

        /* For Intent Updates */
        if (clickAction.equals("Updates")) {

        }

        /* For Intent Transactions */
        if (clickAction.equals("Transaction")) {
            Intent intent = new Intent(this, ItemDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(IntentExtraKeys.ITEM_ID, itemId);
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(this, RequestCodes.ITEM_DETAILS, intent, PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentIntent(pendingIntent);
        }

        if (clickAction.equals("Meetup")) {
            int chatRoomId = Integer.parseInt(remoteMessage.getData().get("ref_id"));
            int meetupId = Integer.parseInt(remoteMessage.getData().get("meetup_id"));
            int itemId2 = Integer.parseInt(remoteMessage.getData().get("item_id"));

            String lat = remoteMessage.getData().get("location_lat");
            String lng = remoteMessage.getData().get("location_lng");
            final String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lng+ "&zoom=15&scale=1&size=600x300&maptype=roadmap&key=" + getResources().getString(R.string.google_map_api_key) + "&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff5050%7Clabel:%7C" + lat + "," + lng;
            MakahanapAccount senderAccount = new MakahanapAccount();
            senderAccount.setId(Integer.parseInt(remoteMessage.getData().get("sender_id")));
            senderAccount.setFirstName(remoteMessage.getData().get("sender_name"));
            senderAccount.setLastName("");
            Intent intent = new Intent(this, ChatConvoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(IntentExtraKeys.CHAT_ID, String.valueOf(chatRoomId));
            intent.putExtra(IntentExtraKeys.ACCOUNT, senderAccount);
            intent.putExtra(IntentExtraKeys.MEET_ID, meetupId);
            intent.putExtra(IntentExtraKeys.ITEM_ID, itemId2);

            PendingIntent pendingIntent = PendingIntent
                    .getActivity(this, RequestCodes.MESSAGE_CONVO, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(url)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull final Bitmap resource,
                                                            @Nullable final Transition<? super Bitmap> transition) {
                                    notificationBuilder.setStyle(
                                            new NotificationCompat.BigPictureStyle().bigPicture(resource));
                                    expandedView.setImageViewBitmap(R.id.expanded_image, resource);
                                    notificationManager.notify(1, notificationBuilder.build());
                                }
                            });
                }
            });
            return;
        }

        final String imageUrl = remoteMessage.getData().get("image_url");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(imageUrl)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull final Bitmap resource,
                                                        @Nullable final Transition<? super Bitmap> transition) {
                                notificationBuilder.setStyle(
                                        new NotificationCompat.BigPictureStyle().bigPicture(resource));
                                expandedView.setImageViewBitmap(R.id.expanded_image, resource);
                                notificationManager.notify(1, notificationBuilder.build());
                            }
                        });
            }
        });
    }
}
