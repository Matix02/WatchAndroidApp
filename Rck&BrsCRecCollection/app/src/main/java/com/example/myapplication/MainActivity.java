package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.appcompat.widget.SearchView;
import androidx.room.Room;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{


    private RecyclerView recyclerView;
    private List<Element> elements = new ArrayList<>();
    private List<Element> elementsFilter = new ArrayList<>();
    private RecyclerView_Config.ElementAdapter elementAdapter;
    private ArrayList<Element> localList = new ArrayList<>();
    private String key;
    private List<String> keys;
    private List<Element> elements2 = new ArrayList<>();
    private Toolbar mToolbar;
    /* O matko za dużo tych static arghhh*/
    public static RoomDatabaseHelper roomDatabaseHelper;
    static int elementsSize;
    static int lastIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Dodać może obrazkowe nagrody, że jak poleca Rock to bedzie R przy tytule, a jak Borys to B, natomiast w obu przypadkach to R&B chyba Rck&Brs byłoby za długie
        mozna by było dodać info o tym w jakiej minucie w odcinku było to omwienie - czyli klik na przycisk i wyskakuje youtube w otwarym odcinkiem i w danej minucie */
        recyclerView = (RecyclerView) findViewById(R.id.ele_listView);
      /* recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ////////////
        Złą praktyką jest gdy wywołujemy w taki spsoób bazę, lepiej zbudować coś w rodzaju tego modelu z Firebase'a, który występuje dotychczas lub
        po prostu poszukać czegoś co pokazuje jak użyć rooma ogólnie i to zmienić i zaimplementować
         */
       roomDatabaseHelper = Room.databaseBuilder(getApplicationContext(), RoomDatabaseHelper.class, "ElementDB").allowMainThreadQueries().build();


      // setSupportActionBar(mToolbar);
     //  deleteEveryElements(localList);
        for (Element element : localList){
            Log.d("Element:", element.getTitle());
        }

        /*
        Teraz należy poprawić ten system co juz zostało oglądniete a co nie!
         */
        new FirebaseDatabaseHelper().readElements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Element> elements, List<String> keys) {
                findViewById(R.id.loading_elements).setVisibility(View.GONE);
            /* Ważne - filtrowana lista jest pusta, dlatego, ze wystepuje tylko w tym wywolaniu nizej- mozna rozdzielic ta metode setConfig na pół że napierw jest konstr
               konstrukcja adaptera z lista elements i keys a nastepie metoda z tej metody daje nam setAdapter, lub sprobowac zrobic metode getList czy cos i stąd WYCIAGANAC
                 tą listę*/

            localList.clear();
            ///////////////////////////////////
                /*
                Naprawić dodawanie do listy nowej czesci elementow, bo sie nawarstwia
                Mozna dodac tylko ten ostatni, choc gdyby nie zamykac okna do dodawania elementow to wtedy nie dodamy wszystkich a tylko ostatni z iluś
                albo czyscic baze i dodawać ją od nowa, napierw u góry dac clear i ta linijke zostawić - może być mało wydajne.
                 */
                localList.addAll(roomDatabaseHelper.getElementDao().getElements());
                lastIndex = (int) localList.get(localList.size()-1).getId();
                elementAdapter = new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, elements, keys, localList, elementAdapter);
                //new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, elements, keys, elementsFilter);
               /* for(Element e : elements){
                    createElement(e.getTitle(), e.getCategory(), e.isWatched);
                }*/
           //     assignRightId(elements);

                for (Element element : localList){
                    Log.d("Element:", String.valueOf(element.isWatched()));
                }
                Log.v("Ostatni element", "Koniec");
                elementsSize = elements.size();
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

            @Override
            public void DataIsSelected(String randomElement) {

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewElement.class);
                startActivity(intent);
            }
        });
        //////////////Pisać poniżej


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

            @Override
            public void DataIsSelected(String randomElement) {

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
/*
Dalej nie działa. Sprawdzić gdzie to się aktywuje, by zmienić opcję do wyszukiwania.
 */



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

       getMenuInflater().inflate(R.menu.nav_bar_items, menu);
   MenuItem menuItem = menu.findItem(R.id.search_item1);

        //SearchView
     SearchView searchView = (SearchView) menuItem.getActionView();
      searchView.setOnQueryTextListener(this);

       // searchView.setIconifiedByDefault(false);
        return true;
    }

    //Metoda, wywoływana gdy naciska się Itemy z Dodatkowych Opcji(trzech kropek)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(getApplicationContext(), PopActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_item1:
                Toast.makeText(getApplicationContext(), "onOptionsItemSelected", Toast.LENGTH_LONG).show();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void createElement(String title, String category, boolean isWatched){
        int lastIdElement = Integer.parseInt(keys.get(keys.size() - 1));

        long id = roomDatabaseHelper.getElementDao().addElement(new Element(lastIdElement, title, category, isWatched));

        Element roomElement = (Element) roomDatabaseHelper.getElementDao().getElement(id);
        if (roomElement != null){
            localList.add(lastIdElement, roomElement);
            //tutaj powinien być jeszcze adapter, ale chyba nie musi
        }
    }

    private void assignRightId(List<Element> idList){
        for (int i=0; i<localList.size(); i++){
            int rightId = (int) idList.get(i).getId();
            roomDatabaseHelper.getElementDao().updateID(rightId, (int) localList.get(i).getId());
            localList.get(i).setId(rightId);
        }
    }

    private void deleteEveryElements(ArrayList<Element> element){
        roomDatabaseHelper.getElementDao().deleteAllElements();
        ArrayList<Element> sampleList = new ArrayList<>();
        sampleList.removeAll(element);
    }


}
