package com.example.i01002706.vokabelapp.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.example.i01002706.vokabelapp.Adapter.AdapterInCardset;
import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Card;
import com.example.i01002706.vokabelapp.Database.CardDao;
import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;
import java.util.List;

public class InCardsetActivity extends AppCompatActivity {
    private List<Card> cards = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterInCardset adapter;
    private int cardsetId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incardset);

        Bundle b = getIntent().getExtras();
        if(b.get("cardsetId")!=null){
            cardsetId = (int) b.get("cardsetId");
        }
        AppDatabase database = AppDatabase.getDatabase(this);
        CardDao cardDao = database.cardDao();
        cards = cardDao.allCards(cardsetId);
        recyclerView = findViewById(R.id.incardset);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterInCardset(this);
        recyclerView.setAdapter(adapter);
        adapter.addCategory(cards);
        enableSwipeToDeleteAndUndo();

    }
    private void enableSwipeToDeleteAndUndo() {

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                adapter.deleteItem(position, getApplicationContext());
                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Card deleted", Toast.LENGTH_SHORT).show();

            }
        });
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}
