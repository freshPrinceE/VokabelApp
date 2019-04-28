package com.example.i01002706.vokabelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Cardset;
import com.example.i01002706.vokabelapp.Database.CardsetDao;
import com.example.i01002706.vokabelapp.R;

public class NewCardset extends AppCompatActivity {

        private int categoryID;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.new_cardset);
            final AppDatabase database = AppDatabase.getDatabase(getApplicationContext());
            Bundle b = getIntent().getExtras();

            if(b.get("categoryID")!=null) {
                categoryID = (int) b.get("categoryID");
            }
            Button next = findViewById(R.id.next);
            //Weiter zur Seite der Erstellung von Karten (NewCard.class), wenn "Next" geklickt wird (Wenn der eigegebene Name nicht leer ist)
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), NewCard.class);
                    Bundle b = new Bundle();
                    EditText nameCardset = findViewById(R.id.nameCardset);
                    String name = nameCardset.getText().toString();
                    if(!name.equals("")) {
                        CardsetDao cardsetDao = database.cardsetDao();
                        Cardset cardset = new Cardset();
                        cardset.setCategoryId(categoryID);
                        cardset.setName(name);
                        int id = (int) cardsetDao.insert(cardset);
                        b.putString("name", name);
                        b.putInt("categoryID", categoryID);
                        b.putInt("cardsetId", id);
                        intent.putExtras(b);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getBaseContext(),"Please enter a name for the cardset", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }


}
