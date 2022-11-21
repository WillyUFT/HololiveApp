package com.example.jololai.pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.jololai.R;
import com.example.jololai.crud_conciertos.AgregarConcierto;
import com.example.jololai.crud_conciertos.EditarConcierto;
import com.example.jololai.crud_usuarios.ListaDeUsuarios;
import com.example.jololai.db.DbCanciones;
import com.example.jololai.db.DbComCanciones;
import com.example.jololai.db.DbConciertos;
import com.example.jololai.db.DbConsultas;
import com.example.jololai.db.DbIdols;
import com.example.jololai.db.DbRepVideos;
import com.example.jololai.db.DbUsuarios;
import com.example.jololai.db.DbVideos;
import com.example.jololai.registros.ListaDeComprasCanciones;
import com.example.jololai.registros.ListaDeReproduccionVideos;

public class PantallaPrincipalStaff extends AppCompatActivity {

    ImageButton botonIdols;
    ImageButton botonUsuarios;
    ImageButton botonCanciones;
    ImageButton botonVideos;
    ImageButton botonComprasCanciones;
    ImageButton botonReproducciones;
    ImageButton botonVerConciertos;

    Button botonReportes, botonCargaInicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal_staff);

        botonIdols = findViewById(R.id.verPantallaIdols);
        botonCanciones = findViewById(R.id.verPantallaCanciones);
        botonUsuarios = findViewById(R.id.verPantallaUsuarios);
        botonVideos = findViewById(R.id.verPantallaVideoStaff);
        botonReproducciones = findViewById(R.id.verHistorialVideos);
        botonComprasCanciones = findViewById(R.id.verHistorialCompraCanciones);
        botonVerConciertos = findViewById(R.id.verConciertos);

        botonIdols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalStaff.this, PantallaDeIdols.class);
                intent.putExtra("SesionActual", "StaffActivo");
                startActivity(intent);

            }
        });

        botonUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalStaff.this, ListaDeUsuarios.class);
                intent.putExtra("SesionActual", "StaffActivo");
                startActivity(intent);

            }
        });

        botonCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalStaff.this, PantallaDeCanciones.class);
                intent.putExtra("SesionActual", "StaffActivo");
                startActivity(intent);

            }
        });

        botonVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalStaff.this, PantallaDeVideos.class);
                intent.putExtra("SesionActual", "StaffActivo");
                startActivity(intent);

            }
        });


        botonReproducciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalStaff.this, ListaDeReproduccionVideos.class);
                intent.putExtra("SesionActual", "StaffActivo");
                startActivity(intent);

            }
        });

        botonComprasCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalStaff.this, ListaDeComprasCanciones.class);
                intent.putExtra("SesionActual", "StaffActivo");
                startActivity(intent);

            }
        });

        botonComprasCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalStaff.this, ListaDeComprasCanciones.class);
                intent.putExtra("SesionActual", "StaffActivo");
                startActivity(intent);

            }
        });

        botonVerConciertos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalStaff.this, PantallaDeConciertos.class);
                intent.putExtra("SesionActual", "StaffActivo");
                startActivity(intent);

            }
        });

        botonReportes = findViewById(R.id.BotonReportes);

        botonReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaPrincipalStaff.this, PantallaReportes.class);
                intent.putExtra("SesionActual", "StaffActivo");
                startActivity(intent);

            }
        });

        botonCargaInicial = findViewById(R.id.botonCarga);

        botonCargaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DbCanciones dbCanciones = new DbCanciones(PantallaPrincipalStaff.this);
                DbComCanciones dbComCanciones = new DbComCanciones(PantallaPrincipalStaff.this);
                DbConciertos dbConciertos = new DbConciertos(PantallaPrincipalStaff.this);
                DbIdols dbIdols = new DbIdols(PantallaPrincipalStaff.this);
                DbRepVideos dbRepVideos = new DbRepVideos(PantallaPrincipalStaff.this);
                DbUsuarios dbUsuarios = new DbUsuarios(PantallaPrincipalStaff.this);
                DbVideos dbVideos = new DbVideos(PantallaPrincipalStaff.this);

                dbCanciones.SubirCanciones();
                dbComCanciones.SubirComprasCanciones();
                dbConciertos.SubirConciertos();
                dbIdols.SubirIdols();
                dbRepVideos.SubirRepVideos();
                dbUsuarios.SubirUsuarios();
                dbVideos.SubirVideos();

            }
        });

    }
}