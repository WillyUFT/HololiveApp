package com.example.jololai.db;

import static com.example.jololai.db.DbHelper.TABLE_USERS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jololai.entidades.RepVideos;
import com.example.jololai.entidades.Usuarios;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class DbUsuarios extends DbHelper {

    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DbUsuarios(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    private void iniciarFirebase() {

        FirebaseApp.initializeApp(context.getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public Boolean insertarUsuario(String usuario,
                             int compras_realizadas,
                             String contraseña) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("usuario", usuario);
        values.put("compras_realizadas", compras_realizadas);
        values.put("contraseña", contraseña);

        long resultado = db.insert(TABLE_USERS, null, values);

        iniciarFirebase();
        
        Usuarios usuarios = new Usuarios();
        usuarios.setUsuario(usuario);
        usuarios.setCompras_realizadas(0);
        usuarios.setContraseña(contraseña);
        databaseReference.child("usuarios").child(usuarios.getUsuario()).setValue(usuarios);

        if(resultado == -1){
            return false;
        } else {
            return true;
        }
    }
    public ArrayList<Usuarios> mostrarUsuarios() {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
        Usuarios usuario;
        Cursor cursorUsuarios;

        cursorUsuarios = db.rawQuery("SELECT * FROM " + TABLE_USERS + " ORDER BY usuario ASC", null);

        if (cursorUsuarios.moveToFirst()) {
            do {
                usuario = new Usuarios();
                usuario.setUsuario(cursorUsuarios.getString(0));
                usuario.setCompras_realizadas(cursorUsuarios.getInt(1));
                usuario.setContraseña(cursorUsuarios.getString(2));

                listaUsuarios.add(usuario);

            } while (cursorUsuarios.moveToNext());
        };

        cursorUsuarios.close();

        return listaUsuarios;
    }

    public void SubirUsuarios(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();


        Cursor recorrerBase = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (recorrerBase.moveToFirst()){
            do {
                Usuarios usuarios = new Usuarios();

                String nombre = recorrerBase.getString(0);
                usuarios.setUsuario(nombre);

                int idCompras = recorrerBase.getInt(1);
                usuarios.setCompras_realizadas(idCompras);

                String pass = recorrerBase.getString(2);
                usuarios.setContraseña(pass);

                databaseReference.child("usuarios").child(String.valueOf(usuarios.getUsuario())).setValue(usuarios);

            } while (recorrerBase.moveToNext());
        }
    }

    public Usuarios verUsuarios(String nombreUsuario) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Usuarios usuario = null;
        Cursor cursorUsuarios;

        cursorUsuarios = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE usuario = ?", new String[] {nombreUsuario});

        if (cursorUsuarios.moveToFirst()) {
            usuario = new Usuarios();
            usuario.setUsuario(cursorUsuarios.getString(0));
            usuario.setCompras_realizadas(cursorUsuarios.getInt(1));
            usuario.setContraseña(cursorUsuarios.getString(2));

        }

        cursorUsuarios.close();

        return usuario;
    }

    public Boolean VerExisteUsuario(String Usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE Usuario = ?", new String[] {Usuario});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean VerExisteContraseña(String Usuario, String Contraseña){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE Usuario = ? AND Contraseña = ?", new String[] {Usuario, Contraseña});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    };

    public boolean editarUsuario(String usuario,
                                 int compras_realizadas,
                                 String contraseña) {

        boolean correcto = false;
        String usuarioAux = usuario;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_USERS + " SET usuario ='" + usuarioAux + "', compras_realizadas = '" + compras_realizadas + "', contraseña = '" + contraseña +"' WHERE usuario = '" + usuario + "'");

            correcto = true;

            iniciarFirebase();

            Usuarios usuarios = new Usuarios();
            usuarios.setUsuario(usuarioAux);
            usuarios.setCompras_realizadas(compras_realizadas);
            usuarios.setContraseña(contraseña);
            databaseReference.child("usuarios").child(usuarios.getUsuario()).setValue(usuarios);

        } catch (Exception ex) {
            ex.toString();
            Log.e("error de la wea", String.valueOf(ex));
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }

    public boolean eliminarUsuario(String usuario) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();

        try {
            db.execSQL("DELETE FROM " + TABLE_USERS + " WHERE usuario = '" + usuario + "'");
            correcto = true;

            Log.e("eliminao", usuario);
            databaseReference.child("usuarios").child(usuario).removeValue();

        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }

}
