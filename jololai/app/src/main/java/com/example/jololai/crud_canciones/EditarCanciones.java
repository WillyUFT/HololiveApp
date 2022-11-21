package com.example.jololai.crud_canciones;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jololai.R;
import com.example.jololai.crud_idols.EditarIdol;
import com.example.jololai.crud_idols.VerIdolInfo;
import com.example.jololai.db.DbCanciones;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Idols;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class EditarCanciones extends AppCompatActivity {

    /********* Declaramos las variables **************/

    boolean correcto = false;
    Integer id = 0;
    Idols Idol;
    Canciones Cancion;

    Button botonEditarCancion;

    Spinner spinnerNombreCantanteEd;
    String idolCantanteStringEd; /* pasarlo a string*/

    ArrayList<String> listaNombreIdolsEd;
    ArrayList<Idols> listaIdolsEd;

    Spinner spinnerCoverUOriginalEd;
    String tipoCancionStringEd; /* pasarlo a string */

    EditText textoNombreCancionEd;
    EditText textoLetraCanciónEd;

    Bitmap guardarImagenCancionEd;
    byte[] imageInBytesCancionEd;
    ImageView imagenDefectoCancionEd; /* Foto de la idol  */
    Button botonFotoCancionEd; /* Boton para escoger la foto */

    /**** invocamos la base de datos, la parte de las idols ******/

    DbIdols dbIdols = new DbIdols(EditarCanciones.this);
    DbCanciones dbCanciones = new DbCanciones(EditarCanciones.this);

    String UsuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_canciones);

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
            if (extras == null) {
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


        Cancion = dbCanciones.verCancion(id);

        /******** llamamos a los edit text ***********/

        textoNombreCancionEd = findViewById(R.id.NombreCancionEd);
        textoLetraCanciónEd = findViewById(R.id.letraCancionEd);

        /************ SPINNERSSSSSSSSSSSS ************/

        /*** Spinner de cover u original ***/

        spinnerCoverUOriginalEd = (Spinner) findViewById(R.id.spinnerCoverUOriginalEd);

        ArrayAdapter<CharSequence> lista_tipos_cancion = ArrayAdapter.createFromResource(this,
                R.array.spinner_tipo_cancion, android.R.layout.simple_spinner_item);

        spinnerCoverUOriginalEd.setAdapter(lista_tipos_cancion);

        spinnerCoverUOriginalEd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                tipoCancionStringEd = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Spinner del nombre de la idol ***/

        spinnerNombreCantanteEd = findViewById(R.id.spinnerIdolCancionEd);

        consultarListaIdolsEd();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listaNombreIdolsEd);

        Log.e("ARRAY", String.valueOf(adaptador));

        spinnerNombreCantanteEd.setAdapter(adaptador);

        spinnerNombreCantanteEd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                idolCantanteStringEd = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Cosas de la imagen ***/

        botonFotoCancionEd = findViewById(R.id.subirFotoCancionEd);
        imagenDefectoCancionEd = findViewById(R.id.imagenIdolEd);

        /*** coso para subir una imagen (no hentai, idealmente) ***/

        botonFotoCancionEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (guardarImagenCancionEd == null) {

                    Log.e("aaaaaa", "OH NO, NULL!");

                    cargarImagenCancion();

                } else {

                    Log.e("aaaaaaaa", "ctm");

                }
            }
        });


        /********** boton para guardar los cambios *************/

        botonEditarCancion = findViewById(R.id.botonEditarCancion);
        imagenDefectoCancionEd = findViewById(R.id.fotoCancionEd);

        Idol = dbIdols.verIdol(Cancion.getId_idol());

        botonEditarCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textoNombreCancionEd.getText().toString().equals("") ||
                        spinnerCoverUOriginalEd.getSelectedItem().toString().equals("Tipo de canción") ||
                        textoLetraCanciónEd.getText().toString().equals("") ||
                        spinnerNombreCantanteEd.getSelectedItem().toString().equals("Idol")){

                    Toast.makeText(EditarCanciones.this, "DEBE LLENAR LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_LONG).show();

                } else {

                    correcto = dbCanciones.editarCancion(id, textoNombreCancionEd.getText().toString(),
                            spinnerCoverUOriginalEd.getSelectedItem().toString(),
                            textoLetraCanciónEd.getText().toString(),
                            imageInBytesCancionEd,
                            Idol.getId());

                    if (correcto) {

                        Toast.makeText(EditarCanciones.this, "REGISTRO MODIFICADO", Toast.LENGTH_LONG).show();
                        lista();

                    } else {

                        Toast.makeText(EditarCanciones.this, "ERROR AL MODIFICAR REGISTRO", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });
    }

    private void consultarListaIdolsEd() {

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

        mostrarListaEd();

    }

    private void mostrarListaEd() {

        listaNombreIdolsEd = new ArrayList<String>();
        listaNombreIdolsEd.add("Idol");

        for(int i = 0; i < listaIdolsEd.size(); i++){

            listaNombreIdolsEd.add(listaIdolsEd.get(i).getNombre());
        }
    }

    private void lista(){
        Intent intent = new Intent(this, ListaDeCanciones.class);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);
    }

    /*** Por algún motivo no se ve bien así, mejor lo sacamos ***/
    private void verCancionEditada(){
        Intent intent = new Intent(this, VerCancionInfo.class);
        intent.putExtra("ID", id);
        startActivity(intent);
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
                guardarImagenCancionEd = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imagenDefectoCancionEd.setImageURI(path);

                ByteArrayOutputStream objectByOutputStream = new ByteArrayOutputStream();

                guardarImagenCancionEd.compress(Bitmap.CompressFormat.JPEG, 100, objectByOutputStream);
                imageInBytesCancionEd = objectByOutputStream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}