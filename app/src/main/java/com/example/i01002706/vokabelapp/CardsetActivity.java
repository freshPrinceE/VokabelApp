package com.example.i01002706.vokabelapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CardsetActivity extends AppCompatActivity implements AdapterCardset.ItemClickListener {
    private AdapterCardset adapter;
    private CardsetDao cardsetDao;
    private int categoryId;
    private List<Cardset> cardsets = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardset);

        ArrayList<Cardset> categories = new ArrayList<>();
        /*Category category1 = new Category("Lektion1");
        Category category2 = new Category("Lektion2");
        Category category3 = new Category("Lektion3");

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);*/



        Bundle b = getIntent().getExtras();
        if(b.get("id")!=null){
            categoryId = (int) b.get("id");
        }
        AppDatabase database = AppDatabase.getDatabase(this);
        final CardsetDao cardsetDao = database.cardsetDao();
        cardsets = cardsetDao.allCardsets(categoryId);




        recyclerView = findViewById(R.id.cardset);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterCardset(this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.addCategory(cardsets);
        enableSwipeToDeleteAndUndo();
        FloatingActionButton myFab = findViewById(R.id.floatingButton2);
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Cardset kks = new Cardset();
                kks.setName("Test");
                kks.setCategoryId(categoryId);
                cardsetDao.insert(kks);*/
                changeActivity();
            }
        });
    }
    private void enableSwipeToDeleteAndUndo() {

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Log.d("Cardtest", "Test2");

                int position = viewHolder.getAdapterPosition();
                adapter.deleteItem(position, getApplicationContext());
                adapter.notifyDataSetChanged();
            }
        });
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
    private void changeActivity(){
        Intent intent = new Intent(this, NewCardset.class);
        intent.putExtra("categoryID", categoryId);
        startActivity(intent);
    }
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, Game.class);
        Bundle b = new Bundle();

        b.putInt("cardsetId", cardsets.get(position).getId());
        intent.putExtras(b);
        startActivity(intent);
    }
}
