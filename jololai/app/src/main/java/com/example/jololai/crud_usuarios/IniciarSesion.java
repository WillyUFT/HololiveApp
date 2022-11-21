package com.example.jololai.crud_usuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jololai.R;
import com.example.jololai.db.DbUsuarios;
import com.example.jololai.pantallas.PantallaPrincipalStaff;
import com.example.jololai.pantallas.PantallaPrincipalUsuario;

public class IniciarSesion extends AppCompatActivity {

    EditText textoUsuario, textoContraseña;
    Button botonIniciarSesion;
    String usuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        textoUsuario = findViewById(R.id.TextoInicioUsuario);
        textoContraseña = findViewById(R.id.TextoInicioContraseña);

        botonIniciarSesion = findViewById(R.id.botonIniciarSesion);

        final DbUsuarios dbUsuarios = new DbUsuarios(IniciarSesion.this);

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuarioString = textoUsuario.getText().toString();
                String contraseñaString = textoContraseña.getText().toString();

                if (usuarioString.equals("") || contraseñaString.equals("")){

                    Toast.makeText(IniciarSesion.this,"Ingresa todos los datos por favor.", Toast.LENGTH_SHORT).show();

                } else {

                    Boolean existeUser = dbUsuarios.VerExisteUsuario(usuarioString);

                    if(existeUser == false){

                        Toast.makeText(IniciarSesion.this,"Este usuario no existe, otro por fa", Toast.LENGTH_SHORT).show();

                    } else {

                        Boolean verificarUser = dbUsuarios.VerExisteContraseña(usuarioString, contraseñaString);
                        if (verificarUser == true){

                            Toast.makeText(IniciarSesion.this,"Pase socio, bienvenido", Toast.LENGTH_SHORT).show();
                            inicioSesion(usuarioString);
                            Intent intent = new Intent(IniciarSesion.this, PantallaPrincipalUsuario.class);
                            intent.putExtra("SesionActual", usuarioSesionActual);
                            startActivity(intent);
                            limpiar();

                        } else {

                            final MediaPlayer mediaPlayer = MediaPlayer.create(IniciarSesion.this,R.raw.ana);
                            mediaPlayer.start();
                            Toast.makeText(IniciarSesion.this,"a ver si nos vamos aprendiendo la contraseña chaval", Toast.LENGTH_SHORT).show();

                        }
                    }
                }


            }

            private void limpiar() {

                textoUsuario.setText("");
                textoContraseña.setText("");

            }

            public void inicioSesion(String usuarioActivo) {

                usuarioSesionActual = usuarioActivo;
            }
        });




    }
}