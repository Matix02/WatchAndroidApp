package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class NewElement extends Activity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioGroup radioRecomGroup;
    private RadioButton radioRecomButton;
    long maxId = 0;

    public NewElement() {
        // Required empty public constructor
    }
//poprawić przyznawanie ID, bo zapisuje jako ostatni element co nie jest wcale dodawaniem nowego elementu, a edycją najstarszego
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_element);

        final EditText editText = findViewById(R.id.nameET);
        radioGroup = findViewById(R.id.categoryRG);
        radioRecomGroup = findViewById(R.id.recomemndationRG);

        final Button saveButton = findViewById(R.id.saveButton);
        Button mBackButton = findViewById(R.id.backButton);

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Element");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxId = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        mBackButton.setOnClickListener(v -> finish());
        /*
        Czy Data w Roomie jest kasowana przez usuwanie cache'u jak sprzątacz czy coś ???
         */
        /////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Poprawić !!! Validation - walidacja
        //przemyslec jak to powinno dzialac, bo tak srednio
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(NewElement.this, "Fill empty fields", Toast.LENGTH_LONG).show();
            } else {
                saveButton.setOnClickListener(v -> {

                    Element element = new Element();

                    int radioId = radioGroup.getCheckedRadioButtonId();
                    int radioRecomId = radioRecomGroup.getCheckedRadioButtonId();

                    radioButton = findViewById(radioId);
                    radioRecomButton = findViewById(radioRecomId);

                    element.setTitle(editText.getText().toString());
                    element.setCategory(radioButton.getText().toString());
                    element.setRecom(radioRecomButton.getText().toString());
                    //String mGroupID = reff.push().getKey();
                    element.setWatched(false); //może dodać do layoutu opcję wybory, przy dodawaniu ...
                    //Tu jest ten static arghhhh !!!!
                    new FirebaseDatabaseHelper().addElement(element, MainActivity.lastIndex, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Element> elements, List<String> keys) {
                        }

                        @Override
                        public void DataIsInserted() {
                            Toast.makeText(NewElement.this, "The element has been inserted successfully", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void DataIsUpdated() {
                        }

                        @Override
                        public void DataIsDeleted() {
                        }

                        @Override
                        public void DataIsSelected(String randomElement) {
                        }
                    });
                    finish();
                });
            }
        });
    }
}
