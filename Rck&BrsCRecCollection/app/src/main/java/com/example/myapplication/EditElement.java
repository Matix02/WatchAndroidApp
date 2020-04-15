package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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

public class EditElement extends AppCompatActivity {


    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DatabaseReference reff;
    private Element element;
    long maxId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_element);

        final EditText editText = findViewById(R.id.nameET);
        radioGroup = findViewById(R.id.categoryRG);
        Button button = findViewById(R.id.saveButton);
        element = new Element();
        reff = FirebaseDatabase.getInstance().getReference().child("Element");


        //ID listener
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxId = (dataSnapshot.getChildrenCount());}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                element.setTitle(editText.getText().toString());
                element.setCategory(radioButton.getText().toString());
               /* element.setWatched(false); */ //może dodać do layoutu opcję wybory, przy dodawaniu ...
                //reff.push().setValue(element);
                reff.child(String.valueOf(maxId)).setValue(element);

                Toast.makeText(EditElement.this, "Data updated Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
