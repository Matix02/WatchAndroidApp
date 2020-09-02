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

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

import static com.example.myapplication.MainActivity.roomDatabaseHelper;

public class PopActivity extends Activity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView tvResult;
    private TextView tvResultCategory;
    String d = "No results";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        Button btnSearch = findViewById(R.id.popBtnSearch);
        radioGroup = findViewById(R.id.popCategoryRG);
        tvResult = findViewById(R.id.popResult);
        tvResultCategory = findViewById(R.id.popResultCategory);

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

        btnSearch.setOnClickListener(v -> {
            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);
            String categoryName = radioButton.getText().toString();

            if (categoryName.equals("Wszystko")) {
                compositeDisposable.add(roomDatabaseHelper.getElementDao().getNoWatchedRandomElement()
                        .subscribeWith(new DisposableSingleObserver<Element>() {
                            @Override
                            public void onSuccess(Element element) {
                                tvResult.setText(element.getTitle());
                                tvResultCategory.setText(element.getCategory());
                            }

                            @Override
                            public void onError(Throwable e) {
                                tvResult.setText(d);
                                tvResultCategory.setText("");
                            }
                        }));
            } else {
                compositeDisposable.add(roomDatabaseHelper.getElementDao().getNoWatchedRandomElementByCategory(categoryName)
                        .subscribeWith(new DisposableSingleObserver<Element>() {
                            @Override
                            public void onSuccess(Element element) {
                                tvResult.setText(element.getTitle());
                                tvResultCategory.setText("");
                            }

                            @Override
                            public void onError(Throwable e) {
                                tvResult.setText(d);
                                tvResultCategory.setText("");
                            }
                        }));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
