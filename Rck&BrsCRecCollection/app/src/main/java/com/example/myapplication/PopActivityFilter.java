package com.example.myapplication;


import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

public class PopActivityFilter extends Activity {

    String zazWszt;
    String odzWszt;
    Switch finishSwitch;
    Switch unFinishSwitch;
    CheckBox filmsCheckBox;
    CheckBox gamesCheckBox;
    CheckBox booksCheckBox;
    CheckBox seriesCheckBox;
    CheckBox allCheckBox;
    Switch rockSwitch;
    Switch borysSwitch;
    Switch rckAndBorysSwitch;
    Switch otherSwitch;
    Switch allSwitch;
    Button saveButton;
    Button defaultButton;
    ArrayList<ElementFilter> elementFilters = new ArrayList<>();
    ArrayList<Element> mainElements = new ArrayList<>();

    /*
    Doać funkcję, która po wciśnięciu lupy (na klawiaturze), zanika
    klawiatura wirtualna. Coś pokombinować.
     */
    /*
    Tego jest tak dużo, że chyba przydałoby się zrobienie jakiegoś Reccycler_view
    coś jak to, gdzie ma się te wszystkie połączenia findView... itd.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_filter2);

        elementFilters.addAll(MainActivity.roomDatabaseHelper.getElementDao().getFilters());
        mainElements.addAll(MainActivity.roomDatabaseHelper.getElementDao().getElements());

        zazWszt = "Zaznacz Wszystko";
        odzWszt = "Odznacz Wszystko";

        finishSwitch = findViewById(R.id.finishSW);
        unFinishSwitch = findViewById(R.id.unfinishSW);

        filmsCheckBox = findViewById(R.id.filmsCB);
        gamesCheckBox = findViewById(R.id.gamesCB);
        booksCheckBox = findViewById(R.id.booksCB);
        seriesCheckBox = findViewById(R.id.seriesCB);
        allCheckBox = findViewById(R.id.allCB);

        rockSwitch = findViewById(R.id.rockSW);
        borysSwitch = findViewById(R.id.borysSW);
        rckAndBorysSwitch = findViewById(R.id.rockBorysSW);
        otherSwitch = findViewById(R.id.otherSW);
        allSwitch = findViewById(R.id.allSW);

        saveButton = findViewById(R.id.saveFilterButton);
        defaultButton = findViewById(R.id.defaultFilterButton);
        /* Część graficzna */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);
        /*
        Część Przypisywania Wartości z Bazy
         */
        finishSwitch.setChecked(elementFilters.get(0).isFinished());
        unFinishSwitch.setChecked(elementFilters.get(0).isUnFinished());
        filmsCheckBox.setChecked(elementFilters.get(0).isFilmCategory());
        gamesCheckBox.setChecked(elementFilters.get(0).isGamesCategory());
        seriesCheckBox.setChecked(elementFilters.get(0).isSeriesCategory());
        booksCheckBox.setChecked(elementFilters.get(0).isBookCategory());
        rockSwitch.setChecked(elementFilters.get(0).isRockRecommedation());
        borysSwitch.setChecked(elementFilters.get(0).isBorysRecommedation());
        rckAndBorysSwitch.setChecked(elementFilters.get(0).isRockBorysRecommedation());
        otherSwitch.setChecked(elementFilters.get(0).isOtherRecommedation());

        /* Część Aktywacji Przycisków */
        allSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    allSwitch.setText(odzWszt);
                    selectAllRecom(false);
                } else {
                    allSwitch.setText(zazWszt);
                    selectAllRecom(true);
                }
            }
        });

        /*
        Wymyślec sposób na to, zaznacz/odznacz były zależne od tego czy
        która kontrolka nie została odkliknięta.
         */
        defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                tutaj mozna by było jakąs metodę zastowoswać, do
                teog aby to zaznaczać, np jesli zaznaAll to wszystko idzie,
                jako metoda
                */
                if (!allSwitch.isChecked()) {
                    selectAllRecom(allSwitch.isChecked());
                    allSwitch.setChecked(true);
                }
                if (!allCheckBox.isChecked()) {
                    selectAllCategory(allCheckBox.isChecked());
                    allCheckBox.setChecked(true);
                }
                finishSwitch.setChecked(true);
                unFinishSwitch.setChecked(true);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElementFilter elementFilter = new ElementFilter();
                elementFilter.setId(1);
                elementFilter.setFinished(finishSwitch.isChecked());
                elementFilter.setUnFinished(unFinishSwitch.isChecked());
                elementFilter.setBookCategory(booksCheckBox.isChecked());
                elementFilter.setFilmCategory(filmsCheckBox.isChecked());
                elementFilter.setGamesCategory(gamesCheckBox.isChecked());
                elementFilter.setSeriesCategory(seriesCheckBox.isChecked());
                elementFilter.setRockRecommedation(rockSwitch.isChecked());
                elementFilter.setBorysRecommedation(borysSwitch.isChecked());
                elementFilter.setRockBorysRecommedation(rckAndBorysSwitch.isChecked());
                elementFilter.setOtherRecommedation(otherSwitch.isChecked());

                new FirebaseDatabaseHelper().updateFilter(elementFilter);

            }
        });
    }

    public void selectAllRecom(boolean statusButton){
        boolean realStatus = !statusButton;

        rockSwitch.setChecked(realStatus);
        borysSwitch.setChecked(realStatus);
        rckAndBorysSwitch.setChecked(realStatus);
        otherSwitch.setChecked(realStatus);
    }

    public void selectAllCategory(boolean statusCheckBox){
        boolean realStatus = !statusCheckBox;

        filmsCheckBox.setChecked(realStatus);
        gamesCheckBox.setChecked(realStatus);
        seriesCheckBox.setChecked(realStatus);
        booksCheckBox.setChecked(realStatus);
        allCheckBox.setChecked(realStatus);
    }
}
//Postarać się o funkcjonlaność o zgrozo