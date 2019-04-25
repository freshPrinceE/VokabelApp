package com.example.i01002706.vokabelapp.Activity;

import android.arch.lifecycle.LiveData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import 	android.support.v7.widget.helper.ItemTouchHelper;

import com.example.i01002706.vokabelapp.Activity.CardsetActivity;
import com.example.i01002706.vokabelapp.Adapter.AdapterCategory;
import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Category;
import com.example.i01002706.vokabelapp.Database.CategoryDao;
import com.example.i01002706.vokabelapp.R;

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
                Toast.makeText(getBaseContext(), "Category deleted", Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    public void onItemClick(View view, int position) {
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
                if(m_Text!= null) {
                    Category category1 = new Category();
                    category1.setTitle(m_Text);

                    categoryDao.insert(category1);

                }else {
                    Toast.makeText(getBaseContext(), "Please enter a Name for the Category", Toast.LENGTH_SHORT).show();
                }
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
