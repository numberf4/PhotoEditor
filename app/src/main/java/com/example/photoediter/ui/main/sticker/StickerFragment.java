package com.example.photoediter.ui.main.sticker;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.common.models.Color;
import com.example.photoediter.common.models.MessageEvent;
import com.example.photoediter.databinding.FragmentStickerBinding;
import com.example.photoediter.interfaces.OnClickDetailImage;
import com.example.photoediter.ui.base.BaseBindingFragment;
import com.example.photoediter.ui.adapter.ColorAdapter;

import java.util.ArrayList;
import java.util.List;

public class StickerFragment extends BaseBindingFragment<FragmentStickerBinding, StickerViewModel> implements OnClickDetailImage {
    private ColorAdapter mStickerAdapter;
    private int stateIdSticker= -1;
    private static final String TAG = "HaiPd";


    public StickerFragment() {
        // Required empty public constructor
    }
    public static StickerFragment newInstance() {
        StickerFragment fragment = new StickerFragment();
        return fragment;
    }

    @Override
    protected Class<StickerViewModel> getViewModel() {
        return StickerViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sticker;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        binding.layoutEditSticker.tvContent.setText(requireContext().getString(R.string.sticker));
        addListStickers();
        eventConfirmSticker();
        undoSticker();
        redoSticker();
        if (savedInstanceState != null) {
            stateIdSticker = savedInstanceState.getInt(Constant.STATE_ID_STICKER_ADAPTER);
            Log.d(TAG, "savedInstanceState sticker: "+stateIdSticker);
            Log.d(TAG, "savedInstanceState sticker adapter: "+mStickerAdapter);
            if (mStickerAdapter == null) {
                Log.d(TAG, "onCreatedView StickerAdapter = null: ");

            }else {
                mStickerAdapter.setIdSticker(stateIdSticker);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constant.STATE_ID_STICKER_ADAPTER,stateIdSticker);
        super.onSaveInstanceState(outState);
    }

    private void addListStickers() {
        viewModel.getListSticker(Constant.FOLDER_STICKER, requireContext());
        viewModel.listSticker.observe(this, data ->{
            showListStickers(data);

        });
        mainViewModel.backSticker.observe(this, data ->{
            if (data) mStickerAdapter.setIdSticker(-1);
        });
    }

    private void showListStickers(List<Color> list) {
        mStickerAdapter = new ColorAdapter(this, requireContext());
        mStickerAdapter.setmListColor(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvSticker.setLayoutManager(layoutManager);
        binding.rvSticker.setAdapter(mStickerAdapter);
        Log.d(TAG, "addListStickers: ");

    }
    @Override
    public void onClickSticker(int position, String sticker) {
        stateIdSticker = position;
        mainViewModel.sticker2.postValue(new MessageEvent(123,sticker));
    }

    private void eventConfirmSticker(){
        binding.layoutEditSticker.ivCancel.setOnClickListener(v ->{
            mainViewModel.setIsClickItemSticker(false);
            mainViewModel.sticker2.postValue(null);
            mStickerAdapter.setIdSticker(-1);
        });
        binding.layoutEditSticker.ivDone.setOnClickListener(v -> mainViewModel.setIsClickItemSticker(false));
    }
    private void undoSticker(){
        binding.layoutEditSticker.ivUndo.setOnClickListener(v -> {
            mainViewModel.setUndoSticker(true);
        });
    }
    private void redoSticker(){
        binding.layoutEditSticker.ivRedo.setOnClickListener(v ->{
            mainViewModel.setRedoSticker(true);
        });
    }

    @Override
    protected void onPermissionGranted() {
    }

    @Override
    public void onCLickFont(int position, String font) {

    }

    @Override
    public void onClickColor(int position, String color) {

    }


}