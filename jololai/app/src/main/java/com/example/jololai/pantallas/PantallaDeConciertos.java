package com.example.jololai.pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.jololai.R;
import com.example.jololai.crud_conciertos.AgregarConcierto;
import com.example.jololai.crud_conciertos.ListaDeConciertos;

public class PantallaDeConciertos extends AppCompatActivity {

    ImageButton crearConcierto, verConciertos;
    String UsuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_conciertos);

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

        crearConcierto = findViewById(R.id.PantallaAgregarConciertos);
        verConciertos = findViewById(R.id.PantallaVerConciertos);

        crearConcierto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaDeConciertos.this, AgregarConcierto.class);
                intent.putExtra("SesionActual", UsuarioSesionActual);
                startActivity(intent);

            }
        });

        verConciertos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaDeConciertos.this, ListaDeConciertos.class);
                intent.putExtra("SesionActual", UsuarioSesionActual);
                startActivity(intent);

            }
        });


    }
}