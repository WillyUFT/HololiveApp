package com.example.jololai.crud_idols;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jololai.R;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Idols;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class EditarIdol extends AppCompatActivity {

    /*** Cosos que tiene la interfaz ***/

    /*** Cosas de la imagen ***/

    Bitmap guardarImagenEd; /*fotoooo*/
    byte[] imageInBytesEd;
    ImageView imagenDefectoEd; /* Foto de la idol  */
    Button botonFotoEd; /* Boton para escoger la foto */

    /*** Los spinners, los combos, listas, como se llamen, si no ocupamos eso no podemos
     usar estadísticas más rato ***/
    Spinner spinnerEstadoEd; /* lista que tiene los estados */
    String estadoStringEd; /* pasarlo a string */

    Spinner spinnerUnidadEd; /* lista quee tiene las unidades */
    String  unidadStringEd; /* pasarlo a string */

    Spinner spinnerDiaCumEd; /* lista que tiene dias cumpleaños */
    String diaCumStringEd; /* pasarlo a string */

    Spinner spinnerMesCumEd; /* lista que tiene los meses cumpleaños */
    String mesCumStringEd; /* pasarlo a string */

    String fechaCumEd; /* juntar dia y mes para hacer un cumpleaños */

    Spinner spinnerGeneracionEd; /* lista que tiene las generaciones */
    String generacionStringEd; /* pasarlo a string */

    /*** coso para el datepicker ***/
    private int diaEd;
    private int mesEd;
    private int anoEd;
    Button botonCalendarioEd;

    /*** Espacios para texto ***/

    EditText textoNombreEd;
    EditText textoApellidoEd;
    EditText textoNombreOriginalEd;
    EditText textoDebutEd;
    EditText textoAlturaEd;
    EditText textoDisenadorEd;
    EditText textoBioEd;
    EditText textoNicknameEd;

    /*** Botón para agregar una idol a la base de datos ***/
    Button botonTerminarEd;

    int id = 0;
    boolean correcto = false;
    Idols idol;

    String UsuarioSesionActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        /********************************* Campos de texto ****************************************/

        textoNombreEd = findViewById(R.id.textoNombreEd);
        textoApellidoEd = findViewById(R.id.textoApellidoEd);
        textoNombreOriginalEd = findViewById(R.id.textoNombreOriginalEd);
        textoAlturaEd = findViewById(R.id.textoAlturaEd);
        textoDisenadorEd = findViewById(R.id.textoDisenadorEd);
        textoBioEd = findViewById(R.id.textoBioEd);
        textoNicknameEd = findViewById(R.id.textoNicknameEd);
        textoDebutEd = findViewById(R.id.textoDebutEd);


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


        final DbIdols dbIdols = new DbIdols(EditarIdol.this);
        idol = dbIdols.verIdol(id);

        if (idol != null) {

            Log.e("textoNombreEd",textoNombreEd.getText().toString());
            Log.e("idol.getNombre()", idol.getNombre());

            textoNombreEd.setText(idol.getNombre());

            Log.e("textoNombreEd Later",textoNombreEd.getText().toString());
            Log.e("idol.getNombre() Later", idol.getNombre());


            textoApellidoEd.setText("");
            textoNombreOriginalEd.setText(idol.getNombreOriginal());
            textoDebutEd.setText(idol.getDebut());
            textoAlturaEd.setText(idol.getAltura());
            textoNicknameEd.setText(idol.getNickname());
            textoDisenadorEd.setText(idol.getDisenador());
            textoBioEd.setText(idol.getBio());
        }


        /*** coso para subir una imagen (no hentai, idealmente) ***/


        imagenDefectoEd = (ImageView) findViewById(R.id.imagenIdolEd);
        botonFotoEd = (Button) findViewById(R.id.botonFotoEd);

        botonFotoEd = findViewById(R.id.botonFotoEd);

        botonFotoEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(guardarImagenEd == null){

                    Log.e("aaaaaa", "OH NO, NULL!");

                    cargarImagen();


                } else {

                    Log.e("aaaaaa", "VEAMOSSSS");
                }
            }
        });

        /************************* SPINNERS *********************************/

        /*** Spinner del estado ***/

        spinnerEstadoEd = (Spinner) findViewById(R.id.spinnerEstadoEd);

        ArrayAdapter<CharSequence> lista_estados = ArrayAdapter.createFromResource(this,
                R.array.spinner_estados, android.R.layout.simple_spinner_item);

        spinnerEstadoEd.setAdapter(lista_estados);

        spinnerEstadoEd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                estadoStringEd = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Spinner de la unidad ***/

        spinnerUnidadEd = (Spinner) findViewById(R.id.spinnerUnidadEd);

        ArrayAdapter<CharSequence> lista_unidades = ArrayAdapter.createFromResource(this,
                R.array.spinner_unidades, android.R.layout.simple_spinner_item);

        spinnerUnidadEd.setAdapter(lista_unidades);

        spinnerUnidadEd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                /*** lista de generaciones, depende de la unidad ***/

                ArrayAdapter<CharSequence> lista_gen_id_cn = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_generaciones_id_cn, android.R.layout.simple_spinner_item);

                ArrayAdapter<CharSequence> lista_gen_en = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_generaciones_en, android.R.layout.simple_spinner_item);

                ArrayAdapter<CharSequence> lista_gen_st = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_generaciones_st, android.R.layout.simple_spinner_item);

                ArrayAdapter<CharSequence> lista_gen_null = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_generaciones_null, android.R.layout.simple_spinner_item);

                ArrayAdapter<CharSequence> lista_gen_jp = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_generaciones_jp, android.R.layout.simple_spinner_item);

                unidadStringEd = parent.getItemAtPosition(i).toString();

                if (spinnerUnidadEd.getSelectedItem().equals("Japan")) {
                    spinnerGeneracionEd.setAdapter(lista_gen_jp);
                } else if (spinnerUnidadEd.getSelectedItem().equals("Indonesia")
                        || spinnerUnidadEd.getSelectedItem().equals("China")) {
                    spinnerGeneracionEd.setAdapter(lista_gen_id_cn);
                } else if (spinnerUnidadEd.getSelectedItem().equals("English")) {
                    spinnerGeneracionEd.setAdapter(lista_gen_en);
                } else if (spinnerUnidadEd.getSelectedItem().equals("Staff")) {
                    spinnerGeneracionEd.setAdapter(lista_gen_st);
                } else {
                    spinnerGeneracionEd.setAdapter(lista_gen_null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** spinner de la generación ***/

        spinnerGeneracionEd = (Spinner) findViewById(R.id.spinnerGeneracionEd);

        spinnerGeneracionEd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                generacionStringEd = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        /*** Spinner del mes de cumpleaños ***/

        spinnerMesCumEd = (Spinner) findViewById(R.id.spinnerMesCumEd);

        ArrayAdapter<CharSequence> lista_meses = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.spinner_meses, android.R.layout.simple_spinner_item);

        spinnerMesCumEd.setAdapter(lista_meses);

        spinnerMesCumEd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                /*** lista de días, depende del mes ***/

                ArrayAdapter<CharSequence> lista_dias_31 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_dias_mesesCon31dias, android.R.layout.simple_spinner_item);

                ArrayAdapter<CharSequence> lista_dias_30 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_dias_mesesCon30dias, android.R.layout.simple_spinner_item);

                ArrayAdapter<CharSequence> lista_dias_29 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_dias_mesesCon29dias, android.R.layout.simple_spinner_item);

                mesCumStringEd = parent.getItemAtPosition(i).toString();

                if (spinnerMesCumEd.getSelectedItem().equals("Enero") ||
                        spinnerMesCumEd.getSelectedItem().equals("Marzo") ||
                        spinnerMesCumEd.getSelectedItem().equals("Mayo") ||
                        spinnerMesCumEd.getSelectedItem().equals("Julio") ||
                        spinnerMesCumEd.getSelectedItem().equals("Agosto") ||
                        spinnerMesCumEd.getSelectedItem().equals("Octubre") ||
                        spinnerMesCumEd.getSelectedItem().equals("Diciembre")) {

                    spinnerDiaCumEd.setAdapter(lista_dias_31);

                } else if (spinnerMesCumEd.getSelectedItem().equals("Abril") ||
                        spinnerMesCumEd.getSelectedItem().equals("Junio") ||
                        spinnerMesCumEd.getSelectedItem().equals("Septiembre") ||
                        spinnerMesCumEd.getSelectedItem().equals("Noviembre")) {

                    spinnerDiaCumEd.setAdapter(lista_dias_30);

                } else {
                    spinnerDiaCumEd.setAdapter(lista_dias_29);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /*** Spinner del dia de cumpleaños ***/

        spinnerDiaCumEd = (Spinner) findViewById(R.id.spinnerDiaCumEd);

        spinnerDiaCumEd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                diaCumStringEd = parent.getItemAtPosition(i).toString();

                /*** Hacer fecha cumpleaños ***/

                fechaCumEd = diaCumStringEd + "/" + mesCumStringEd;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /******************************** Coso para la fecha de debut *****************************/

        botonCalendarioEd = (Button) findViewById(R.id.botonCalendarioEd);

        botonCalendarioEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == botonCalendarioEd) {
                    final Calendar c = Calendar.getInstance();
                    diaEd = c.get(Calendar.DAY_OF_MONTH);
                    mesEd = c.get(Calendar.MONTH);
                    anoEd = c.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditarIdol.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayofMonth) {

                            textoDebutEd.setText(dayofMonth + "/" + monthOfYear + "/" + year);
                        }
                    }
                            , diaEd, mesEd, anoEd);
                    datePickerDialog.show();
                }
            }
        });

        /************************************ Botón para guardar los cambios *********************************/

        botonTerminarEd = findViewById(R.id.botonTerminarEdd);

        botonTerminarEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!textoNombreEd.getText().toString().equals("") &&
                        !textoApellidoEd.getText().toString().equals("") &&
                        !textoNombreOriginalEd.getText().toString().equals("") &&
                        !spinnerEstadoEd.getSelectedItem().toString().equals("Estado") &&
                        !spinnerUnidadEd.getSelectedItem().toString().equals("Unidad")  &&
                        !spinnerGeneracionEd.getSelectedItem().toString().equals("Generación") &&
                        !textoDebutEd.getText().toString().equals("") &&
                        !textoNicknameEd.getText().toString().equals("") &&
                        !spinnerDiaCumEd.getSelectedItem().toString().equals("Día de cumpleaños") &&
                        !spinnerMesCumEd.getSelectedItem().toString().equals("Mes de cumpleaños")  &&
                        !textoAlturaEd.getText().toString().equals("") &&
                        !textoDisenadorEd.getText().toString().equals("") &&
                        !textoBioEd.getText().toString().equals("")){

                    correcto = dbIdols.editarIdol(id,
                            textoNombreEd.getText().toString() + " " + textoApellidoEd.getText().toString(),
                            textoNombreOriginalEd.getText().toString(),
                            spinnerEstadoEd.getSelectedItem().toString(),
                            spinnerUnidadEd.getSelectedItem().toString(),
                            spinnerGeneracionEd.getSelectedItem().toString(),
                            textoDebutEd.getText().toString(),
                            textoNicknameEd.getText().toString(),
                            spinnerDiaCumEd.getSelectedItem().toString() + "/" +  spinnerMesCumEd.getSelectedItem().toString(),
                            textoAlturaEd.getText().toString(),
                            textoDisenadorEd.getText().toString(),
                            textoBioEd.getText().toString(),
                            imageInBytesEd);

                    if(correcto){
                        Toast.makeText(EditarIdol.this, "REGISTRO MODIFICADO", Toast.LENGTH_LONG).show();
                        //verIdolEditada();
                        lista();
                    } else {
                        Toast.makeText(EditarIdol.this, "ERROR AL MODIFICAR REGISTRO", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EditarIdol.this, "DEBE LLENAR LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void lista(){
        Intent intent = new Intent(this, ListaDeIdols.class);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);
    }

/***********
    private void verIdolEditada(){
        Intent intent = new Intent(this, VerIdolInfo.class);
        intent.putExtra("ID", id);
        intent.putExtra("SesionActiva",UsuarioSesionActual);
        startActivity(intent);
    }
**************/

    /*** Función para subir la imagen ***/
    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Selecciones una imagen"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri path = data.getData();
            try {
                guardarImagenEd = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imagenDefectoEd.setImageURI(path);

                ByteArrayOutputStream objectByOutputStream = new ByteArrayOutputStream();

                guardarImagenEd.compress(Bitmap.CompressFormat.JPEG, 100, objectByOutputStream);
                imageInBytesEd = objectByOutputStream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}