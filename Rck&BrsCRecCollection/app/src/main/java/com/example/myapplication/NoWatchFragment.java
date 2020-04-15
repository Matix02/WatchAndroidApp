package com.example.myapplication;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * A simple {@link Fragment} subclass.
 */
public class NoWatchFragment extends WatchingState{
    private LayoutInflater layoutInflater;
    private List<String> L1 = new ArrayList<String>();
    private List<String> L2 = new ArrayList<String>();
    private View view;
    private ArrayAdapter<String> adapter;
    private DatabaseReference reff;

    public NoWatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        L1.add("Once upon a hollywood");
        L1.add("Metro Exdodus");
        L2.add("Film");
        L2.add("Gra");
        reff = FirebaseDatabase.getInstance().getReference().child("Element");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                   // L1.add(String.valueOf(postSnapshot.getKey()));
                    L2.add(Objects.requireNonNull(dataSnapshot.getValue(Element.class)).getTitle());
                    System.out.println(L2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if(view == null)
            view = inflater.inflate(R.layout.fragment_no_watch, container, false);
        else {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        ListAdapter adapter = new ListAdapter(getActivity(), R.layout.fragment_no_watch, L1, L2);
        ListView listView = (ListView) view.findViewById(R.id.exp_no_listview);

        // adapter = new ListAdapter(getActivity(), R.layout.fragment_no_watch ,L1, L2 );
       // adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_expandable_list_item_1, L1);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        return view;
    }
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        inflater.inflate(R.menu.main_context_menu, menu);

        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onContextItemSelected(item);
                return false;
            }
        }; }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.delete_id) {
            L1.remove(info.position);
            adapter.notifyDataSetChanged();
            return true;
        }
        else if(item.getItemId() == R.id.edit_id) {

            return true;
        }
        return super.onContextItemSelected(item);
    }}