package com.example.jololai.pantallas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.jololai.R;
import com.example.jololai.crud_canciones.ListaDeCanciones;
import com.example.jololai.crud_idols.ListaDeIdols;
import com.example.jololai.crud_videos.ListaDeVideos;

public class PantallaPrincipalUsuario extends AppCompatActivity {

    ImageButton botonCanciones, botonIdols, botonVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal_usuario);

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

        botonCanciones = findViewById(R.id.botonVerCancionesUser);
        botonIdols = findViewById(R.id.botonVerIdolsUser);
        botonVideos = findViewById(R.id.botonVerVideosUser);

        botonCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalUsuario.this, ListaDeCanciones.class);
                intent.putExtra("SesionActual", UsuarioSesionActual);
                startActivity(intent);

            }
        });

        botonIdols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalUsuario.this, ListaDeIdols.class);
                intent.putExtra("SesionActual", UsuarioSesionActual);
                startActivity(intent);

            }
        });

        botonVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalUsuario.this, ListaDeVideos.class);
                intent.putExtra("SesionActual", UsuarioSesionActual);
                startActivity(intent);

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cerrar_sesion, menu);
        return true; }

    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.BotonMenuCerrarSesion:
                CerrarSesion();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void CerrarSesion() {

        finish();
        Intent intent = new Intent(PantallaPrincipalUsuario.this, PantallaPrincipal.class);
        startActivity(intent);

    }

}