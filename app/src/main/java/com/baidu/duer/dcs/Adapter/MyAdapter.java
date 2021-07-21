package com.baidu.duer.dcs.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.duer.dcs.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Integer> mBackgroundList;
    int currentIndex;
    private OnItemClickListener mOnItemClickListener;



    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hichina_xihuselectphoto, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        final int index = position;
        int background = mBackgroundList.get(index);
        holder.imageView.setImageResource(background);
        currentIndex = position;
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(index);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(index);
                    return false;
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return mBackgroundList.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }




    public MyAdapter(List<Integer> mBackgroundList) {
        this.mBackgroundList = mBackgroundList;
    }


    public int getCurrentIndex() {
        return currentIndex;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.xihu_selectphoto_item);
        }

    }
}
