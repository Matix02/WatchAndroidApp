package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.widget.SearchView;
import androidx.room.Room;

import android.widget.CheckBox;

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
    private ArrayList<Element> localList = new ArrayList<>();
    private String key;
    private List<String> keys;
    private List<Element> elements2 = new ArrayList<>();
    public static RoomDatabaseHelper roomDatabaseHelper;
    static int elementsSize;


    //co niby nie ma połączenia, brak reakcji na to co jest wpisywane, funkcja w debbugerze - nieaktywna
    //dwa albo trzy filmiki zostały zamieszczone, by to ogarnąć


    /*
    !!!!!! Teraz skopiować wszystkie elementy z bazy firebase do bazy tej, która jest tą lokalną
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Dodać może obrazkowe nagrody, że jak poleca Rock to bedzie R przy tytule, a jak Borys to B, natomiast w obu przypadkach to R&B
        //chyba Rck&Brs byłoby za długie
        //mozna by było dodać info o tym w jakiej minucie w odcinku było to omwienie - czyli klik na przycisk i wyskakuje youtube w otwarym odcinkiem i w danej minucie
        recyclerView = (RecyclerView) findViewById(R.id.ele_listView);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
        Złą praktyką jest gdy wywołujemy w taki spsoób bazę, lepiej zbudować coś w rodzaju tego modelu z Firebase'a, który występuje dotychczas lub
        po prostu poszukać czegoś co pokazuje jak użyć rooma ogólnie i to zmienić i zaimplementować
         */
       roomDatabaseHelper = Room.databaseBuilder(getApplicationContext(), RoomDatabaseHelper.class, "ElementDB").allowMainThreadQueries().build();
      //  createElement("Elo", "Kanapka", true);

        Element e1 = new Element("dzien dobry", "razy dwa", true);
       // localList.add(e1);
        localList.addAll(roomDatabaseHelper.getElementDao().getElements());
        /*
        Zadziałało!
        Trzeba teraz spróbować opracować opcję, która będzie zakreślać te rzeczy, które juz oglądneliśmy na równie z bazą z firebase'a.
         */
       // Element r0 =  localList.get(0);
       // deleteElement(localList.get(0), 0);
       // deleteElement(localList.get(0),0);

       /* deleteEveryElements(localList);
        for (Element element : localList){
            Log.d("Element:", element.getTitle());
        }*/
        new FirebaseDatabaseHelper().readElements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Element> elements, List<String> keys) {
                findViewById(R.id.loading_elements).setVisibility(View.GONE);
//!!!!!!!!!!! Ważne - filtrowana lista jest pusta, dlatego, ze wystepuje tylko w tym wywolaniu nizej- mozna rozdzielic ta metode setConfig na pół że napierw jest konstr
                // konstrukcja adaptera z lista elements i keys a nastepie metoda z tej metody daje nam setAdapter, lub sprobowac zrobic metode getList czy cos i stąd WYCIAGANAC
                // tą listę
                elementAdapter = new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, elements, keys, localList, elementAdapter);
                //new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, elements, keys, elementsFilter);
               /* for(Element e : elements){
                    createElement(e.getTitle(), e.getCategory(), e.isWatched);
                }*/
                for (Element element : localList){
                    Log.d("Element:", String.valueOf(element.getId()));
                }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_bar_items, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item1);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

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

    private void createElement(String title, String category, boolean isWatched){
        long id = roomDatabaseHelper.getElementDao().addElement(new Element(0, title, category, isWatched));

        Element roomElement = (Element) roomDatabaseHelper.getElementDao().getElement(id);

        if (roomElement != null){
            localList.add(0, roomElement);
            //tutaj powinien być jeszcze adapter.
        }
    }
    private void deleteEveryElements(ArrayList<Element> element){
        roomDatabaseHelper.getElementDao().deleteAllElements();
        ArrayList<Element> sampleList = new ArrayList<>();
        sampleList.removeAll(element);
   //     roomDatabaseHelper.getElementDao().deleteElement(element);
//        Element e = roomDatabaseHelper.getElementDao().getElement(index);
//        roomDatabaseHelper.getElementDao().deleteElement(e);
        //Super rzutowanie, nie wiadomo co robisz
        //Element roomElement =  roomDatabaseHelper.getElementDao().getElement(element);


      //  localList.remove(index);
       /* if (roomElement != null){
            localList.remove(roomElement);
            //tutaj powinien być jeszcze adapter.
        }*/
    }
}
