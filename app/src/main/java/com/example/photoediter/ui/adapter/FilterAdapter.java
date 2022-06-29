package com.example.photoediter.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photoediter.R;
import com.example.photoediter.databinding.ItemFilterListBinding;
import com.filter.base.GPUImageFilter;
import com.filter.helper.FilterManager;
import com.filter.helper.MagicFilterType;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private List<MagicFilterType> magicFilterTypeList = new ArrayList<>();
    private final OnFilterClick mOnFilterClick;
    private final Context context;
    private  List<Bitmap> listbmFilter;
    private int id = 0;

    public void setId(int id) {
        this.id = id;
        notifyDataSetChanged();
    }

    public FilterAdapter( OnFilterClick mOnFilterClick, Context context) {
        this.mOnFilterClick = mOnFilterClick;
        this.context = context;
    }


    public void setMagicFilterTypeList(List<MagicFilterType> magicFilterTypeList) {
        this.magicFilterTypeList = magicFilterTypeList;
        notifyDataSetChanged();
    }

    public void setListbmFilter(List<Bitmap> listbmFilter) {
        this.listbmFilter = listbmFilter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFilterListBinding iconFilterListBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_filter_list, parent, false);
        return new ViewHolder(iconFilterListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (magicFilterTypeList != null) {
            try {
                holder.iconBinding.imgFilterAvt.setImageBitmap(listbmFilter.get(position));
            } catch (Exception ignored) {
            }
        }
        holder.iconBinding.tvNameFilter.setText(FilterManager.getInstance().getFilterName(magicFilterTypeList.get(position)));
        holder.iconBinding.ctlIconFilter.setOnClickListener(v -> {
            mOnFilterClick.onCLick(FilterManager.getInstance().getFilter(magicFilterTypeList.get(position)), position);
            if (id >= 0) notifyItemChanged(id);
            if (id != position) {
                id = position;
                notifyItemChanged(id);
            }
        });
        if (id == position) {
            holder.iconBinding.tvNameFilter.setTextColor(context.getResources().getColor(R.color.light_blue));
            holder.iconBinding.viewFilter.setBackground(context.getResources().getDrawable(R.drawable.option_select_item_filter));
        } else {
            holder.iconBinding.tvNameFilter.setTextColor(context.getResources().getColor(R.color.white));
            holder.iconBinding.viewFilter.setBackground(context.getResources().getDrawable(R.drawable.option_not_select_item_filter));
        }
    }

    @Override
    public int getItemCount() {
        return magicFilterTypeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemFilterListBinding iconBinding;

        public ViewHolder(@NonNull ItemFilterListBinding itemView) {
            super(itemView.getRoot());
            iconBinding = itemView;

        }
    }

    public interface OnFilterClick {
        void onCLick(GPUImageFilter imageFilter, int position);
    }
}
