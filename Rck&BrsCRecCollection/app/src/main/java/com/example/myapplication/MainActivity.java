package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView_Config.ElementAdapter elementAdapter;
    private ArrayList<Element> localList = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    private List<Element> buforElementList = new ArrayList<>();
    private List<Element> buforFuckingList = new ArrayList<>();
    private List<Element> revengeList = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    public static RoomDatabaseHelper roomDatabaseHelper;
    private ElementViewModel elementViewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;


    /*
    Było napisane, że apliacja robi za dużo onMainThread.
    Ogólnie przyjrzeć się tym wszystkim wiadomością, które są wyświetlane na bieżąco w zakładce RUN,
    Debug, Logcat - na czerwono.
    /////////////////////////////
    Może stworzyć własny pakiet, jak w książce dotyczący tego, aby zaimpelemntować w innych projektach
    bazę Firebase i Room'a.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        elementViewModel = ViewModelProviders.of(this).get(ElementViewModel.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.setLifecycleOwner(this);
        //elementViewModel = new ViewModelProvider(this).get(ElementViewModel.class);


        /*
        Działa odświeżanie, ale SUrowo został postawiony ten Swipe, trzeba poszukać lepszego przykładu. 
        /////////////////////////////////
        Dodać może obrazkowe nagrody, że jak poleca Rock to bedzie R przy tytule, a jak Borys to B, natomiast w obu przypadkach to R&B chyba Rck&Brs byłoby za długie
        mozna by było dodać info o tym w jakiej minucie w odcinku było to omwienie - czyli klik na przycisk i wyskakuje youtube w otwarym odcinkiem i w danej minucie */
        elementViewModel.getAllElements().observe(MainActivity.this, elements1 -> {
            Log.d("Bufor", "BuforFuckingList/Elements1 size " + elements1.size() + " from MainActivity");
            buforFuckingList.clear();
            buforFuckingList.addAll(elements1);
        });

        checkObserver();
        roomDatabaseHelper = Room.databaseBuilder(getApplicationContext(), RoomDatabaseHelper.class, "ElementDB").allowMainThreadQueries().build();


        // loadData();

        setRecycleView();

        /*
        Przetestować na dwóch urządzeniach, kiedy jedno będzie dodawać a drugie oczekiwać i refreshować
         */
        //Nic nie działa!!!!!!!!!
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> {
            // refreshData();
            checkObserver();
            swipeContainer.setRefreshing(false);
        });
        findViewById(R.id.loading_elements).setVisibility(View.GONE);
    }

    private void checkObserver() {
        final Observer<List<Element>> observer = new Observer<List<Element>>() {
            @Override
            public void onChanged(List<Element> elements) {
                elementAdapter.updateList(elements, keysAssign(elements));
                Log.d("Bufor", "Elements size " + elements.size() + " from Observer");
            }
        };
        elementViewModel.getAllElements().observe(this, observer);
    }

    private void loadData() {
        new FirebaseDatabaseHelper().readElements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Element> elements, List<String> keys) {
                elementViewModel.getAllElements().observe(MainActivity.this, elements1 -> {
                    findViewById(R.id.loading_elements).setVisibility(View.GONE);
                    Log.d("Bufor", "Elements1 size " + elements1.size() + " from LoadData");
                    localList.clear();
                    localList.addAll(elements1);
                    //  revengeList.addAll(new FirebaseDatabaseHelper().complementationList(localList));
                    Log.d("Bufor", "LocalList size " + localList.size() + " from LoadData");
                    Log.d("Bufor", "Keys size " + keys.size() + " from LoadData/Firebase");
                });
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
    }

    private void setRecycleView() {
        recyclerView = findViewById(R.id.ele_listView);
        Log.d("Bufor", " Keys size " + keys.size() + " from setRecycleView");

        elementAdapter = new RecyclerView_Config().setConfig(recyclerView, MainActivity.this, keys, localList);

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
            refreshData();
        }
    }

    private void refreshData() {
        elementViewModel.getAllElements().observe(MainActivity.this, elements -> {
            Log.d("Bufor", "RevengeList size " + elements.size() + " from RefreshData/");
            revengeList.clear();
            revengeList.addAll(elements);
            elementAdapter.updateList(revengeList, keysAssign(revengeList));
        });
        Log.d("Bufor", "RevengeList size " + revengeList.size() + " from RefreshData/");
        Log.d("Bufor", "BuforFuckingList size " + buforFuckingList.size() + " from RefreshData");
        buforElementList = new ArrayList<>(buforFuckingList);
        Log.d("Bufor", "BuforElementList size " + buforElementList.size() + " from RefreshData");
        new FirebaseDatabaseHelper().complementationList(buforElementList);
        Log.d("Bufor", "BuforElementList after Completition size " + buforElementList.size() + " from RefreshData");
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        String userInput = newText.toLowerCase();
        List<Element> newList = new ArrayList<>();
        List<String> newKeyList = new ArrayList<>();

        elementViewModel.getAllElements().observe(MainActivity.this, elements -> {
            for (Element e : elements) {
                if (e.getTitle().toLowerCase().contains(userInput)) {
                    newList.add(e);
                    newKeyList.add(Long.toString(e.getId()));
                }
            }
            elementAdapter.updateList(newList, newKeyList);
        });
        return true;
    }

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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            Intent intent = new Intent(getApplicationContext(), PopActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.filter) {
            Intent intent = new Intent(getApplicationContext(), PopActivityFilter.class);
            intent.putExtra("id", 1);
            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    ///////////Interfejs Zarządzania Bazą Lokalną//////////////////////
   /* private void createElement(String title, String category, boolean isWatched, String recom) {
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

    private void deleteEveryElements(ArrayList<Element> element) {
        roomDatabaseHelper.getElementDao().deleteAllElements();
        element.clear();
    }
*/
    public List<String> keysAssign(List<Element> s) {
        List<String> keyList = new ArrayList<>();
        for (Element e : s) {
            String keyStr = String.valueOf(e.getId());
            keyList.add(keyStr);
        }
        return keyList;
    }
}
