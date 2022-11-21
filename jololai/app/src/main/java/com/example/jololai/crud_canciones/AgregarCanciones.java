package com.example.jololai.crud_canciones;

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
import com.example.jololai.db.DbCanciones;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Idols;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class AgregarCanciones extends AppCompatActivity {

    /********* Declaramos las variables **************/

    Button botonAgregarCancion;

    Spinner spinnerNombreCantante;
    String idolCantanteString; /* pasarlo a string */

    ArrayList<String> listaNombreIdols;
    ArrayList<Idols> listaIdols;

    Spinner spinnerCoverUOriginal;
    String tipoCancionString; /* pasarlo a string */

    EditText textoNombreCancion;
    EditText textoLetraCanción;

    Bitmap guardarImagenCancion;
    byte[] imageInBytesCancion;
    ImageView imagenDefectoCancion; /* Foto de la idol  */
    Button botonFotoCancion; /* Boton para escoger la foto */

    /**** invocamos la base de datos, la parte de las idols ******/

    DbIdols dbIdols = new DbIdols(AgregarCanciones.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_canciones);

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

        textoNombreCancion = findViewById(R.id.NombreCancion);
        textoLetraCanción = findViewById(R.id.letraCancion);

        /************ SPINNERSSSSSSSSSSSS ************/

        /*** Spinner de cover u original ***/

        spinnerCoverUOriginal = (Spinner) findViewById(R.id.spinnerCoverUOriginal);

        ArrayAdapter<CharSequence> lista_tipos_cancion = ArrayAdapter.createFromResource(this,
                R.array.spinner_tipo_cancion, android.R.layout.simple_spinner_item);

        spinnerCoverUOriginal.setAdapter(lista_tipos_cancion);

        spinnerCoverUOriginal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                tipoCancionString = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Spinner del nombre de la idol ***/

        spinnerNombreCantante = findViewById(R.id.spinnerIdolCancion);

        consultarListaIdols();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listaNombreIdols);

        spinnerNombreCantante.setAdapter(adaptador);

        spinnerNombreCantante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                idolCantanteString = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Cosas de la imagen ***/

        botonFotoCancion = findViewById(R.id.subirFotoCancion);
        imagenDefectoCancion = findViewById(R.id.imagenIdol);

        /*** coso para subir una imagen (no hentai, idealmente) ***/

        botonFotoCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (guardarImagenCancion == null) {

                    Log.e("aaaaaa", "OH NO, NULL!");

                    cargarImagenCancion();

                } else {

                    Log.e("aaaaaaaa", "ctm");

                }
            }
        });


        /********** boton para guardar los cambios *************/

        botonAgregarCancion = findViewById(R.id.botonAgregarCancion);
        imagenDefectoCancion = findViewById(R.id.fotoCancion);

        botonAgregarCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Nombre de la canción ", textoNombreCancion.getText().toString());
                Log.e("Tipo de canción ", tipoCancionString);
                Log.e("letra", textoLetraCanción.getText().toString());
                Log.e("cantante", idolCantanteString);

                if (textoNombreCancion.getText().toString().equals("") ||
                        spinnerCoverUOriginal.getSelectedItem().toString().equals("Tipo de canción") ||
                        textoLetraCanción.getText().toString().equals("") ||
                        spinnerNombreCantante.getSelectedItem().toString().equals("Idol")) {

                    Toast.makeText(AgregarCanciones.this, "Rellene todos los campos", Toast.LENGTH_LONG).show();

                } else {

                    DbCanciones dbCanciones = new DbCanciones(AgregarCanciones.this);

                    int idSpinner = (int) spinnerNombreCantante.getSelectedItemId();

                    if (idSpinner != 0) {
                        int idIdol = listaIdols.get(idSpinner - 1).getId();

                        long id = dbCanciones.insertarCancion(textoNombreCancion.getText().toString(),
                                spinnerCoverUOriginal.getSelectedItem().toString(),
                                textoLetraCanción.getText().toString(),
                                imageInBytesCancion,
                                idIdol);

                        if (id > 0) {

                            Toast.makeText(AgregarCanciones.this, "Canción guardada", Toast.LENGTH_LONG).show();
                            finish();
                            limpiar();

                        } else {
                            Toast.makeText(AgregarCanciones.this, "Se ha producido un error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private void limpiar() {

        textoNombreCancion.setText("");
        textoLetraCanción.setText("");

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
    private void cargarImagenCancion() {
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
                guardarImagenCancion = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imagenDefectoCancion.setImageURI(path);

                ByteArrayOutputStream objectByOutputStream = new ByteArrayOutputStream();

                guardarImagenCancion.compress(Bitmap.CompressFormat.JPEG, 100, objectByOutputStream);
                imageInBytesCancion = objectByOutputStream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}