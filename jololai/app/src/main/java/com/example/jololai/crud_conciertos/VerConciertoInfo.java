package com.example.jololai.crud_conciertos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.jololai.db.DbComConciertos;
import com.example.jololai.db.DbConciertos;
import com.example.jololai.db.DbIdols;
import com.example.jololai.db.DbUsuarios;
import com.example.jololai.entidades.Conciertos;
import com.example.jololai.entidades.Idols;
import com.example.jololai.entidades.Usuarios;
import com.example.jololai.pantallas.PantallaPrincipal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VerConciertoInfo extends AppCompatActivity {

    TextView tituloConcierto, idolCantante, duracionConcierto, fechaConcierto;
    ImageView idolFoto;
    Button comprarEntradas;
    FloatingActionButton botonEliminar;

    Idols idols;
    Usuarios usuarios;
    Conciertos conciertos;
    Integer idConcierto;

    String contraseñaUsuario;
    String UsuarioSesionActual;

    public int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_concierto_info);

        /********* No editas ***********/
        botonEliminar = findViewById(R.id.botonEliminarConcierto);

        /********** Editables ************/
        tituloConcierto = findViewById(R.id.textViewNombreConcierto);
        idolCantante = findViewById(R.id.textViewNombreIdolConcierto);
        duracionConcierto = findViewById(R.id.textViewDuracionConcierto);
        fechaConcierto = findViewById(R.id.FechaConcierto);
        idolFoto = findViewById(R.id.imageViewConcierto);

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
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

        final DbUsuarios dbUsuarios = new DbUsuarios(VerConciertoInfo.this);
        usuarios = dbUsuarios.verUsuarios(UsuarioSesionActual);

        /********* Base de datos de los conciertos ***********/
        final DbConciertos dbConciertos = new DbConciertos(VerConciertoInfo.this);
        conciertos = dbConciertos.verConcierto(id);

        if (conciertos != null) {

            tituloConcierto.setText(conciertos.getNombre_Concierto().toString());
            duracionConcierto.setText(conciertos.getDuracion_Minutos().toString());
            fechaConcierto.setText(conciertos.getFecha_Concierto().toString());

            /******** bdd de las idols (Para la cantante) ************/
            final DbIdols dbIdols = new DbIdols(VerConciertoInfo.this);
            idols = dbIdols.verIdol(conciertos.getID_Idol());

            idConcierto = conciertos.getID_Concierto();

            Log.e("cantante", idols.getNombre().toString());
            idolCantante.setText(idols.getNombre().toString());

            byte[] imagen = idols.getImagen();
            Log.e("foto", String.valueOf(imagen));
        }

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerConciertoInfo.this);
                builder.setMessage("¿Desea eliminar este concierto, pese a ser el tremendo espectáculo?????")
                        .setPositiveButton("SÍ, soy tonto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (dbConciertos.eliminarConcierto(id)){
                                    lista();
                                }

                            }
                        })
                        .setNegativeButton("No, soy un hombre de cultura", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        if (UsuarioSesionActual.equals("StaffActivo")){

            inflater.inflate(R.menu.menu_ver_conciertos, menu);

        } else {

            inflater.inflate(R.menu.menu_cerrar_sesion, menu);

        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.editarConcierto:
                editarConcierto();
                return true;

            case R.id.eliminarConcierto:
                botonEliminar.callOnClick();
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
        Intent intent = new Intent(VerConciertoInfo.this, PantallaPrincipal.class);
        startActivity(intent);

    }

    private void editarConcierto() {

        Intent intent = new Intent(this, EditarConcierto.class);
        intent.putExtra("ID", id);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);

    }

    private void lista(){
        Intent intent = new Intent(this, ListaDeConciertos.class);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);
    }

}