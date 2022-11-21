package com.example.jololai.crud_usuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jololai.R;
import com.example.jololai.db.DbUsuarios;
import com.example.jololai.entidades.Usuarios;

import java.lang.reflect.Field;

public class EditarUsuario extends AppCompatActivity {


    /******** Declaramos las variables ****************/
    EditText textUsuarioEd, ContraseñaEd, reContraseñaEd;
    Button botonGuardarCambios;

    String usuarioSesionActual;
    boolean sesionActiva = false;

    boolean correcto = false;
    Usuarios Usuarios;

    public Integer comprasAlMomento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        textUsuarioEd = findViewById(R.id.TextoUsuarioEd);
        ContraseñaEd = findViewById(R.id.TextoContraseñaEd);
        reContraseñaEd = findViewById(R.id.TextoReIngresarContraseñaED);

        botonGuardarCambios = findViewById(R.id.BotonRegistrarseED);


        /*********** Invocar la base de datos *******************/

        final DbUsuarios dbUsuarios = new DbUsuarios(EditarUsuario.this);

        /******* esto es por el error ese que dice OMG WHY IT IS SO BIG????
         * (No cabe en la pantalla) *********/

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        String UsuarioRecupeado;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                UsuarioRecupeado = null;
            } else {
                UsuarioRecupeado = extras.getString("Usuariooo");
            }
        } else {
            UsuarioRecupeado = (String) savedInstanceState.getSerializable("Usuariooo");
        }

        Usuarios = dbUsuarios.verUsuarios(UsuarioRecupeado);

        comprasAlMomento = dbUsuarios.verUsuarios(UsuarioRecupeado).getCompras_realizadas();


        botonGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuarioString = textUsuarioEd.getText().toString();
                String contraseñaString = ContraseñaEd.getText().toString();
                String reContraseñaString = reContraseñaEd.getText().toString();

                if (usuarioString.equals("") ||
                        contraseñaString.equals("") ||
                        reContraseñaString.equals("")){

                    Toast.makeText(EditarUsuario.this, "LLENA TODO POR FAVOR", Toast.LENGTH_SHORT).show();

                } else {

                    if (contraseñaString.equals(reContraseñaString)){

                        correcto = dbUsuarios.editarUsuario(usuarioString,
                                comprasAlMomento,
                                    contraseñaString);

                        if (correcto) {

                            Toast.makeText(EditarUsuario.this, "Usuario modificado", Toast.LENGTH_LONG).show();
                            verUsuarioEditado(usuarioString);


                        } else {

                            Toast.makeText(EditarUsuario.this, "Algo falló padre, no sé qué, gomenasorry", Toast.LENGTH_LONG).show();
                        }

                    } else {

                        Toast.makeText(EditarUsuario.this, "Contraseñas no coinciden. Asegurese de que coincidan.", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            private void verUsuarioEditado(String Usuariooo){
                Intent intent = new Intent(EditarUsuario.this, VerUsuarioInfo.class);
                intent.putExtra("Usuariooo", Usuariooo);
                startActivity(intent);
            }

            public void inicioSesion(String usuarioActivo) {

                sesionActiva = true;
                usuarioSesionActual = usuarioActivo;

            }
        });
    }
}