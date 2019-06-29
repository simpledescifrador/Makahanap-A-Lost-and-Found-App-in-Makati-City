package com.makatizen.makahanap.ui.intro;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.di.qualifiers.ActivityContext;
import javax.inject.Inject;

public class IntroAdapter extends PagerAdapter {

    private Context mContext;

    private String[] mIntroDescriptions = {
            "Makati City Lost and Found Mobile Application",
            "Your personal things, pets and even person can be reported and be posted.",
            "Posting items you have lost or found made easier when you can do it whenever you are and whenever you want.",
            "Send message to other users on your own convenience. Chatting makes it easier to communicate online.",
            "Finding lost or found item made easy for you by typing a keywords or details about the item will be displayed instant.",
            "Become alert when there is a match to your reported lost item or someone wants to connects with you and when there is a message from the app. Notification features got your back."
    };

    private int[] mIntroImages = {
            R.drawable.app_logo_with_text,
            R.drawable.missing_something,
            R.drawable.map,
            R.drawable.conversation,
            R.drawable.search,
            R.drawable.notification
    };

    private String[] mIntroTitles = {
            "Welcome to Maka-Hanap",
            "Report your Lost or Found Item",
            "Post Whenever Wherever",
            "Communicate Better with Realtime ChatItem",
            "Search a Lost or Found Item",
            "Be Notified"
    };

    @Inject
    public IntroAdapter(@ActivityContext Context context) {
        mContext = context;
    }

    @Override
    public void destroyItem(@NonNull final ViewGroup container, final int position, @NonNull final Object object) {
        container.removeView((FrameLayout) object);
    }

    @Override
    public int getCount() {
        return mIntroTitles.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        final LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.intro_slides, container, false);
        FrameLayout imageBackground = view.findViewById(R.id.intro_image_background);
        ImageView imageView = view.findViewById(R.id.intro_image);
        TextView title = view.findViewById(R.id.intro_title);
        TextView description = view.findViewById(R.id.intro_description);
//        if (position != 0) {
//            title.setTextColor(Color.WHITE);
//            description.setTextColor(Color.WHITE);
//        }
        imageView.setImageResource(mIntroImages[position]);
        title.setText(mIntroTitles[position]);
        description.setText(mIntroDescriptions[position]);
        imageBackground.setBackgroundColor(Color.WHITE);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull final Object o) {
        return view == o;
    }
}
