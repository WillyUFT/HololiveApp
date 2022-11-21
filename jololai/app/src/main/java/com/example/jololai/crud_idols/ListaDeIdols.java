package com.example.jololai.crud_idols;

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
import com.example.jololai.adaptadores.ListaIdolsAdapter;
import com.example.jololai.crud_canciones.ListaDeCanciones;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Idols;
import com.example.jololai.pantallas.PantallaPrincipal;
import com.example.jololai.pantallas.PantallaPrincipalUsuario;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListaDeIdols extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView txtBuscar;
    RecyclerView listaIdols;
    ArrayList<Idols> listaArrayIdols;
    ListaIdolsAdapter adapter;

    String UsuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_idols);
        txtBuscar = findViewById(R.id.txtBuscar);
        listaIdols = findViewById(R.id.listaIdols);

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

        listaIdols.setLayoutManager(new LinearLayoutManager(this));

        DbIdols dbIdols = new DbIdols(ListaDeIdols.this);

        listaArrayIdols = new ArrayList<>();

        adapter = new ListaIdolsAdapter(dbIdols.mostrarIdols());
        listaIdols.setAdapter(adapter);

        txtBuscar.setOnQueryTextListener(this);

        ListaIdolsAdapter.obtenerUsuarioActivo(UsuarioSesionActual);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        if (UsuarioSesionActual.equals("StaffActivo")){

            inflater.inflate(R.menu.menu_principal, menu);

        } else {

            inflater.inflate(R.menu.menu_cerrar_sesion, menu);

        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nuevaIdolMenu:
                nuevoRegistro();
                return true;
            case R.id.BotonMenuCerrarSesion:
                CerrarSesion();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CerrarSesion() {

        finish();
        Intent intent = new Intent(ListaDeIdols.this, PantallaPrincipal.class);
        startActivity(intent);

    }

    private void nuevoRegistro(){
        Intent intent = new Intent(this, AgregarIdol.class);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);
        return false;
    }
}