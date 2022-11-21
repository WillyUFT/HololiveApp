package com.example.jololai.crud_conciertos;

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
import com.example.jololai.adaptadores.ListaConciertosAdapter;
import com.example.jololai.adaptadores.ListaIdolsAdapter;
import com.example.jololai.crud_canciones.AgregarCanciones;
import com.example.jololai.crud_canciones.ListaDeCanciones;
import com.example.jololai.db.DbCanciones;
import com.example.jololai.db.DbConciertos;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Conciertos;
import com.example.jololai.entidades.Idols;
import com.example.jololai.pantallas.PantallaPrincipal;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListaDeConciertos extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView txtBuscarConcierto;
    RecyclerView listaConcierto;

    ArrayList<Conciertos> listaArrayConciertos;
    ArrayList<Idols> listaArrayIdols;

    ListaConciertosAdapter adapterConciertos;
    ListaIdolsAdapter adapterIdols;

    String UsuarioSesionActual;
    String usuarioSesionActual2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_conciertos);

        txtBuscarConcierto = findViewById(R.id.txtBuscarConcierto);
        listaConcierto = findViewById(R.id.listaConcierto);

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

        ListaConciertosAdapter.obtenerUsuarioActivo(UsuarioSesionActual);

        Log.e("aaaaaa", String.valueOf(ListaConciertosAdapter.obtenerUsuarioActivo(UsuarioSesionActual)));

        listaConcierto.setLayoutManager(new LinearLayoutManager(this));

        DbConciertos dbConciertos = new DbConciertos(ListaDeConciertos.this);
        DbIdols dbIdols = new DbIdols(ListaDeConciertos.this);

        listaArrayIdols = new ArrayList<>();
        listaArrayConciertos = new ArrayList<>();

        adapterIdols = new ListaIdolsAdapter(dbIdols.mostrarIdols());
        adapterConciertos = new ListaConciertosAdapter(dbConciertos.mostrarConciertos());

        listaConcierto.setAdapter(adapterConciertos);

        txtBuscarConcierto.setOnQueryTextListener(this);



    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        Log.e("usuario activo", String.valueOf(UsuarioSesionActual));

        if (UsuarioSesionActual.equals("StaffActivo")){

            inflater.inflate(R.menu.menu_principal_concierto, menu);

        } else {

            inflater.inflate(R.menu.menu_cerrar_sesion, menu);

        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nuevoConciertoMenu:
                nuevoRegistro();
                return true;
            case R.id.BotonMenuCerrarSesion:
                CerrarSesion();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void nuevoRegistro(){
        Intent intent = new Intent(this, AgregarConcierto.class);
        startActivity(intent);
    }

    private void CerrarSesion() {

        finish();
        Intent intent = new Intent(ListaDeConciertos.this, PantallaPrincipal.class);
        startActivity(intent);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterConciertos.filtrado(s);
        return false;
    }
}