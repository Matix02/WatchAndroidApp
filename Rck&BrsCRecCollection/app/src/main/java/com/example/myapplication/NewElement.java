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

    // Required empty public constructor
    public NewElement() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_element);

        final EditText editText = findViewById(R.id.nameET);
        radioGroup = findViewById(R.id.categoryRG);
        radioRecomGroup = findViewById(R.id.recomemndationRG);

        final Button saveButton = findViewById(R.id.saveButton);
        Button mBackButton = findViewById(R.id.backButton);

        /* Metoda do wybierania ostatniego ID z Firebase'a
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Element");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxId = (dataSnapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });*/
        mBackButton.setOnClickListener(v -> finish());
        /*
        Czy Data w Roomie jest kasowana przez usuwanie cache'u jak sprzątacz czy coś ???
         */
        /////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Poprawić !!! Validation - walidacja


        saveButton.setOnClickListener(v -> {
            int radioId = radioGroup.getCheckedRadioButtonId();
            if (editText.getText().toString().equals("") || radioId == -1) {
                Toast.makeText(NewElement.this, "Fill empty fields", Toast.LENGTH_LONG).show();
            } else {

                Element element = new Element();

                int radioRecomId = radioRecomGroup.getCheckedRadioButtonId();

                radioButton = findViewById(radioId);
                radioRecomButton = findViewById(radioRecomId);

                element.setTitle(editText.getText().toString());
                element.setCategory(radioButton.getText().toString());
                element.setRecom(radioRecomButton.getText().toString());
                element.setWatched(false); //może dodać do layoutu opcję wybory, przy dodawaniu ...
                new FirebaseDatabaseHelper().addElement(element, new FirebaseDatabaseHelper.DataStatus() {
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
                    });
                finish();

            }
        });

    }
}
