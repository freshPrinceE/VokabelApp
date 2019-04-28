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
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import 	android.support.v7.widget.helper.ItemTouchHelper;

import com.example.i01002706.vokabelapp.Adapter.AdapterCategory;
import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Category;
import com.example.i01002706.vokabelapp.Database.CategoryDao;
import com.example.i01002706.vokabelapp.Helper.SwipeToDeleteCallback;
import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

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
        //öffnen der Cardset-Seite zu dem angeklickten Category
        adapter.setClickListener(new AdapterCategory.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), CardsetActivity.class);
                Bundle b = new Bundle();
                b.putInt("id", adapter.getItem(position).getId());
                b.putString("title", adapter.getItem(position).getTitle());
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void onEditClick(int position) {
                changeName(position).show();
            }
        });
        recyclerView.setAdapter(adapter);
        enableSwipeToDeleteAndUndo();
        FloatingActionButton myFab = findViewById(R.id.floatingButton);

        //Falls keine Categories vorhanden sind Text anzeigen
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

    private AlertDialog.Builder changeName(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change name");

        // Set up the input
        final EditText input = new EditText(this);
        final int position2 = position;
        input.setText(adapter.getItem(position).getTitle());
        //input.setText(adapter.getItem(position).getTitle());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if(!m_Text.equals("")) {
                    Category category1 = adapter.getItem(position2);
                    category1.setTitle(m_Text);
                    categoryDao.update(category1);
                    categories.get(position2).setTitle(m_Text);
                    adapter.addCategory(categoryDao.allCategories());
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

                    int id = (int) categoryDao.insert(category1);
                    category1.setId(id);
                    categories.add(category1);
                    adapter.addCategory(categoryDao.allCategories());
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

    //Searchbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        //adapter.addCategory(categories);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String userInput = s.toLowerCase();
        List<Category> newList = new ArrayList<>();
        for(Category category: categories){
            if(category.getTitle().toLowerCase().contains(userInput)){
                newList.add(category);
            }
        }
        adapter.addCategory(newList);
        return true;
    }
}
