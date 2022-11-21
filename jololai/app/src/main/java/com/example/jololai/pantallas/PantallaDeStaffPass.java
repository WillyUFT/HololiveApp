package com.example.jololai.pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jololai.R;

public class PantallaDeStaffPass extends AppCompatActivity {

    EditText contraseña;
    Button botonEntrar;
    TextView passStaff;
    String UsuarioSesionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_staff_pass);

        contraseña = findViewById(R.id.contraseñaStaff);
        botonEntrar = findViewById(R.id.botonContraseñaStaff);
        passStaff = findViewById(R.id.contraseñaStaff);

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




        botonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pass = contraseña.getText().toString();

                if(pass.equals("pekopekopeko")){

                    Intent intent = new Intent(PantallaDeStaffPass.this, PantallaPrincipalStaff.class);
                    startActivity(intent);
                    contraseña.setText("");

                } else {

                    Toast.makeText(PantallaDeStaffPass.this, "NO INTENTES ENTRAR DE NUEVO", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}