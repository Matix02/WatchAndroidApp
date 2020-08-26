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

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PopActivityFilter extends Activity {

    String zazWszt = "Zaznacz Wszystko";
    String odzWszt = "Odznacz Wszystko";
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
    private CompositeDisposable disposable = new CompositeDisposable();


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

        /* RxJava
        elementFilters.addAll(MainActivity.roomDatabaseHelper.getElementDao().getFilters());

     mainElements.addAll(MainActivity.roomDatabaseHelper.getElementDao().getElements());
      */




        /* Część graficzna */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .9));

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        /* Część Przypisywania Wartości z Bazy */

        disposable.add(MainActivity.roomDatabaseHelper.getElementDao().getFiltersFlow()
                .subscribeOn(Schedulers.io()) //nie mam pewności co do tej operacji czy compution czy io
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(elements1 -> {
                            elementFilters.clear();
                            elementFilters.addAll(elements1);
                            //localList = (ArrayList<Element>) new FirebaseDatabaseHelper().complementationList(testRoomList);
                            //elementAdapter.notifyDataSetChanged(); - Not Working
                            // elementAdapter.updateList(localList, keys);
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

                            finishSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                                if (!unFinishSwitch.isChecked() && !isChecked)
                                    unFinishSwitch.setChecked(true);
                            }));
                            unFinishSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                                if (!finishSwitch.isChecked() && !isChecked)
                                    finishSwitch.setChecked(true);
                            }));


                            if (!seriesCheckBox.isChecked() || !filmsCheckBox.isChecked() || !gamesCheckBox.isChecked() || !booksCheckBox.isChecked()) {
                                allCheckBox.setChecked(false);
                                allCheckBox.setText(zazWszt);
                            } else {
                                allCheckBox.setChecked(true);
                                allCheckBox.setText(odzWszt);
                            }

                            filmsCheckBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                                if (!isChecked) {
                                    allCheckBox.setChecked(false);
                                    allCheckBox.setText(zazWszt);
                                }

                            }));
                            booksCheckBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                                if (!isChecked) {
                                    allCheckBox.setChecked(false);
                                    allCheckBox.setText(zazWszt);
                                }

                            }));
                            gamesCheckBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                                if (!isChecked) {
                                    allCheckBox.setChecked(false);
                                    allCheckBox.setText(zazWszt);
                                }
                            }));
                            seriesCheckBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                                if (!isChecked) {
                                    allCheckBox.setChecked(false);
                                    allCheckBox.setText(zazWszt);
                                }
                            }));

                            allCheckBox.setOnClickListener(v -> selectAllCategory(!allCheckBox.isChecked()));

                            /* Część Aktywacji Przycisków */
                            if (!borysSwitch.isChecked() || !rockSwitch.isChecked() || !rckAndBorysSwitch.isChecked() || !otherSwitch.isChecked()) {
                                allSwitch.setChecked(false);
                                allSwitch.setText(zazWszt);
                            } else {
                                allSwitch.setChecked(true);
                                allSwitch.setText(odzWszt);
                            }

                            borysSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (!isChecked) {
                                    allSwitch.setChecked(false);
                                    allSwitch.setText(zazWszt);
                                }
                            });
                            rockSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (!isChecked) {
                                    allSwitch.setChecked(false);
                                    allSwitch.setText(zazWszt);
                                }
                            });
                            rckAndBorysSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (!isChecked) {
                                    allSwitch.setChecked(false);
                                    allSwitch.setText(zazWszt);
                                }
                            });
                            otherSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (!isChecked) {
                                    allSwitch.setChecked(false);
                                    allSwitch.setText(zazWszt);
                                }
                            });

                            allSwitch.setOnClickListener(v -> selectAllRecom(!allSwitch.isChecked()));

                        }, throwable -> {

                        }
                ));


        defaultButton.setOnClickListener(v -> {
            /*
            tutaj mozna by było jakąs metodę zastowoswać, do
            tego aby to zaznaczać, np jesli zaznaAll to wszystko idzie,
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

            Intent intent = new Intent();
            intent.putExtra("id", 1);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    public void selectAllRecom(boolean statusButton) {
        boolean realStatus = !statusButton;

        rockSwitch.setChecked(realStatus);
        borysSwitch.setChecked(realStatus);
        rckAndBorysSwitch.setChecked(realStatus);
        otherSwitch.setChecked(realStatus);
        if (realStatus)
            allSwitch.setText(odzWszt);
        else
            allSwitch.setText(zazWszt);
    }

    public void selectAllCategory(boolean statusCheckBox) {
        boolean realStatus = !statusCheckBox;

        filmsCheckBox.setChecked(realStatus);
        gamesCheckBox.setChecked(realStatus);
        seriesCheckBox.setChecked(realStatus);
        booksCheckBox.setChecked(realStatus);
        allCheckBox.setChecked(realStatus);
        if (realStatus)
            allCheckBox.setText(odzWszt);
        else
            allCheckBox.setText(zazWszt);
    }

}
