package com.example.i01002706.vokabelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Game extends AppCompatActivity {

    private int cardsetId;
    private List<Card> cards = new ArrayList<>();
    private CardDao cardDao;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle b = getIntent().getExtras();
        if(b.get("cardsetId")!=null){
            cardsetId = (int) b.get("cardsetId");
        }
        AppDatabase database = AppDatabase.getDatabase(this);
        cardDao = database.cardDao();
        cards = cardDao.allCards(cardsetId);
        TextView question = findViewById(R.id.gameQuestion);
        TextView answer = findViewById(R.id.gameQuestion);

        question.setText(cards.get(count).getFrage());
        answer.setText(cards.get(count).getAntwort());
    }
}
