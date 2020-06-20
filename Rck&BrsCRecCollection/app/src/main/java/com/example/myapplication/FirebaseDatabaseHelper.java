package com.example.myapplication;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceBooks;
    private List<Element> elements = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<Element> elements, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooks = mDatabase.getReference("Element");
    }

    void readElements(final DataStatus dataStatus) {
        mReferenceBooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                elements.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Element element = keyNode.getValue(Element.class);
                    elements.add(element);
                }
                dataStatus.DataIsLoaded(elements, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
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
    }

    void updateElement(String key, Element element, final DataStatus dataStatus) {
        mReferenceBooks.child(key).setValue(element)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }

    void deleteElement(String key, final DataStatus dataStatus) {
        mReferenceBooks.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

    /*można powstawiać break'y w tych pętlach, by nie wykonywały się tak do końca zawsze
    bo to może ograniczyć wydajność aplikacji
     */
    /*
    inny pomsysł, bo też można zebrać wszystkie informacje do tablicy, jak już pętla ją
    z'for'uje i wtedy wylosować, może to być wydajniejsze
     */
    /*
    Raz działa a raz nie, znaczy działa za drugim razem (!!!), i jeszcze trzeba popatrzeć na filtrację, bo trzeba
    zrobić odejmowanie oglądniętych i tych samych z tego RadioGroupa ... ta dam
     */
    void randomElement(final int sizeOfList, final DataStatus dataStatus) {
        mReferenceBooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double randomIndex = Math.floor(Math.random() * sizeOfList);
                mReferenceBooks.orderByChild("index").startAt(randomIndex).endAt(randomIndex + 1);
                elements.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
             //       for (DataSnapshot randomKeyNode : keyNode.getChildren()) {
                        for (int i = 0; i < randomIndex; i++) {
                            if (i == randomIndex - 1) {
                                keys.add(keyNode.getKey());
                                Element element = keyNode.getValue(Element.class);
                                elements.add(element);
                            }
                        }
             //       }
                }
                Log.i("Numer losowego indexu to - ", Double.toString(randomIndex));
                Log.i("Tytuł losowego elementu to - ", elements.get(0).getTitle());
                dataStatus.DataIsLoaded(elements, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
