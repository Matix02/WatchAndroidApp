package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;


public class NewElement extends Activity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DatabaseReference reff;
    private Element element;
    private Button mBackButton;
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
        final Button saveButton = findViewById(R.id.saveButton);
        mBackButton = findViewById(R.id.backButton);
        element = new Element();
        reff = FirebaseDatabase.getInstance().getReference().child("Element");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxId = (dataSnapshot.getChildrenCount());}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); return;
            }
        });
        /////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Poprawić !!! Validation - walidacja
        //przemyslec jak to powinno dzialac, bo tak srednio
       radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(NewElement.this, "Fill empty fields", Toast.LENGTH_LONG).show();
                }
                else {
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Element element = new Element();
                            int radioId = radioGroup.getCheckedRadioButtonId();

                            radioButton = findViewById(radioId);
                            element.setTitle(editText.getText().toString());
                            element.setCategory(radioButton.getText().toString());
                            String mGroupID = reff.push().getKey();
                            element.setWatched(false); //może dodać do layoutu opcję wybory, przy dodawaniu ...
                            new FirebaseDatabaseHelper().addElement(element, maxId+1, new FirebaseDatabaseHelper.DataStatus() {
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

                        }
                    });
                }
                //Zabrane znaki do elementów wyżej
            }
        }
        );
//Koniec zabranych znaków
     /*   saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            maxId = (dataSnapshot.getChildrenCount());}
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });

                //co bedzie sie działo po naacisnieciu, dokonac poxniej jak juz zakocznyc sie instalacja firebase'a

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                element.setTitle(editText.getText().toString());
                element.setCategory(radioButton.getText().toString());
                element.setWatched(false); //może dodać do layoutu opcję wybory, przy dodawaniu ...
                //reff.push().setValue(element);
                reff.child(String.valueOf(maxId+1)).setValue(element);
                Toast.makeText(NewElement.this, "Data inserted Successfully", Toast.LENGTH_LONG).show();
            }
        });*/
    }
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) Objects.requireNonNull(findViewById(radioId));
        Toast.makeText(this, "sdasd", Toast.LENGTH_SHORT).show();
    }
}
