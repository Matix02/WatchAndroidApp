package com.example.myapplication;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

import io.reactivex.disposables.CompositeDisposable;

import static com.example.myapplication.MainActivity.roomDatabaseHelper;

class FirebaseDatabaseHelper {
    private List<Element> elements = new ArrayList<>();
    private DatabaseReference mReferenceBooks;
    private List<Element> testFirebaseList = new ArrayList<>();

    //zastanowić się czy aby na pewno musi to byc static

    public interface DataStatus {
        void DataIsLoaded(List<Element> elements, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    FirebaseDatabaseHelper() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooks = mDatabase.getReference("Element");
    }

    void readElements(final DataStatus dataStatus) {
        mReferenceBooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                elements.clear();
                List<String> keys = new ArrayList<>();

                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Element element = keyNode.getValue(Element.class);
                    testFirebaseList.add(element);
                }

                elements = complementationList(testFirebaseList);
                Log.d("Bufor", " Elements size " + elements.size() + " from Firebase - ReadElements");
                Log.d("Bufor", " Keys size " + keys.size() + " from Firebase - ReadElements");

                dataStatus.DataIsLoaded(elements, keys);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    void addElement(Element element, final DataStatus dataStatus) {

        long id = roomDatabaseHelper.getElementDao().getLastIndex();
        mReferenceBooks.child(String.valueOf((id + 1)))
                .setValue(element)
                .addOnSuccessListener(aVoid -> dataStatus.DataIsInserted());
        element.setId(id + 1);
        roomDatabaseHelper.getElementDao().addElement(element);
    }

    void updateElement(String key, Element element, final DataStatus dataStatus) {
        mReferenceBooks.child(key).setValue(element)
                .addOnSuccessListener(aVoid -> dataStatus.DataIsUpdated());
        roomDatabaseHelper.getElementDao().updateElementById(Long.parseLong(key), element.getTitle(), element.getRecom(), element.getCategory());
    }

    void deleteElement(String key, final DataStatus dataStatus) {
        mReferenceBooks.child(key).setValue(null)
                .addOnSuccessListener(aVoid -> dataStatus.DataIsDeleted());

        roomDatabaseHelper.getElementDao().deleteIdElement(Integer.parseInt(key));
    }


    public void updateFilter(ElementFilter elementFilter) {
        roomDatabaseHelper.getFilterDao().updateFilter(elementFilter);
    }

    //Przekierować strumień z RoomDatabaseHelper'a który znajduje się w MainActivity do tego z ViewModel
    public List<Element> complementationList(List<Element> elements) {

        List<ElementFilter> elementFilters = new ArrayList<>(roomDatabaseHelper.getFilterDao().getFilters());

        List<Element> completeList;

        boolean finished = elementFilters.get(0).isFinished();
        boolean unFinished = elementFilters.get(0).isUnFinished();
        boolean books = elementFilters.get(0).isBookCategory();
        boolean games = elementFilters.get(0).isGamesCategory();
        boolean series = elementFilters.get(0).isSeriesCategory();
        boolean films = elementFilters.get(0).isFilmCategory();
        boolean rock = elementFilters.get(0).isRockRecommedation();
        boolean borys = elementFilters.get(0).isBorysRecommedation();
        boolean rockBorys = elementFilters.get(0).isRockBorysRecommedation();
        boolean others = elementFilters.get(0).isOtherRecommedation();

        //#1 Oglądane i Nieoglądane
        if (finished && !unFinished)
            completeList = elements.stream().filter(Element::isWatched).collect(Collectors.toList());
        else if (!finished && unFinished)
            completeList = elements.stream().filter(p -> !p.isWatched()).collect(Collectors.toList());
        else
            completeList = new ArrayList<>(elements);
        //#2 Kategorie
        elements.clear();
        if (!games || !books || !series || !films) {
            elements = categoryFilter(games, films, series, books, completeList);
        } else {
            elements = new ArrayList<>(completeList);
        }
        //#3 Polecane
        completeList.clear();
        if (!rock || !borys || !rockBorys || !others) {
            completeList = promFilter(rock, borys, rockBorys, others, elements);
        } else {
            completeList = new ArrayList<>(elements);
        }
        return completeList;
    }

    List<Element> promFilter(boolean promRock, boolean promBorys, boolean promRockBorys, boolean others, List<Element> elements) {

        List<Element> completePromList = new ArrayList<>();
        //średnio wydajne pewnie
        for (Element e : elements) {
            if (e.getRecom().equals("Rock") & promRock)
                completePromList.add(e);
            else if (e.getRecom().equals("Borys") & promBorys)
                completePromList.add(e);
            else if (e.getRecom().equals("Rck&Brs") & promRockBorys)
                completePromList.add(e);
            else if (e.getRecom().equals("Inne") & others)
                completePromList.add(e);
        }
        return completePromList;
    }

    List<Element> categoryFilter(boolean catGames, boolean catFilms, boolean catSeries, boolean catBooks, List<Element> elements) {

        List<Element> completePromList = new ArrayList<>();
        for (Element e : elements) {
            if (e.getCategory().equals("Książka") & catBooks)
                completePromList.add(e);
            else if (e.getCategory().equals("Film") & catFilms)
                completePromList.add(e);
            else if (e.getCategory().equals("Gra") & catGames)
                completePromList.add(e);
            else if (e.getCategory().equals("Serial") & catSeries)
                completePromList.add(e);
        }
        return completePromList;
    }

}


