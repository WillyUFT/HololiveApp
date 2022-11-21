package com.example.jololai.crud_idols;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jololai.R;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Idols;
import com.example.jololai.pantallas.PantallaPrincipal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VerIdolInfo extends AppCompatActivity {

    /*********** Declaramos los textos que solo señalan ******************/

    TextView tituloNombre, tituloNombreOriginal, tituloEstado, tituloUnidad;
    TextView tituloGeneracion, tituloDebut, tituloNickname, tituloCumple;
    TextView tituloAltura, tituloDisenador;

    /*********** Declaramos los textos que vamos a cambiar ******************/

    TextView textoNombre, textoNombreOriginal, textoEstado, textoUnidad;
    TextView textoGeneracion, textoDebut, textoNickname, textoCumple;
    TextView textoAltura, textoDisenador, textoBio, titulonDeArriba;

    /*********** Botones *************************/
    FloatingActionButton fabEliminar;

    /******** imagen ************/
    ImageView imagenIdol;

    Idols Idol;
    public int id = 0;

    String UsuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idol_info);



        /************************** Importamos todos los textos de arriba **************/
        
        /*** No editables (técnicamente sí, pero no lo vamos a hacer) ***/
        tituloNombre = findViewById(R.id.textoDetalleNombre);
        tituloNombreOriginal = findViewById(R.id.textoDetalleNombreOriginal);
        tituloEstado = findViewById(R.id.textoDetalleEstado);
        tituloUnidad = findViewById(R.id.textoDetalleUnidad);
        tituloGeneracion = findViewById(R.id.textoDetalleGeneracion);
        tituloDebut = findViewById(R.id.textoDetalleDebut);
        tituloNickname = findViewById(R.id.textoDetalleNickname);
        tituloCumple = findViewById(R.id.textoDetalleCumple);
        tituloAltura = findViewById(R.id.textoDetalleAltura);
        tituloDisenador = findViewById(R.id.textoDetalleDisenador);
        
        /*** Editables ***/
        
        textoNombre = findViewById(R.id.textoDetalleIdolVar);
        textoNombreOriginal = findViewById(R.id.textoDetalleNombreOriginalVar);
        textoEstado = findViewById(R.id.textoDetalleEstadoVar);
        textoUnidad = findViewById(R.id.textoDetalleUnidadVar);
        textoGeneracion = findViewById(R.id.textoDetalleGeneracionVar);
        textoDebut = findViewById(R.id.textoDetalleDebutVar);
        textoNickname = findViewById(R.id.textoDetalleNicknameVar);
        textoCumple = findViewById(R.id.textoDetalleCumpleVar);
        textoAltura = findViewById(R.id.textoDetalleAlturaVar);
        textoDisenador = findViewById(R.id.textoDetalleDisenadorVar);
        textoBio = findViewById(R.id.textoDetalleBioVar);
        
        /*** Botones ***/
        fabEliminar = findViewById(R.id.fabEliminar);

        /*** Imagen ***/
        imagenIdol = findViewById(R.id.imagenIdolDetalle);

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

        final DbIdols dbIdols = new DbIdols(VerIdolInfo.this);
        Idol = dbIdols.verIdol(id);

        if(Idol != null) {

            textoNombre.setText(Idol.getNombre());

            textoNombreOriginal.setText(Idol.getNombreOriginal());
            textoEstado.setText(Idol.getEstado());
            textoUnidad.setText(Idol.getUnidad());
            textoGeneracion.setText(Idol.getGeneracion());
            textoDebut.setText(Idol.getDebut());
            textoNickname.setText(Idol.getNickname());
            textoCumple.setText(Idol.getCumple());
            textoAltura.setText(Idol.getAltura());
            textoDisenador.setText(Idol.getDisenador());
            textoBio.setText(Idol.getBio());

            byte[] imagen = Idol.getImagen();
            if (imagen == null){
                imagenIdol.setImageResource(R.drawable.img_base);
                Log.e("Avatar inai", String.valueOf(Idol.getImagen()));
            } else {
                Bitmap bmp = BitmapFactory.decodeByteArray(imagen, 0 , imagen.length);
                imagenIdol.setImageBitmap(bmp);
                //imagenIdol.setRotation(90);
                Log.e("avatar", String.valueOf(Idol.getImagen()));
            }
        }

        fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerIdolInfo.this);
                builder.setMessage("¿Desea eliminar este Idol?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(dbIdols.eliminarIdol(id)){
                                    lista();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
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

            inflater.inflate(R.menu.menu_ver_idol, menu);

        } else {

            inflater.inflate(R.menu.menu_cerrar_sesion, menu);

        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.editarIdol:
                editarIdol();
                return true;

            case R.id.eliminarIdol:
                fabEliminar.callOnClick();
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
        Intent intent = new Intent(VerIdolInfo.this, PantallaPrincipal.class);
        startActivity(intent);

    }

    private void editarIdol() {

        Intent intent = new Intent(this, EditarIdol.class);
        intent.putExtra("ID", id);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);

    }

    private void lista(){
        Intent intent = new Intent(this, ListaDeIdols.class);
        intent.putExtra("SesionActual", UsuarioSesionActual);
        startActivity(intent);
    }
}



