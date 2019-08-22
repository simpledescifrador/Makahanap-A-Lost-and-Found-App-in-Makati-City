package com.makatizen.makahanap.ui.main.account.gallery;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import java.util.List;

public interface AccountGalleryMvpView extends BaseMvpView {

    void setAccountItemImages(List<String> itemImages);
}
