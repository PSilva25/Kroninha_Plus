package com.xetelas.nova;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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
    Dialog myDialog;
    EditText tell;
    String num = null;

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
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (id == R.id.action_settings){
            myDialog = new Dialog(this);
            ShowPopup();
        }

        return super.onOptionsItemSelected(item);
    }

    public void ShowPopup() {
        myDialog.setContentView(R.layout.popup_tell_att);
        final EditText dd = myDialog.findViewById(R.id.edit_tell_ddd);
        final EditText x5 = myDialog.findViewById(R.id.edit_tell_5num);
        final EditText x4 = myDialog.findViewById(R.id.edit_tell_4num);

        dd.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(dd.length() > 1) x5.requestFocus();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        x5.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(x5.length() > 3) x4.requestFocus();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        Button filtro = myDialog.findViewById(R.id.bot_addtell);
        filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tell = dd.getText().toString() + x5.getText().toString() + x4.getText().toString();
                databaseReference.child(user.getDisplayName() + " - " + user.getUid()).child("telefone").setValue(tell);
                myDialog.dismiss();
            }
        });

        Button cancel = myDialog.findViewById(R.id.bot_cancel);
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

