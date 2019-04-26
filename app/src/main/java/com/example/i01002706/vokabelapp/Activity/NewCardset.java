package com.example.i01002706.vokabelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.i01002706.vokabelapp.Activity.NewCard;
import com.example.i01002706.vokabelapp.R;

public class NewCardset extends AppCompatActivity {

        private int categoryID;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.new_cardset);
            Bundle b = getIntent().getExtras();
            if(b.get("categoryID")!=null) {
                categoryID = (int) b.get("categoryID");
            }
            Button next = findViewById(R.id.next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), NewCard.class);
                    Bundle b = new Bundle();
                    EditText nameCardset = findViewById(R.id.nameCardset);
                    String name = nameCardset.getText().toString();
                    b.putString("name", name);
                    b.putInt("categoryID", categoryID);
                    intent.putExtras(b);
                    startActivity(intent);

                }
            });
        }


}
