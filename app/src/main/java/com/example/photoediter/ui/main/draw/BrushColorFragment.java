package com.example.photoediter.ui.main.draw;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.common.models.Color;
import com.example.photoediter.common.models.DataColor;
import com.example.photoediter.databinding.FragmentBrushColorBinding;
import com.example.photoediter.interfaces.OnClickDetailImage;
import com.example.photoediter.ui.base.BaseBindingFragment;
import com.example.photoediter.ui.dialog.ColorPickerDialog;
import com.example.photoediter.ui.adapter.ColorAdapter;

import java.util.ArrayList;
import java.util.List;

public class BrushColorFragment extends BaseBindingFragment<FragmentBrushColorBinding, DrawViewModel> implements ColorPickerDialog.OnColorChangedListener, OnClickDetailImage {
    private ColorAdapter colorAdapter;
    private int stateIdColor;
    private String stateColor;
    private int colorCustom;
    private static final String TAG = "HaiPd";
    public static BrushColorFragment newInstance() {
        BrushColorFragment fragment = new BrushColorFragment();
        return fragment;
    }

    @Override
    protected Class<DrawViewModel> getViewModel() {
        return DrawViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_brush_color;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onCreatedView BrushColor: ");
        binding.layoutEditSticker.tvContent.setText(requireContext().getString(R.string.brush_color));
        binding.layoutEditSticker.ivUndo.setVisibility(View.GONE);
        binding.layoutEditSticker.ivRedo.setVisibility(View.GONE);
        addListColor();
        confirmBrushColor();
        chooseCustomColor();
        mainViewModel.backColorBrush.observe(this, data ->{
            if (data) colorAdapter.setIdTextColor(1);
        });
        if (savedInstanceState!= null){
            stateColor = savedInstanceState.getString(Constant.STATE_COLOR_DRAW);
            stateIdColor = savedInstanceState.getInt(Constant.STATE_ID_COLOR_DRAW);
            colorCustom = savedInstanceState.getInt(Constant.STATE_CUSTOM_COLOR_DRAW);
            Log.d(TAG, "savedInstanceState BrushColor: stateColor:"+stateColor+", stateIdColor:"+stateIdColor+", colorCustom:"+colorCustom);

            if (colorAdapter!= null){
                Log.d(TAG, "savedInstanceState BrushColor: ");
                if (colorCustom == 0){
                    colorAdapter.setIdTextColor(stateIdColor);
                    mainViewModel.setColorBrush(new DataColor(stateColor, stateIdColor));

                }else {
                    colorAdapter.setIdTextColor(-1);
                    mainViewModel.setCustomColorBrush(colorCustom);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constant.STATE_CUSTOM_COLOR_DRAW, colorCustom);
        outState.putInt(Constant.STATE_ID_COLOR_DRAW, stateIdColor);
        outState.putString(Constant.STATE_COLOR_DRAW, stateColor);
        super.onSaveInstanceState(outState);
    }

    private void chooseCustomColor() {
        binding.ivAllColor.setOnClickListener(v ->
                new ColorPickerDialog(requireContext(), this::colorChanged, ContextCompat.getColor(requireContext(), R.color.white)).show());
    }

    private void addListColor() {
        viewModel.getListColorBrush(Constant.TYPE_COLOR);
        viewModel.listData.observe(this, data ->{
            if (data != null) {
                colorAdapter = new ColorAdapter( this, getContext());
                colorAdapter.setmListColor(data);
                colorAdapter.setIdTextColor(1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
                layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                binding.rcvBrushColor.setLayoutManager(layoutManager);
                binding.rcvBrushColor.setAdapter(colorAdapter);
                Log.d(TAG, "addListBrushColor: ");
            }
        });
    }

    private void confirmBrushColor() {
        binding.layoutEditSticker.ivCancel.setOnClickListener(v -> {
            mainViewModel.setColorBrush(new DataColor(null,-1));
            colorAdapter.setIdTextColor(1);
            mainViewModel.setIsClickColorDraw(false);
        });
        binding.layoutEditSticker.ivDone.setOnClickListener(v -> mainViewModel.setIsClickColorDraw(false));
    }

    @Override
    protected void onPermissionGranted() {

    }

    @Override
    public void onCLickFont(int position, String font) {

    }

    @Override
    public void onClickColor(int position, String color) {
        mainViewModel.setColorBrush(new DataColor(color, position));
        stateColor = color;
        stateIdColor = position;
        colorCustom = 0;
    }

    @Override
    public void onClickSticker(int position, String sticker) {

    }

    @Override
    public void colorChanged(int color) {
        mainViewModel.setCustomColorBrush(color);
        colorAdapter.setIdTextColor(-1);
        colorCustom = color;
        stateIdColor =-1;
        stateColor = null;
    }
}