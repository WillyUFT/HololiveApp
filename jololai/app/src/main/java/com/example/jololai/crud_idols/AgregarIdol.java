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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AgregarIdol extends AppCompatActivity {

    /*** Cosos que tiene la interfaz ***/

    /*** Cosas de la imagen ***/


    Bitmap guardarImagen;
    byte[] imageInBytes;
    ImageView imagenDefecto; /* Foto de la idol  */
    Button botonFoto; /* Boton para escoger la foto */

    /*** Los spinners, los combos, listas, como se llamen, si no ocupamos eso no podemos
     usar estadísticas más rato ***/
    Spinner spinnerEstado; /* lista que tiene los estados */
    String estadoString; /* pasarlo a string */

    Spinner spinnerUnidad; /* lista que tiene las unidades */
    String  unidadString; /* pasarlo a string */

    Spinner spinnerDiaCum; /* lista que tiene dias cumpleaños */
    String diaCumString; /* pasarlo a string */

    Spinner spinnerMesCum; /* lista que tiene los meses cumpleaños */
    String mesCumString; /* pasarlo a string */

    String fechaCum; /* juntar dia y mes para hacer un cumpleaños */

    Spinner spinnerGeneracion; /* lista que tiene las generaciones */
    String generacionString; /* pasarlo a string */

    /*** coso para el datepicker ***/
    private int dia;
    private int mes;
    private int ano;
    Button botonCalendario;

    /*** Espacios para texto ***/

    EditText textoNombre;
    EditText textoApellido;
    EditText textoNombreOriginal;
    EditText textoDebut;
    EditText textoAltura;
    EditText textoDisenador;
    EditText textoBio;
    EditText textoNickname;

    /*** Botón para agregar una idol a la base de datos ***/
    Button botonTerminarAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_idol);

        imagenDefecto = findViewById(R.id.imagenIdol);
        botonFoto = findViewById(R.id.botonFoto);

        /*** coso para subir una imagen (no hentai, idealmente) ***/

        botonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(guardarImagen == null){

                    Log.e("aaaaaa", "OH NO, NULL!");

                    cargarImagen();

                } else {

                    Log.e("aaaaaaaa", "ctm");

                }


            }
        });

        /************************* SPINNERS *********************************/

        /*** Spinner del estado ***/

        spinnerEstado = (Spinner) findViewById(R.id.spinnerEstado);

        ArrayAdapter<CharSequence> lista_estados = ArrayAdapter.createFromResource(this,
                R.array.spinner_estados, android.R.layout.simple_spinner_item);

        spinnerEstado.setAdapter(lista_estados);

        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                estadoString =  parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** Spinner de la unidad ***/

        spinnerUnidad = (Spinner) findViewById(R.id.spinnerUnidad);

        ArrayAdapter<CharSequence> lista_unidades = ArrayAdapter.createFromResource(this,
                R.array.spinner_unidades, android.R.layout.simple_spinner_item);

        spinnerUnidad.setAdapter(lista_unidades);

        spinnerUnidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                unidadString =  parent.getItemAtPosition(i).toString();

                if (spinnerUnidad.getSelectedItem().equals("Japan")){
                    spinnerGeneracion.setAdapter(lista_gen_jp);
                } else if (spinnerUnidad.getSelectedItem().equals("Indonesia")
                        || spinnerUnidad.getSelectedItem().equals("China")){
                    spinnerGeneracion.setAdapter(lista_gen_id_cn);
                } else if (spinnerUnidad.getSelectedItem().equals("English")){
                    spinnerGeneracion.setAdapter(lista_gen_en);
                } else if (spinnerUnidad.getSelectedItem().equals("Staff")) {
                    spinnerGeneracion.setAdapter(lista_gen_st);
                } else {
                    spinnerGeneracion.setAdapter(lista_gen_null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*** spinner de la generación ***/

        spinnerGeneracion = (Spinner) findViewById(R.id.spinnerGeneracion);

        spinnerGeneracion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                generacionString = parent.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        /*** Spinner del mes de cumpleaños ***/

        spinnerMesCum = (Spinner) findViewById(R.id.spinnerMesCum);

        ArrayAdapter<CharSequence> lista_meses = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.spinner_meses, android.R.layout.simple_spinner_item);

        spinnerMesCum.setAdapter(lista_meses);

        spinnerMesCum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                /*** lista de días, depende del mes ***/

                ArrayAdapter<CharSequence> lista_dias_31 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_dias_mesesCon31dias, android.R.layout.simple_spinner_item);

                ArrayAdapter<CharSequence> lista_dias_30 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_dias_mesesCon30dias, android.R.layout.simple_spinner_item);

                ArrayAdapter<CharSequence> lista_dias_29 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_dias_mesesCon29dias, android.R.layout.simple_spinner_item);

                mesCumString =  parent.getItemAtPosition(i).toString();

                if (spinnerMesCum.getSelectedItem().equals("Enero") ||
                        spinnerMesCum.getSelectedItem().equals("Marzo") ||
                        spinnerMesCum.getSelectedItem().equals("Mayo") ||
                        spinnerMesCum.getSelectedItem().equals("Julio") ||
                        spinnerMesCum.getSelectedItem().equals("Agosto") ||
                        spinnerMesCum.getSelectedItem().equals("Octubre") ||
                        spinnerMesCum.getSelectedItem().equals("Diciembre")) {

                    spinnerDiaCum.setAdapter(lista_dias_31);

                }else if (spinnerMesCum.getSelectedItem().equals("Abril") ||
                        spinnerMesCum.getSelectedItem().equals("Junio") ||
                        spinnerMesCum.getSelectedItem().equals("Septiembre") ||
                        spinnerMesCum.getSelectedItem().equals("Noviembre")){

                    spinnerDiaCum.setAdapter(lista_dias_30);

                }else{
                    spinnerDiaCum.setAdapter(lista_dias_29);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /*** Spinner del dia de cumpleaños ***/

        spinnerDiaCum = (Spinner) findViewById(R.id.spinnerDiaCum);

        spinnerDiaCum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                diaCumString =  parent.getItemAtPosition(i).toString();

                /*** Hacer fecha cumpleaños ***/

                fechaCum = diaCumString + "/" + mesCumString;

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /********************************* Campos de texto ****************************************/

        textoNombre = findViewById(R.id.textoNombre);
        textoApellido = findViewById(R.id.textoApellido);
        textoNombreOriginal = findViewById(R.id.textoNombreOriginal);
        textoAltura = findViewById(R.id.textoAltura);
        textoDisenador = findViewById(R.id.textoDisenador);
        textoBio = findViewById(R.id.textoBio);
        textoNickname = findViewById(R.id.textoNickname);


        /******************************** Coso para la fecha de debut *****************************/

        textoDebut = (EditText) findViewById(R.id.textoDebut);
        botonCalendario = (Button) findViewById(R.id.botonCalendario);

        botonCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == botonCalendario) {
                    final Calendar c = Calendar.getInstance();
                    dia = c.get(Calendar.DAY_OF_MONTH);
                    mes = c.get(Calendar.MONTH);
                    ano = c.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(AgregarIdol.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayofMonth) {

                            textoDebut.setText(dayofMonth + "/" + monthOfYear + "/" + year);
                        }
                    }
                            , dia, mes, ano);
                    datePickerDialog.show();
                }
            }
        });


        /************************************ Botón para guardar los cambios *********************************/

        botonTerminarAdd = findViewById(R.id.botonTerminarAdd);

        botonTerminarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textoNombre.getText().toString().equals("") ||
                        textoApellido.getText().toString().equals("") ||
                        spinnerEstado.getSelectedItem().toString().equals("Estado") ||
                        spinnerUnidad.getSelectedItem().toString().equals("Unidad")  ||
                        spinnerGeneracion.getSelectedItem().toString().equals("Generación") ||
                        textoDebut.getText().toString().equals("") ||
                        textoNickname.getText().toString().equals("") ||
                        spinnerDiaCum.getSelectedItem().toString().equals("Día de cumpleaños") ||
                        spinnerMesCum.getSelectedItem().toString().equals("Mes de cumpleaños")  ||
                        textoAltura.getText().toString().equals("") ||
                        textoDisenador.getText().toString().equals("") ||
                        textoBio.getText().toString().equals("")){

                    Toast.makeText(AgregarIdol.this, "Rellene todos los campos, por favor", Toast.LENGTH_LONG).show();

                } else{

                    DbIdols dbIdols = new DbIdols(AgregarIdol.this);

                    long id = dbIdols.insertarIdol(textoNombre.getText().toString() + " " + textoApellido.getText().toString(),
                            textoNombreOriginal.getText().toString(),
                            estadoString,
                            unidadString,
                            generacionString,
                            textoDebut.getText().toString(),
                            textoNickname.getText().toString(),
                            fechaCum,
                            textoAltura.getText().toString(),
                            textoDisenador.getText().toString(),
                            textoBio.getText().toString(),
                            imageInBytes);

                    if (id > 0) {
                        Toast.makeText(AgregarIdol.this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                        finish();
                        limpiar();
                    } else {
                        Toast.makeText(AgregarIdol.this, "XDDDDDDDDDDD", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


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
                guardarImagen = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imagenDefecto.setImageURI(path);
                ByteArrayOutputStream objectByOutputStream = new ByteArrayOutputStream();
                guardarImagen.compress(Bitmap.CompressFormat.JPEG, 100, objectByOutputStream);
                imageInBytes = objectByOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void limpiar() {
        textoNombre.setText("");
        textoApellido.setText("");
        textoNombreOriginal.setText("");
        textoAltura.setText("");
        textoBio.setText("");
        textoDisenador.setText("");
        textoNickname.setText("");
    }


}