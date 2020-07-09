package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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
    //gównianie zdefiniowana metoda do usieniecia lub poprawienia
    public RecyclerView_Config.ElementAdapter setConfig(RecyclerView recyclerView, Context context, List<Element> elements, List<String> keys, List<Element> elementsFilter, ElementAdapter elementAdapter) {
        mContext = context;
        this.elementAdapter = new ElementAdapter(elements, keys, elementsFilter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(this.elementAdapter);
        return this.elementAdapter;
    }

    class ElementItemView extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView category;
        private CheckBox isWatched;
        private String key;
        AdapterView.OnItemClickListener mListener;

        ElementItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.mylist, parent, false));
            title = itemView.findViewById(R.id.titleTextView);
            category =  itemView.findViewById(R.id.categoryTextView);
            isWatched =  itemView.findViewById(R.id.checkBox);
            CardView cardView = itemView.findViewById(R.id.cardLayout);

          /*
           Zawsze mozna sprobowac zrobić coś z tym SearchView niby,
           bo CardView działa i ma się dobre.

                  MenuItem menuItem = menu.findItem(R.id.search_item1);
        SearchView searchView = (SearchView) menuItem.getActionView();
          SearchView searchView = itemView.findViewById(R.id.search_item1);

            searchView.setOnCreateContextMenuListener(this);
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }); */

          //  cardView.setOnCreateContextMenuListener(this);
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


            /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   * *********************************
            * !!!!!!!!!!!!!!!!*/
            //Tu się dzieje, gdy klika się oglądane lub też nie
            isWatched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Element element = new Element();
                    element.setId(Integer.parseInt(key));
                    element.setWatched(isWatched.isChecked());
                    element.setTitle(title.getText().toString());
                    element.setCategory(category.getText().toString());

                    MainActivity.roomDatabaseHelper.getElementDao().updateElemet(element);
                }
            });
        }

        //łączenie elementów z listy do wyswietlenia na ekranie
        void bind(Element element, String key, Element roomE) {
            //  title.setText(element.getTitle());
            String e = element.getTitle();
            title.setText(e);
            category.setText(element.getCategory());
            //Local isWatched
            isWatched.setChecked(roomE.isWatched());
            this.key = key;
        }

//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.add(this.getAdapterPosition(), 121, 0, "Delete this item");
//            menu.add(this.getAdapterPosition(), 122, 1, "Update this item");
//        }
    }


    public class ElementAdapter extends RecyclerView.Adapter<ElementItemView> implements Filterable{
        private List<Element> elementList;
        private List<String> keysList;
        List<Element> filterElementList;

        /*pierwotny konstruktor
        public ElementAdapter(List<Element> elementList, List<String> keysList) {
            this.elementList = elementList;
            this.keysList = keysList;
        } */

        //Konstruktor do filtracji
        ElementAdapter(List<Element> elementList, List<String> keysList, List<Element> filterElementList) {
            this.elementList = elementList;
            this.keysList = keysList;
            this.filterElementList = filterElementList;
        }

        private void updateLocalElement(String title, String category, boolean isWatched, int position){
            Element element = filterElementList.get(position);

            element.setTitle(title);
            element.setCategory(category);
            element.setWatched(isWatched);

        }
        @NonNull
        @Override
        public ElementItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ElementItemView(parent);
            //  return new ElementItemView((ViewGroup) view);
        }


        @Override
        public void onBindViewHolder(@NonNull ElementItemView holder, int position) {
            holder.bind(elementList.get(position), keysList.get(position), filterElementList.get(position));
        }

        @Override
        public int getItemCount() {
            return elementList.size();
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

        void updateList(List<Element> newList){
            elementList = new ArrayList<>();
            elementList.addAll(newList);
            notifyDataSetChanged();
        }
    }

}
