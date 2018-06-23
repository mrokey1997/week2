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
import com.example.mrokey.week2.model.Multimedium;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Doc> docs;
    private Context context;
    private ItemClickListener itemClickListener;

    public ArticleAdapter(Context context) {
        docs = new ArrayList<>();
        this.context = context;
    }

    /**
     * Set data
     * @param docs list of doc
     */
    public void setData(List<Doc> docs) {
        if (getItemCount() == 0)
            this.docs = docs;
        else this.docs.addAll(docs);
        notifyDataSetChanged();
    }

    /**
     * Clear all data
     */
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
                .load(getImageURL(doc))
                .into(viewHolder.img_cover);
        viewHolder.tv_title.setText(doc.getHeadline().getMain());
    }

    /**
     * Get Image URL
     * @param doc ...
     * @return url
     */
    private String getImageURL(Doc doc) {
        int size = doc.getMultimedia().size();
        if (size == 0) return "";
        for (int i = 0; i < size; i++) {
            String subtype = doc.getMultimedia().get(i).getSubtype();
            if (subtype.equals("xlarge"))
                return doc.getMultimedia().get(i).getUrl();
            else if (subtype.equals("hpLarge"))
                return doc.getMultimedia().get(i).getUrl();
        }
        return doc.getMultimedia().get(0).getUrl();
    }

    /**
     * Get number of item
     * @return number of item
     */
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
