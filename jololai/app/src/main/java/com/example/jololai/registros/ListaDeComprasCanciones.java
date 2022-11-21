package com.example.jololai.registros;

import android.database.CursorWindow;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jololai.R;
import com.example.jololai.adaptadores.ListaComCancionesAdapter;
import com.example.jololai.adaptadores.ListaCancionesAdapter;
import com.example.jololai.db.DbComCanciones;
import com.example.jololai.db.DbCanciones;
import com.example.jololai.entidades.ComCanciones;
import com.example.jololai.entidades.Canciones;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListaDeComprasCanciones extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView listaComCanciones;

    ArrayList<ComCanciones> listaArrayComCanciones;
    ArrayList<Canciones> listaArrayCanciones;

    ListaComCancionesAdapter adapterComCanciones;
    ListaCancionesAdapter adapterCanciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_de_compras_canciones);

        listaComCanciones = findViewById(R.id.listaCompras);

        /********* Error de eso de las cosas gigantes ***********/
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        listaComCanciones.setLayoutManager(new LinearLayoutManager(this));

        /********* tablasss ************/

        DbComCanciones dbComCanciones = new DbComCanciones(ListaDeComprasCanciones.this);
        DbCanciones dbCanciones = new DbCanciones(ListaDeComprasCanciones.this);

        listaArrayComCanciones = new ArrayList<>();
        listaArrayCanciones = new ArrayList<>();

        adapterComCanciones = new ListaComCancionesAdapter(dbComCanciones.mostrarComCanciones());
        adapterCanciones = new ListaCancionesAdapter(dbCanciones.mostrarCanciones());

        listaComCanciones.setAdapter(adapterComCanciones);

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
