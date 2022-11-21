package com.example.jololai.crud_usuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jololai.R;
import com.example.jololai.db.DbUsuarios;
import com.example.jololai.entidades.Usuarios;
import com.example.jololai.pantallas.PantallaPrincipal;
import com.example.jololai.pantallas.PantallaPrincipalUsuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgregarUsuario extends AppCompatActivity {

    /******** Declaramos las variables ****************/
    EditText textUsuario, Contraseña, reContraseña;
    Button botonRegistrarse, botonIngresar;

    String usuarioSesionActual;
    boolean sesionActiva = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);

        textUsuario = findViewById(R.id.TextoUsuario);
        Contraseña = findViewById(R.id.TextoContraseña);
        reContraseña = findViewById(R.id.TextoReIngresarContraseña);

        botonRegistrarse = findViewById(R.id.BotonRegistrarse);
        botonIngresar = findViewById(R.id.BotonIngresar);

        final DbUsuarios dbUsuarios = new DbUsuarios(AgregarUsuario.this);

        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuarioString = textUsuario.getText().toString();
                String contraseñaString = Contraseña.getText().toString();
                String reContraseñaString = reContraseña.getText().toString();

                if (usuarioString.equals("") ||
                contraseñaString.equals("") ||
                reContraseñaString.equals("")){

                    Log.e("Usuario", textUsuario.getText().toString());
                    Toast.makeText(AgregarUsuario.this, "LLENA TODO POR FAVOR", Toast.LENGTH_SHORT).show();

                } else {

                    if (contraseñaString.equals(reContraseñaString)){

                        Boolean existeUser = dbUsuarios.VerExisteUsuario(usuarioString);

                        if (existeUser == false) {

                            Boolean ingresar = dbUsuarios.insertarUsuario(usuarioString,
                                    0,contraseñaString);

                            if (ingresar == true){

                                inicioSesion(usuarioString);
                                Intent intent = new Intent(getApplicationContext(), PantallaPrincipalUsuario.class);
                                limpiar();
                                startActivity(intent);

                                Toast.makeText(AgregarUsuario.this, "Usuario registrado", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(AgregarUsuario.this, "Registro Fallido, intente nuevamente", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            Toast.makeText(AgregarUsuario.this, "Usuario ya existente. Intente con otro usuario", Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Toast.makeText(AgregarUsuario.this, "Contraseñas no coinciden. Asegurese de que coincidan.", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            private void limpiar() {

                textUsuario.setText("");
                reContraseña.setText("");
                Contraseña.setText("");

            }

            public void inicioSesion(String usuarioActivo) {

                sesionActiva = true;
                usuarioSesionActual = usuarioActivo;

            }
        });



        botonIngresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), IniciarSesion.class);
                startActivity(intent);

            }
        });
    }

}