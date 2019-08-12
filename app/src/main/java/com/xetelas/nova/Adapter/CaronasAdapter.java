package com.xetelas.nova.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xetelas.nova.Objects.Caronas;
import com.xetelas.nova.R;

import java.util.List;

public class CaronasAdapter extends BaseAdapter {

    private Context context;
    private List<Caronas> fragments;
    private FragmentManager fragmentManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();


    public CaronasAdapter(Context context, List<Caronas> fragments, FragmentManager fragmentManager) {
        this.context = context;
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
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
        View view = View.inflate(context, R.layout.listview_procurar, null);
        TextView usuario, origem, or, destino, des, data, da, hora, ho, comentario, coment2;
        ImageView whats, face;

        usuario = view.findViewById(R.id.usuario);
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

        whats = view.findViewById(R.id.whats);
        face = view.findViewById(R.id.face);

        whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = "+55 "; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact + fragments.get(position).getTell();
                try {
                    PackageManager pm = context.getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();




                String url = fragments.get(position).getLink();

                if( url != null ){

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);


                }else {

                    Toast toast = Toast.makeText(context, "LINK NÃO DISPONIVEL", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();


                }
            }

        });

        usuario.setText(fragments.get(position).getNome());
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

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}

