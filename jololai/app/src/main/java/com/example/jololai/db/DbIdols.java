package com.example.jololai.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DebugUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Idols;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DbIdols extends DbHelper {

    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DbIdols(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    private void iniciarFirebase() {

        FirebaseApp.initializeApp(context.getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public long insertarIdol(String nombre,
                             String nombre_original,
                             String estado,
                             String unidad,
                             String generacion,
                             String debut,
                             String nickname,
                             String cumple,
                             String altura,
                             String disenador,
                             String bio,
                             byte[] imagen) {

        long id = 0;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("nombre_original", nombre_original);
            values.put("estado", estado);
            values.put("unidad", unidad);
            values.put("generacion", generacion);
            values.put("debut", debut);
            values.put("nickname", nickname);
            values.put("cumple", cumple);
            values.put("altura", altura);
            values.put("disenador", disenador);
            values.put("bio", bio);
            values.put("imagen", imagen);

            id = db.insert(TABLE_IDOLS, null, values);

            Cursor buscarId = db.rawQuery("SELECT * FROM " + TABLE_IDOLS + " ORDER BY 1 DESC LIMIT 1", null);

            iniciarFirebase();

            if (buscarId.moveToFirst()){
                do {
                    Idols idol = new Idols();
                    int idInt= buscarId.getInt(0);

                    idol.setId(idInt);
                    idol.setNombre(nombre);
                    idol.setNombreOriginal(nombre_original);
                    idol.setEstado(estado);
                    idol.setUnidad(unidad);
                    idol.setGeneracion(generacion);
                    idol.setDebut(debut);
                    idol.setNickname(nickname);
                    idol.setCumple(cumple);
                    idol.setAltura(altura);
                    idol.setDisenador(disenador);
                    idol.setBio(bio);
                    idol.setImagenString(String.valueOf(imagen));

                    databaseReference.child("idols").child(String.valueOf(idol.getId())).setValue(idol);

                } while (buscarId.moveToNext());
            }

        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }

    public void SubirIdols(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();

        Cursor recorrerBase = db.rawQuery("SELECT * FROM " + TABLE_IDOLS, null);

        if (recorrerBase.moveToFirst()){
            do {
                Idols idols = new Idols();
                int idInt = recorrerBase.getInt(0);
                idols.setId(idInt);

                String nombre = recorrerBase.getString(1);
                idols.setNombre(nombre);

                String nombreOriginal = recorrerBase.getString(2);
                idols.setNombreOriginal(nombreOriginal);

                String estado = recorrerBase.getString(3);
                idols.setEstado(estado);

                String unidad = recorrerBase.getString(4);
                idols.setUnidad(unidad);

                String generacio = recorrerBase.getString(5);
                idols.setGeneracion(generacio);

                String debut = recorrerBase.getString(6);
                idols.setDebut(debut);

                String nick = recorrerBase.getString(7);
                idols.setNickname(nick);

                String cumple = recorrerBase.getString(8);
                idols.setCumple(cumple);

                String altura = recorrerBase.getString(9);
                idols.setAltura(altura);

                String diseñador = recorrerBase.getString(10);
                idols.setDisenador(diseñador);

                String bio = recorrerBase.getString(11);
                idols.setBio(bio);

                idols.setImagenString(String.valueOf(recorrerBase.getBlob(12)));

                databaseReference.child("idols").child(String.valueOf(idols.getId())).setValue(idols);

            } while (recorrerBase.moveToNext());

        }

    }



    public ArrayList<Idols> mostrarIdols() {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Idols> listaIdols = new ArrayList<>();
        Idols idol;
        Cursor cursorIdols;


        cursorIdols = db.rawQuery("SELECT * FROM " + TABLE_IDOLS + " ORDER BY nombre ASC", null);

        if (cursorIdols.moveToFirst()) {
            do {
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
                idol.setImagen(cursorIdols.getBlob(12));
                listaIdols.add(idol);

            } while (cursorIdols.moveToNext());
        }

        cursorIdols.close();

        return listaIdols;
    }

    public Idols verIdol(int id) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Idols idol = null;
        Cursor cursorIdols;

        cursorIdols = db.rawQuery("SELECT * FROM " + TABLE_IDOLS + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorIdols.moveToFirst()) {
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
            idol.setImagen(cursorIdols.getBlob(12));
        }

        cursorIdols.close();

        return idol;
    }

    public boolean editarIdol(int id,
                              String nombre,
                              String nombre_original,
                              String estado,
                              String unidad,
                              String generacion,
                              String debut,
                              String nickname,
                              String cumple,
                              String altura,
                              String disenador,
                              String bio,
                              byte[] imagen) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();

        try {
            db.execSQL("UPDATE " + TABLE_IDOLS + " SET nombre = '" + nombre + "', nombre_original = '" + nombre_original + "'," +
                    " estado = '" + estado + "', unidad = '" + unidad + "'," +
                    " generacion = '" + generacion + "', debut = '" + debut + "'," +
                    " nickname = '" + nickname + "', cumple = '" + cumple + "'," +
                    " altura = '" + altura + "', disenador = '" + disenador + "'," +
                    " bio = '" + bio + "', imagen = '" + imagen + "' WHERE id='" + id + "' ");

            correcto = true;

            Idols idol = new Idols();

            idol.setId(id);
            idol.setNombre(nombre);
            idol.setNombreOriginal(nombre_original);
            idol.setEstado(estado);
            idol.setUnidad(unidad);
            idol.setGeneracion(generacion);
            idol.setDebut(debut);
            idol.setNickname(nickname);
            idol.setCumple(cumple);
            idol.setAltura(altura);
            idol.setDisenador(disenador);
            idol.setBio(bio);
            idol.setImagenString(String.valueOf(imagen));

            databaseReference.child("idols").child(String.valueOf(idol.getId())).setValue(idol);

        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }

    public boolean eliminarIdol(int id) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();

        try {
            db.execSQL("DELETE FROM " + TABLE_IDOLS + " WHERE id = '" + id + "'");
            correcto = true;

            String idString = String.valueOf(id);
            databaseReference.child("idols").child(idString).removeValue();

        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }
}
