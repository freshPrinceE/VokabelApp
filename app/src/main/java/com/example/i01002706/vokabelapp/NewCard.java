package com.example.i01002706.vokabelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewCard extends AppCompatActivity {

    private static List<Card> cards = new ArrayList<>();
    private String question;
    private String answer;
    private CardDao cardDao;
    private CardsetDao cardsetDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        final EditText q = (EditText) findViewById(R.id.question);
        question = q.getText().toString();
        final EditText a = (EditText) findViewById(R.id.answer);
        answer = a.getText().toString();
        Button nextCard = (Button) findViewById(R.id.nextCard);
        Button finish = (Button) findViewById(R.id.finish);
        AppDatabase database = AppDatabase.getDatabase(this);
        cardDao = database.cardDao();
        cardsetDao = database.cardsetDao();
        Bundle b = getIntent().getExtras();
        final String name = (String) b.get("name");
        final int categoryId = (int) b.get("categoryID");


        TextView tv = findViewById(R.id.titleCardset);
        tv.setText(name);

        nextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question != "" && answer != "") {
                    Card card = new Card();
                    card.setAntwort(answer);
                    card.setFrage(question);
                    cards.add(card);
                }
                q.setText("");
                a.setText("");

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cardset cardset = new Cardset();
                cardset.setName(name);
                cardset.setCategoryId(categoryId);
                int id = (int) cardsetDao.insert(cardset);
                if(question != ""){
                    Card card = new Card();
                    card.setFrage(question);
                    card.setAntwort(answer);
                }
                for (Card card : cards){
                    card.setCardset_id(id);
                    cardDao.insert(card);
                }
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                finish();
                startActivity(intent);

            }
        });

    }

}
