package com.xetelas.nova;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xetelas.nova.Adapter.MyFragmentPagerAdapter;
import com.xetelas.nova.Fragments.Fragment_Cadastrar;
import com.xetelas.nova.Fragments.Fragment_Minhas;
import com.xetelas.nova.Fragments.Fragment_Procurar;


public class Profile extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.adicionar(new Fragment_Procurar(), "Caronas\nDisponíveis");
        adapter.adicionar(new Fragment_Cadastrar(), "Oferecer\nCarona");
        adapter.adicionar(new Fragment_Minhas(), "Minhas\nOfertas");

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sair) {
            firebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (id == R.id.action_settings) {
            ShowPopup();
        }

        return super.onOptionsItemSelected(item);
    }

    public void ShowPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Atualizar Telefone");

        LayoutInflater factory = LayoutInflater.from(this);
        View content = factory.inflate(R.layout.popup_tell_att, null);

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
            .setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String tell = dd.getText().toString() + x5.getText().toString() + x4.getText().toString();
                    databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("telefone").setValue(tell);
                    Toast toast5 = Toast.makeText(getApplicationContext(), "TELEFONE ALTERADO COM SUCESSO!!", Toast.LENGTH_LONG);
                    toast5.setGravity(Gravity.CENTER, 0, 0);
                    toast5.show();
                }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

        builder.show();
    }
}

