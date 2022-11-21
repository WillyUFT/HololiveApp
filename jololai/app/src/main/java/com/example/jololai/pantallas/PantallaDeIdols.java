package com.example.jololai.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jololai.R;
import com.example.jololai.crud_idols.AgregarIdol;
import com.example.jololai.crud_idols.ListaDeIdols;
import com.example.jololai.db.DbIdols;

public class PantallaDeIdols extends AppCompatActivity {

    ImageButton botonAgregarIdol;
    ImageButton botonVerIdols;

    String UsuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_idols);

        String UsuarioSesionActual;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                UsuarioSesionActual = null;
            } else {
                UsuarioSesionActual = extras.getString("SesionActual");
            }
        } else {
            UsuarioSesionActual = (String) savedInstanceState.getSerializable("SesionActual");
        }

        DbIdols dbIdols = new DbIdols(PantallaDeIdols.this);

        botonAgregarIdol = findViewById(R.id.botonAgregarIdol);

        Log.e("Usuario pant Idols", UsuarioSesionActual);

        botonAgregarIdol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent agregarIdols = new Intent(PantallaDeIdols.this, AgregarIdol.class);
                agregarIdols.putExtra("SesionActual", UsuarioSesionActual);
                startActivity(agregarIdols);

            }
        });

        botonVerIdols = findViewById(R.id.verListaIdols);

        botonVerIdols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent verIdols = new Intent(PantallaDeIdols.this, ListaDeIdols.class);
                verIdols.putExtra("SesionActual", UsuarioSesionActual);
                startActivity(verIdols);

            }
        });
    }
}

