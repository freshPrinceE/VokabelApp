package com.example.i01002706.vokabelapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i01002706.vokabelapp.Adapter.AdapterCardset;
import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Card;
import com.example.i01002706.vokabelapp.Database.CardDao;
import com.example.i01002706.vokabelapp.Database.Cardset;
import com.example.i01002706.vokabelapp.Database.CardsetDao;
import com.example.i01002706.vokabelapp.Database.Category;
import com.example.i01002706.vokabelapp.Helper.SwipeToDeleteCallback;
import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;
import java.util.List;


public class CardsetActivity extends AppCompatActivity {
    private AdapterCardset adapter;
    private int categoryId;
    private List<Cardset> cardsets = new ArrayList<>();
    private RecyclerView recyclerView;
    private String m_Text="";
    private CardsetDao cardsetDao;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardset);


        Bundle b = getIntent().getExtras();
        if(b.get("id")!=null){
            categoryId = (int) b.get("id");
        }
        if(b.get("title")!=null){
            String title = (String) b.get("title");
            setTitle(title);
        }
        final AppDatabase database = AppDatabase.getDatabase(this);
        cardsetDao = database.cardsetDao();
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

            @Override
            public void onEditButtonClick(int position) {
                changeName(position).show();
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.addCategory(cardsets);
        enableSwipeToDelete();
        text = findViewById(R.id.text);
        //Anzeigen eines Textes, falls keine Cardsets vorhanden sind
        if(adapter.getItemCount()<=0){
            text.setText("No Cardsets found. Create some new!");
        }else{
            text.setText("");
        }
        FloatingActionButton myFab = findViewById(R.id.floatingButton2);
        //Erstellen eines neuen Cardsets
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewCardset.class);
                intent.putExtra("categoryID", categoryId);
                startActivity(intent);
            }
        });
    }

    private void enableSwipeToDelete() {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getApplicationContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                adapter.deleteItem(position, getApplicationContext());
                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Cardset deleted", Toast.LENGTH_SHORT).show();
                if(adapter.getItemCount()<=0){
                    text.setText("No Cardsets found. Create some new!");
                }else{
                    text.setText("");
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private AlertDialog.Builder changeName(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change name");

        // Set up the input
        final EditText input = new EditText(this);
        final int position2 = position;
        input.setText(adapter.getItem(position).getName());
        //input.setText(adapter.getItem(position).getTitle());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if(!m_Text.equals("")) {
                    Cardset cardset = adapter.getItem(position2);
                    cardset.setName(m_Text);
                    cardsetDao.update(cardset);
                    cardsets.get(position2).setName(m_Text);
                    adapter.addCategory(cardsetDao.allCardsets(categoryId));
                    adapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(getBaseContext(), "Please enter a Name for the Cardset", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();

    }
}
