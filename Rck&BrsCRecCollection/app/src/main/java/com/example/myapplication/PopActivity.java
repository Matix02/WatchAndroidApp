package com.example.myapplication;



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

import org.reactivestreams.Publisher;

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

import static com.example.myapplication.MainActivity.roomDatabaseHelper;

public class PopActivity extends Activity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView tvResult;
    private TextView tvResultCategory;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<String[]> myObservable;
    private DisposableObserver<String[]> myObserver;
    String[] d = {"No results"};
    private DisposableObserver<Element> myTrueObserver;
    private List<Element> testRoomList = new ArrayList<>();
    private String[] resultTitle;


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
            //     String category = radioButton.getText().toString();
            String categoryName = radioButton.getText().toString();

            Flowable<List<Element>> e = roomDatabaseHelper.getElementDao().getElements();


            compositeDisposable.add(e
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap((Function<List<Element>, Flowable<Element>>)
                            elements -> Flowable.fromArray(elements.toArray(new Element[0])))
                    .filter(element -> {
                        if (!element.isWatched()) {
                            if (categoryName.equals("Wszystko"))
                                return true;
                            else return element.category.equals(categoryName);
                        } else
                            return false;
                    })
                    .subscribeWith()
            );


           /* myObservable = new FirebaseDatabaseHelper().countr(categoryName);

            compositeDisposable.add(new FirebaseDatabaseHelper().countfuck(categoryName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe());

            compositeDisposable.add(myObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getObserver(categoryName))
            );*/


            // String[] finalRandomTitle = new FirebaseDatabaseHelper().countCategory(categoryName);


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

    private DisposableObserver fuckThat() {
        myTrueObserver = new DisposableObserver<Element>() {
            @Override
            public void onNext(Element element) {
                Log.i("Test", element.getTitle());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        return myTrueObserver;
    }


    public int generateRandomIndex(int sizeOfList) {
        Random r = new Random();
        return r.nextInt((sizeOfList) / 2) * 2;
    }

    private DisposableObserver<String[]> getObserver(String category) {
        myObserver = new DisposableObserver<String[]>() {
            @Override
            public void onNext(String[] s) {
                tvResult.setText("");
                tvResult.setText(s[0]);
                if (category.equals("Wszystko"))
                    tvResultCategory.setText(s[1]);
                else
                    tvResultCategory.setText("");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        return myObserver;
    }

}
