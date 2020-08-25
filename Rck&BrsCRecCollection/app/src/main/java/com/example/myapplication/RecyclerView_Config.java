package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_Config {
    private Context mContext;

    RecyclerView_Config.ElementAdapter setConfig(RecyclerView recyclerView, Context context, List<String> keys, List<Element> elementsFilter) {
        mContext = context;
        ElementAdapter elementAdapter = new ElementAdapter(keys, elementsFilter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(elementAdapter);
        return elementAdapter;
    }

    class ElementItemView extends RecyclerView.ViewHolder {
        private String key;
        private TextView title;
        private TextView category;
        private CheckBox isWatched;
        private TextView recommendation;

        ElementItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.mylist, parent, false));
            title = itemView.findViewById(R.id.titleTextView);
            category = itemView.findViewById(R.id.categoryTextView);
            isWatched = itemView.findViewById(R.id.checkBox);
            recommendation = itemView.findViewById(R.id.recomemndation);
            CardView cardView = itemView.findViewById(R.id.cardLayout);

            //ListView - informacje o naciśniętym elemencie
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, EditElement.class);
                intent.putExtra("key", key);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("category", category.getText().toString());
                mContext.startActivity(intent);
            });

            //CheckBox - Oglądane lub Nieoglądane
            isWatched.setOnClickListener(v -> {

                Element element = new Element();
                element.setId(Integer.parseInt(key));
                element.setWatched(isWatched.isChecked());
                element.setTitle(title.getText().toString());
                element.setCategory(category.getText().toString());
                element.setRecom(recommendation.getText().toString());

                MainActivity.roomDatabaseHelper.getElementDao().updateElement(element);
            });
        }

        //łączenie elementów z listy do wyswietlenia na ekranie
        void bind(String key, Element roomE) {
            String e = roomE.getTitle();
            this.key = key;
            title.setText(e);
            category.setText(roomE.getCategory());
            recommendation.setText(roomE.getRecom());
            isWatched.setChecked(roomE.isWatched());
        }
    }

    public class ElementAdapter extends RecyclerView.Adapter<ElementItemView>{
        private List<String> keysList;
        List<Element> filterElementList;

        //Konstruktor do filtracji
        ElementAdapter(List<String> keysList, List<Element> filterElementList) {
            this.keysList = keysList;
            this.filterElementList = filterElementList;
        }

        @NonNull
        @Override
        public ElementItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ElementItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ElementItemView holder, int position) {
            holder.bind(keysList.get(position), filterElementList.get(position));
        }

        @Override
        public int getItemCount() {
            return filterElementList.size();
        }

        void updateList(List<Element> newList, List<String> newKeyList) {
            keysList = new ArrayList<>();
            //filterElementList.clear();
            filterElementList = new ArrayList<>();
            keysList.addAll(newKeyList);
            filterElementList.addAll(newList);
            notifyDataSetChanged();
        }
    }
}
