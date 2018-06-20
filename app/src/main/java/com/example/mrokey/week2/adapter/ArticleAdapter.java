package com.example.mrokey.week2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mrokey.week2.R;
import com.example.mrokey.week2.model.Doc;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Doc> docs;
    private Context context;
    private ItemClickListener itemClickListener;

    public ArticleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Doc> docs) {
        this.docs = docs;
        notifyDataSetChanged();
    }

    public void clearData() {
        docs.clear();
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_rv_article, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Doc doc = docs.get(i);
        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.placeholder))
                .load(doc.getMultimedia().size() == 0 ? "" : doc.getMultimedia().get(0).getUrl())
                .into(viewHolder.img_cover);
        viewHolder.tv_title.setText(doc.getHeadline().getMain());
    }

    @Override
    public int getItemCount() {
        return docs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img_cover;
        TextView tv_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cover = itemView.findViewById(R.id.img_cover);
            tv_title = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClickItem(docs.get(getAdapterPosition()));
        }
    }
}
