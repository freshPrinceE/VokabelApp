package com.example.i01002706.vokabelapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.CardDao;
import com.example.i01002706.vokabelapp.Database.Cardset;
import com.example.i01002706.vokabelapp.Database.CardsetDao;
import com.example.i01002706.vokabelapp.Database.Category;
import com.example.i01002706.vokabelapp.Database.CategoryDao;
import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {

    private List<Category> mData = new ArrayList<>();
    private final LayoutInflater mInflater;
    private AppDatabase database;
    private OnItemClickListener onClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onEditClick(int position);
    }
    // data is passed into the constructor
    public AdapterCategory(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.database = AppDatabase.getDatabase(context);
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_category, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = mData.get(position);
        holder.categoryName.setText(category.getTitle());
        holder.categorySize.setText(database.cardsetDao().allCardsets(category.getId()).size() + " Cardsets");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView categoryName;
        final TextView categorySize;
        final ImageButton edit;

        ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categorySize = itemView.findViewById(R.id.categorySize);
            edit = itemView.findViewById(R.id.edit);
            edit.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onClickListener != null){

                if(view.getId() == edit.getId()){
                    onClickListener.onEditClick(getAdapterPosition());
                }else {
                    onClickListener.onItemClick(getAdapterPosition());
                }
            }
        }
    }

    // convenience method for getting data at click position
    public Category getItem(int id) {
        return mData.get(id);
    }

    public void deleteItem(int id, Context context) {
        if (database == null) {

            return;
        }
        int id_category = getItem(id).getId();
        database = AppDatabase.getDatabase(context);
        CardDao cardDao = database.cardDao();
        CardsetDao cardsetDao = database.cardsetDao();
        CategoryDao categoryDao = database.categoryDao();
        List<Cardset> cardsets = cardsetDao.allCardsets(id_category);

        for (Cardset cardset : cardsets) {
            cardDao.deleteQuery(cardset.getId());
        }

        cardsetDao.deleteQuery(id_category);

        categoryDao.delete(getItem(id));
        mData.remove(id);
    }

    // allows clicks events to be caught
    public void setClickListener(OnItemClickListener itemClickListener) {
        this.onClickListener = itemClickListener;
    }


    public void addCategory(List<Category> category) {
        mData = category;
        notifyDataSetChanged();
    }


}