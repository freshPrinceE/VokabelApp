package com.example.i01002706.vokabelapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CardsetActivity extends AppCompatActivity implements AdapterCardset.ItemClickListener {
    AdapterCardset adapter;
    private CardsetDao cardsetDao;
    private int categoryId;
    private List<Cardset> cardsets = new ArrayList<>();
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




        RecyclerView recyclerView = findViewById(R.id.cardset);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterCardset(this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.addCategory(cardsets);
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.floatingButton2);
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

    private void changeActivity(){
        Intent intent = new Intent(this, NewCardset.class);
        intent.putExtra("id", categoryId);
        startActivity(intent);
    }
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, Game.class);
        Bundle b = new Bundle();

        b.putInt("cardsetsId", cardsets.get(position).getId());
        intent.putExtras(b);
        startActivity(intent);
    }
}
