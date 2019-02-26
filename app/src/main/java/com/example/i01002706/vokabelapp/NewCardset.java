package com.example.i01002706.vokabelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewCardset extends AppCompatActivity {


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.new_cardset);
            Bundle b = getIntent().getExtras();
            final int categoryID = (int) b.get("categoryID");
            Button next = (Button) findViewById(R.id.next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), NewCard.class);
                    Bundle b = new Bundle();
                    EditText nameCardset = findViewById(R.id.nameCardset);
                    String name = nameCardset.getText().toString();
                    b.putString("name", name);
                    intent.putExtras(b);
                    intent.putExtra("categoryID", categoryID);
                    startActivity(intent);

                }
            });
        }


}
