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
import com.example.jololai.db.DbIdols;
import com.example.jololai.db.DbVideos;
import com.example.jololai.entidades.Idols;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class AgregarVideos extends AppCompatActivity {

    /********* Declaramos las variables **************/

    Button botonAgregarVideo;

    Spinner spinnerNombreIdolVideo;
    String idolVideoString; /* pasarlo a string*/

    ArrayList<String> listaNombreIdols;
    ArrayList<Idols> listaIdols;

    Spinner spinnerPromocional;
    String promocionalString; /* pasarlo a string */

    Spinner spinnerGenero;
    String generoString; /* pasarlo a string */

    EditText textoNombreVideo;
    EditText textoLinkVideo;

    Bitmap guardarImagenVideo;
    byte[] imageInBytesVideo;
    ImageView imagenDefectoVideo; /* Foto de la idol  */
    Button botonFotoVideo; /* Boton para escoger la foto */

    /**** invocamos la base de datos, la parte de las idols ******/

    DbIdols dbIdols = new DbIdols(AgregarVideos.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_videos);

        /******* esto es por el error ese que dice OMG WHY IT IS SO BIG????
         * (No cabe en la pantalla) *********/

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        /******** llamamos a los edit text ***********/

        textoNombreVideo = findViewById(R.id.EditTextNombreVideo);
        textoLinkVideo = findViewById(R.id.EditTextLinkVideo);

        /************ SPINNERSSSSSSSSSSSS ************/

        /*** Spinner de si es promocional o no  ***/

        spinnerPromocional = (Spinner) findViewById(R.id.spinnerPromocionalVideo);

        ArrayAdapter<CharSequence> promocional = ArrayAdapter.createFromResource(this,
                R.array.spinner_promocional, android.R.layout.simple_spinner_item);

        spinnerPromocional.setAdapter(promocional);

        spinnerPromocional.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                promocionalString = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Spinner del genero del video ***/

        spinnerGenero = (Spinner) findViewById(R.id.spinnerGeneroVideo);

        ArrayAdapter<CharSequence> lista_generos = ArrayAdapter.createFromResource(this,
                R.array.spinner_generos, android.R.layout.simple_spinner_item);

        spinnerGenero.setAdapter(lista_generos);

        spinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                generoString = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Spinner del nombre de la idol ***/

        spinnerNombreIdolVideo = findViewById(R.id.spinnerIdolConcierto);

        consultarListaIdols();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listaNombreIdols);

        spinnerNombreIdolVideo.setAdapter(adaptador);

        spinnerNombreIdolVideo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                idolVideoString = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Cosas de la imagen ***/

        botonFotoVideo = findViewById(R.id.subirFotoVideo);
        imagenDefectoVideo = findViewById(R.id.imagenIdol);

        /*** coso para subir una imagen (no hentai, idealmente) ***/

        botonFotoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (guardarImagenVideo == null) {

                    Log.e("aaaaaa", "OH NO, NULL!");

                    cargarImagenVideo();

                } else {

                    Log.e("aaaaaaaa", "ctm");

                }


            }
        });


        /********** boton para guardar los cambios *************/

        botonAgregarVideo = findViewById(R.id.botonAgregarVideo);
        imagenDefectoVideo = findViewById(R.id.fotoVideo);

        DbVideos dbVideos = new DbVideos(AgregarVideos.this);

        botonAgregarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textoNombreVideo.getText().toString().equals("") ||
                        spinnerPromocional.getSelectedItem().toString().equals("Promocional o no?") ||
                        spinnerNombreIdolVideo.getSelectedItem().toString().equals("Idol") ||
                        spinnerGenero.getSelectedItem().toString().equals("Género")) {

                    Toast.makeText(AgregarVideos.this, "Rellene todos los campos", Toast.LENGTH_LONG).show();

                } else {

                    int idSpinner = (int) spinnerNombreIdolVideo.getSelectedItemId();

                    if (idSpinner != 0) {
                        int idIdol = listaIdols.get(idSpinner - 1).getId();

                        long id = dbVideos.insertarVideo(textoNombreVideo.getText().toString(),
                                spinnerGenero.getSelectedItem().toString(),
                                spinnerPromocional.getSelectedItem().toString(),
                                imageInBytesVideo,
                                textoLinkVideo.getText().toString(),
                                idIdol);

                        if (id > 0) {

                            Toast.makeText(AgregarVideos.this, "Video guardado", Toast.LENGTH_LONG).show();
                            finish();
                            limpiar();

                        } else {
                            Toast.makeText(AgregarVideos.this, "Se ha producido un error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private void limpiar() {

        textoNombreVideo.setText("");

    }

    private void consultarListaIdols() {

        SQLiteDatabase db = dbIdols.getReadableDatabase();
        Idols idol = null;
        listaIdols = new ArrayList<Idols>();

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

            listaIdols.add(idol);
        }

        mostrarLista();

    }

    private void mostrarLista() {

        listaNombreIdols = new ArrayList<String>();
        listaNombreIdols.add("Idol");

        for(int i = 0; i < listaIdols.size(); i++){

            listaNombreIdols.add(listaIdols.get(i).getNombre());
        }
    }

    /*** Función para subir la imagen ***/
    private void cargarImagenVideo() {
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
                guardarImagenVideo = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imagenDefectoVideo.setImageURI(path);

                ByteArrayOutputStream objectByOutputStream = new ByteArrayOutputStream();

                guardarImagenVideo.compress(Bitmap.CompressFormat.JPEG, 100, objectByOutputStream);
                imageInBytesVideo = objectByOutputStream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}