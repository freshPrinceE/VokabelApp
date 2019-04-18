package com.example.i01002706.vokabelapp;

import android.arch.lifecycle.LiveData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import 	android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterCategory.ItemClickListener {

    private AdapterCategory adapter;
    private String m_Text="";
    List<Category> categories = new ArrayList<>();
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private  RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // data to populate the RecyclerView with
/*        final Category category1 = new Category("Spanisch");
        Category category2 = new Category("Italienisch");
        Category category3 = new Category("Englisch");

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);*/


        // set up the RecyclerView



        AppDatabase database = AppDatabase.getDatabase(this);
        categoryDao = database.categoryDao();

        allCategories = categoryDao.allCategories();



        allCategories.observe(this, new android.arch.lifecycle.Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categoryList) {
                adapter.addCategory(categoryList);
            }
        });

        recyclerView = findViewById(R.id.categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterCategory(this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        enableSwipeToDeleteAndUndo();
        FloatingActionButton myFab = findViewById(R.id.floatingButton);

        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               showDialog().show();

            }
        });

    }
    private void enableSwipeToDeleteAndUndo() {


        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                adapter.deleteItem(position, getApplicationContext());
                adapter.notifyDataSetChanged();
            }
        });
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position).getId() + " on row number " + position, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, CardsetActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", adapter.getItem(position).getId());
        intent.putExtras(b);
        startActivity(intent);
    }

    private AlertDialog.Builder showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Category");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                Category category1 = new Category();
                category1.setTitle(m_Text);

                categoryDao.insert(category1);
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
