package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;
import java.util.Collection;
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
    private List<Element> elementsFilter = new ArrayList<>();

    //ten prawdziwy setConfig - setAdapter
//    public void setConfig(RecyclerView recyclerView, Context context, List<Element> elements, List<String> keys) {
//        mContext = context;
//        elementAdapter = new ElementAdapter(elements, keys);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(elementAdapter);
//    }

    public void setConfig(RecyclerView recyclerView, Context context, List<Element> elements, List<String> keys, List<Element> elementsFilter) {
        mContext = context;
        elementAdapter = new ElementAdapter(elements, keys, elementsFilter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(elementAdapter);
    }
    //przeciążona metoda wersji wyżej - tej prawdziwej
    public RecyclerView_Config.ElementAdapter setConfig(RecyclerView recyclerView, Context context, List<Element> elements, List<String> keys, List<Element> elementsFilter, ElementAdapter elementAdapter) {
        mContext = context;
        this.elementAdapter = new ElementAdapter(elements, keys, elementsFilter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(this.elementAdapter);
        return this.elementAdapter;
    }

    class ElementItemView extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView title;
        private TextView category;
        private CheckBox isWatched;
        private String key;
        private CardView cardView;
        SearchView sv;
        AdapterView.OnItemClickListener mListener;
        MenuItem searchItem;

        public ElementItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.mylist, parent, false));
            title = (TextView) itemView.findViewById(R.id.titleTextView);
            category = (TextView) itemView.findViewById(R.id.categoryTextView);
            isWatched = (CheckBox) itemView.findViewById(R.id.checkBox);
            cardView = itemView.findViewById(R.id.cardLayout);

            cardView.setOnCreateContextMenuListener(this);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EditElement.class);
                    intent.putExtra("key", key);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("category", category.getText().toString());
                    mContext.startActivity(intent);
                }
            });
            isWatched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Element element = new Element();
                    //boolean isOrNot =   element.isWatched();
                    element.setWatched(isWatched.isChecked());
                    element.setTitle(title.getText().toString());
                    element.setCategory(category.getText().toString());
                    new FirebaseDatabaseHelper().updateElement(key, element, new FirebaseDatabaseHelper.DataStatus() {
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
                        }
                    });
                }
            });
        }

        public void bind(Element element, String key) {
            //  title.setText(element.getTitle());
            String e = element.getTitle();
            title.setText(e);
            category.setText(element.getCategory());
            isWatched.setChecked(element.isWatched());
            this.key = key;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 121, 0, "Delete this item");
            menu.add(this.getAdapterPosition(), 122, 1, "Update this item");
        }
    }


    public class ElementAdapter extends RecyclerView.Adapter<ElementItemView> implements Filterable{
        private List<Element> elementList;
        private List<String> keysList;
        public List<Element> filterElementList;

        public ElementAdapter(List<Element> elementList, List<String> keysList) {
            this.elementList = elementList;
            this.keysList = keysList;
        }

        //Konstruktor do filtracji
        public ElementAdapter(List<Element> elementList, List<String> keysList, List<Element> filterElementList) {
            this.elementList = elementList;
            this.keysList = keysList;
            this.filterElementList = filterElementList;
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

        @Override
        public int getItemCount() {
            return elementList.size();
        }

        public void removeItem(int position) {
            elementList.remove(position);
            notifyDataSetChanged();
        }

        @Override
        public Filter getFilter() {
            return (Filter) exampleFilter;
        }

        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Element> elementList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0){
                    filterElementList.addAll(elementList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Element e : elementList){
                        if (e.getTitle().toLowerCase().contains(filterPattern))
                            filterElementList.add(e);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filterElementList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                elementList.clear();
                elementList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
        public void updateList(List<Element> newList){
            elementList = new ArrayList<>();
            elementList.addAll(newList);
            notifyDataSetChanged();
        }
    }

}
