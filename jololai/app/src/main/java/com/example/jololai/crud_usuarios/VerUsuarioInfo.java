package com.example.jololai.crud_usuarios;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jololai.R;
import com.example.jololai.crud_canciones.EditarCanciones;
import com.example.jololai.crud_canciones.ListaDeCanciones;
import com.example.jololai.crud_canciones.VerCancionInfo;
import com.example.jololai.db.DbUsuarios;
import com.example.jololai.entidades.Usuarios;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VerUsuarioInfo extends AppCompatActivity {

    /********** Declaramos variables **************/

    TextView nombreUsuario, tituloComprasRealizadas, numeroCompras;
    ImageView Aqua;
    String usuarioAEditar;
    Button eliminarUsuario;
    Usuarios Usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario_info);

        /******************* Llamamos a los elementos de la UI ****************/

        nombreUsuario = findViewById(R.id.NombreUsuarioVer);
        tituloComprasRealizadas = findViewById(R.id.textViewComprasRealizadas);
        numeroCompras = findViewById(R.id.textViewComprasNumero);
        eliminarUsuario = findViewById(R.id.botonEliminarUsuario);

        Aqua = findViewById(R.id.AquaCompras);

        String UsuarioRecupeado;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                UsuarioRecupeado = null;
            } else {
                UsuarioRecupeado = extras.getString("Usuariooo");
            }
        } else {
            UsuarioRecupeado = (String) savedInstanceState.getSerializable("Usuariooo");
        }

        final DbUsuarios dbUsuarios = new DbUsuarios(VerUsuarioInfo.this);
        Usuarios = dbUsuarios.verUsuarios(UsuarioRecupeado);

        usuarioAEditar = UsuarioRecupeado;

        if(Usuarios != null){

            nombreUsuario.setText(Usuarios.getUsuario());

            numeroCompras.setText(String.valueOf(Usuarios.getCompras_realizadas()));

        }

        eliminarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerUsuarioInfo.this);
                builder.setMessage("Vamos a eliminar a esta persona? Imagino que debe ser algún degenerado")
                        .setPositiveButton("Así es, es un degenerado", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (dbUsuarios.eliminarUsuario(Usuarios.getUsuario())){
                                    lista();
                                }

                            }
                        })
                        .setNegativeButton("No, es inocente, déjalo vivir un poco más", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal_usuarios, menu);
        return true; }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.editarUsuario:
                editarUsuario();
                return true;

            case R.id.eliminarUsuario:
                eliminarUsuario.callOnClick();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editarUsuario() {

        Intent intent = new Intent(this, EditarUsuario.class);
        intent.putExtra("Usuariooo", usuarioAEditar);
        startActivity(intent);

    }

    private void lista(){
        Intent intent = new Intent(this, ListaDeUsuarios.class);
        intent.putExtra("SesionActual", usuarioAEditar);
        startActivity(intent);
    }

}