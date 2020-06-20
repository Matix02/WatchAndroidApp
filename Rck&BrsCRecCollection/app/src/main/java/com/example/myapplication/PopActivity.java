package com.example.myapplication;



import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

public class PopActivity extends Activity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        btnSearch = (Button) findViewById(R.id.popBtnSearch);
        radioGroup = (RadioGroup) findViewById(R.id.popCategoryRG);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String category = radioButton.getText().toString();
                int sizeOfList = MainActivity.elementsSize;
                String rozmiarConv = Integer.toString(sizeOfList);

                new FirebaseDatabaseHelper().randomElement(sizeOfList, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Element> elements, List<String> keys) {
                        Toast.makeText(PopActivity.this,  "costam", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
               // Log.i("Rozmiar tablicy", rozmiarConv);
                /*Może być tak, że Id, które wylosowano nie występuje w bazie (tak ja wtedy), czyli jest 6 elementów, ale brakuje nr.3
                można sprawdzić jak już zostanei zwrócony dany element, przy pomocy getTitle nie jest null, jeśli jest losuj dalej itd.
                 */
            }
    });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //zmiana rozmiaru popap-u
        getWindow().setLayout((int)(width*.8), (int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

    }
}
