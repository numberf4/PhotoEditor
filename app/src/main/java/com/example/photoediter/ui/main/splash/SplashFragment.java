package com.example.photoediter.ui.main.splash;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.databinding.FragmentSplashBinding;
import com.example.photoediter.ui.base.BaseBindingFragment;
import com.example.photoediter.ui.main.MainViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.Calendar;

public class SplashFragment extends BaseBindingFragment<FragmentSplashBinding, MainViewModel> {
    private final String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    private SharedPreferences sharedPreferences;
    private static Dialog dialog;
    private long mLastClickTime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                Window window = requireActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        window.setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);
        setupView();
    }

    private void setupView() {
        dialog = createDialog();
        sharedPreferences = requireActivity().getSharedPreferences(Constant.MY_PREFS_PICK_IMAGE, MODE_PRIVATE);
    }

    private void startCrop(Uri data) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.REQUEST_URI_FROM_SPLASH, data.toString());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.ucropFragment, bundle);
    }

    @Override
    protected Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void isHaveAllPermissions() {
        for (String temp : permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), temp) == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    dialog.dismiss();
                    sharedPreferences.edit().putInt(Constant.ID_SHAREDPREFERENCES1, 0).apply();
                    getImageFromDevice();
                    Toast.makeText(requireActivity(), requireContext().getText(R.string.alert_granted_permissions), Toast.LENGTH_SHORT).show();
                } else {
                    sharedPreferences.edit().putInt(Constant.ID_SHAREDPREFERENCES1, 1).apply();
                    Toast.makeText(requireActivity(), requireContext().getText(R.string.alert_denied_permissions), Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        binding.btnSplash.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (sharedPreferences.getInt(Constant.ID_SHAREDPREFERENCES1, 1) == 1) {
                dialog.show();
            } else if (sharedPreferences.getInt(Constant.ID_SHAREDPREFERENCES1, 0) == 0) {
                getImageFromDevice();
            }
        });
    }

    private void getImageFromDevice() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(Constant.TYPE_IMAGE);
        startActivityForResult(intent, Constant.REQUEST_PICK_FROM_DEVICE);
    }


    private Dialog createDialog() {
        Dialog dialog = new Dialog(requireActivity(), R.style.blurDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
        dialog.setContentView(R.layout.fragment_grant_access_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.dimAmount = 0.6f;
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        dialog.findViewById(R.id.btn_allow_access).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isHaveAllPermissions();
            }

        });
        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_PICK_FROM_DEVICE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();
                startCrop(selectedImage);
            }
        }
    }

    @Override
    protected void onPermissionGranted() {
    }
}