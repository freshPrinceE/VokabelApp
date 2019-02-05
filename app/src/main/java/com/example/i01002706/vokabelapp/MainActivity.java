package com.example.i01002706.vokabelapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private MyRecyclerViewAdapter adapter;
    private String m_Text="";
    private AlertDialog.Builder builder;
    final ArrayList<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // data to populate the RecyclerView with
        final Category category1 = new Category("Spanisch");
        Category category2 = new Category("Italienisch");
        Category category3 = new Category("Englisch");

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, categories);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.floatingButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                showDialog().show();

            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    private AlertDialog.Builder showDialog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Category");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                Category category = new Category(m_Text);
                categories.add(category);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    return builder;
    }

}
