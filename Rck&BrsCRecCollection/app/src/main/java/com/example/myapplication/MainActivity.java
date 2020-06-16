package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.widget.SearchView;

import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private RecyclerView_Config adapter;
    private FloatingActionButton fab;
    private CheckBox isWatched;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<Element> elements = new ArrayList<>();
    private List<Element> elementsFilter = new ArrayList<>();
    private RecyclerView_Config.ElementAdapter elementAdapter;
    private List<String> keys;
    private String key;
    private List<Element> elements2 = new ArrayList<>();


    //co niby nie ma połączenia, brak reakcji na to co jest wpisywane, funkcja w debbugerze - nieaktywna
    //dwa albo trzy filmiki zostały zamieszczone, by to ogarnąć

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Dodać może obrazkowe nagrody, że jak poleca Rock to bedzie R przy tytule, a jak Borys to B, natomiast w obu przypadkach to R&B
        //chyba Rck&Brs byłoby za długie
        //mozna by było dodać info o tym w jakiej minucie w odcinku było to omwienie - czyli klik na przycisk i wyskakuje youtube w otwarym odcinkiem i w danej minucie
        recyclerView = (RecyclerView) findViewById(R.id.ele_listView);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(this));


        new FirebaseDatabaseHelper().readElements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Element> elements, List<String> keys) {
                findViewById(R.id.loading_elements).setVisibility(View.GONE);
//!!!!!!!!!!! Ważne - filtrowana lista jest pusta, dlatego, ze wystepuje tylko w tym wywolaniu nizej- mozna rozdzielic ta metode setConfig na pół że napierw jest konstr
                // konstrukcja adaptera z lista elements i keys a nastepie metoda z tej metody daje nam setAdapter, lub sprobowac zrobic metode getList czy cos i stąd WYCIAGANAC
                // tą listę
                elementAdapter = new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, elements, keys, elementsFilter, elementAdapter);
                //new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, elements, keys, elementsFilter);
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
    @Override
    public boolean onQueryTextChange(final String newText) {
        new FirebaseDatabaseHelper().readElements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Element> elements, List<String> keys) {
                String userInput = newText.toLowerCase();
                List<Element> newList = new ArrayList<>();

                for (Element name : elements){
                    if(name.getTitle().contains(userInput)){
                        newList.add(name);
                    }
                }
                elementAdapter.updateList(newList);
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

        return true;
    }
    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        elementAdapter = new RecyclerView_Config().new ElementAdapter(elements, keys, elementsFilter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(elementAdapter);
    }
    private RecyclerView_Config.ElementAdapter setUpRecyclerView(RecyclerView_Config.ElementAdapter  elementAdapter){
        elementAdapter = new RecyclerView_Config().new ElementAdapter(elements, keys, elementsFilter);
        return elementAdapter;
    }
//    private ArrayList<Element> getElements(){
//        ArrayList<Element> elemTryList = new ArrayList<>();
//        Element e;
//
//        for (int i=0; i<elements.size(); i++){
//            e = new Element(elements.get(i).getTitle(), elements.get(i).getCategory(), elements.get(i).isWatched());
//            elemTryList.add(e);
//        }
//        return elemTryList;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_bar_items, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item1);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }
      //  SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

//        SearchView searchView = null;
//        if(searchView == null){
//            searchView = (SearchView) menuItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
//        }
 //       assert searchView != null;
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //ble kod, w ogóle szajs bym powiedział poprawic jak bedzie działac
//                elementAdapter = setUpRecyclerView(elementAdapter);
//                elementAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
       // return super.onCreateOptionsMenu(menu);

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        Toast.makeText(this, "Query Inserted", Toast.LENGTH_SHORT).show();
//        return true;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        elementAdapter.getFilter().filter(newText);
//      //  adapter.setConfig(recyclerView, MainActivity.this, elements, keys, elementsFilter);
//        return false;
//    }
    //spróbowac utworzyc tak jak to jest pokazane w NoWatchFragment, czyli set Config jako new Adapter albo utworzyc tam konstruktor, i zbudować
    //go tam samo, jako było w przypadku ListAdaptera
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item1) {
           // Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), PopActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                return true;
            case 122:
                return true;
            default:
                return  super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
