package com.example.i01002706.vokabelapp.Activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private int count;
    private int bound = 100;
    private Card currentCard;
    private TextView answer;
    private Button gewusst;
    private Button nichtGewusst;
    //private ImageView image;
    private TextView question;
    private List<Card> level0= new ArrayList<>();
    private List<Card> level1= new ArrayList<>();
    private List<Card> level2= new ArrayList<>();
    private List<Card> level3= new ArrayList<>();
    private List<Card> level4= new ArrayList<>();



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle b = getIntent().getExtras();
        if(b.get("cardsetId")!=null){
            cardsetId = (int) b.get("cardsetId");
        }

        count = 0;
        final AppDatabase database = AppDatabase.getDatabase(this);
        final CardDao cardDao = database.cardDao();
        cards = cardDao.allCards(cardsetId);
        defineLevels(cards);
        chooseCard();
        question = findViewById(R.id.gameQuestion);
        answer = findViewById(R.id.gameAnswer);
        //image = findViewById(R.id.imageView2);
        answer.setText("?");
        //answer.setTextSize(40);
        answer.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //question.setTextSize(40);
        question.setText(currentCard.getFrage());
        question.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        gewusst = findViewById(R.id.gewusst);
        nichtGewusst = findViewById(R.id.nichtGewusst);
        gewusst.setWidth(getWindowManager().getDefaultDisplay().getWidth()/2);
        gewusst.setHeight(getWindowManager().getDefaultDisplay().getHeight()/4);
        nichtGewusst.setWidth(getWindowManager().getDefaultDisplay().getWidth()/2);
        nichtGewusst.setHeight(getWindowManager().getDefaultDisplay().getHeight()/4);

        gewusst.setVisibility(View.INVISIBLE);
        nichtGewusst.setVisibility(View.INVISIBLE);
        gewusst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Eintrag in der Tabelle
                if(currentCard.getLevel()<4) {
                    currentCard.setLevel(currentCard.getLevel() + 1);
                    cardDao.update(currentCard);

                }
                cards = cardDao.allCards(cardsetId);
                defineLevels(cards);
                chooseCard();
                loadData();
            }
        });
        nichtGewusst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentCard.getLevel()>0) {
                    cardDao.update(currentCard);
                    currentCard.setLevel(currentCard.getLevel() - 1);
                }
                //Eintrag in der Tabelle
                cards = cardDao.allCards(cardsetId);
                defineLevels(cards);
                chooseCard();
                loadData();
            }
        });
        View view = findViewById(R.id.touch);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setText(currentCard.getAntwort());
                gewusst.setVisibility(View.VISIBLE);
                nichtGewusst.setVisibility(View.VISIBLE);
            }
        });
        //view.setBackgroundColor(Color.GRAY);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //answer.setText(currentCard.getAntwort());
                Log.d("Test", "image" + currentCard.getImage());
               /* try {
                    Uri uri = Uri.parse(currentCard.getImage());
                    InputStream imageStream = getContentResolver().openInputStream(uri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    image.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*/

                //gewusst.setVisibility(View.VISIBLE);
                //nichtGewusst.setVisibility(View.VISIBLE);
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
    private void chooseCard(){
        Random r = new Random();
        int level = r.nextInt(bound);
        if(level >= 0 && level <40){
            if(level0.size()<=0){
                chooseCard();
                return;
            }
            Card card = level0.get( r.nextInt(level0.size()));
            if(!(currentCard==card)) {
                currentCard = card;
            }else{
                chooseCard();
                return;
            }
        }
        else if(level >= 40 && level < 65){
            if(level1.size()<=0){
                chooseCard();
                return;
            }
            Card card = level1.get( r.nextInt(level1.size()));
            if(!(currentCard==card)) {
                currentCard = card;
            }else{
                chooseCard();
                return;
            }
        }
        else if(level >= 65 && level < 80){
            if(level2.size()<=0){
                chooseCard();
                return;
            }
            Card card = level2.get( r.nextInt(level2.size()));
            if(!(currentCard==card)) {
                currentCard = card;
            }else{
                chooseCard();
                return;
            }
        }
        else if(level >= 80 && level < 95){
            if(level3.size()<=0){
                chooseCard();
                return;
            }

            Card card = level3.get( r.nextInt(level3.size()));
            if(!(currentCard==card)) {
                currentCard = card;
            }else{
                chooseCard();
                return;
            }
        }
        else if(level >= 95 && level < 100){
            if(level4.size()<=0){
                chooseCard();
                return;
            }
            Card card = level4.get( r.nextInt(level4.size()));
            if(!(currentCard==card)) {
                currentCard = card;
            }else{
                chooseCard();
                return;
            }
        }
    }
}