package com.example.i01002706.vokabelapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Card;
import com.example.i01002706.vokabelapp.Database.CardDao;
import com.example.i01002706.vokabelapp.Database.CardsetDao;
import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;
import java.util.List;


public class NewCard extends AppCompatActivity {

    private static final List<Card> cards = new ArrayList<>();
    private String question;
    private String answer;
    private CardDao cardDao;
    private String name;
    private int categoryId;
    private  EditText q;
    private EditText a;
    private int cardsetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);


        Button nextCard = findViewById(R.id.nextCard);
        Button finish = findViewById(R.id.finish);
        AppDatabase database = AppDatabase.getDatabase(getApplicationContext());
        cardDao = database.cardDao();
        CardsetDao cardsetDao = database.cardsetDao();
        Bundle b = getIntent().getExtras();
        if(b.get("name")!=null) {
            name = (String) b.get("name");
        }
        if(b.get("categoryID")!=null) {
            categoryId = (int) b.get("categoryID");
        }
        if(b.get("cardsetId")!=null) {
            cardsetId = (int) b.get("cardsetId");
        }
        TextView tv = findViewById(R.id.titleCardset);
        tv.setText(name);

        //Erstellen der Karten, wenn "Next" gedrückt wird, löschen der Felder
        nextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q = findViewById(R.id.question);
                question = q.getText().toString();
                a = findViewById(R.id.answer);
                answer = a.getText().toString();

                if(!question.equals("") && !answer.equals("")) {
                    Card card = new Card();
                    card.setAntwort(answer);
                    card.setFrage(question);
                    card.setLevel(0);
                    cards.add(card);
                }else{
                    Toast.makeText(getBaseContext(),"Please enter a question and an answer", Toast.LENGTH_SHORT).show();
                }
                q.setText("");
                a.setText("");

            }
        });

        //Erstellen der Karten in der Datenbank, wenn "Finish" gedrückt wird
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q = findViewById(R.id.question);
                question = q.getText().toString();
                a = findViewById(R.id.answer);
                answer = a.getText().toString();
                /*Cardset cardset = new Cardset();
                cardset.setName(name);
                cardset.setCategoryId(categoryId);
                long id = cardsetDao.insert(cardset);*/
                if(!question.equals("")&& !answer.equals("")){
                    Card card = new Card();
                    card.setFrage(question);
                    card.setAntwort(answer);
                    card.setLevel(0);
                    cards.add(card);

                }
                for (Card card : cards){
                    card.setCardset_id(cardsetId);
                    cardDao.insert(card);
                }
                cards.clear();
                Intent intent = new Intent(getBaseContext(), CardsetActivity.class);
                intent.putExtra("id", categoryId);
                startActivity(intent);
                Toast.makeText(getBaseContext(), "New Cardset added", Toast.LENGTH_SHORT).show();

            }
        });


    }



}
