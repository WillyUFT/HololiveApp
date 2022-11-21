package com.example.jololai.crud_canciones;

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
import com.example.jololai.adaptadores.ListaIdolsAdapter;
import com.example.jololai.db.DbCanciones;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Idols;
import com.example.jololai.pantallas.PantallaPrincipal;
import com.example.jololai.pantallas.PantallaPrincipalUsuario;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListaDeCanciones extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView txtBuscarCancion;
    RecyclerView listaCanciones;

    ArrayList<Canciones> listaArrayCanciones;
    ArrayList<Idols> listaArrayIdols;

    ListaCancionesAdapter adapterCanciones;
    ListaIdolsAdapter adapterIdols;

    String UsuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_canciones);

        txtBuscarCancion = findViewById(R.id.txtBuscarCancion);
        listaCanciones = findViewById(R.id.listaCanciones);

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

        listaCanciones.setLayoutManager(new LinearLayoutManager(this));

        DbCanciones dbCanciones = new DbCanciones(ListaDeCanciones.this);
        DbIdols dbIdols = new DbIdols(ListaDeCanciones.this);

        listaArrayIdols = new ArrayList<>();
        listaArrayCanciones = new ArrayList<>();

        adapterIdols = new ListaIdolsAdapter(dbIdols.mostrarIdols());
        adapterCanciones = new ListaCancionesAdapter (dbCanciones.mostrarCanciones());

        listaCanciones.setAdapter(adapterCanciones);

        txtBuscarCancion.setOnQueryTextListener(this);

        ListaCancionesAdapter.obtenerUsuarioActivo(UsuarioSesionActual);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        if (UsuarioSesionActual.equals("StaffActivo")){

            inflater.inflate(R.menu.menu_principal_canciones, menu);

        } else {

            inflater.inflate(R.menu.menu_cerrar_sesion, menu);

        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nuevaCancionMenu:
                nuevoRegistro();
                return true;
            case R.id.BotonMenuCerrarSesion:
                CerrarSesion();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void nuevoRegistro(){
        Intent intent = new Intent(this, AgregarCanciones.class);
        startActivity(intent);
    }

    private void CerrarSesion() {

        finish();
        Intent intent = new Intent(ListaDeCanciones.this, PantallaPrincipal.class);
        startActivity(intent);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterCanciones.filtrado(s);
        return false;
    }
}