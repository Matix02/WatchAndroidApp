package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
/*
zaprojektować. Mam pewien pomysł, by dodawanie kolejnych filtrów obfitowało w pokazywanie wybranych ChipButtonów na pasku pod searchView/toolbarem wraz z
możliwością ich wyłączenia poprzez naciśnięcie X. A wybranie kolejnych/nowych/edycja itd. to tylko w formie popup bazowo
 */
/*
Bug Founded.
Jak się wybierze opcję zaznacz, że oglądane podczas wyników wyszukiwania to sie nie zaznacza.
 */
/*
Last Update, spróbować zmienić, tą funkcję z updateList i dodać edycję wraz z NewList i dodatkwoym argumentem, który bedzie tez aktualizowac listę KeyList
 */

/*
Naprawić pojawianie się, a raczej brak aktualizacji w przypadku listy. Element jest aktualizowany, ale powrót na
stronę główną jest już przezd tą zmianą, bo baza danych lokalna nie jest wtedy aktualizowana!!!
 */

    private RecyclerView recyclerView;
    private List<Element> elements = new ArrayList<>();
    private List<Element> elementsFilter = new ArrayList<>();
    private RecyclerView_Config.ElementAdapter elementAdapter;
    private ArrayList<Element> localList = new ArrayList<>();
    private List<String> keys;
    private List<ElementFilter> filterList = new ArrayList<>();
    /* O matko za dużo tych static arghhh*/
    public static RoomDatabaseHelper roomDatabaseHelper;
    static int elementsSize;
    static int lastIndex = 0;

    /*
    Zrobiono migrację bazy i bedzie potrzebna tez druga. Teraz przetestowac
    trzeba naprawić eto buga, który wywala listę do poprzedniej formy, czyli bez zaznaczonego IsWatched
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Dodać może obrazkowe nagrody, że jak poleca Rock to bedzie R przy tytule, a jak Borys to B, natomiast w obu przypadkach to R&B chyba Rck&Brs byłoby za długie
        mozna by było dodać info o tym w jakiej minucie w odcinku było to omwienie - czyli klik na przycisk i wyskakuje youtube w otwarym odcinkiem i w danej minucie */
        recyclerView =  findViewById(R.id.ele_listView);
      /* recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ////////////
        Złą praktyką jest gdy wywołujemy w taki spsoób bazę, lepiej zbudować coś w rodzaju tego modelu z Firebase'a, który występuje dotychczas lub
        po prostu poszukać czegoś co pokazuje jak użyć rooma ogólnie i to zmienić i zaimplementować
         */
       roomDatabaseHelper = Room.databaseBuilder(getApplicationContext(), RoomDatabaseHelper.class, "ElementDB").allowMainThreadQueries().build();

     /*   deleteEveryElements(localList);
       for (Element element : localList){
            Log.d("Element:", element.getTitle());
        }


        Teraz należy poprawić ten system co juz zostało oglądniete a co nie!
         */
        new FirebaseDatabaseHelper().readElements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Element> elements, List<String> keys) {
                findViewById(R.id.loading_elements).setVisibility(View.GONE);
                localList.clear();
                filterList.clear();

                filterList.addAll(roomDatabaseHelper.getElementDao().getFilters());
                /*
                Naprawić dodawanie do listy nowej czesci elementow, bo sie nawarstwia
                Mozna dodac tylko ten ostatni, choc gdyby nie zamykac okna do dodawania elementow to wtedy nie dodamy wszystkich a tylko ostatni z iluś
                albo czyscic baze i dodawać ją od nowa, napierw u góry dac clear i ta linijke zostawić - może być mało wydajne.
                 */
              localList.addAll(roomDatabaseHelper.getElementDao().getElements());
                //filtracja listy, nie poprzez query w interfejsie Room'a, a przez mechanizm for
               lastIndex = (int) localList.get(localList.size()-1).getId();
               // lastIndex = 1;
                elementAdapter = new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, elements, keys, localList, elementAdapter);
                /*new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, elements, keys, elementsFilter);
                for(Element e : elements){
                    createElement(e.getTitle(), e.getCategory(), e.isWatched);
                }
               assignRightId(elements);*/
                elementsSize = elements.size();
            }
            @Override
            public void DataIsInserted() { }
            @Override
            public void DataIsUpdated() { }
            @Override
            public void DataIsDeleted() { }
            @Override
            public void DataIsSelected(String randomElement) { }
        });
        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewElement.class);
                startActivity(intent);
            }
        });
        //////////////Pisać poniżej/////////////////////

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
    public boolean onQueryTextChange(final String newText) {
        new FirebaseDatabaseHelper().readElements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Element> elements, List<String> keys) {
                String userInput = newText.toLowerCase();
                List<Element> newList = new ArrayList<>();
                List<String> newKeyList = new ArrayList<>();
                localList.clear();
                localList.addAll(roomDatabaseHelper.getElementDao().getElements());
                for (Element name : localList){
                    if(name.getTitle().toLowerCase().contains(userInput)){
                        newList.add(name);
                        newKeyList.add(Long.toString(name.getId()));
                    }
                }
                elementAdapter.updateList(newList, newKeyList);
            }
            @Override
            public void DataIsInserted() { }
            @Override
            public void DataIsUpdated() { }
            @Override
            public void DataIsDeleted() { }
            @Override
            public void DataIsSelected(String randomElement) { }
        });
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) { return false; }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.nav_bar_items, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item1);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);

        return true;
    }

    //Metoda, wywoływana gdy naciska się Itemy z Dodatkowych Opcji(trzech kropek)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            Intent intent = new Intent(getApplicationContext(), PopActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.filter){
            Intent intent = new Intent(getApplicationContext(), PopActivityFilter.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ///////////Interfejs Zarządzania Bazą Lokalną//////////////////////
    private void createElement(String title, String category, boolean isWatched, String recom){
        int lastIdElement = Integer.parseInt(keys.get(keys.size() - 1));

        long id = roomDatabaseHelper.getElementDao().addElement(new Element(lastIdElement, title, category, isWatched, recom));

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
    private void Table (){

       // roomDatabaseHelper.getElementDao().addFilter(new ElementFilter(()));
    }
}
