package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
        radioGroup =  findViewById(R.id.categoryRG);
        Button mUpdate_btn = findViewById(R.id.updateButton);
        Button mDelete_btn = findViewById(R.id.deleteButton);
        Button mBack_btn = findViewById(R.id.backButton);
        //poniżej linijka może być niepotrzebna redudant

        int radioId = getIndex(category);
        radioGroup.check(radioId);

        mUpdate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Element element = new Element();
                element.setTitle(editText.getText().toString());
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                element.setCategory(radioButton.getText().toString());

                new FirebaseDatabaseHelper().updateElement(key, element, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Element> elements, List<String> keys) { }
                    @Override
                    public void DataIsInserted() { }
                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(EditElement.this, "Element has been updated", Toast.LENGTH_LONG).show(); }
                    @Override
                    public void DataIsDeleted() { }
                    @Override
                    public void DataIsSelected(String randomElement) { }
                });
            }
        });
        mDelete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelper().deleteElement(key, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Element> elements, List<String> keys) { }
                    @Override
                    public void DataIsInserted() { }
                    @Override
                    public void DataIsUpdated() { }
                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(EditElement.this, "Element has been deleted", Toast.LENGTH_LONG).show();
                        finish(); }
                    @Override
                    public void DataIsSelected(String randomElement) { }
                });
            }
        });
        mBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
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
//        for (int i=0; i<radioGroup.getChildCount();i++ ){
//            if(i==index){
              //  index = i;
              //  radioButton = findViewById(index);
//                break;
//            }
//        }
        return  index;
    }
}
