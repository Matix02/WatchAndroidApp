package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;


public class NewElement extends Fragment {

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    public NewElement() {
        // Required empty public constructor
    }

//Dokończyć projektowanie firebase 0:44 poradnik video YT

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_element, container, false);

        radioGroup = view.findViewById(R.id.categoryRG);
        EditText editText = view.findViewById(R.id.nameET);
        Button button = view.findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //co bedzie sie działo po naacisnieciu, dokonac poxniej jak juz zakocznyc sie instalacja firebase'a
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton =  view.findViewById(radioId);



            }
        });
        return view;
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = Objects.requireNonNull(getView()).findViewById(radioId);

         Toast.makeText(getActivity(), "sdasd", Toast.LENGTH_SHORT).show();
    }
}
