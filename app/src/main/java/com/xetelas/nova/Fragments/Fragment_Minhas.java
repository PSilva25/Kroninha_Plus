package com.xetelas.nova.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xetelas.nova.Adapter.CaronasAdapterMinhas;
import com.xetelas.nova.Objects.Caronas;
import com.xetelas.nova.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment_Minhas extends Fragment {
    ListView lv;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseref;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();

    List<Caronas> dados = new ArrayList<>();
    List<Caronas> ordenado = new ArrayList<>();
    Context context;
    CaronasAdapterMinhas ad;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        View view = inflater.inflate(R.layout.fragment_minhas, container, false);

        context = getContext();
        lv = view.findViewById(R.id.lista_minhas);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dados.clear();

                for (DataSnapshot objSnapshot:dataSnapshot.child(user.getDisplayName() + " - " + user.getUid()).child("Caronas").getChildren()){
                    Caronas car = new Caronas();

                    car.setNome((String) objSnapshot.child("usuario").getValue());
                    car.setOrigem((String) objSnapshot.child("origem").getValue());
                    car.setDestino((String) objSnapshot.child("destino").getValue());
                    car.setData((String) objSnapshot.child("data").getValue());
                    car.setId((String) objSnapshot.child("id").getValue());
                    car.setId_post((String) objSnapshot.child("id_post").getValue());
                    car.setHora((String) objSnapshot.child("hora").getValue());
                    car.setComent((String)objSnapshot.child("comentario").getValue());

                    dados.add(car);
                }

                Collections.sort(dados);

                ad = new CaronasAdapterMinhas(context,ordena());

                lv.setAdapter(ad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    public List<Caronas> ordena() {
        ordenado.clear();

        for (int i = dados.size() - 1; i >= 0; i--) {
            ordenado.add(dados.get(i));
        }
        return ordenado;
    }
}
