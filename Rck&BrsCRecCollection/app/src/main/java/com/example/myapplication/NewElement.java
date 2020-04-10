package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class NewElement extends Activity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DatabaseReference reff;
    private Element element;

    public NewElement() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_element);

        radioGroup = findViewById(R.id.categoryRG);
        final EditText editText = findViewById(R.id.nameET);
        Button button = findViewById(R.id.saveButton);
        element = new Element();
        reff = FirebaseDatabase.getInstance().getReference().child("Element");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //co bedzie sie działo po naacisnieciu, dokonac poxniej jak juz zakocznyc sie instalacja firebase'a
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                element.setTitle(editText.getText().toString());
                element.setCategory(radioButton.getText().toString());
                element.setWatched(false); //może dodać do layoutu opcję wybory, przy dodawaniu ...
                reff.push().setValue(element);
                Toast.makeText(NewElement.this, "Data inserted Successfully", Toast.LENGTH_LONG).show();
            }

        });
    }
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton) Objects.requireNonNull(findViewById(radioId));

         Toast.makeText(this, "sdasd", Toast.LENGTH_SHORT).show();
    }
}
