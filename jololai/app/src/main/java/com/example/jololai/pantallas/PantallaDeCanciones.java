package com.example.jololai.pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.jololai.R;
import com.example.jololai.crud_canciones.AgregarCanciones;
import com.example.jololai.crud_canciones.ListaDeCanciones;
import com.example.jololai.db.DbCanciones;

public class PantallaDeCanciones extends AppCompatActivity {

    ImageButton botonAgregarCanciones;
    ImageButton botonVerCanciones;

    String UsuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_de_canciones);

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


        DbCanciones dbCanciones = new DbCanciones(PantallaDeCanciones.this);

        botonAgregarCanciones = findViewById(R.id.PantallaAgregarCancion);

        botonAgregarCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pantallaAgregarCanciones = new Intent(PantallaDeCanciones.this, AgregarCanciones.class);
                pantallaAgregarCanciones.putExtra("SesionActual", UsuarioSesionActual);
                startActivity(pantallaAgregarCanciones);
            }
        });

        botonVerCanciones = findViewById(R.id.PantallaVerConciertos);

        botonVerCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pantallaVerCanciones = new Intent(PantallaDeCanciones.this, ListaDeCanciones.class);
                pantallaVerCanciones.putExtra("SesionActual", UsuarioSesionActual);
                startActivity(pantallaVerCanciones);

            }
        });


    }
}