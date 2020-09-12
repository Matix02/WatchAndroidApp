package com.example.myapplication;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

public class ElementViewModel extends AndroidViewModel {

    private ElementRoomRepository elementRoomRepository;
    LiveData<List<Element>> listLiveData;
    private MutableLiveData<List<Element>> elementLiveData = new MutableLiveData<>();


    //Dodać metodę, która filtruje ogołną bazę, w metodzie get, wyciąć te filtry i zwracam observable
    public ElementViewModel(@NonNull Application application) {
        super(application);

        //       elementRoomRepository = Transformations.switchMap()

        elementRoomRepository = new ElementRoomRepository(application);
    }

    public LiveData<List<Element>> getAllElements() {


        return elementRoomRepository.getElementLiveData();
    }

    /*
    Czy to w ogóle się zmieni po edycji, niby powinno zapewnić edycję, albo będzie się tak działo, gdy PopFilter będzie edytowac te dane przy pomocy,
    metod, które tutaj powinny się znaleźć, jak Edycja Filtra a następnie użycie ich w PopFilter, może wtedy zadziała trigger modyfikacji filtra.
    Może najpierw sprawdzić czy działa te dodawanie stąd 
     */
    public LiveData<ElementFilter> getFilter() {
        return elementRoomRepository.getDatabase();
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
