package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

public class EditElement extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText editText;
    private String key;
    private RadioGroup radioRecomGroup;
    private RadioButton radioRecomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_element);

        key = getIntent().getStringExtra("key");
        String title = getIntent().getStringExtra("title");
        String category = getIntent().getStringExtra("category");

        //radioButton = findViewById(radioId);
        editText = findViewById(R.id.nameET);
        editText.setText(title);
        radioGroup = findViewById(R.id.categoryRG);
        radioRecomGroup = findViewById(R.id.recomemndationEditRG);

        Button mUpdate_btn = findViewById(R.id.updateButton);
        Button mDelete_btn = findViewById(R.id.deleteButton);
        Button mBack_btn = findViewById(R.id.backButton);
        //poniżej linijka może być niepotrzebna redudant

        int radioId = getIndex(category);
        radioGroup.check(radioId);

        mUpdate_btn.setOnClickListener(v -> {
            Element element = new Element();
            element.setTitle(editText.getText().toString());

            int radioId1 = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId1);
            element.setCategory(radioButton.getText().toString());

            int radioRecomId = radioRecomGroup.getCheckedRadioButtonId();
            radioRecomButton = findViewById(radioRecomId);
            element.setRecom(radioRecomButton.getText().toString());

            new FirebaseDatabaseHelper().updateElement(key, element, new FirebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<Element> elements, List<String> keys) {
                }

                @Override
                public void DataIsInserted() {
                }

                @Override
                public void DataIsUpdated() {
                    Toast.makeText(EditElement.this, "Element has been updated", Toast.LENGTH_LONG).show();
                }

                @Override
                public void DataIsDeleted() {
                }

            });
        });
        mDelete_btn.setOnClickListener(v -> new FirebaseDatabaseHelper().deleteElement(key, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Element> elements, List<String> keys) {
            }

            @Override
            public void DataIsInserted() {
            }

            @Override
            public void DataIsUpdated() {
            }

            @Override
            public void DataIsDeleted() {
                Toast.makeText(EditElement.this, "Element has been deleted", Toast.LENGTH_LONG).show();
                finish();
            }

        }));
        mBack_btn.setOnClickListener(v -> finish());
    }

    //Alternatywne rozwiązanie do tego, ze względu na ciągle zmieniające się indexy
    private int getIndex(String item){
        int index = 0;
        switch (item) {
            case "Film":
                index = 2131230862;
                break;
            case "Serial":
                index = 2131230728;
                break;
            case "Książka":
                index = 2131230887;
                break;
            case "Gra":
                index = 2131230870;
                break;
        }
        return  index;
    }
}
