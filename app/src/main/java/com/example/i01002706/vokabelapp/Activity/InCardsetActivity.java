package com.example.i01002706.vokabelapp.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i01002706.vokabelapp.Adapter.AdapterInCardset;
import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Card;
import com.example.i01002706.vokabelapp.Database.CardDao;
import com.example.i01002706.vokabelapp.Helper.SwipeToDeleteCallback;
import com.example.i01002706.vokabelapp.R;

import java.util.List;

public class InCardsetActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterInCardset adapter;
    private int cardsetId;
    private AppDatabase database;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incardset);
        database = AppDatabase.getDatabase(getApplicationContext());
        Bundle b = getIntent().getExtras();
        if(b.get("cardsetId")!=null){
            cardsetId = (int) b.get("cardsetId");
        }
        if(b.get("title")!=null){
            String title = (String) b.get("title");
            setTitle(title);
        }
        text = findViewById(R.id.text);
        AppDatabase database = AppDatabase.getDatabase(this);
        CardDao cardDao = database.cardDao();
        List<Card> cards = cardDao.allCards(cardsetId);
        recyclerView = findViewById(R.id.incardset);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterInCardset(this);
        recyclerView.setAdapter(adapter);
        adapter.addCategory(cards);
        enableSwipeToDeleteAndUndo();
        text.setText("");
        if(adapter.getItemCount()<=0){
            text.setText("No Cards found, add some new!");
        }
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }
    private void showDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View dialog_layout = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        // Create the text field in the alert dialog...
        final EditText text1 = dialog_layout.findViewById(R.id.text1);
        final EditText text2 = dialog_layout.findViewById(R.id.text2);
        Button add = dialog_layout.findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = text1.getText().toString();
                String answer = text2.getText().toString();
                addCard(question, answer);
                alertDialog.dismiss();
                text.setText("");
            }
        });
        alertDialog.setView(dialog_layout);
        alertDialog.show();
    }
    private void addCard(String question, String answer){
        CardDao cardDao = database.cardDao();
        Card card = new Card();
        card.setCardset_id(cardsetId);
        card.setAntwort(answer);
        card.setFrage(question);
        card.setLevel(0);
        cardDao.insert(card);
        adapter.addCard(card);
    }
    private void enableSwipeToDeleteAndUndo() {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getApplicationContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                adapter.deleteItem(position, getApplicationContext());
                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Card deleted", Toast.LENGTH_SHORT).show();
                if(adapter.getItemCount()<=0){
                    text.setText("No Cards found, add some new!");
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();

    }
}
