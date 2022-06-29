package com.example.photoediter.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.databinding.LayoutItemColorTextBinding;
import com.example.photoediter.databinding.LayoutItemStickerBinding;
import com.example.photoediter.interfaces.OnClickDetailImage;
import com.example.photoediter.common.models.Color;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter {
    private  List<Color> mListColor;
    private final OnClickDetailImage mOnclick;
    private final Context context;
    private int idTextColor = -1;
    private int idSticker = -1;


    @SuppressLint("NotifyDataSetChanged")
    public void setIdTextColor(int idTextColor) {
        this.idTextColor = idTextColor;
        notifyDataSetChanged();
    }

    public void setIdSticker(int idSticker) {
        this.idSticker = idSticker;
        notifyDataSetChanged();
    }

    public void setmListColor(List<Color> mListColor) {
        this.mListColor = mListColor;
    }

    public ColorAdapter(OnClickDetailImage mOnclick, Context context) {
        this.mOnclick = mOnclick;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_COLOR) {
            LayoutItemColorTextBinding colorTextBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_item_color_text, parent, false);
            return new ViewHolderColor(colorTextBinding);

        } else if (viewType == Constant.STICKER) {
            LayoutItemStickerBinding stickerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_item_sticker, parent, false);
            return new ViewHolderSticker(stickerBinding);
        }
        return null;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Color color = mListColor.get(position);
        int type = color.getType();
        if (type == Constant.TYPE_COLOR){
            ((ViewHolderColor) holder).itemColorTextBinding.civItemColor.setCircleBackgroundColor(android.graphics.Color.parseColor(Constant.Color_promoted + color.getColor()));
            setCircleBorderImage(idTextColor, position,((ViewHolderColor) holder));
            ((ViewHolderColor) holder).itemColorTextBinding.civItemColor.setOnClickListener(v ->{
                if (holder.getAdapterPosition() >= 0) mOnclick.onClickColor(holder.getAdapterPosition(), mListColor.get(holder.getAdapterPosition()).getColor());
                if (idTextColor >= 0) notifyItemChanged(idTextColor);
                if (idTextColor != holder.getAdapterPosition()){
                    idTextColor = holder.getAdapterPosition();
                    notifyItemChanged(idTextColor);
                }
            });
        } else if (type == Constant.STICKER){
            Glide.with(((ViewHolderSticker) holder).stickerBinding.getRoot().getContext())
                    .load(color.getColor())
                    .into(((ViewHolderSticker) holder).stickerBinding.imgSticker);
            ((ViewHolderSticker) holder).stickerBinding.imgSticker.setOnClickListener(v ->{
                mOnclick.onClickSticker(holder.getAdapterPosition(), mListColor.get(holder.getAdapterPosition()).getColor());
                if (idSticker >= 0) notifyItemChanged(idSticker);
                if (idSticker != holder.getAdapterPosition()) {
                    idSticker = holder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        } else if (type == Constant.TYPE_BACKGROUND_COLOR){
            setCircleBorderImage(idTextColor, holder.getAdapterPosition(),((ViewHolderBackgroundColor) holder));
        }
    }

    @Override
    public int getItemCount() {
        return mListColor.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = mListColor.get(position).getType();
        if (type == Constant.STICKER)
            return Constant.STICKER;
        else
            return Constant.TYPE_COLOR;
    }
    public class ViewHolderColor extends RecyclerView.ViewHolder{
        LayoutItemColorTextBinding itemColorTextBinding;
        public ViewHolderColor(@NonNull LayoutItemColorTextBinding itemView){
            super(itemView.getRoot());
            itemColorTextBinding = itemView;

        }
    }
    public class ViewHolderBackgroundColor extends RecyclerView.ViewHolder {
        LayoutItemColorTextBinding itemColorBinding;

        public ViewHolderBackgroundColor(@NonNull LayoutItemColorTextBinding itemView) {
            super(itemView.getRoot());
            itemColorBinding = itemView;
        }
    }

    private void setCircleBorderImage(int id, int position, RecyclerView.ViewHolder holder) {
        if (id == position) {
            ((ViewHolderColor) holder).itemColorTextBinding.civItemColor.setBorderColor(ContextCompat.getColor(context,R.color.light_blue));
            ((ViewHolderColor) holder).itemColorTextBinding.civItemColor.setBorderWidth(Constant.WIDTH_CIRCLE_BORDER);
        } else {
            ((ViewHolderColor) holder).itemColorTextBinding.civItemColor.setBorderColor(0);
            ((ViewHolderColor) holder).itemColorTextBinding.civItemColor.setBorderWidth(0);
        }
    }

    public class ViewHolderSticker extends RecyclerView.ViewHolder {
        LayoutItemStickerBinding stickerBinding;

        public ViewHolderSticker(@NonNull LayoutItemStickerBinding itemView) {
            super(itemView.getRoot());
            stickerBinding = itemView;
//            stickerBinding.imgSticker.setOnClickListener(v -> );
        }
    }

}
