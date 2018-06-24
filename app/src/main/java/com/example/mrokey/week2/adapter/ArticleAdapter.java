package com.example.mrokey.week2.adapter;

import android.annotation.SuppressLint;
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

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Doc> docs;
    private Context context;
    private ItemClickListener itemClickListener;
    final int ARTS = 0, FASHION_STYLE = 1, SPORTS = 2, ANOTHER = 3, UNAVAILABLE = -1;

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

    @Override
    public int getItemViewType(int position) {
        String newDesk = docs.get(position).getNewsDesk();
        if (newDesk == null) return UNAVAILABLE;
        if (newDesk.equals("Arts"))
            return ARTS;
        else if (newDesk.equals("Fashion & Style"))
            return FASHION_STYLE;
        else if (newDesk.equals("Sports"))
            return SPORTS;
        else return ANOTHER;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_rv_article, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Doc doc = docs.get(i);

        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.placeholder_tnyt))
                .load(getImageURL(doc))
                .apply(RequestOptions.bitmapTransform(
                        new RoundedCornersTransformation(111, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(viewHolder.img_cover);
        viewHolder.tv_background.setText(doc.getHeadline().getMain());
        viewHolder.tv_title.setText(doc.getHeadline().getMain());
        viewHolder.tv_publish.setText(doc.getPubDate());
        int TYPE = viewHolder.getItemViewType();
        if (TYPE == ARTS)
            viewHolder.ic_genre.setImageResource(R.drawable.ic_arts);
        else if (TYPE == FASHION_STYLE)
            viewHolder.ic_genre.setImageResource(R.drawable.ic_fashion);
        else if (TYPE == SPORTS)
            viewHolder.ic_genre.setImageResource(R.drawable.ic_sports);
        else viewHolder.ic_genre.setImageResource(R.drawable.ic_genre);
        if (TYPE != UNAVAILABLE) viewHolder.tv_genre.setText(doc.getNewsDesk());
        else viewHolder.tv_genre.setText("Unavailable");
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
        TextView tv_background;
        TextView tv_title;
        ImageView ic_genre;
        TextView tv_genre;
        ImageView ic_publish;
        TextView tv_publish;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cover = itemView.findViewById(R.id.img_cover);
            tv_background = itemView.findViewById(R.id.tv_background);
            tv_title = itemView.findViewById(R.id.tv_title);
            ic_genre = itemView.findViewById(R.id.ic_genre);
            tv_genre = itemView.findViewById(R.id.tv_genre);
            ic_publish = itemView.findViewById(R.id.ic_publish);
            tv_publish = itemView.findViewById(R.id.tv_publish);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClickItem(docs.get(getAdapterPosition()));
        }
    }
}
