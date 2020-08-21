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
            String categoryName = radioButton.getText().toString();

            String[] finalRandomTitle = new FirebaseDatabaseHelper().countCategory(categoryName);
            tvResult.setText("");
            tvResult.setText(finalRandomTitle[0]);
            if (category.equals("Wszystko"))
                tvResultCategory.setText(finalRandomTitle[1]);
            else
                tvResultCategory.setText("");
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
}
