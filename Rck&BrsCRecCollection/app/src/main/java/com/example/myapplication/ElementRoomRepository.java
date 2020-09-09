package com.example.myapplication;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ElementRoomRepository {

    private Application application;
    private RoomDatabaseHelper roomDatabaseHelper;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<Element>> elementLiveData = new MutableLiveData<>();
    private List<Element> elementList;
    private long rowIdOfTheItemInserted;
    ElementFilter elementFilter;


    public ElementRoomRepository(Application application) {
        this.application = application;
        elementList = new ArrayList<>();

        if (roomDatabaseHelper == null)
            roomDatabaseHelper = Room.databaseBuilder(application.getApplicationContext(), RoomDatabaseHelper.class, "ElementDB").build();

        getDatabase();
        compositeDisposable.add(roomDatabaseHelper.getElementDao().getElements()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .flatMap(new Function<List<Element>, Observable<Element>>() {
                    @Override
                    public Observable<Element> apply(List<Element> elements) throws Exception {
                        return Observable.fromArray(elements.toArray(new Element[0]));
                    }
                })
                .filter(element -> {
                    if (elementFilter.isFinished() && !elementFilter.isUnFinished())
                        return true;
                    else if (!elementFilter.isFinished() && elementFilter.isUnFinished())
                        return true;
                    else
                        return true;
                })
                .filter(element -> {
                    if (elementFilter.isBookCategory() && element.getCategory().equals("Książka"))
                        return true;
                    else if (elementFilter.isFilmCategory() && element.getCategory().equals("Film"))
                        return true;
                    else if (elementFilter.isGamesCategory() && element.getCategory().equals("Gra"))
                        return true;
                    else if (elementFilter.isSeriesCategory() && element.getCategory().equals("Serial"))
                        return true;

                    return false;
                })
                .filter(element -> {
                    if (elementFilter.isRockRecommedation() && element.getRecom().equals("Rock"))
                        return true;
                    else if (elementFilter.isBorysRecommedation() && element.getRecom().equals("Borys"))
                        return true;
                    else if (elementFilter.isRockBorysRecommedation() && element.getRecom().equals("Rck&Brs"))
                        return true;
                    else if (elementFilter.isOtherRecommedation() && element.getRecom().equals("Inne"))
                        return true;
                    return false;
                })
                .subscribe(element -> {
                    Log.d("Bufor", "działa");
                    elementList.add(element);
                })
        );
        elementLiveData.postValue(elementList);
        Log.d("Bufor", " ElementList size " + elementList.size() + " from Contructor");

        /* TRUE
        compositeDisposable.add(roomDatabaseHelper.getElementDao().getElements()

                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
              /*  .filter(elements -> {
                    Single<ElementFilter> e = roomDatabaseHelper.getFilterDao().getSingleFilters();


                            return false;
                        }

                )

                .subscribe(elements -> {

                            elementLiveData.postValue(elements);
                        },
                        throwable -> { }
                ));*/
    }

    public void getDatabase() {
        compositeDisposable.add(roomDatabaseHelper.getFilterDao().getSingleFilters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(elementFilter1 -> {
                    elementFilter = elementFilter1;
                }));
    }

    public void getFilter(Observable<Element> elementObservable) {
        compositeDisposable.add(roomDatabaseHelper.getFilterDao().getSingleFilters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(elementFilter -> {
                    elementFilter.isFinished();
                    elementFilter.isUnFinished();

                    elementObservable.map(new Function<Element, Element>() {
                        @Override
                        public Element apply(Element element) throws Exception {

                            return element;
                        }
                    });
                })
        );
    }

    public void updateFilter(ElementFilter elementFilter) {
        compositeDisposable.add(Completable.fromAction(() -> roomDatabaseHelper.getFilterDao().updateFilter(elementFilter))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(application.getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                    }
                }));
    }

    public void createElement(int id, final String title, final String category, final String reccomendation, Boolean isWached) {
        compositeDisposable.add(Completable.fromAction(() -> rowIdOfTheItemInserted = roomDatabaseHelper.getElementDao().addElement(new Element(id, title, category, isWached, reccomendation)))
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

    public void updateElement(final Element element) {
        compositeDisposable.add(Completable.fromAction(() -> roomDatabaseHelper.getElementDao().updateElement(element))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(application.getApplicationContext(), "Element has been updated successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(application.getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                    }
                })
        );
    }

    public void deleteElement(final Element element) {
        compositeDisposable.add(Completable.fromAction(() -> roomDatabaseHelper.getElementDao().deleteElement(element))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(application.getApplicationContext(), "Element has been deleted successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(application.getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                    }
                })
        );
    }

    public MutableLiveData<List<Element>> getElementLiveData() {
        return elementLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }


}
