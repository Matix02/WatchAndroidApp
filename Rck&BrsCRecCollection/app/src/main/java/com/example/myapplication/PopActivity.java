package com.example.myapplication;



import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PopActivity extends Activity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView tvResult;
    private TextView tvResultCategory;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<String[]> myObservable;
    private DisposableObserver<String[]> myObserver;

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

            myObservable = new FirebaseDatabaseHelper().countr(categoryName);

            compositeDisposable.add(myObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getObserver(categoryName))
            );


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

    private DisposableObserver getObserver(String category) {
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
