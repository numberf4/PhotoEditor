package com.example.photoediter.ui.main.share;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.databinding.FragmentShareBinding;
import com.example.photoediter.ui.base.BaseBindingFragment;
import com.example.photoediter.ui.main.MainViewModel;
import com.example.photoediter.utils.BitmapUtils;
import com.example.photoediter.utils.DialogUtils;

public class ShareFragment extends BaseBindingFragment<FragmentShareBinding, MainViewModel> {
    private Dialog dialog;
    private String image;
    private long mLastClickTime = 0;

    @Override
    protected Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_share;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Window window = requireActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(View.VISIBLE);
        showImage();
        share();
        eventBack();
        if (savedInstanceState!= null){
            image = savedInstanceState.getString(Constant.STATE_IMAGE_SHARE_FRAGMENT);
            setImage(image);
        }
    }
    private void setImage(String image1){
        Glide.with(requireView()).asBitmap().load(image1).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                binding.ivShare.setImageBitmap(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Constant.STATE_IMAGE_SHARE_FRAGMENT, image);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainViewModel.pathLiveData.postValue(null);
    }

    private void showImage() {
        dialog = DialogUtils.getDiaLogLoading(getContext());
        dialog.show();
        mainViewModel.pathLiveData.observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                image = data;
                setImage(image);
            }else binding.ivShare.setImageResource(0);

        });
        new Handler().postDelayed(() -> dialog.dismiss(), Constant.TIME_DELAY_1000);
    }
    private void share(){
        binding.btnShare.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            BitmapUtils.shareImage(image,requireContext());
        });
    }

    private void eventBack() {
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                Navigation.findNavController(binding.getRoot()).popBackStack(R.id.splashFragment, false);
            }
            return true;
        });
        binding.ivBackShare.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack(R.id.splashFragment, false));
    }

    @Override
    protected void onPermissionGranted() {

    }
}