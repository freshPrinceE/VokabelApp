package com.example.i01002706.vokabelapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import 	android.support.v7.widget.helper.ItemTouchHelper;

import com.example.i01002706.vokabelapp.Activity.CardsetActivity;
import com.example.i01002706.vokabelapp.Adapter.AdapterCategory;
import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Category;
import com.example.i01002706.vokabelapp.Database.CategoryDao;
import com.example.i01002706.vokabelapp.Helper.SwipeToDeleteCallback;
import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterCategory.ItemClickListener {

    private AdapterCategory adapter;
    private String m_Text="";
    private List<Category> categories = new ArrayList<>();
    private CategoryDao categoryDao;
    //private LiveData<List<Category>> allCategories;
    private  RecyclerView recyclerView;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppDatabase database = AppDatabase.getDatabase(this);
        categoryDao = database.categoryDao();
        categories = categoryDao.allCategories();
        //allCategories = categoryDao.allCategories();
        text = findViewById(R.id.text);

        recyclerView = findViewById(R.id.categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterCategory(this);
        adapter.addCategory(categories);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        enableSwipeToDeleteAndUndo();
        FloatingActionButton myFab = findViewById(R.id.floatingButton);
        if(adapter.getItemCount()<=0){
            text.setText("No Categories found, create some new!");
        }else{
            text.setText("");
        }
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               showDialog().show();

            }
        });

    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getApplicationContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                adapter.deleteItem(position, getApplicationContext());
                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Category deleted", Toast.LENGTH_SHORT).show();
                if(adapter.getItemCount()<=0){
                    text.setText("No Categories found, create some new!");
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, CardsetActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", adapter.getItem(position).getId());
        b.putString("title", adapter.getItem(position).getTitle());
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
                if(!m_Text.equals("")) {
                    Category category1 = new Category();
                    category1.setTitle(m_Text);

                    categoryDao.insert(category1);
                    adapter.addCategoryOne(category1);
                    adapter.notifyDataSetChanged();
                    text.setText("");

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

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();

    }
}
