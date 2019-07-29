package com.xetelas.nova.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xetelas.nova.Objects.Caronas;
import com.xetelas.nova.R;

import java.util.List;

public class CaronasAdapterMinhas extends BaseAdapter {


    private Context context;
    private List<Caronas> fragments;
    Dialog myDialog;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public CaronasAdapterMinhas(Context context, List<Caronas> fragments) {
        this.context = context;
        this.fragments = fragments;

    }

    @Override
    public Caronas getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.listview_minhas,null);
        TextView origem, or, destino, des, data, da, hora, ho, comentario, coment2;
        ImageView button;

        origem = view.findViewById(R.id.origem);
        or = view.findViewById(R.id.origem2);
        destino = view.findViewById(R.id.destino);
        des = view.findViewById(R.id.destino2);
        data = view.findViewById(R.id.data);
        da = view.findViewById(R.id.data2);
        hora = view.findViewById(R.id.hora);
        ho = view.findViewById(R.id.hora2);
        comentario = view.findViewById(R.id.comentario);
        coment2 = view.findViewById(R.id.comentario2);

        button = view.findViewById(R.id.del);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(position);
            }
        });

        origem.setText("Origem: ");
        or.setText(fragments.get(position).getOrigem());
        destino.setText("Destino: ");
        des.setText(fragments.get(position).getDestino());
        data.setText("Data: ");
        da.setText(fragments.get(position).getData());
        hora.setText("Hora: ");
        ho.setText(fragments.get(position).getHora());
        comentario.setText("Informações adicionais:");
        coment2.setText(fragments.get(position).getComent());

        view.setTag(fragments.get(position).getId());

        return view;
    }

    public void deleta(int position){
        DatabaseReference desertRef = databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Caronas").child(fragments.get(position).getId_post());

        desertRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    public void ShowPopup(final int position) {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.popup_delete);

        Button filtro = myDialog.findViewById(R.id.bot_deleta);
        filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleta(position);
                myDialog.dismiss();
            }
        });

        Button cancel = myDialog.findViewById(R.id.bot_nao);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        myDialog.show();
    }

}

