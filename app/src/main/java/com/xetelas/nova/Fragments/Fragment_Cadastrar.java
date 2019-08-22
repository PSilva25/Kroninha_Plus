package com.xetelas.nova.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xetelas.nova.Objects.Caronas;
import com.xetelas.nova.MainActivity;
import com.xetelas.nova.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Fragment_Cadastrar extends Fragment {

    AutoCompleteTextView de, para;
    EditText data, hora, coment;
    String opaLink = MainActivity.link;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseref,databaseface, databaserefcont, databasetell, databaseverifica, dataBasedata;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    final Calendar myCalendar = Calendar.getInstance();
    String contadora = "0";
    String[] cities;
    private View view;
    private Button button;
    long maxid = 0;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseface = firebaseDatabase.getReference();


        view = inflater.inflate(R.layout.fragment_cadastrar, container, false);

        de = view.findViewById(R.id.spinner_de);
        para = view.findViewById(R.id.spinner_para);
        data = view.findViewById(R.id.edit_Data);
        hora = view.findViewById(R.id.edit_Hora);
        coment = view.findViewById(R.id.edit_coment);

        context = getContext();

        verificaface();

        cities = getResources().getStringArray(R.array.cidades);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cities);

        de.setAdapter(adapter);
        para.setAdapter(adapter);

        databaseref = firebaseDatabase.getReference().child(user.getDisplayName() + " - " + user.getUid()).child("Caronas");


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
                    int resposta = veriFuturo(pegadataentrada1, pega1);

                    if (resposta == 1) {


                        Toast toast = Toast.makeText(getContext(), "DATA LIMITE DE CADASTRO:ATE 7 DIAS DO DIA ATUAL, COLOQUE UMA DATA DIFERENTE", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else if ((diaatual >= diacadastrado && mesatual > mescadastrado && anoatual == anocadastrado)) {

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

                    } else if ((diaatual > diacadastrado && mesatual == mescadastrado && anoatual <= anocadastrado)) {

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
                    ShowPopup();
                } else if (dataSnapshot.getValue().toString().equals("")) {
                    ShowPopup();
                } else if (dataSnapshot.exists()) {
                    cadastra();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void ShowPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater factory = LayoutInflater.from(getContext());
        View content = factory.inflate(R.layout.popup_tell, null);

        builder.setTitle("Insira seu número telefone (DDD + 8 numeros)");

        final EditText dd = content.findViewById(R.id.edit_tell_ddd);
        final EditText x5 = content.findViewById(R.id.edit_tell_5num);
        final EditText x4 = content.findViewById(R.id.edit_tell_4num);

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


        builder.setView(content)
                .setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String tell = dd.getText().toString() + x5.getText().toString() + x4.getText().toString();
                        databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("telefone").setValue(tell);
                        Toast toast5 = Toast.makeText(getContext(), "TELEFONE CADASTRADO COM SUCESSO!!", Toast.LENGTH_LONG);
                        toast5.setGravity(Gravity.CENTER, 0, 0);
                        toast5.show();
                        verificaTell();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.show();
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
        int diaatual = 0, diacadastrado = 0, mesatual = 0, mescadastrado = 0, anoatual = 0, anocadastrado = 0, minatual = 0, mincadastrado = 0;
        int xd, resposta = 0, novodia = 0, novomes = 0, novoano = 0;

        String[] pega1 = pega.split("-");
        String[] pegadataentrada1 = pegadataentrada.split("/");

        diaatual = Integer.valueOf(pega1[0]);
        mesatual = Integer.valueOf(pega1[1]);
        anoatual = Integer.valueOf(pega1[2]);
        diacadastrado = Integer.valueOf(pegadataentrada1[0]);
        mescadastrado = Integer.valueOf(pegadataentrada1[1]);
        anocadastrado = Integer.valueOf(pegadataentrada1[2]);

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

        if ((novodia < diacadastrado && novomes == mescadastrado && novoano == anocadastrado) || (novodia < diacadastrado && novomes < mescadastrado && novoano == anocadastrado) || (novodia < diacadastrado && novomes == mescadastrado && novoano < anocadastrado) || (novodia < diacadastrado && novomes < mescadastrado && novoano < anocadastrado)) {
            x = 1;
        }

        return x;
    }

    public void verificaface(){


        databaseface.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.child(user.getDisplayName() + " - " + user.getUid()).child("linkFace").exists()){

                    databaseface.child(user.getDisplayName() + " - " + user.getUid()).child("linkFace").setValue(opaLink);
                    loadUserprofile();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void loadUserprofile() {

        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {

                    String link = object.getString("link");

                    databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("linkFace").setValue(link);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle bundle = new Bundle();

        bundle.putString("fields", "first_name,last_name,email,id,link");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

    }



}
