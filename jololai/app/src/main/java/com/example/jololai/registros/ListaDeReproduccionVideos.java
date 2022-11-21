package com.example.jololai.registros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.CursorWindow;
import android.os.Bundle;
import android.widget.SearchView;

import com.example.jololai.R;
import com.example.jololai.adaptadores.ListaRepVideosAdapter;
import com.example.jololai.adaptadores.ListaVideosAdapter;
import com.example.jololai.db.DbRepVideos;
import com.example.jololai.db.DbVideos;
import com.example.jololai.entidades.RepVideos;
import com.example.jololai.entidades.Videos;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListaDeReproduccionVideos extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView listaRepVideos;

    ArrayList<RepVideos> listaArrayRepVideos;
    ArrayList<Videos> listaArrayVideos;

    ListaRepVideosAdapter adapterRepVideos;
    ListaVideosAdapter adapterVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_de_reproduccion_videos);

        listaRepVideos = findViewById(R.id.listaReproducciones);

        /********* Error de eso de las cosas gigantes ***********/
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        listaRepVideos.setLayoutManager(new LinearLayoutManager(this));

        /********* tablasss ************/

        DbRepVideos dbRepVideos = new DbRepVideos(ListaDeReproduccionVideos.this);
        DbVideos dbVideos = new DbVideos(ListaDeReproduccionVideos.this);

        listaArrayRepVideos = new ArrayList<>();
        listaArrayVideos = new ArrayList<>();

        adapterRepVideos = new ListaRepVideosAdapter(dbRepVideos.mostrarRepVideos());
        adapterVideos = new ListaVideosAdapter(dbVideos.mostrarVideos());

        listaRepVideos.setAdapter(adapterRepVideos);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}