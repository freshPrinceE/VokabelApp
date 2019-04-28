package com.example.i01002706.vokabelapp.Activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Card;
import com.example.i01002706.vokabelapp.Database.CardDao;
import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends AppCompatActivity {

    private int cardsetId;
    private List<Card> cards = new ArrayList<>();
    private Card currentCard;
    private TextView answer;
    private Button gewusst;
    private Button nichtGewusst;
    private TextView question;
    private final List<Card> level0= new ArrayList<>();
    private final List<Card> level1= new ArrayList<>();
    private final List<Card> level2= new ArrayList<>();
    private final List<Card> level3= new ArrayList<>();
    private final List<Card> level4= new ArrayList<>();
    private Random r;
    private CardDao cardDao;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle b = getIntent().getExtras();
        if(b.get("cardsetId")!=null){
            cardsetId = (int) b.get("cardsetId");
        }
        if(b.get("title")!=null){
            String title = (String) b.get("title");
            setTitle(title);
        }
        init();
        gewusst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentCard.getLevel()<4) {
                    currentCard.setLevel(currentCard.getLevel() + 1);
                    cardDao.update(currentCard);

                }
                //cards = cardDao.allCards(cardsetId);
                defineLevels(cards);
                chooseCard();
                loadData();

            }
        });
        nichtGewusst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentCard.getLevel()>0) {
                    currentCard.setLevel(currentCard.getLevel() - 1);
                    cardDao.update(currentCard);

                }
                //Eintrag in der Tabelle
                //cards = cardDao.allCards(cardsetId);
                defineLevels(cards);
                chooseCard();
                loadData();

            }
        });

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setText(currentCard.getAntwort());
                gewusst.setVisibility(View.VISIBLE);
                nichtGewusst.setVisibility(View.VISIBLE);
            }
        });

    }

    private void init(){
        r = new Random();
        currentCard = new Card();
        currentCard.setFrage("");
        currentCard.setAntwort("");
        currentCard.setLevel(0);
        AppDatabase database = AppDatabase.getDatabase(this);
        cardDao = database.cardDao();
        cards = cardDao.allCards(cardsetId);
        defineLevels(cards);
        chooseCard();
        question = findViewById(R.id.gameQuestion);
        answer = findViewById(R.id.gameAnswer);
        answer.setText("?");
        answer.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        answer.setMovementMethod(new ScrollingMovementMethod());
        question.setText(currentCard.getFrage());
        question.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        question.setMovementMethod(new ScrollingMovementMethod());
        gewusst = findViewById(R.id.gewusst);
        nichtGewusst = findViewById(R.id.nichtGewusst);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        gewusst.setWidth(size.x/2);
        gewusst.setHeight(size.y/4);
        gewusst.setBackgroundColor(Color.rgb(125, 244, 66));
        nichtGewusst.setWidth(size.x/2);
        nichtGewusst.setHeight(size.y/4);
        nichtGewusst.setBackgroundColor(Color.rgb(244, 95, 65));
        gewusst.setVisibility(View.INVISIBLE);
        nichtGewusst.setVisibility(View.INVISIBLE);

    }
    private void loadData(){

        answer.setText("?");
        question.setText(currentCard.getFrage());
        gewusst = findViewById(R.id.gewusst);
        nichtGewusst = findViewById(R.id.nichtGewusst);
        gewusst.setVisibility(View.INVISIBLE);
        nichtGewusst.setVisibility(View.INVISIBLE);
    }
    private void defineLevels(List<Card> cards){
        for (Card card: cards) {
            if(card.getLevel()==0){
                level0.add(card);
            }
            else if(card.getLevel()==1){
                level1.add(card);
            }
            else if(card.getLevel()==2){
                level2.add(card);
            }
            else if(card.getLevel()==3){
                level3.add(card);
            }
            else if(card.getLevel()==4){
                level4.add(card);
            }
        }
    }
    //Auswählen der Karte nach Wahrscheinlichkeit, anhand des Levels
    private void chooseCard(){
        int bound = 100;
        int level = r.nextInt(bound);
        if((level >= 95 && level < 100)){
            chooseFromLevel4();
        }else if((level >= 80 && level < 95)){
            chooseFromLevel3();
        }else if((level >= 65 && level < 80)){
            chooseFromLevel2();
        }else if((level >= 40 && level < 65)){
            chooseFromLevel1();
        }else if((level >= 0 && level <40) ){
            chooseFromLevel0();
        }
    }
    //Karte zufällig auswählen von Level0, falls keine vorhanden, aus Level1 wählen
    private void chooseFromLevel0(){
        if(level0.size()<=0){
            chooseFromLevel1();
        }else{
            Card card = level0.get( r.nextInt(level0.size()));
            if((currentCard.getFrage().equals(card.getFrage()))&&cards.size()>1) {
                chooseCard();
            }else{
                currentCard = card;
            }
        }
    }
    //Karte zufällig auswählen von Level1, falls keine vorhanden, aus Level2 wählen
    private void chooseFromLevel1(){
        if(level1.size()<=0){
            chooseFromLevel2();
        }else{
            Card card = level1.get( r.nextInt(level1.size()));
            if((currentCard.getFrage().equals(card.getFrage()))&&cards.size()>1) {
                chooseCard();
            }else{
                currentCard = card;
            }
        }
    }
    //Karte zufällig auswählen von Level2, falls keine vorhanden, aus Level3 wählen
    private void chooseFromLevel2(){
        if(level2.size()<=0){
            chooseFromLevel3();
        }else{
            Card card = level2.get( r.nextInt(level2.size()));
            if((currentCard.getFrage().equals(card.getFrage()))&&cards.size()>1) {
                chooseCard();
            }else{
                currentCard = card;
            }
        }
    }
    //Karte zufällig auswählen von Level3, falls keine vorhanden, aus Level4 wählen
    private void chooseFromLevel3(){
        if(level3.size()<=0){
            chooseFromLevel4();
        }else{
            Card card = level3.get( r.nextInt(level3.size()));
            if((currentCard.getFrage().equals(card.getFrage()))&&cards.size()>1) {
                chooseCard();
            }else{
                currentCard = card;
            }
        }
    }
    //Karte zufällig auswählen von Level4, falls keine vorhanden, aus Level0 wählen
    private void chooseFromLevel4(){
        if(level4.size()<=0){
            chooseFromLevel0();
        }else{
            Card card = level4.get( r.nextInt(level4.size()));
            if((currentCard.getFrage().equals(card.getFrage()))&&cards.size()>1) {
                chooseCard();
            }else{
                currentCard = card;
            }
        }
    }

}
