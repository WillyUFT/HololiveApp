package com.example.jololai.crud_videos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jololai.R;
import com.example.jololai.crud_canciones.EditarCanciones;
import com.example.jololai.crud_canciones.ListaDeCanciones;
import com.example.jololai.crud_canciones.VerCancionInfo;
import com.example.jololai.db.DbIdols;
import com.example.jololai.db.DbVideos;
import com.example.jololai.entidades.Idols;
import com.example.jololai.entidades.Videos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class EditarVideos extends AppCompatActivity {

    /********* Declaramos las variables **************/

    boolean correcto = false;
    Integer id = 0;
    Idols Idol;
    Videos Video;

    Button botonAgregarVideoEd;

    Spinner spinnerNombreIdolVideoEd;
    String idolVideoStringEd; /* pasarlo a string*/

    ArrayList<String> istaNombreIdolsEd;
    ArrayList<Idols> listaIdolsEd;

    Spinner spinnerPromocionalEd;
    String promocionalStringEd; /* pasarlo a string */

    Spinner spinnerGeneroEd;
    String generoStringEd; /* pasarlo a string */

    EditText textoNombreVideoEd;
    EditText textoLinkVideoEd;

    Bitmap guardarImagenVideoEd;
    byte[] imageInBytesVideoEd;
    ImageView imagenDefectoVideoEd; /* Foto de la idol  */
    Button botonFotoVideoEd; /* Boton para escoger la foto */

    String UsuarioSesionActual;

    /**** invocamos la base de datos, la parte de las idols ******/

    DbIdols dbIdols = new DbIdols(EditarVideos.this);
    DbVideos dbVideos = new DbVideos(EditarVideos.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_videos);

        /******* esto es por el error ese que dice OMG WHY IT IS SO BIG????
         * (No cabe en la pantalla) *********/

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

        /********** Coso para guardar el id de la pantalla anteior ****************/

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        Video = dbVideos.verVideo(id);


        /******** llamamos a los edit text ***********/

        textoNombreVideoEd = findViewById(R.id.EditTextNombreVideoEd);
        textoLinkVideoEd = findViewById(R.id.EditTextLinkVideoEd);

        /************ SPINNERSSSSSSSSSSSS ************/

        /*** Spinner de si es promocional o no  ***/

        spinnerPromocionalEd = (Spinner) findViewById(R.id.spinnerPromocionalVideoEd);

        ArrayAdapter<CharSequence> promocional = ArrayAdapter.createFromResource(this,
                R.array.spinner_promocional, android.R.layout.simple_spinner_item);

        spinnerPromocionalEd.setAdapter(promocional);

        spinnerPromocionalEd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                promocionalStringEd = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Spinner del genero del video ***/

        spinnerGeneroEd = (Spinner) findViewById(R.id.spinnerGeneroVideoEd);

        ArrayAdapter<CharSequence> lista_generos = ArrayAdapter.createFromResource(this,
                R.array.spinner_generos, android.R.layout.simple_spinner_item);

        spinnerGeneroEd.setAdapter(lista_generos);

        spinnerGeneroEd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                generoStringEd = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Spinner del nombre de la idol ***/

        spinnerNombreIdolVideoEd = findViewById(R.id.spinnerIdolVideoEd);

        consultarListaIdols();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, istaNombreIdolsEd);

        spinnerNombreIdolVideoEd.setAdapter(adaptador);

        spinnerNombreIdolVideoEd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                idolVideoStringEd = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Cosas de la imagen ***/

        botonFotoVideoEd = findViewById(R.id.subirFotoVideoEd);
        imagenDefectoVideoEd = findViewById(R.id.imagenIdolEd);

        /*** coso para subir una imagen (no hentai, idealmente) ***/

        botonFotoVideoEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (guardarImagenVideoEd == null) {

                    cargarImagenVideoEd();

                } else {

                }


            }
        });


        /********** boton para guardar los cambios *************/

        botonAgregarVideoEd = findViewById(R.id.botonAgregarVideoEd);
        imagenDefectoVideoEd = findViewById(R.id.fotoVideoEd);

        Idol = dbIdols.verIdol(Video.getId_idol());

        botonAgregarVideoEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textoNombreVideoEd.getText().toString().equals("") ||
                        spinnerPromocionalEd.getSelectedItem().toString().equals("Promocional o no?") ||
                        spinnerNombreIdolVideoEd.getSelectedItem().toString().equals("Idol") ||
                        spinnerGeneroEd.getSelectedItem().toString().equals("Género")) {

                    Toast.makeText(EditarVideos.this, "Rellene todos los campos", Toast.LENGTH_LONG).show();

                } else {

                    correcto = dbVideos.editarVideo(id,
                            textoNombreVideoEd.getText().toString(),
                            spinnerGeneroEd.getSelectedItem().toString(),
                            spinnerPromocionalEd.getSelectedItem().toString(),
                            imageInBytesVideoEd,
                            textoLinkVideoEd.getText().toString(),
                            Idol.getId());

                    if (correcto) {

                        Toast.makeText(EditarVideos.this, "Video modificado", Toast.LENGTH_LONG).show();
                        lista();

                    } else {

                        Toast.makeText(EditarVideos.this, "Algo pasó papito, perdón", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

    /**** Da problemas ****/
    private void verVideoEditado(){
        Intent intent = new Intent(this, VerVideoInfo.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    private void lista(){
        Intent intent = new Intent(this, ListaDeVideos.class);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);
    }


    private void consultarListaIdols() {

        SQLiteDatabase db = dbIdols.getReadableDatabase();
        Idols idol = null;
        listaIdolsEd = new ArrayList<Idols>();

        /** No entiendo porqué no pude ocupar el de DbIdols, lo hice de nuevo no más xd **/

        Cursor cursorIdols;

        cursorIdols = db.rawQuery("SELECT * FROM " + DbIdols.TABLE_IDOLS + " ORDER BY nombre ASC", null);

        while(cursorIdols.moveToNext()){

            idol = new Idols();
            idol.setId(cursorIdols.getInt(0));
            idol.setNombre(cursorIdols.getString(1));
            idol.setNombreOriginal(cursorIdols.getString(2));
            idol.setEstado(cursorIdols.getString(3));
            idol.setUnidad(cursorIdols.getString(4));
            idol.setGeneracion(cursorIdols.getString(5));
            idol.setDebut(cursorIdols.getString(6));
            idol.setNickname(cursorIdols.getString(7));
            idol.setCumple(cursorIdols.getString(8));
            idol.setAltura(cursorIdols.getString(9));
            idol.setDisenador(cursorIdols.getString(10));
            idol.setBio(cursorIdols.getString(11));
            idol.setImagen(cursorIdols.getBlob(12));;

            listaIdolsEd.add(idol);
        }

        mostrarLista();

    }

    private void mostrarLista() {

        istaNombreIdolsEd = new ArrayList<String>();
        istaNombreIdolsEd.add("Idol");

        for(int i = 0; i < listaIdolsEd.size(); i++){

            istaNombreIdolsEd.add(listaIdolsEd.get(i).getNombre());
        }
    }

    /*** Función para subir la imagen ***/
    private void cargarImagenVideoEd() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Selecciones una imagen"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            try {
                guardarImagenVideoEd = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imagenDefectoVideoEd.setImageURI(path);

                ByteArrayOutputStream objectByOutputStream = new ByteArrayOutputStream();

                guardarImagenVideoEd.compress(Bitmap.CompressFormat.JPEG, 100, objectByOutputStream);
                imageInBytesVideoEd = objectByOutputStream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}