package com.example.photoediter.ui.main.addtext;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.common.models.Color;
import com.example.photoediter.common.models.DataColor;
import com.example.photoediter.common.models.Font;
import com.example.photoediter.databinding.FragmentOptionTextBinding;
import com.example.photoediter.interfaces.OnClickDetailImage;
import com.example.photoediter.ui.adapter.ColorAdapter;
import com.example.photoediter.ui.adapter.FontAdapter;
import com.example.photoediter.ui.base.BaseBindingFragment;
import com.example.photoediter.ui.dialog.ColorPickerDialog;

import java.util.ArrayList;
import java.util.List;

public class OptionTextFragment extends BaseBindingFragment<FragmentOptionTextBinding, AddTextViewModel> implements ColorPickerDialog.OnColorChangedListener, OnClickDetailImage {
    private FontAdapter fontAdapter;
    private ColorAdapter colorAdapter;
    private LinearLayoutManager layoutManager;
    private String type;
    private int idColor = 1;
    private int idBgColor = -1;
    private List<Color> listColor = new ArrayList<>();
    private List<Font> fontList = new ArrayList<>();

    @Override
    protected Class getViewModel() {
        return AddTextViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_option_text;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        setupData();
    }

    private void setupData() {
        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        fontAdapter = new FontAdapter(this);
        colorAdapter = new ColorAdapter(this, requireContext());
        viewModel.getListFont(Constant.FOLDER_FONT, requireContext());
        viewModel.listFonts.observe(this, dataFont -> {
            if (dataFont != null) {
                fontList.clear();
                fontList.addAll(dataFont);
            }
        });
        viewModel.getListColor(Constant.TYPE_COLOR);
        viewModel.listData.observe(getViewLifecycleOwner(), dataColor -> {
            if (dataColor != null) {
                listColor.clear();
                listColor.addAll(dataColor);
            }
        });
        mainViewModel.typeText.observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                if (data.equals(Constant.TYPE_FONT_ADD_TEXT)) {
                    setupFont();
                    confirmFont();
                } else if (data.equals(Constant.TYPE_COLOR_ADD_TEXT)) {
                    this.type = Constant.TYPE_COLOR_ADD_TEXT;
                    setupColor();
                    confirmColor();
                } else if (data.equals(Constant.TYPE_BG_COLOR_ADD_TEXT)) {
                    this.type = Constant.TYPE_BG_COLOR_ADD_TEXT;
                    setupBgColor();
                    confirmBgColor();
                }
            }
        });

    }

    private void setupFont() {
        binding.ivAllColor.setVisibility(View.GONE);
        hideIcon(requireContext().getString(R.string.select_font));
        mainViewModel.backFontText.observe(this, dataBack -> {
            if (dataBack) fontAdapter.setId(-1);
        });
        fontAdapter.setmListFonts(fontList);
        binding.rcvOptionText.setLayoutManager(layoutManager);
        binding.rcvOptionText.setAdapter(fontAdapter);
    }

    private void setupColor() {
        binding.ivAllColor.setVisibility(View.VISIBLE);
        hideIcon(requireContext().getString(R.string.text_color));
        colorAdapter.setmListColor(listColor);
        colorAdapter.setIdTextColor(idColor);
        binding.rcvOptionText.setLayoutManager(layoutManager);
        binding.rcvOptionText.setAdapter(colorAdapter);
        chooseCustomColor();
        mainViewModel.backColorText.observe(this, data -> {
            if (data) colorAdapter.setIdTextColor(1);
        });
    }

    private void setupBgColor() {
        binding.ivAllColor.setVisibility(View.VISIBLE);
        hideIcon(requireContext().getString(R.string.background_color));
        colorAdapter.setmListColor(listColor);
        colorAdapter.setIdTextColor(idBgColor);
        mainViewModel.backColorBgText.observe(this, data -> {
            if (data) colorAdapter.setIdTextColor(-1);
        });
        chooseCustomColor();
        binding.rcvOptionText.setLayoutManager(layoutManager);
        binding.rcvOptionText.setAdapter(colorAdapter);
    }
    private void confirmBgColor(){
        binding.layoutEditSticker.ivCancel.setOnClickListener(v -> {
            idBgColor =-1;
            mainViewModel.isClickColorBgText.setValue(false);
            mainViewModel.setDataBgColor(null);
            colorAdapter.setIdTextColor(-1);
        });
        binding.layoutEditSticker.ivDone.setOnClickListener(v -> {
            mainViewModel.isClickColorBgText.setValue(false);

        });
    }

    private void confirmColor() {
        binding.layoutEditSticker.ivCancel.setOnClickListener(v -> {
            idColor =1;
            mainViewModel.isClickColorText.setValue(false);
            mainViewModel.setDataColor(null);
            colorAdapter.setIdTextColor(1);
        });
        binding.layoutEditSticker.ivDone.setOnClickListener(v -> {
            mainViewModel.isClickColorText.setValue(false);

        });
    }

    private void confirmFont() {
        binding.layoutEditSticker.ivCancel.setOnClickListener(v -> {
            mainViewModel.isClickFont.setValue(false);
            mainViewModel.typeFont.setValue(null);
            mainViewModel.positionFont.setValue(-1);
            fontAdapter.setId(-1);
        });
        binding.layoutEditSticker.ivDone.setOnClickListener(v -> mainViewModel.isClickFont.setValue(false));
    }

    private void hideIcon(String text) {
        binding.layoutEditSticker.tvContent.setText(text);
        binding.layoutEditSticker.ivUndo.setVisibility(View.GONE);
        binding.layoutEditSticker.ivRedo.setVisibility(View.GONE);
    }

    private void chooseCustomColor() {
        binding.ivAllColor.setOnClickListener(v ->{
            new ColorPickerDialog(requireContext(), this::colorChanged, ContextCompat.getColor(requireContext(), R.color.white)).show();
        });
    }


    @Override
    protected void onPermissionGranted() {

    }

    @Override
    public void onCLickFont(int position, String font) {
        mainViewModel.typeFont.setValue(font);
        mainViewModel.positionFont.setValue(position);
    }

    @Override
    public void onClickColor(int position, String color) {
        if (type.equals(Constant.TYPE_COLOR_ADD_TEXT)){
            idColor = position;
            mainViewModel.setDataColor(new DataColor(color, position));
        }else if (type.equals(Constant.TYPE_BG_COLOR_ADD_TEXT)){
            idBgColor =position;
            mainViewModel.setDataBgColor(new DataColor(color, position));
        }
    }

    @Override
    public void onClickSticker(int position, String sticker) {
    }

    @Override
    public void colorChanged(int color) {
        colorAdapter.setIdTextColor(-1);
        if (type.equals(Constant.TYPE_COLOR_ADD_TEXT)){
            mainViewModel.setColorText(color);
            idColor = -1;
        }else if (type.equals(Constant.TYPE_BG_COLOR_ADD_TEXT)){
            mainViewModel.setBgColorText(color);
            idBgColor = -1;
        }
    }
}
