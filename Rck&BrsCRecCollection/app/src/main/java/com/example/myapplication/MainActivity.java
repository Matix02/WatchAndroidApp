package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView_Config adapter;
    private FloatingActionButton fab;
    private CheckBox isWatched;
    private RecyclerView recyclerView;
    private List<String> L1 = new ArrayList<String>();
    private List<String> L2 = new ArrayList<String>();
    private DatabaseReference databaseReference;
    private List<Element> elements = new ArrayList<>();
    private RecyclerView_Config.ElementAdapter elementAdapter;
    private List<String> keys;
    private String key;
    private List<Element> elements2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//Dodać może obrazkowe nagrody, że jak poleca Rock to bedzie R przy tytule, a jak Borys to B, natomiast w obu przypadkach to R&B
        //chyba Rck&Brs byłoby za długie
        recyclerView = (RecyclerView) findViewById(R.id.ele_listView);
      //  isWatched = (CheckBox) findViewById(R.id.checkBox);
       //   isWatched = (CheckBox)


        new FirebaseDatabaseHelper().readElements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Element> elements, List<String> keys) {
                findViewById(R.id.loading_elements).setVisibility(View.GONE);
                 new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, elements, keys);

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

        fab =  findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewElement.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Element> getElements(){
        ArrayList<Element> elemTryList = new ArrayList<>();
        Element e;

        for (int i=0; i<elements.size(); i++){
            e = new Element(elements.get(i).getTitle(), elements.get(i).getCategory(), elements.get(i).isWatched());
            elemTryList.add(e);

        }
        return elemTryList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.nav_bar_items, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item1);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Search Here");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//
//                return false;
//            }
//        });

        return true;
    }
//spróbowac utworzyc tak jak to jest pokazane w NoWatchFragment, czyli set Config jako new Adapter albo utworzyc tam konstruktor, i zbudować
    //go tam samo, jako było w przypadku ListAdaptera
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.search_item1:
//                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
//                return true;
            case R.id.item1:
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case 121:
            //   adapter.removeItem(item.getGroupId());
            //   adapter.notifyDataSetChanged();
               // int cnt = item
               // String nr = ((String) cnt);
           /*     new FirebaseDatabaseHelper().deleteElement(item.getItemId(), new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Element> elements, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_LONG).show();
                    }
                });*/
                return true;
            case 122:
                return true;
            default:
                return  super.onContextItemSelected(item);
        }

    }

}
