package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentActivity;

public class ListAdapter extends ArrayAdapter<String > {
    private final Context context;
    private final List<String> title;
    private final List<String> category;
    private final int resource;
    private Holder holder;
    private View view;



    //private final boolean //Check box bollean
    public ListAdapter(FragmentActivity context, int resource, List<String> title, List<String> subtitle) {
        super(context, resource, title);
        this.context=context;
        this.resource = resource;
        this.title=title;
        this.category=subtitle;
    }



    public View getView(int position, View view, ViewGroup parent) {
       // view = view;
        if(view == null){
           LayoutInflater inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            view = inflater.inflate(R.layout.mylist, parent,false);
        }
        holder = new Holder();
        String item = title.get(position);
        String item2 = category.get(position);
        if(item!=null && item2!=null){
            holder.titleTv = (TextView) view.findViewById(R.id.titleTextView);
            holder.subtitleTv = (TextView)   view.findViewById(R.id.categoryTextView);
            if(holder.titleTv!=null && holder.subtitleTv!=null){
                holder.titleTv.setText(item);
                holder.subtitleTv.setText(item2);
            }
        }
        return view;
    };


    public class Holder
    {
        TextView titleTv;
        TextView subtitleTv;

    }
}
