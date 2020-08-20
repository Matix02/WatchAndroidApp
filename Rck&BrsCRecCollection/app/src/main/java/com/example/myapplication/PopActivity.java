package com.example.myapplication;



import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class PopActivity extends Activity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView tvResult;
    private TextView tvResultCategory;
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
            String category = radioButton.getText().toString();
            int sizeOfList = MainActivity.elementsSize;
            String rozmiarConv = Integer.toString(sizeOfList);
            String categoryName = radioButton.getText().toString();

            String[] finalRandomTitle = new FirebaseDatabaseHelper().countCategory(categoryName);
            tvResult.setText("");
            tvResult.setText(finalRandomTitle[0]);
            if (category.equals("Wszystko"))
                tvResultCategory.setText(finalRandomTitle[1]);
            else
                tvResultCategory.setText("");

            //
            /*Może być tak, że Id, które wylosowano nie występuje w bazie (tak ja wtedy), czyli jest 6 elementów, ale brakuje nr.3
            można sprawdzić jak już zostanei zwrócony dany element, przy pomocy getTitle nie jest null, jeśli jest losuj dalej itd.
             */
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
    public int generateRandomIndex(int sizeOfList) {
        Random r = new Random();
        // return r.nextInt(sizeOfList / 2) * 2 + 1;
        return r.nextInt((sizeOfList) / 2) * 2;

        //   return r.ints(1,1, sizeOfList+1).findFirst().getAsInt();
    }
}
