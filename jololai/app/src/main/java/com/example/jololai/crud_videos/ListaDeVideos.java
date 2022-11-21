package com.example.jololai.crud_videos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.jololai.R;
import com.example.jololai.adaptadores.ListaCancionesAdapter;
import com.example.jololai.adaptadores.ListaVideosAdapter;
import com.example.jololai.adaptadores.ListaIdolsAdapter;
import com.example.jololai.crud_canciones.ListaDeCanciones;
import com.example.jololai.db.DbVideos;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Videos;
import com.example.jololai.entidades.Idols;
import com.example.jololai.pantallas.PantallaPrincipal;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListaDeVideos extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView txtBuscarVideo;
    RecyclerView listaVideos;

    ArrayList<Videos> listaArrayVideos;
    ArrayList<Idols> listaArrayIdols;

    ListaVideosAdapter adapterVideos;
    ListaIdolsAdapter adapterIdols;

    String UsuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_videos);

        txtBuscarVideo = findViewById(R.id.txtBuscarVideos);
        listaVideos = findViewById(R.id.listaVideos);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        listaVideos.setLayoutManager(new LinearLayoutManager(this));

        DbVideos dbVideos = new DbVideos(ListaDeVideos.this);
        DbIdols dbIdols = new DbIdols(ListaDeVideos.this);

        listaArrayIdols = new ArrayList<>();
        listaArrayVideos = new ArrayList<>();

        adapterIdols = new ListaIdolsAdapter(dbIdols.mostrarIdols());
        adapterVideos = new ListaVideosAdapter(dbVideos.mostrarVideos());

        listaVideos.setAdapter(adapterVideos);

        txtBuscarVideo.setOnQueryTextListener(this);

        ListaVideosAdapter.obtenerUsuarioActivo(UsuarioSesionActual);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        if (UsuarioSesionActual.equals("StaffActivo")){

            inflater.inflate(R.menu.menu_principal_videos, menu);

        } else {

            inflater.inflate(R.menu.menu_cerrar_sesion, menu);

        }

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nuevoVideoMenu:
                nuevoRegistro();
                return true;

            case R.id.BotonMenuCerrarSesion:
                CerrarSesion();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CerrarSesion() {

        finish();
        Intent intent = new Intent(ListaDeVideos.this, PantallaPrincipal.class);
        startActivity(intent);

    }

    private void nuevoRegistro(){
        Intent intent = new Intent(this, AgregarVideos.class);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterVideos.filtrado(s);
        return false;
    }
}