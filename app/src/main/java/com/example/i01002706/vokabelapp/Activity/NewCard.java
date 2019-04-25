package com.example.i01002706.vokabelapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i01002706.vokabelapp.Activity.MainActivity;
import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Card;
import com.example.i01002706.vokabelapp.Database.CardDao;
import com.example.i01002706.vokabelapp.Database.Cardset;
import com.example.i01002706.vokabelapp.Database.CardsetDao;
import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;
import java.util.List;

public class NewCard extends AppCompatActivity {

    private static final int PICK_IMG = 100;
    private static List<Card> cards = new ArrayList<>();
    private String question;
    private String answer;
    private CardDao cardDao;
    private CardsetDao cardsetDao;
    private String name;
    private int categoryId;
    private  EditText q;
    private EditText a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);


        Log.d("Test", "Question: " + question + "  Answer: " + answer);
        Button nextCard = findViewById(R.id.nextCard);
        Button finish = findViewById(R.id.finish);
        AppDatabase database = AppDatabase.getDatabase(this);
        cardDao = database.cardDao();
        cardsetDao = database.cardsetDao();
        Bundle b = getIntent().getExtras();
        name = (String) b.get("name");
        categoryId = (int) b.get("categoryID");

        TextView tv = findViewById(R.id.titleCardset);
        tv.setText(name);

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
                }
                q.setText("");
                a.setText("");

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q = findViewById(R.id.question);
                question = q.getText().toString();
                a = findViewById(R.id.answer);
                answer = a.getText().toString();
                Cardset cardset = new Cardset();
                cardset.setName(name);
                cardset.setCategoryId(categoryId);
                int id = (int) cardsetDao.insert(cardset);
                if(!question.equals("")&& !answer.equals("")){
                    Card card = new Card();
                    card.setFrage(question);
                    card.setAntwort(answer);
                    card.setLevel(0);
                    cards.add(card);

                }
                for (Card card : cards){
                    card.setCardset_id(id);
                    cardDao.insert(card);
                }
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                finish();
                startActivity(intent);
                Toast.makeText(getBaseContext(), "New Cardset added", Toast.LENGTH_SHORT).show();

            }
        });

    }
}