package com.example.i01002706.vokabelapp.Activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.i01002706.vokabelapp.Adapter.AdapterCardset;
import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Card;
import com.example.i01002706.vokabelapp.Database.CardDao;
import com.example.i01002706.vokabelapp.Database.Cardset;
import com.example.i01002706.vokabelapp.Database.CardsetDao;
import com.example.i01002706.vokabelapp.Helper.SwipeToDeleteCallback;
import com.example.i01002706.vokabelapp.R;

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



        Bundle b = getIntent().getExtras();
        if(b.get("id")!=null){
            categoryId = (int) b.get("id");
        }
        if(b.get("title")!=null){
            String title = (String) b.get("title");
            setTitle(title);
        }
        final AppDatabase database = AppDatabase.getDatabase(this);
        final CardsetDao cardsetDao = database.cardsetDao();
        cardsets = cardsetDao.allCardsets(categoryId);




        recyclerView = findViewById(R.id.cardset);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterCardset(this);
        adapter.setClickListener(new AdapterCardset.OnItemClickListener() {

            //ClickListener für die Cardsets im Recylclerview
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), InCardsetActivity.class);
                Bundle b = new Bundle();

                b.putInt("cardsetId", cardsets.get(position).getId());
                b.putString("title", cardsets.get(position).getName());
                intent.putExtras(b);
                startActivity(intent);
            }
            //ClickListener für die Play-Buttons in den Cardsets
            @Override
            public void onPlayButtonClick(int position) {
                CardDao cardDao = database.cardDao();
                List<Card> cards = cardDao.allCards(adapter.getItem(position).getId());
                Log.d("Test:","Cardsize:" + cards.size());
                if(!(cards.size()<=0)) {
                    Intent intent = new Intent(getApplicationContext(), Game.class);
                    Bundle b = new Bundle();

                    b.putInt("cardsetId", cardsets.get(position).getId());
                    b.putString("title", cardsets.get(position).getName());
                    intent.putExtras(b);
                    startActivity(intent);
                }else{
                    Toast.makeText(getBaseContext(),"Cardset has no Cards", Toast.LENGTH_LONG).show();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.addCategory(cardsets);
        enableSwipeToDeleteAndUndo();
        TextView text = findViewById(R.id.text);
        if(adapter.getItemCount()<=0){
            text.setText("No Cardsets found. Create some new!");
        }else{
            text.setText("");
        }
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

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getApplicationContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                adapter.deleteItem(position, getApplicationContext());
                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Cardset deleted", Toast.LENGTH_SHORT).show();

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void changeActivity(){
        Intent intent = new Intent(this, NewCardset.class);
        intent.putExtra("categoryID", categoryId);
        startActivity(intent);
    }
    @Override
    public void onItemClick(View view, int position) {

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();

    }
}
