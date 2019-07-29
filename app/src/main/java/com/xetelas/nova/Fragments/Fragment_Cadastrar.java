package com.xetelas.nova.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xetelas.nova.FireMissilesLimit;
import com.xetelas.nova.Objects.Caronas;
import com.xetelas.nova.MainActivity;
import com.xetelas.nova.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Fragment_Cadastrar extends Fragment {

    AutoCompleteTextView de, para;
    EditText data, hora, coment;
    String opaLink = MainActivity.link;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseref, databaserefcont, databasetell, databaseverifica, dataBasedata;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    final Calendar myCalendar = Calendar.getInstance();
    String num = null;
    Dialog myDialog;
    EditText tell;
    String contadora = "0";
    int conta2 = 0;
    int diax = 0, mesx = 0, anox = 0, diaatual = 0, mesatual = 0, anoatual = 0, quantidade, MESATUAL =0;
    String[] cities;
    final String[] verifica = {""};
    private View view;
    private Button button;
    long maxid = 0;
    Context context;
    int veri = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        view = inflater.inflate(R.layout.fragment_cadastrar, container, false);

        de = view.findViewById(R.id.spinner_de);
        para = view.findViewById(R.id.spinner_para);
        data = view.findViewById(R.id.edit_Data);
        hora = view.findViewById(R.id.edit_Hora);
        coment = view.findViewById(R.id.edit_coment);

        context = getContext();


        cities = getResources().getStringArray(R.array.cidades);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cities);

        de.setAdapter(adapter);
        para.setAdapter(adapter);

        databaseref = firebaseDatabase.getReference().child(user.getDisplayName() + " - " + user.getUid()).child("Caronas");

        databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("linkFace").setValue(opaLink);
        verificaTell();
        databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxid = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaserefcont = firebaseDatabase.getReference().child("total_caronas");

        databaserefcont.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contadora = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button = view.findViewById(R.id.bot_cadastrar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date data2 = new Date();
                String dataFormatada;
                dataFormatada = formataData.format(data2);

                String[] pegaHoraatual = null, pegaHoracadastrada = null, pega = null, pegadataentrada = null;
                int horaatual = 0, horacadastrada = 0, diaatual = 0, diacadastrado = 0, mesatual = 0, mescadastrado = 0, anoatual = 0, anocadastrado = 0, minatual = 0, mincadastrado = 0;

                int z;
                z = verify();

                if (de.getText().toString().equals("") || de.getText().toString().equals("") || data.toString().equals("") || hora.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getContext(), "PREENCHA OS CAMPOS OBRIGATORIOS (*)  ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (z == -1) {
                    Toast toast = Toast.makeText(getContext(), "ORIGEM E DESTINO PRECISAM SER DIFERENTES", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (z == 1 || z == 0) {
                    Toast toast = Toast.makeText(getContext(), "CIDADE NAO ENCONTRADA", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (z == 2) {

                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    Date data_atual = cal.getTime();
                    String hora_atual = dateFormat_hora.format(data_atual);

                    pegaHoraatual = hora_atual.split(":");
                    pegaHoracadastrada = hora.getText().toString().split(":");

                    horaatual = Integer.valueOf(pegaHoraatual[0]);
                    minatual = Integer.valueOf(pegaHoraatual[1]);
                    horacadastrada = Integer.valueOf(pegaHoracadastrada[0]);
                    mincadastrado = Integer.valueOf(pegaHoracadastrada[1]);

                    pega = dataFormatada.split("-");
                    pegadataentrada = data.getText().toString().split("/");
                    String pega1 = dataFormatada;
                    String pegadataentrada1 = data.getText().toString();

                    diaatual = Integer.valueOf(pega[0]);
                    mesatual = Integer.valueOf(pega[1]);
                    anoatual = Integer.valueOf(pega[2]);
                    diacadastrado = Integer.valueOf(pegadataentrada[0]);
                    mescadastrado = Integer.valueOf(pegadataentrada[1]);
                    anocadastrado = Integer.valueOf(pegadataentrada[2]);
                    int resposta = veriFuturo(pegadataentrada1,pega1);

                   if(resposta==1){


                       Toast toast = Toast.makeText(getContext(), "DATA LIMITE DE CADASTRO:ATE 7 DIAS DO DIA ATUAL, COLOQUE UMA DATA DIFERENTE", Toast.LENGTH_LONG);
                       toast.setGravity(Gravity.CENTER, 0, 0);
                       toast.show();

                   }else


                    if ((diaatual >= diacadastrado && mesatual > mescadastrado && anoatual == anocadastrado)) {

                        Toast toast = Toast.makeText(getContext(), "ESSA DATA JÁ PASSOU! ESCOLHA UMA NOVA DATA...", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else if ((diaatual <= diacadastrado && mesatual > mescadastrado && anoatual == anocadastrado)) {

                        Toast toast = Toast.makeText(getContext(), "ESSA DATA JÁ PASSOU! ESCOLHA UMA NOVA DATA...", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else if ((diaatual <= diacadastrado && mesatual < mescadastrado && anoatual > anocadastrado)) {

                        Toast toast = Toast.makeText(getContext(), "ESSA DATA JÁ PASSOU! ESCOLHA UMA NOVA DATA...", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else  if ((diaatual > diacadastrado && mesatual == mescadastrado && anoatual <= anocadastrado)) {

                        Toast toast = Toast.makeText(getContext(), "ESSA DATA JÁ PASSOU! ESCOLHA UMA NOVA DATA...", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {

                        verificaTell();

                    }
                }

            }

        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));
                data.setText(sdf.format(myCalendar.getTime()));
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

        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hora.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Selecione a hora:");
                mTimePicker.show();
            }
        });

        return view;
    }

    public void verificaTell() {
        databasetell = firebaseDatabase.getReference().child(user.getDisplayName() + " - " + user.getUid()).child("telefone");

        databasetell.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    myDialog = new Dialog(context);
                    ShowPopup();
                } else if (dataSnapshot.getValue().toString().equals("")) {
                    myDialog = new Dialog(getContext());
                    ShowPopup();
                } else if (dataSnapshot.exists()) {

                    verificaQuantPosts();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void ShowPopup() {
        myDialog.setContentView(R.layout.popup_tell);
        final EditText dd = myDialog.findViewById(R.id.edit_tell_ddd);
        final EditText x5 = myDialog.findViewById(R.id.edit_tell_5num);
        final EditText x4 = myDialog.findViewById(R.id.edit_tell_4num);

        dd.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (dd.length() > 1) x5.requestFocus();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        x5.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (x5.length() > 3) x4.requestFocus();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        Button filtro = myDialog.findViewById(R.id.bot_addtell);
        filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tell = dd.getText().toString() + x5.getText().toString() + x4.getText().toString();
                databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("telefone").setValue(tell);
                myDialog.dismiss();

                Toast toast5 = Toast.makeText(getContext(), "TELEFONE CADASTRADO COM SUCESSO!!", Toast.LENGTH_LONG);
                toast5.setGravity(Gravity.CENTER, 0, 0);
                toast5.show();
                verificaTell();


            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        myDialog.show();
    }

    public int verify() {
        int x = 0;
        int y = 0;
        int z = 0;

        for (int i = 0; i < cities.length; i++) {
            if (de.getText().toString().trim().equals(cities[i])) {
                x = x + 1;
                break;
            }
        }

        for (int j = 0; j < cities.length; j++) {
            if (para.getText().toString().trim().equals(cities[j])) {
                y = y + 1;
                break;
            }
        }

        z = x + y;

        if (para.getText().toString().trim().equals(de.getText().toString().trim())) {
            z = -1;
        }
        return z;
    }

    public void verificaQuantPosts() {
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data2 = new Date();
        final String dataFormatada;
        dataFormatada = formataData.format(data2);



        databaseverifica = firebaseDatabase.getReference().child(user.getDisplayName() + " - " + user.getUid());
        databaseverifica.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int conta = 0;

                if (dataSnapshot.child("Qtd_caronas").exists()) {
                    veri = 1;
                }

                if (!dataSnapshot.child("Caronas").exists()) {
                    conta = 0;
                } else if (dataSnapshot.child("Caronas").exists()) {
                    for (DataSnapshot objSnapshot : dataSnapshot.child("Caronas").getChildren()) {
                        if (objSnapshot.child("data_postagem").exists()) {
                            String datacadastrada = objSnapshot.child("data_postagem").getValue().toString();
                            if (datacadastrada.equals(dataFormatada)) {
                                conta++;
                            }
                        }
                    }
                }

                conta2 = conta;
                if (veri == 1) {
                    dataBasedata = firebaseDatabase.getReference().child(user.getDisplayName() + " - " + user.getUid());
                    dataBasedata.addValueEventListener(new ValueEventListener() {
                        String pegatab = null;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            pegatab = dataSnapshot.child("Qtd_caronas").getValue().toString();

                            String[] x = pegatab.split("-");
                            String[] y = dataFormatada.split("-");
                            int diatual = Integer.valueOf(y[0]);
                            int mesatual = Integer.valueOf(y[1]);
                            int anotual = Integer.valueOf(y[2]);

                            int diatab = Integer.valueOf(x[1]);
                            int mestab = Integer.valueOf(x[2]);
                            int anotab = Integer.valueOf(x[3]);

                            quantidade = Integer.valueOf(x[0]);
                            diax = diatab;
                            mesx = mestab;
                            anox = anotab;
                            diaatual = diatual;
                            MESATUAL = mesatual;
                            anoatual = anotual;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if((mesx < MESATUAL) && quantidade>0){
            quantidade = 0;
            cadastra();

        }else if((mesx==12 && MESATUAL==1)  && quantidade>0){
            quantidade = 0;
            cadastra();

        }

        if (quantidade > 0) {
            if (quantidade <= 3) {
                cadastra();
                databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Qtd_caronas").setValue(String.valueOf((quantidade + 1) + "-" + dataFormatada));

            } else if (quantidade > 3 ) {
                FireMissilesLimit opa = new FireMissilesLimit();
                opa.show(getFragmentManager(), "missiles");
            }
        } else if (veri == 0 || quantidade == 0) {
            cadastra();

        }
    }

    public void cadastra() {

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data2 = new Date();
        final String dataFormatada;
        dataFormatada = formataData.format(data2);

        Caronas dados = new Caronas();

        dados.setId(String.valueOf(maxid + 1));
        dados.setOrigem(de.getText().toString().trim());
        dados.setDestino(para.getText().toString().trim());
        dados.setData(data.getText().toString());
        dados.setHora(hora.getText().toString());
        dados.setComent(coment.getText().toString());
        long contadora1 = Long.valueOf(contadora);

        de.setText("");
        para.setText("");
        data.setText("");
        hora.setText("");
        coment.setText("");

        if (!dados.getOrigem().equals("")) {

            databaseReference.child("total_caronas").setValue(String.valueOf(contadora1 + 1));

            databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Caronas").child(String.valueOf(contadora1 + 1)).child("data_postagem").setValue(dataFormatada);
            databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Caronas").child(String.valueOf(contadora1 + 1)).child("id_post").setValue(String.valueOf(contadora1 + 1));
            databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Caronas").child(String.valueOf(contadora1 + 1)).child("usuario").setValue(user.getDisplayName());
            databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Caronas").child(String.valueOf(contadora1 + 1)).child("origem").setValue(dados.getOrigem());
            databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Caronas").child(String.valueOf(contadora1 + 1)).child("destino").setValue(dados.getDestino());
            databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Caronas").child(String.valueOf(contadora1 + 1)).child("data").setValue(dados.getData());
            databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Caronas").child(String.valueOf(contadora1 + 1)).child("hora").setValue(dados.getHora());
            databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Caronas").child(String.valueOf(contadora1 + 1)).child("comentario").setValue(dados.getComent());
            databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("Qtd_caronas").setValue(String.valueOf((quantidade + 1) + "-" + dataFormatada));

            dados.setComent("");
            dados.setHora("");
            dados.setOrigem("");
            dados.setDestino("");
            dados.setData("");

            Toast toast2 = Toast.makeText(getContext(), "CADASTRO EFETUADO COM SUCESSO!!", Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }
    }

    public int veriFuturo(String pegadataentrada, String pega) {

        int x = 0;
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data2 = new Date();
        String dataFormatada;
        dataFormatada = formataData.format(data2);


        int horaatual = 0, horacadastrada = 0, diaatual = 0, diacadastrado = 0, mesatual = 0, mescadastrado = 0, anoatual = 0, anocadastrado = 0, minatual = 0, mincadastrado = 0;

        int xd = 0, resposta = 0,novodia=0,novomes=0,novoano=0;

        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date data_atual = cal.getTime();
        String hora_atual = dateFormat_hora.format(data_atual);


        String[] pega1 = pega.split("-");
        String[] pegadataentrada1 = pegadataentrada.split("/");

        diaatual = Integer.valueOf(pega1[0]);
        mesatual = Integer.valueOf(pega1[1]);
        anoatual = Integer.valueOf(pega1[2]);
        diacadastrado = Integer.valueOf(pegadataentrada1[0]);
        mescadastrado = Integer.valueOf(pegadataentrada1[1]);
        anocadastrado = Integer.valueOf(pegadataentrada1[2]);

        if (mesatual == 1 || mesatual == 3 || mesatual == 5 || mesatual == 7 || mesatual == 8 || mesatual == 10 || mesatual == 12) {

                xd = diaatual+7;
                novodia = xd;
                novomes=mesatual;
                novoano=anoatual;

                if (xd > 31) {

                    resposta = xd-31;

                    novodia = resposta;
                    novomes = mesatual+1;
                    novoano = anoatual;

                    if(novomes>12){

                        novoano = anoatual+1;
                        novomes= 1;
                        novodia = resposta;
                    }


                }





        }else  if (mesatual == 2 || mesatual == 4 || mesatual == 6 || mesatual == 9 || mesatual ==11) {

            xd = diaatual+7;
            novodia=xd;
            novomes=mesatual;
            novoano=anoatual;

            if (xd > 30) {

                    resposta = xd-30;

                    novodia = resposta;
                    novomes = mesatual+1;


                    if(novomes>12){

                        novoano = anoatual+1;
                        novomes= 1;
                        novodia = resposta;
                    }


                } else if (xd > 28 && mesatual==2) {

                    resposta = xd-28;

                    novodia = resposta;
                    novomes = mesatual+1;

                }else if (xd > 29 && mesatual==2) {

                    resposta = xd-29;

                    novodia = resposta;
                    novomes = mesatual+1;

                }

        }



        if((novodia<diacadastrado && novomes == mescadastrado && novoano==anocadastrado)||(novodia<diacadastrado && novomes< mescadastrado && novoano==anocadastrado)||(novodia<diacadastrado && novomes == mescadastrado && novoano<anocadastrado)||(novodia<diacadastrado && novomes< mescadastrado && novoano<anocadastrado)){

            x = 1;
        }

        return x;
    }

}
