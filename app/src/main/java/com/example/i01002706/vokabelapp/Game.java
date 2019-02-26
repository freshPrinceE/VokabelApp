package com.example.i01002706.vokabelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Game extends AppCompatActivity {

    private int cardsetId;
    private List<Card> cards = new ArrayList<>();
    private CardDao cardDao;
    private static int count = 0;
    private TextView answer;
    private Button gewusst;
    private Button nichtGewusst;
    private TextView question;

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
        question = findViewById(R.id.gameQuestion);
        answer = findViewById(R.id.gameAnswer);
        answer.setText("?");
        question.setText(cards.get(count).getFrage());
        gewusst = findViewById(R.id.gewusst);
        nichtGewusst = findViewById(R.id.nichtGewusst);
        gewusst.setVisibility(View.INVISIBLE);
        nichtGewusst.setVisibility(View.INVISIBLE);
        gewusst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Eintrag in der Tabelle
                loadData();
            }
        });
        nichtGewusst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Eintrag in der Tabelle
                loadData();
            }
        });
        View view = findViewById(R.id.touch);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setText(cards.get(count).getAntwort());
                gewusst.setVisibility(View.VISIBLE);
                nichtGewusst.setVisibility(View.VISIBLE);
            }
        });


    }
    private void loadData(){
        if(count>=cards.size()-1){
            count = 0;
        }else {
            count++;
        }


        answer.setText("?");
        question.setText(cards.get(count).getFrage());
        gewusst = findViewById(R.id.gewusst);
        nichtGewusst = findViewById(R.id.nichtGewusst);
        gewusst.setVisibility(View.INVISIBLE);
        nichtGewusst.setVisibility(View.INVISIBLE);
    }
}
