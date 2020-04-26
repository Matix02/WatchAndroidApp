package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_Config {
    private Context mContext;
    private ElementAdapter elementAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Element> elements, List<String> keys){
        mContext = context;
        elementAdapter = new ElementAdapter(elements, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(elementAdapter);
    }
//spróbować zmodyfikować tą klasę jak z filmiku, by zadziałoało popup menu

    class ElementItemView extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView category;
        private CheckBox isWatched;
        private String key;

        public ElementItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.mylist, parent, false) );
            title = (TextView) itemView.findViewById(R.id.titleTextView);
            category = (TextView) itemView.findViewById(R.id.categoryTextView);
            isWatched = (CheckBox) itemView.findViewById(R.id.checkBox);
        }

        public void bind(Element element, String key){
          //  title.setText(element.getTitle());
            String e = element.getTitle();
            title.setText(e);
            category.setText(element.getCategory());
            //isWatched.set
            this.key = key;
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
            return new ElementItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ElementItemView holder, int position) {
            holder.bind(elementList.get(position), keysList.get(position));
        }

        @Override
        public int getItemCount() {
            return elementList.size();
        }
    }
}
