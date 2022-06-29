package com.example.photoediter.ui.main.draw;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.databinding.FragmentDrawBinding;
import com.example.photoediter.ui.base.BaseBindingFragment;
import com.example.photoediter.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class DrawFragment extends BaseBindingFragment<FragmentDrawBinding, DrawViewModel> {
    private List<Fragment> fragmentList = new ArrayList<>();
    private BrushColorFragment brushColorFragment;
    private BrushSizeFragment brushSizeFragment;
    private Boolean checkShowSize, checkShowColor;
    public static DrawFragment newInstance() {
        DrawFragment fragment = new DrawFragment();
        return fragment;
    }

    @Override
    protected Class<DrawViewModel> getViewModel() {
        return DrawViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_draw;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        binding.layoutEditSticker.tvContent.setText(requireContext().getString(R.string.draw));
        addFragment();
        initFunc();
        observeView();
        undoView();
        redoView();
        confirmDraw();
        if (savedInstanceState!= null){
            checkShowSize = savedInstanceState.getBoolean(Constant.SHOW_SIZE_DRAW);
            checkShowColor = savedInstanceState.getBoolean(Constant.SHOW_COLOR_DRAW);
            mainViewModel.isClickColorDraw.setValue(checkShowColor);
            mainViewModel.isClickSizeDraw.setValue(checkShowSize);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(Constant.SHOW_COLOR_DRAW, checkShowColor);
        outState.putBoolean(Constant.SHOW_SIZE_DRAW, checkShowSize);

        super.onSaveInstanceState(outState);
    }

    private void initFunc(){
        binding.tvColorDraw.setOnClickListener(v -> mainViewModel.setIsClickColorDraw(true));
        binding.tvSizeDraw.setOnClickListener(v -> mainViewModel.setIsClickSizeDraw(true));
    }
    private void observeView(){
        mainViewModel.isClickColorDraw.observe(getViewLifecycleOwner(), data ->{
            if (data) {
                showFragment(0);
                checkShowColor = true;
//                mainViewModel.iShowDrawColor.setValue(true);
            }
            else {
                hideFragment(0);
                checkShowColor = false;
//                mainViewModel.iShowDrawColor.setValue(false);
            }
        });
        mainViewModel.isClickSizeDraw.observe(getViewLifecycleOwner(), data ->{
            if (data) {
                showFragment(1);
                checkShowSize = true;
            }
            else {
                hideFragment(1);
                checkShowSize = false;
            }
        });

    }
    private void undoView(){
        binding.layoutEditSticker.ivUndo.setOnClickListener(v ->
                mainViewModel.setIsUndoDraw(true));
    }
    private void redoView(){
        binding.layoutEditSticker.ivRedo.setOnClickListener(v -> mainViewModel.setIsRedoDraw(true));
    }

    private void confirmDraw() {
        binding.layoutEditSticker.ivCancel.setOnClickListener(v -> {
            mainViewModel.setIsDeleteDraw(true);
            mainViewModel.setIsClickItemDraw(false);
        });
        binding.layoutEditSticker.ivDone.setOnClickListener(v -> {
            mainViewModel.setIsClickItemDraw(false);
        });
    }
    private void addFragment(){
        brushColorFragment = BrushColorFragment.newInstance();
        brushSizeFragment = BrushSizeFragment.newInstance();
        fragmentList.add(brushColorFragment);
        fragmentList.add(brushSizeFragment);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.csl_draw,brushColorFragment).hide(brushColorFragment);
        ft.add(R.id.csl_draw, brushSizeFragment).hide(brushSizeFragment);
        ft.commit();
    }
    private void showFragment(int fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
            if (i == fragment) ft.show(fragmentList.get(fragment));
            else ft.hide(fragmentList.get(i));
        }
        ft.commit();
    }

    private void hideFragment(int fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentList.get(fragment).isAdded()) {
            ft.hide(fragmentList.get(fragment));
            ft.commit();
        }
    }

    @Override
    protected void onPermissionGranted() {

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TAG", "onStart Draw: ");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "onStop Draw: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TAG", "onDestroyView Draw: ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "onDestroy Draw: ");

    }
}