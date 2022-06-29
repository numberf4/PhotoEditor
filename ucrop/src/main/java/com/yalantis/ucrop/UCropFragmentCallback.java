package com.yalantis.ucrop;

import android.net.Uri;

public interface UCropFragmentCallback {

    /**
     * Return loader status
     * @param showLoader
     */
    void loadingProgress(boolean showLoader);

    //custom
    void onCrop(String path);

}
