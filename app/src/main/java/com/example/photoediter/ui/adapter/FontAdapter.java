package com.example.photoediter.ui.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photoediter.R;
import com.example.photoediter.common.Constant;
import com.example.photoediter.common.models.Font;
import com.example.photoediter.databinding.LayoutItemFontBinding;
import com.example.photoediter.interfaces.OnClickDetailImage;

import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {
    private List<Font> mListFonts;
    private OnClickDetailImage onClick;
    private int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyDataSetChanged();
    }
    public void setmListFonts(List<Font> list){
        this.mListFonts = list;
        notifyDataSetChanged();
    }

    public FontAdapter( OnClickDetailImage onClick) {
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemFontBinding itemFontBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_item_font, parent, false);
        return new ViewHolder(itemFontBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (id == holder.getAdapterPosition()){
            holder.itemFontBinding.tvFont.setTextColor(holder.itemFontBinding.getRoot().getContext().getResources().getColor(R.color.light_blue));
            holder.itemFontBinding.tvItemAgFont.setTextColor(holder.itemFontBinding.getRoot().getContext().getResources().getColor(R.color.light_blue));
        }else {
            holder.itemFontBinding.tvFont.setTextColor(holder.itemFontBinding.getRoot().getContext().getResources().getColor(R.color.white));
            holder.itemFontBinding.tvItemAgFont.setTextColor(holder.itemFontBinding.getRoot().getContext().getResources().getColor(R.color.white));
        }
        holder.itemFontBinding.tvFont.setText(mListFonts.get(position).getName());
        holder.itemFontBinding.cslFont.setOnClickListener( v->{
            if (holder.getAdapterPosition() >= 0){
                String fonts = Constant.Font_pronoted+mListFonts.get(holder.getAdapterPosition()).getFont();
                onClick.onCLickFont(holder.getAdapterPosition(), fonts);
            }
            if (id >= 0) notifyItemChanged(id);
            if (id!= holder.getAdapterPosition()){
                id = holder.getAdapterPosition();
                notifyItemChanged(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFonts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutItemFontBinding itemFontBinding;

        public ViewHolder(@NonNull LayoutItemFontBinding itemView) {
            super(itemView.getRoot());
            itemFontBinding = itemView;

        }
    }
}
