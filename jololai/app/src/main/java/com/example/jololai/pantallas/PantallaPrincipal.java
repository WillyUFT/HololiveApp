package com.example.jololai.pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.jololai.R;
import com.example.jololai.crud_usuarios.AgregarUsuario;
import com.example.jololai.crud_usuarios.IniciarSesion;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**********************
 *                    *
 * Tomás Astudillo    *
 * Williams Durán     *
 * Felipe Martinez    *
 *                    *
 **********************/

public class PantallaPrincipal extends AppCompatActivity {

    ImageButton RegistrarUsuario, BotonIniciarSesion, CosasStaff;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);

        RegistrarUsuario = findViewById(R.id.botonRegistrarUsuario);
        BotonIniciarSesion = findViewById(R.id.botonIniciarSesionMiko);
        CosasStaff = findViewById(R.id.botonFuncionesStaff);

        /************* boton para registrar ***************/

        RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipal.this, AgregarUsuario.class);
                startActivity(intent);

            }
        });

        BotonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipal.this, IniciarSesion.class);
                startActivity(intent);

            }
        });

        CosasStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipal.this, PantallaDeStaffPass.class);
                startActivity(intent);

            }
        });
    }
}

