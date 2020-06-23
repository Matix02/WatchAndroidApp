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

import java.util.List;

public class EditElement extends AppCompatActivity {


    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Element element;
    private EditText editText;

    private Button mUpdate_btn;
    private Button mDelete_btn;
    private Button mBack_btn;
    long maxId = 0;

    private String key;
    private String title;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_element);

        key = getIntent().getStringExtra("key");
        title = getIntent().getStringExtra("title");
        category = getIntent().getStringExtra("category");

        //radioButton = findViewById(radioId);
        editText = (EditText) findViewById(R.id.nameET);
        editText.setText(title);
        radioGroup = (RadioGroup) findViewById(R.id.categoryRG);
        mUpdate_btn = (Button) findViewById(R.id.updateButton);
        mDelete_btn = (Button) findViewById(R.id.deleteButton);
        mBack_btn = (Button) findViewById(R.id.backButton);
        //poniżej linijka może być niepotrzebna redudant
        radioGroup.check(getIndex(radioGroup, category));

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

                    @Override
                    public void DataIsSelected(String randomElement) {

                    }
                });
            }
        });
        mDelete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelper().deleteElement(key, new FirebaseDatabaseHelper.DataStatus() {
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

                    @Override
                    public void DataIsSelected(String randomElement) {

                    }
                });
            }
        });
        mBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    //    reff = FirebaseDatabase.getInstance().getReference().child("Element");


        //ID listener
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    maxId = (dataSnapshot.getChildrenCount());}
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
//        });
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int radioId = radioGroup.getCheckedRadioButtonId();
//                radioButton = findViewById(radioId);
//                element.setTitle(editText.getText().toString());
//                element.setCategory(radioButton.getText().toString());
//               /* element.setWatched(false); */ //może dodać do layoutu opcję wybory, przy dodawaniu ...
//                //reff.push().setValue(element);
//                reff.child(String.valueOf(maxId)).setValue(element);
//
//                Toast.makeText(EditElement.this, "Data updated Successfully", Toast.LENGTH_LONG).show();
//            }
//        });
    }
    private int getIndex(RadioGroup radioGroup, String item){
        int index = 0;
        switch (item) {
            case "Film":
                index = 2131230861;
                break;
            case "Serial":
                index = 2131230728;
                break;
            case "Książka":
                index = 2131230885;
                break;
            case "Gra":
                index = 2131230869;
                break;
        }

//        for (int i=0; i<radioGroup.getChildCount();i++ ){
//            if(i==index){
              //  index = i;
                radioButton = findViewById(index);
//                break;
//            }
//        }
        return  index;
    }
}
