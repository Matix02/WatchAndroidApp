package com.example.myapplication;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DefaultSubscriber;
import io.reactivex.subscribers.DisposableSubscriber;

import static com.example.myapplication.MainActivity.elementsSize;
import static com.example.myapplication.MainActivity.roomDatabaseHelper;

public class PopActivity extends Activity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView tvResult;
    private TextView tvResultCategory;
    PublishSubject<Element> publishSubject = PublishSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<String[]> myObservable;
    private DisposableSubscriber e;
    String[] d = {"No results"};
    private MutableLiveData<List<Element>> elementLiveData = new MutableLiveData<>();
    private Observable<Element> elementObservable;

    private DisposableSubscriber<Element> myObserver;
    private DisposableSubscriber<Element> myTrueObserver;
    private String[] resultTitle;
    private List<Element> testRoomList;
    private Element noWatchedList = new Element();


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        Button btnSearch = findViewById(R.id.popBtnSearch);
        radioGroup = findViewById(R.id.popCategoryRG);
        tvResult = findViewById(R.id.popResult);
        tvResultCategory = findViewById(R.id.popResultCategory);

        btnSearch.setOnClickListener(v -> {
            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);
            //String category = radioButton.getText().toString();
            String categoryName = radioButton.getText().toString();

            Flowable<List<Element>> e = roomDatabaseHelper.getElementDao().getElements();
            Flowable<List<Element>> e2 = roomDatabaseHelper.getElementDao().getElements();

            //    Observable.fromArray(e)
            testRoomList = new ArrayList<>();
            noWatchedList = roomDatabaseHelper.getElementDao().getNoWatchedRandomElement();

            tvResult.setText(noWatchedList.getTitle());
            tvResultCategory.setText(noWatchedList.getCategory());
            // roomDatabaseHelper.getElementDao().getNoWatchedElements()

        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //zmiana rozmiaru popap-u
        getWindow().setLayout((int) (width * .8), (int) (height * .7));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

    }

    private FlowableSubscriber<Element> fuckThat() {
        myTrueObserver = new DisposableSubscriber<Element>() {

            @Override
            public void onNext(Element element) {
                Log.i("Test", element.getTitle());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d("Test", "Done");
            }
        };
        return myTrueObserver;
    }


    public int generateRandomIndex(int sizeOfList) {
        Random r = new Random();
        return r.nextInt((sizeOfList) / 2) * 2;
    }

    /* Pomysły:
    #1 - Zwracanie MutableLiveData, jak przeklepanie metody z udemy
    #2 - Zwracanie stream'a jak Observables, a następnie rozbicie tego przy pomocy Observer'a
    #3 - Nowa Funckja w DAO, jako argument przyjmuje

     */
   /* public MutableLiveData<List<Element>> getElementLiveData(){
        testRoomList = new ArrayList<>();
        elementObservable=roomDatabaseHelper.getElementDao().getElements();


    }
*/

}
