package com.example.myapplication;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_Config {
    private Context mContext;
    private ElementAdapter elementAdapter;
    private DatabaseReference reff;
    private List<Element> elements = new ArrayList<>();
//możliwe że nie trzeba tego wszystkie tu umieszczać, tylko listę na której sie operuje.
  /*  public RecyclerView_Config(Context mContext, RecyclerView recyclerView, List<Element> elements, List<String> keys) {
        this.mContext = mContext;
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(elementAdapter);
    }*/

  //niby adapter
     public RecyclerView_Config(final List<Element> elements){
      this.elements = elements;
         reff = FirebaseDatabase.getInstance().getReference().child("Element");
         reff.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 List<String> keys = new ArrayList<>();
                 for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                     keys.add(keyNode.getKey());
                     Element element = keyNode.getValue(Element.class);
                     elements.add(element);
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) { }
         });
     }

     public RecyclerView_Config() { }

    public void setConfig(RecyclerView recyclerView, Context context, List<Element> elements, List<String> keys){
        mContext = context;
        elementAdapter = new ElementAdapter(elements, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(elementAdapter);
    }


//spróbować zmodyfikować tą klasę jak z filmiku, by zadziałoało popup menu

    class ElementItemView extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView title;
        private TextView category;
        private CheckBox isWatched;
        private String key;
        private CardView cardView;

        public ElementItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.mylist, parent, false) );
            title = (TextView) itemView.findViewById(R.id.titleTextView);
            category = (TextView) itemView.findViewById(R.id.categoryTextView);
            isWatched = (CheckBox) itemView.findViewById(R.id.checkBox);
            cardView = itemView.findViewById(R.id.cardLayout);
            cardView.setOnCreateContextMenuListener(this);
        }

        public void bind(Element element, String key){
          //  title.setText(element.getTitle());
            String e = element.getTitle();
            title.setText(e);
            category.setText(element.getCategory());
            //isWatched.set
            this.key = key;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 121, 0, "Delete this item");
            menu.add(this.getAdapterPosition(), 122, 1, "Update this item");
        }
    }


        class ElementAdapter extends RecyclerView.Adapter<ElementItemView>{
        private List<Element> elementList;
        private List<String> keysList;

        public ElementAdapter(List<Element> elementList, List<String> keysList) {
            this.elementList = elementList;
            this.keysList = keysList;
        }


        @NonNull
        @Override
        public ElementItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mylist, parent, false);

           return new ElementItemView(parent);
          //  return new ElementItemView((ViewGroup) view);
        }

        @Override
        public void onBindViewHolder(@NonNull ElementItemView holder, int position) {
            holder.bind(elementList.get(position), keysList.get(position));
        }

//Ostatni filmik pokazuje jak zrobić to context menu ale my działamy na innej zasadzie, kontynuować ze starego filmiku
        @Override
        public int getItemCount() {
            return elementList.size();
        }

        public void removeItem(int position){
            elementList.remove(position);
            notifyDataSetChanged();
        }
    }

}
