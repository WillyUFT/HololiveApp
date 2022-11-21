package com.example.jololai.crud_usuarios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.CursorWindow;
import android.os.Bundle;
import android.widget.SearchView;

import com.example.jololai.R;
import com.example.jololai.adaptadores.ListaUsuarioAdapter;
import com.example.jololai.db.DbUsuarios;
import com.example.jololai.entidades.Usuarios;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListaDeUsuarios extends AppCompatActivity implements SearchView.OnQueryTextListener{

    SearchView txtBuscarUsuario;
    RecyclerView listaUsuarios;
    ArrayList<Usuarios> listaArrayUsuarios;
    ListaUsuarioAdapter adapterUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_usuarios);

        txtBuscarUsuario = findViewById(R.id.txtBuscarUsuario);
        listaUsuarios = findViewById(R.id.listaUsuarios);

        /**** Para que no diga OMG WHY IT IS SO BIG ************/

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        listaUsuarios.setLayoutManager(new LinearLayoutManager(this));

        DbUsuarios dbUsuarios = new DbUsuarios(ListaDeUsuarios.this);

        listaArrayUsuarios = new ArrayList<>();

        adapterUsuarios = new ListaUsuarioAdapter(dbUsuarios.mostrarUsuarios());
        listaUsuarios.setAdapter(adapterUsuarios);

        txtBuscarUsuario.setOnQueryTextListener(this);

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