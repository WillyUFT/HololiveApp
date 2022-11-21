package com.example.jololai.pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jololai.R;
import com.example.jololai.db.DbConsultas;
import com.example.jololai.entidades.ComCanciones;
import com.example.jololai.entidades.RepVideos;
import com.example.jololai.entidades.Videos;

public class PantallaReportes extends AppCompatActivity {

    ComCanciones comCanciones;
    Videos Video;
    RepVideos repVideos;

    ImageView haachamaa;

    TextView simpMaximo, videoMaximo, suscriptorMaximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_reportes);

        final DbConsultas dbConsultas = new DbConsultas(PantallaReportes.this);
        comCanciones = dbConsultas.verMaximoSimp();

        simpMaximo = findViewById(R.id.textoSimpMaximo);

        simpMaximo.setText(comCanciones.getNombre_usuario());

        Video = dbConsultas.verVideoMaximo();

        videoMaximo = findViewById(R.id.textoVideoMasVisto);

        videoMaximo.setText(Video.getNombreVideo());

        repVideos = dbConsultas.verPersonaMirona();

        suscriptorMaximo = findViewById(R.id.textoSuscriptorMaximo);

        suscriptorMaximo.setText(repVideos.getNombre_usuario());

        haachamaa = findViewById(R.id.haachama);


    }
}