package com.example.photoediter.ui.main.filter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.databinding.FragmentFilterBinding;
import com.example.photoediter.ui.base.BaseBindingFragment;
import com.example.photoediter.ui.main.MainActivity;
import com.example.photoediter.ui.adapter.FilterAdapter;
import com.example.photoediter.utils.FilterOption;
import com.example.photoediter.utils.SaveUtils;
import com.filter.base.GPUImageFilter;
import com.filter.helper.FilterManager;
import com.filter.helper.MagicFilterType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends BaseBindingFragment<FragmentFilterBinding, FilterViewModel> implements FilterAdapter.OnFilterClick {
    private FilterAdapter mFilterAdapter;
    private static SharedPreferences sharedPreferences;

    private List<MagicFilterType> magicFilterTypeList = new ArrayList<>();
    protected int position = 0;
    private String uri;
    private static final String TAG = "HaiPd";

    public FilterFragment() {
    }

    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
        return fragment;
    }

    @Override
    protected Class<FilterViewModel> getViewModel() {
        return FilterViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_filter;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState filter: ");
        outState.putInt(Constant.STATE_POSITION_ID_FILTER_ADAPTER, position);
        outState.putString(Constant.STATE_URI_FILTER_ADAPTER, uri.toString());

        sharedPreferences.edit().putString(Constant.SAVE_URI_FILTER, uri.toString()).apply();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onCreatedView Filter: ");
        setupView();
        eventConfirmFilter();
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(Constant.STATE_POSITION_ID_FILTER_ADAPTER);
            uri = savedInstanceState.getString(Constant.STATE_URI_FILTER_ADAPTER);
            if (mFilterAdapter != null) {
                mFilterAdapter.setId(position);
                mFilterAdapter.setMagicFilterTypeList(getListFilter());
                mainViewModel.filter.setValue(FilterManager.getInstance().getFilter(getListFilter().get(position)));
            }
        }
    }

    private void setupView() {
        sharedPreferences = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        binding.layoutEditSticker.tvContent.setText(requireContext().getString(R.string.filter));
        binding.layoutEditSticker.ivUndo.setVisibility(View.GONE);
        binding.layoutEditSticker.ivRedo.setVisibility(View.GONE);
        mainViewModel.backFilter.observe(getViewLifecycleOwner(), data -> {
            if (data) mFilterAdapter.setId(0);
        });

        MainActivity activity = (MainActivity) getActivity();
        uri = activity.getUri();
        if (uri != null) {
        }
    }

    public void addFilter(Bitmap bitmap) {
        viewModel.getListFilter(requireContext(), bitmap);

        viewModel.liveEvent.observe(this, filterOption -> {
            if (filterOption != null) {
                if (filterOption instanceof FilterOption) {
                    magicFilterTypeList.clear();
                    magicFilterTypeList.addAll(((FilterOption) filterOption).getListFilter());
                    setListFilter(magicFilterTypeList);
                    mFilterAdapter.setMagicFilterTypeList(magicFilterTypeList);
                    mFilterAdapter.setListbmFilter(((FilterOption) filterOption).getListBmFilter());
                }
            }
        });
        mFilterAdapter = new FilterAdapter(this, requireContext());

        binding.rvFilter.post(() -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvFilter.setLayoutManager(layoutManager);
            binding.rvFilter.setAdapter(mFilterAdapter);
            mFilterAdapter.notifyDataSetChanged();
        });
    }

    public void eventConfirmFilter() {
        binding.layoutEditSticker.ivCancel.setOnClickListener(v -> {
            mainViewModel.setIsClickItemFilter(false);
            mainViewModel.filter.setValue(new GPUImageFilter());
            if (mFilterAdapter != null) mFilterAdapter.setId(0);
        });
        binding.layoutEditSticker.ivDone.setOnClickListener(v -> {
            mainViewModel.setIsClickItemFilter(false);
        });
    }

    public <T> void setListFilter(List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(json);
    }

    public static void set(String value) {
        sharedPreferences.edit().putString(Constant.STATE_LIST_STICKER_ADAPTER, value).apply();
    }

    public List<MagicFilterType> getListFilter() {
        List<MagicFilterType> arrayItems = null;
        String serializedObject = sharedPreferences.getString(Constant.STATE_LIST_STICKER_ADAPTER, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<MagicFilterType>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    @Override
    protected void onPermissionGranted() {

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences.edit().clear();
    }

    @Override
    public void onCLick(GPUImageFilter imageFilter, int position) {
        mainViewModel.filter.setValue(imageFilter);
        this.position = position;
    }
}