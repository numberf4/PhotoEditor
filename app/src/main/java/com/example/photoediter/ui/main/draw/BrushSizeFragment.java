package com.example.photoediter.ui.main.draw;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.photoediter.R;
import com.example.photoediter.databinding.FragmentBrushSizeBinding;
import com.example.photoediter.ui.base.BaseBindingFragment;
import com.example.photoediter.ui.main.MainViewModel;

public class BrushSizeFragment extends BaseBindingFragment<FragmentBrushSizeBinding, MainViewModel>  {

    public static BrushSizeFragment newInstance() {
        return new BrushSizeFragment();
    }

    @Override
    protected Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_brush_size;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        binding.layoutEditText.tvContent.setText(requireContext().getString(R.string.brush_size));
        binding.layoutEditText.ivUndo.setVisibility(View.GONE);
        binding.layoutEditText.ivRedo.setVisibility(View.GONE);
        mainViewModel.backSizeBrush.observe(this, data ->{
            resetImage();
            binding.ivBrushSize2.setImageResource(R.drawable.ic_pick_brush_size2);
        });
        selectBrushSize();
        confirmBrushSize();
    }
    private void selectBrushSize(){
        selectSize(5,binding.ivBrushSize1,R.drawable.ic_pick_brush_size1);
        selectSize(10,binding.ivBrushSize2,R.drawable.ic_pick_brush_size2);
        selectSize(15,binding.ivBrushSize3,R.drawable.ic_pick_brush_size3);
        selectSize(25,binding.ivBrushSize4,R.drawable.ic_pick_brush_size4);
        selectSize(30,binding.ivBrushSize5,R.drawable.ic_pick_brush_size5);
        selectSize(35,binding.ivBrushSize6,R.drawable.ic_pick_brush_size6);
        selectSize(40,binding.ivBrushSize7,R.drawable.ic_pick_brush_size7);
    }
    private void selectSize(int size, View view, int image){
        view.setOnClickListener(v ->{
            resetImage();
            if (view instanceof AppCompatImageView) ((AppCompatImageView) view).setImageResource(image);
            mainViewModel.setBrushSize(size);
        });
    }
    private void resetImage(){
        binding.ivBrushSize1.setImageResource(R.drawable.ic_brush_size1);
        binding.ivBrushSize2.setImageResource(R.drawable.ic_brush_size2);
        binding.ivBrushSize3.setImageResource(R.drawable.ic_brush_size3);
        binding.ivBrushSize4.setImageResource(R.drawable.ic_brush_size4);
        binding.ivBrushSize5.setImageResource(R.drawable.ic_brush_size5);
        binding.ivBrushSize6.setImageResource(R.drawable.ic_brush_size6);
        binding.ivBrushSize7.setImageResource(R.drawable.ic_brush_size7);
    }


    private void confirmBrushSize() {
        binding.layoutEditText.ivCancel.setOnClickListener(v -> {
            mainViewModel.setIsClickSizeDraw(false);
            mainViewModel.setBrushSize(10);
            resetImage();
            binding.ivBrushSize2.setImageResource(R.drawable.ic_pick_brush_size2);
        });
        binding.layoutEditText.ivDone.setOnClickListener(v -> mainViewModel.setIsClickSizeDraw(false));
    }

    @Override
    protected void onPermissionGranted() {

    }

}