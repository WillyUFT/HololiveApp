package com.example.jololai.pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.jololai.R;
import com.example.jololai.crud_videos.AgregarVideos;
import com.example.jololai.crud_videos.ListaDeVideos;

public class PantallaDeVideos extends AppCompatActivity {

    ImageButton AgregarVideo, VerVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_videos);

        AgregarVideo = findViewById(R.id.PantallaAgregarVideo);

        AgregarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaDeVideos.this, AgregarVideos.class);
                startActivity(intent);

            }
        });

        VerVideo = findViewById(R.id.PantallaVerVideos);

        VerVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PantallaDeVideos.this, ListaDeVideos.class);
                intent.putExtra("SesionActual", "StaffActivo");
                startActivity(intent);

            }
        });



    }
}