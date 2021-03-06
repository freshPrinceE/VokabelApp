package com.example.i01002706.vokabelapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.CardDao;
import com.example.i01002706.vokabelapp.Database.Cardset;
import com.example.i01002706.vokabelapp.Database.CardsetDao;
import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;
import java.util.List;


public class AdapterCardset extends RecyclerView.Adapter<AdapterCardset.ViewHolder> {

    private List<Cardset> mData = new ArrayList<>();
    private final LayoutInflater mInflater;
    private OnItemClickListener onClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onPlayButtonClick(int position);
        void onEditButtonClick(int position);
    }
    private AppDatabase database;
    // data is passed into the constructor
    public AdapterCardset(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.database = AppDatabase.getDatabase(context);
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_cardset, parent, false);
        return new ViewHolder(view);
    }



    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cardset cardset = mData.get(position);
        holder.myTextView.setText(cardset.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView myTextView;
        final Button play;
        final ImageButton edit;
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.categoryName);
            itemView.setOnClickListener(this);
            play = itemView.findViewById(R.id.play);
            play.setOnClickListener(this);
            edit = itemView.findViewById(R.id.edit);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onClickListener != null){
                if(view.getId() == play.getId()){
                    onClickListener.onPlayButtonClick(getAdapterPosition());
                }else if(view.getId() == edit.getId()){
                    onClickListener.onEditButtonClick(getAdapterPosition());
                }
                else {
                    onClickListener.onItemClick(getAdapterPosition());
                }
            }
        }
    }

    // convenience method for getting data at click position
    public Cardset getItem(int id) {
        return mData.get(id);
    }

    public void deleteItem(int id, Context context) {

        if (database == null) {
            return;
        }
        int id_cardset = getItem(id).getId();
        database = AppDatabase.getDatabase(context);
        CardDao cardDao = database.cardDao();
        CardsetDao cardsetDao = database.cardsetDao();
        cardDao.deleteQuery(id_cardset);
        cardsetDao.delete(getItem(id));
        mData.remove(id);
    }

    // allows clicks events to be caught
    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }



    public void addCategory(List<Cardset> cardsets){
        mData = cardsets;
    }

}