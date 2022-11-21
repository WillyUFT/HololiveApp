package com.example.jololai.crud_canciones;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jololai.R;
import com.example.jololai.crud_idols.EditarIdol;
import com.example.jololai.crud_idols.VerIdolInfo;
import com.example.jololai.crud_usuarios.EditarUsuario;
import com.example.jololai.crud_videos.VerVideoInfo;
import com.example.jololai.db.DbCanciones;
import com.example.jololai.db.DbComCanciones;
import com.example.jololai.db.DbIdols;
import com.example.jololai.db.DbUsuarios;
import com.example.jololai.db.DbVideos;
import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Usuarios;
import com.example.jololai.pantallas.PantallaPrincipal;
import com.example.jololai.pantallas.PantallaPrincipalUsuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.jololai.entidades.Idols;

import java.util.Calendar;

public class VerCancionInfo extends AppCompatActivity {

    TextView nombreCancion, nombreIdol, letraCancion, tipoDeCancion;

    FloatingActionButton botonEliminar;

    ImageView imagenCancion;

    Button ComprarCancion;

    Idols Idol;
    Usuarios Usuario;
    Canciones Cancion;
    public int id = 0;

    boolean correcto = false;

    String mesString = null;
    Integer idCancion;

    String contraseñaUsuario;
    Integer comprasUsuario;
    String UsuarioSesionActual;

    Integer comprasDespues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cancion_info);

        /************* No editables (técnicamente sí, pero no lo vamos a hacer) ***/

        /************* Editables ********************/
        nombreCancion = findViewById(R.id.textViewNombreCancion);
        nombreIdol = findViewById(R.id.textViewNombreIdol);
        tipoDeCancion = findViewById(R.id.textViewTipoCancion);
        letraCancion = findViewById(R.id.textViewLetraCancion);

        /************* imagen ****************/
        imagenCancion = findViewById(R.id.imageViewCancion);

        /*********** botones ******************/
        botonEliminar = findViewById(R.id.botonEliminarCancion);

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


        /************ capturamos el mes de reproducción ******************/
        Calendar calendar = Calendar.getInstance();
        int mesInt = calendar.get(Calendar.MONTH) + 1;

        if(mesInt == 1){
            mesString = "Enero";
        } else if (mesInt == 2){
            mesString = "Febrero";
        } else if (mesInt == 3){
            mesString = "Marzo";
        } else if (mesInt == 4){
            mesString = "Abril";
        } else if (mesInt == 5){
            mesString = "Mayo";
        } else if (mesInt == 6){
            mesString = "Junio";
        } else if (mesInt == 7){
            mesString = "Julio";
        } else if (mesInt == 8){
            mesString = "Agosto";
        } else if (mesInt == 9){
            mesString = "Septiembre";
        } else if (mesInt == 10){
            mesString = "Octubre";
        } else if (mesInt == 11){
            mesString = "Noviembre";
        } else if (mesInt == 12){
            mesString = "Diciembre";
        }

        /******** bdd de las canciones ************/
        final DbCanciones dbCanciones = new DbCanciones(VerCancionInfo.this);
        Cancion = dbCanciones.verCancion(id);

        if(Cancion != null){

            nombreCancion.setText(Cancion.getNombre());

            /******** bdd de las idols (Para la cantante) ************/
            final DbIdols dbIdols = new DbIdols(VerCancionInfo.this);
            Idol = dbIdols.verIdol(Cancion.getId_idol());

            nombreIdol.setText(Idol.getNombre());

            idCancion = Cancion.getId();

            tipoDeCancion.setText(Cancion.getTipo_cancion());
            letraCancion.setText(Cancion.getLetra_cancion());

            byte[] imagen = Cancion.getImagen_cancion();

            if (imagen == null){
                imagenCancion.setImageResource(R.drawable.img_base);
                Log.e("Foto inai", String.valueOf(Cancion.getImagen_cancion()));

            } else {

                Bitmap bmp = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
                imagenCancion.setImageBitmap(bmp);
                //imagenCancion.setRotation(90);
                Log.e("Foto yabai", String.valueOf(Cancion.getImagen_cancion()));

            }
        }

        ComprarCancion = findViewById(R.id.botonComprarCancion);

        DbComCanciones dbComCanciones = new DbComCanciones(VerCancionInfo.this);

        final DbUsuarios dbUsuarios = new DbUsuarios(VerCancionInfo.this);
        Usuario = dbUsuarios.verUsuarios(UsuarioSesionActual);

        Log.e("SESION ACTUAL", String.valueOf(UsuarioSesionActual));

        contraseñaUsuario = Usuario.getContraseña();
        comprasUsuario = Usuario.getCompras_realizadas();
        comprasDespues = comprasUsuario + 1 ;


        ComprarCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long id = dbComCanciones.insertarComCancion(UsuarioSesionActual,
                        idCancion,
                        mesString);

                        Log.e("compras antes", String.valueOf(comprasUsuario));

                    if (id > 0) {

                        correcto = dbUsuarios.editarUsuario(UsuarioSesionActual,
                                comprasDespues,
                                contraseñaUsuario);

                        Log.e("compras despues", String.valueOf(comprasDespues));

                        if (correcto){

                            Toast.makeText(VerCancionInfo.this, "+1 Compra", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(VerCancionInfo.this, PantallaPrincipalUsuario.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(VerCancionInfo.this, "Algo pasó", Toast.LENGTH_SHORT).show();

                        }
                }
            }
        });

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerCancionInfo.this);
                builder.setMessage("¿Desea eliminar esta canción, pese a ser el tremendo temón?????")
                        .setPositiveButton("SÍ, soy tonto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (dbCanciones.eliminarCancion(id)){
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

            inflater.inflate(R.menu.menu_ver_canciones, menu);

        } else {

            inflater.inflate(R.menu.menu_cerrar_sesion, menu);

        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.editarCancion:
                editarCancion();
                return true;

            case R.id.eliminarCancion:
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
        Intent intent = new Intent(VerCancionInfo.this, PantallaPrincipal.class);
        startActivity(intent);

    }

    private void editarCancion() {

        Intent intent = new Intent(this, EditarCanciones.class);
        intent.putExtra("ID", id);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);

    }

    private void lista(){
        Intent intent = new Intent(this, ListaDeCanciones.class);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);
    }


}