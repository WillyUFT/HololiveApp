package com.example.jololai.crud_videos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import java.util.Calendar;

import com.example.jololai.R;
import com.example.jololai.db.DbIdols;
import com.example.jololai.db.DbRepVideos;
import com.example.jololai.db.DbVideos;
import com.example.jololai.entidades.Idols;
import com.example.jololai.entidades.Videos;
import com.example.jololai.pantallas.PantallaPrincipal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VerVideoInfo extends AppCompatActivity {

    TextView nombreVideo, nombreIdol, PromocionalVideo, GeneroVideo;

    FloatingActionButton botonEliminar;

    Button botonVerVideo;

    ImageView imagenVideo;

    Idols Idol;
    Videos Video;
    public int id = 0;
    String mesString = null;
    Integer idVideo;

    String UsuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_video_info);

        /************* No editables (técnicamente sí, pero no lo vamos a hacer) ***/

        /************* Editables ********************/
        nombreVideo = findViewById(R.id.textViewNombreVideo);
        nombreIdol = findViewById(R.id.textViewNombreIdolVideo);
        PromocionalVideo = findViewById(R.id.textViewPromocionalVideo);
        GeneroVideo = findViewById(R.id.textViewGeneroVideo);

        /************* imagen ****************/
        imagenVideo = findViewById(R.id.imageViewVideo);

        /*********** botones ******************/
        botonEliminar = findViewById(R.id.botonEliminarVideo);
        botonVerVideo = findViewById(R.id.BotonVerVideoLink);

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

        /******** bdd de los videos ************/
        final DbVideos dbVideos = new DbVideos(VerVideoInfo.this);
        Video = dbVideos.verVideo(id);

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

        if(Video != null){

            nombreVideo.setText(Video.getNombreVideo());

            Log.e("nombre video", Video.getNombreVideo());

            /******** bdd de las idols (Para la que aparece) ************/
            final DbIdols dbIdols = new DbIdols(VerVideoInfo.this);
            Idol = dbIdols.verIdol(Video.getId_idol());

            idVideo = Video.getId();

            nombreIdol.setText(Idol.getNombre());

            PromocionalVideo.setText(Video.getPromocional());
            GeneroVideo.setText(Video.getGenero_video());

            byte[] imagen = Video.getImagen_video();

            if (imagen == null){
                imagenVideo.setImageResource(R.drawable.img_base);
                Log.e("Foto inai", String.valueOf(Video.getImagen_video()));

            } else {

                Bitmap bmp = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
                imagenVideo.setImageBitmap(bmp);
                Log.e("Foto yabai", String.valueOf(Video.getImagen_video()));

            }
        }

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerVideoInfo.this);
                builder.setMessage("¿De verdad vamos a eliminar este pedazo de clip???? Pero si es buenísimo")
                        .setPositiveButton("SÍ, soy tonto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (dbVideos.eliminarVideo(id)){
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

        botonVerVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            /********** bdd para el registro de reproducción **********/

            DbRepVideos dbRepVideos = new DbRepVideos(VerVideoInfo.this);


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Video.getLink().toString()));
                startActivity(intent);


                long id = dbRepVideos.insertarRepVideo(UsuarioSesionActual,
                        idVideo,
                        mesString);

                if (id > 0){

                    Toast.makeText(VerVideoInfo.this, "+1 Reproducción", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        if (UsuarioSesionActual.equals("StaffActivo")){

            inflater.inflate(R.menu.menu_ver_videos, menu);

        } else {

            inflater.inflate(R.menu.menu_cerrar_sesion, menu);

        }
        return true; }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.editarVideo:
                editarVideo();
                return true;

            case R.id.eliminarVideo:
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
        Intent intent = new Intent(VerVideoInfo.this, PantallaPrincipal.class);
        startActivity(intent);

    }

    private void editarVideo() {

        Intent intent = new Intent(this, EditarVideos.class);
        intent.putExtra("ID", id);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);

    }

    private void lista(){
        Intent intent = new Intent(this, ListaDeVideos.class);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);
    }



}