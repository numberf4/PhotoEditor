package com.example.photoediter.ui.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.widget.Toast;

import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.databinding.ActivityMainBinding;
import com.example.photoediter.ui.base.BaseBindingActivity;
import com.example.photoediter.utils.DialogUtils;
import com.yalantis.ucrop.UCropFragmentCallback;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding, MainViewModel> implements UCropFragmentCallback {
    private static String resultUri;
    private Dialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public void setupView(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public String getUri() {
        return resultUri;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void loadingProgress(boolean showLoader) {
        dialog = DialogUtils.getDiaLogLoading(this);
        dialog.show();
        new Handler().postDelayed(dialog::dismiss, Constant.TIME_DELAY_1000);
    }

    @Override
    public void onCrop(String path) {
        resultUri = path;
        if (resultUri != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.REQUEST_URI_FROM_MAINACTIVITY, resultUri);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.homeFragment, bundle);
        }

    }
}