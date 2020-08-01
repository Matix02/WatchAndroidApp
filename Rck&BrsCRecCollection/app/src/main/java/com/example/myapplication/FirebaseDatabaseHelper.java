package com.example.myapplication;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import androidx.annotation.NonNull;

class FirebaseDatabaseHelper {
    private DatabaseReference mReferenceBooks;
    private List<Element> elements = new ArrayList<>();
    private static String resultTitle;
    //private RoomDatabaseHelper roomDatabaseHelper;

    public interface DataStatus {
        void DataIsLoaded(List<Element> elements, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();

        void DataIsSelected(String randomElement);
    }

    FirebaseDatabaseHelper() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooks = mDatabase.getReference("Element");
    }

    void readElements(final DataStatus dataStatus) {
        mReferenceBooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                elements.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    //Jakaś metoda, która pobiera argumenty filtra
                   // if(Objects.requireNonNull(keyNode.getValue(Element.class)).getCategory().equals("Gra")){
                        keys.add(keyNode.getKey());
                        Element element = keyNode.getValue(Element.class);
                        elements.add(element);
                  //  }
                }
                dataStatus.DataIsLoaded(elements, keys);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    void addElement(Element element, long id, final DataStatus dataStatus) {
        // String key = mReferenceBooks.push().getKey();
        mReferenceBooks.child(String.valueOf((id + 1))).setValue(element)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
        element.setId(id+1);
        MainActivity.roomDatabaseHelper.getElementDao().addElement(element);
    }

    void updateElement(String key, Element element, final DataStatus dataStatus) {
//        roomDatabaseHelper.getElementDao().updateElemet(element);
        mReferenceBooks.child(key).setValue(element)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
        MainActivity.roomDatabaseHelper.getElementDao().updateElementById(Long.parseLong(key), element.getTitle(), element.getRecom(), element.getCategory());
    }

    void deleteElement(String key, final DataStatus dataStatus) {
        mReferenceBooks.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });

        MainActivity.roomDatabaseHelper.getElementDao().deleteIdElement(Integer.parseInt(key));
    }

    /*
    #1. można powstawiać break'y w tych pętlach, by nie wykonywały się tak do końca zawsze
    bo to może ograniczyć wydajność aplikacji

    #2. inny pomsysł, bo też można zebrać wszystkie informacje do tablicy, jak już pętla ją
    z'for'uje i wtedy wylosować, może to być wydajniejsze

    #3. Raz działa a raz nie, znaczy działa za drugim razem (!!!), i jeszcze trzeba popatrzeć na filtrację, bo trzeba
    zrobić odejmowanie oglądniętych i tych samych z tego RadioGroupa ... ta dam

    #.4 - Update#1
        Poprawić wyświetlanie pustej listy, gdy w danej kategorii nie ma ani jednego elementu.
        Można przyjrzeć się blokowi try/catch, lub sprawdzać czy w danej liście znajduje się cokolwiek, jeśli nie RadioButton jest wyłączany
        przy rzeczonej kategorii.
    #.4 - Update#2
        Po co szukać czegoś co już zostało oglądnięte :(
        Czyli dodać kolejną filtrację ...

    #5. - Trzeba innaczej to nazwać, jest mylące*/

    void countCategory(final String category, final DataStatus dataStatus){
        mReferenceBooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                if (category.equals("Wszystko")){
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        keys.add(Objects.requireNonNull(keyNode.getValue(Element.class)).getTitle());
                    }
                }
                else{
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        if (category.equals(Objects.requireNonNull(keyNode.getValue(Element.class)).getCategory())){
                            keys.add(Objects.requireNonNull(keyNode.getValue(Element.class)).getTitle());
                        }
                    }
                }
                //gdy kategoria bedzie pusta - no chyba jednak nie ...
                try{
                    int pop = new PopActivity().generateRandomIndex(keys.size());
                    resultTitle = keys.get(pop-1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dataStatus.DataIsSelected(resultTitle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void updateFilter(ElementFilter elementFilter) {
        MainActivity.roomDatabaseHelper.getElementDao().updateFilter(elementFilter);
    }

}
