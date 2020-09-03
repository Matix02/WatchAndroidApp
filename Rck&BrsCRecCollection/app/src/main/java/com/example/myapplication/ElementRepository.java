package com.example.myapplication;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class ElementRepository {

    private Application application;
    private RoomDatabaseHelper roomDatabaseHelper;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Element>> elementLiveData = new MutableLiveData<>();
    private long rowIdOfTheItemInserted;

    public ElementRepository(Application application) {
        this.application = application;
        roomDatabaseHelper = Room.databaseBuilder(application.getApplicationContext(), RoomDatabaseHelper.class, "ElementDB").build();

        compositeDisposable.add(roomDatabaseHelper.getElementDao().getElements()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(elements -> elementLiveData.postValue(elements),
                        throwable -> {

                        }
                ));
    }

    public void createElement(final String title, final String category, final String reccomendation, Boolean isWached,) {
        compositeDisposable.add(Completable.fromAction(() -> rowIdOfTheItemInserted = roomDatabaseHelper.getElementDao().addElement(new Element(0, title, category, isWached, reccomendation)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(application.getApplicationContext(), "Element has been added successfully " + rowIdOfTheItemInserted, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(application.getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                    }
                }));
    }
    

}
