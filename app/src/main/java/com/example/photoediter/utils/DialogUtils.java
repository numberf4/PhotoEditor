package com.example.photoediter.utils;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.databinding.LayoutLoadingBinding;

public class DialogUtils {
    public static Dialog getDiaLogLoading(Context context) {
        LayoutLoadingBinding loadingBinding;
        Dialog dialog = new Dialog(context);
        loadingBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_loading, null, false);
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                loadingBinding.progressCircular.setProgress(progress);
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(Constant.TIME_DURATION);
        animator.start();
        dialog = new Dialog(context);
        dialog.setContentView(loadingBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int widthDl = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        int heightDl = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.3);
        dialog.getWindow().setLayout(widthDl, heightDl);
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_progress);
        return dialog;
    }
}
