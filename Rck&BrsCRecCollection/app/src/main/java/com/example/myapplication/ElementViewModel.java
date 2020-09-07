package com.example.myapplication;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ElementViewModel extends AndroidViewModel {

    private ElementRoomRepository elementRoomRepository;

    public ElementViewModel(@NonNull Application application) {
        super(application);

        elementRoomRepository = new ElementRoomRepository(application);
    }

    public LiveData<List<Element>> getAllElements() {
        return elementRoomRepository.getElementLiveData();
    }

    public void createElement(int id, String title, String category, String reccomendation, boolean isWatched) {
        elementRoomRepository.createElement(id, title, category, reccomendation, isWatched);
    }

    public void updateElement(Element element) {
        elementRoomRepository.updateElement(element);
    }

    public void deleteElement(Element element) {
        elementRoomRepository.deleteElement(element);
    }

    public void clear() {
        elementRoomRepository.clear();
    }

}
