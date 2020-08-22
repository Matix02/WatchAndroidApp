package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private RecyclerView recyclerView;
    RecyclerView_Config.ElementAdapter elementAdapter;
    private ArrayList<Element> localList = new ArrayList<>();
    private List<String> keys;
    private List<Element> testRoomList = new ArrayList<>();
    public static RoomDatabaseHelper roomDatabaseHelper;
    static int elementsSize;

    /*
    Było napisane, że apliacja robi za dużo onMainThread.
    Ogólnie przyjrzeć się tym wszystkim wiadomością, które są wyświetlane na bieżąco w zakładce RUN,
    Debug, Logcat - na czerwono.
     */
    /*
    Może stworzyć własny pakiet, jak w książce dotyczący tego, aby zaimpelemntować w innych projektach
    bazę Firebase i Room'a.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Dodać może obrazkowe nagrody, że jak poleca Rock to bedzie R przy tytule, a jak Borys to B, natomiast w obu przypadkach to R&B chyba Rck&Brs byłoby za długie
        mozna by było dodać info o tym w jakiej minucie w odcinku było to omwienie - czyli klik na przycisk i wyskakuje youtube w otwarym odcinkiem i w danej minucie */
        recyclerView = findViewById(R.id.ele_listView);
      /*
        Złą praktyką jest gdy wywołujemy w taki spsoób bazę, lepiej zbudować coś w rodzaju tego modelu z Firebase'a, który występuje dotychczas lub
        po prostu poszukać czegoś co pokazuje jak użyć rooma ogólnie i to zmienić i zaimplementować
         */
       roomDatabaseHelper = Room.databaseBuilder(getApplicationContext(), RoomDatabaseHelper.class, "ElementDB").allowMainThreadQueries().build();

        new FirebaseDatabaseHelper().readElements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Element> elements, List<String> keys) {
                findViewById(R.id.loading_elements).setVisibility(View.GONE);

                testRoomList.addAll(roomDatabaseHelper.getElementDao().getElements());

                localList = (ArrayList<Element>) new FirebaseDatabaseHelper().complementationList(testRoomList);

                elementAdapter = new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, keys, localList);
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

        });

        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewElement.class);
            startActivity(intent);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && !fab.isShown())
                    fab.show();
                else if (dy > 0 && fab.isShown())
                    fab.hide();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            testRoomList.addAll(roomDatabaseHelper.getElementDao().getElements());
            ArrayList<Element> elements = (ArrayList<Element>) new FirebaseDatabaseHelper().complementationList(testRoomList);
            List<String> keyList = keysAssign(elements);
            elementAdapter.updateList(elements, keyList);
        }
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
                testRoomList.clear();
                //trochę mało wydajne
                testRoomList.addAll(roomDatabaseHelper.getElementDao().getElements());
                localList = (ArrayList<Element>) new FirebaseDatabaseHelper().complementationList(testRoomList);

                for (Element name : localList) {
                    if (name.getTitle().toLowerCase().contains(userInput)) {
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
        searchView.setQuery("", false);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            Intent intent = new Intent(getApplicationContext(), PopActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.filter) {
            Intent intent = new Intent(getApplicationContext(), PopActivityFilter.class);
            intent.putExtra("id", 1);
            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<String> keysAssign(ArrayList<Element> s) {
        List<String> keyList = new ArrayList<>();
        for (Element e : s) {
            String keyStr = String.valueOf(e.getId());
            keyList.add(keyStr);
        }
        return keyList;
    }

    ///////////Interfejs Zarządzania Bazą Lokalną//////////////////////
    private void createElement(String title, String category, boolean isWatched, String recom) {
        int lastIdElement = Integer.parseInt(keys.get(keys.size() - 1));

        long id = roomDatabaseHelper.getElementDao().addElement(new Element(lastIdElement, title, category, isWatched, recom));

        Element roomElement = (Element) roomDatabaseHelper.getElementDao().getElement(id);
        if (roomElement != null) {
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
        element.clear();
    }
}
