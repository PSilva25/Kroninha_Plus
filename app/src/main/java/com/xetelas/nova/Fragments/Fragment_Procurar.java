package com.xetelas.nova.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xetelas.nova.Adapter.CaronasAdapter;
import com.xetelas.nova.Objects.Caronas;
import com.xetelas.nova.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Procurar extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FloatingActionButton fab, fabdelete;
    List<Caronas> dados = new ArrayList<>();
    List<Caronas> ordenado = new ArrayList<>();
    List<Caronas> dados2 = new ArrayList<>();
    CaronasAdapter ad;
    Context context;
    String tellphone, link;
    Boolean isFilter = false;
    TextView origem, destino, date;
    EditText data;
    Button cancel, filtro;
    AutoCompleteTextView de, para;
    Calendar myCalendar = Calendar.getInstance();
    ListView lv;


    int xd, resposta = 0, novodia = 0, novomes = 0, novoano = 0;
    int diaatual = 0,mesatual = 0,anoatual = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_procurar, container, false);
        fab = view.findViewById(R.id.fab);
        fabdelete = view.findViewById(R.id.delete);

        context = getContext();

        if (isFilter) {
            fabdelete.show();
        } else {
            fabdelete.hide();
        }

        fabdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preencher();
                isFilter = false;
                fabdelete.hide();
            }
        });

        lv = view.findViewById(R.id.lista_geral);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup();
            }
        });

        preencher();

        return view;
    }

    public void preencher() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        SimpleDateFormat formataData1 = new SimpleDateFormat("dd-MM-yyyy");
        Date data1 = new Date();
        String dataFormatada1;
        dataFormatada1 = formataData1.format(data1);

        String[] pegadata = dataFormatada1.split("-");

        final int pegadia = Integer.valueOf(pegadata[0]);
        final int pegames = Integer.valueOf(pegadata[1]);
        final int pegaano = Integer.valueOf(pegadata[2]);
        int x = 0;


        diaatual = Integer.valueOf(pegadata[0]);
        mesatual = Integer.valueOf(pegadata[1]);
        anoatual = Integer.valueOf(pegadata[2]);

        if (mesatual == 1 || mesatual == 3 || mesatual == 5 || mesatual == 7 || mesatual == 8 || mesatual == 10 || mesatual == 12) {

            xd = diaatual + 7;
            novodia = xd;
            novomes = mesatual;
            novoano = anoatual;

            if (xd > 31) {
                resposta = xd - 31;
                novodia = resposta;
                novomes = mesatual + 1;
                novoano = anoatual;

                if (novomes > 12) {
                    novoano = anoatual + 1;
                    novomes = 1;
                    novodia = resposta;
                }
            }


        } else if (mesatual == 2 || mesatual == 4 || mesatual == 6 || mesatual == 9 || mesatual == 11) {

            xd = diaatual + 7;
            novodia = xd;
            novomes = mesatual;
            novoano = anoatual;

            if (xd > 30) {
                resposta = xd - 30;
                novodia = resposta;
                novomes = mesatual + 1;


                if (novomes > 12) {
                    novoano = anoatual + 1;
                    novomes = 1;
                    novodia = resposta;
                }


            } else if (xd > 28 && mesatual == 2) {

                resposta = xd - 28;

                novodia = resposta;
                novomes = mesatual + 1;

            } else if (xd > 29 && mesatual == 2) {

                resposta = xd - 29;
                novodia = resposta;
                novomes = mesatual + 1;
            }
        }


        databaseReference.addValueEventListener(new ValueEventListener() {


            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dados.clear();
                String[] datapost = {""};
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    tellphone = (String) userSnapshot.child("telefone").getValue();
                    link = (String) userSnapshot.child("linkFace").getValue();
                    for (DataSnapshot objSnapshot : userSnapshot.child("Caronas").getChildren()) {


                        if (objSnapshot.child("data").exists()) {

                            datapost = objSnapshot.child("data").getValue().toString().split("/");


                            int diapost = Integer.valueOf(datapost[0]);
                            int mespost = Integer.valueOf(datapost[1]);
                            int anopost = Integer.valueOf(datapost[2]);

                            if (diapost > diaatual) {

                                diapost = diaatual;

                            }

                            if ((diapost >= diaatual && diapost <= novodia) && (mespost >= mesatual || mespost >= novomes) && (anopost >= anoatual || anopost <= novoano)) {
                                Caronas car = new Caronas();

                                car.setTell(tellphone);
                                car.setLink(link);
                                car.setId_post((String) objSnapshot.child("id_post").getValue());
                                car.setNome((String) objSnapshot.child("usuario").getValue());
                                car.setOrigem((String) objSnapshot.child("origem").getValue());
                                car.setDestino((String) objSnapshot.child("destino").getValue());
                                car.setData((String) objSnapshot.child("data").getValue());
                                car.setId((String) objSnapshot.child("id").getValue());
                                car.setHora((String) objSnapshot.child("hora").getValue());
                                car.setComent((String) objSnapshot.child("comentario").getValue());

                                dados.add(car);
                            }
                        }
                    }
                }

                Collections.sort(dados);

                ad = new CaronasAdapter(context, ordena(), getFragmentManager());
                if (!ad.isEmpty())

                    lv.setAdapter(ad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void filtro(final String or, final String des, final String da) {
        if (or.equals("") && des.equals("") && da.equals("")) {
            Toast.makeText(getContext(), "Selecione um filtro...", Toast.LENGTH_SHORT).show();
        } else {
            isFilter = true;
            fabdelete.show();
            dados2.clear();

            for (int x = 0; x < dados.size(); x++) {
                String prim = dados.get(x).getOrigem();
                String seg = dados.get(x).getDestino();
                String ter = dados.get(x).getData();

                if (!or.equals("") && des.equals("") && da.equals("")) {
                    if (prim.equals(or)) {
                        encherFiltro(x);
                    }
                } else if (or.equals("") && !des.equals("") && da.equals("")) {
                    if (seg.equals(des)) {
                        encherFiltro(x);
                    }
                } else if (or.equals("") && des.equals("") && !da.equals("")) {
                    if (ter.equals(da)) {
                        encherFiltro(x);
                    }
                } else if (!or.equals("") && !des.equals("") && da.equals("")) {
                    if (prim.equals(or) && seg.equals(des)) {
                        encherFiltro(x);
                    }
                } else if (!or.equals("") && des.equals("") && !da.equals("")) {
                    if (prim.equals(or) && ter.equals(da)) {
                        encherFiltro(x);
                    }
                } else if (or.equals("") && !des.equals("") && !da.equals("")) {
                    if (seg.equals(des) && ter.equals(da)) {
                        encherFiltro(x);
                    }
                }
            }

            ad = new CaronasAdapter(getContext(), dados2, getFragmentManager());

            lv.setAdapter(ad);
        }
    }

    public void encherFiltro(int x) {
        Caronas car = new Caronas();

        car.setNome(dados.get(x).getNome());
        car.setOrigem(dados.get(x).getOrigem());
        car.setDestino(dados.get(x).getDestino());
        car.setData(dados.get(x).getData());
        car.setId(dados.get(x).getId());
        car.setId_post(dados.get(x).getId_post());
        car.setHora(dados.get(x).getHora());
        car.setComent(dados.get(x).getComent());
        car.setTell(dados.get(x).getTell());
        car.setLink(dados.get(x).getLink());

        dados2.add(car);

        Collections.sort(dados2);
    }

    public void ShowPopup() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater factory = LayoutInflater.from(getContext());
        View content = factory.inflate(R.layout.popup_filtro, null);

        builder.setTitle("Encontre sua carona:");

        origem = content.findViewById(R.id.id_de);
        destino = content.findViewById(R.id.id_para);
        date = content.findViewById(R.id.text_date);

        de = content.findViewById(R.id.spinner_de);
        para = content.findViewById(R.id.spinner_para);
        data = content.findViewById(R.id.edit_Data);

        String[] cities = getResources().getStringArray(R.array.cidades);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cities);

        de.setAdapter(adapter);
        para.setAdapter(adapter);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        builder.setView(content)
                .setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        filtro(de.getText().toString(), para.getText().toString(), data.getText().toString());
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.show();
    }

    public List<Caronas> ordena() {
        ordenado.clear();

        for (int i = dados.size() - 1; i >= 0; i--) {
            ordenado.add(dados.get(i));
        }
        return ordenado;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));

        data.setText(sdf.format(myCalendar.getTime()));
    }
}