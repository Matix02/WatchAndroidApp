package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoWatchFragment extends  WatchingState{

    private ExpandableListView expandableListView;
    private LayoutInflater layoutInflater;

    public NoWatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_no_watch, container, false);
        expandableListView = (ExpandableListView) view.findViewById(R.id.exp_no_listview);


        List<String> headings = new ArrayList<String>();
        List<String> L1 = new ArrayList<String>();
        List<String> L2 = new ArrayList<String>();
        HashMap<String, List<String>> childList = new HashMap<String, List<String>>();
        headings.add("naglowiek 1");
        headings.add("naglwoek 2");

        L1.add("l1 element");
        L1.add("l1 element nr.2");
        L2.add("l2 element");
        L2.add("l2 element nr.2");

        childList.put(headings.get(0), L1);
        childList.put(headings.get(1), L2);
        MyAdapter myAdapter = new MyAdapter(this, headings, childList );
        expandableListView.setAdapter(myAdapter);



        return view;
    }
}
