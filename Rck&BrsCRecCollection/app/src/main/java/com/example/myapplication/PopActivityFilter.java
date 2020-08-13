package com.example.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;

import androidx.fragment.app.DialogFragment;

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

    /*To jest jak naciśniesz back-powrót na telefonie
    Może tutaj dodać też zapisywanie tego, że nie bedzie potrzebny przycisk SAVE
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

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

        getWindow().setLayout((int) (width * .8), (int) (height * .8));


        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        /* Część Przypisywania Wartości z Bazy */
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
        allSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                allSwitch.setText(odzWszt);
                selectAllRecom(false);
            } else {
                allSwitch.setText(zazWszt);
                selectAllRecom(true);
            }
        });


        /*
        Wymyślec sposób na to, zaznacz/odznacz były zależne od tego czy
        która kontrolka nie została odkliknięta.
         */
        defaultButton.setOnClickListener(v -> {
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
        });

        saveButton.setOnClickListener(v -> {
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

            /*!!! finish(), refresh itd. jest częścią tego yyym, kijowego kodu, który zamyka i Activity,
                a następnie jego część w filtrze otwiera go na nowo. Słabe rowiązanie ...*/
            // Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
            // startActivity(refresh);

            /*!!!
            onResume() jest drugą metodą do aktualizacji, ale wciąż wydaje się 2/10
             */
            // onResume();

            /*!!!
            Jednak notifyDataChenged jest tą włąściwą opcją, jeśli chodzi o aktualizacje daty/listy
             */


            Intent intent = new Intent();
            intent.putExtra("id", 1);
            setResult(RESULT_OK, intent);
            finish();
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