package com.example.jololai.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Usuarios;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DbCanciones extends DbHelper {

    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DbCanciones(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    private void iniciarFirebase() {

        FirebaseApp.initializeApp(context.getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public long insertarCancion(String nombre,
                                String tipo_cancion,
                                String letra_cancion,
                                byte[] imagen_cancion,
                                Integer id_idol){

        long id = 0;

        try{

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("tipo_cancion", tipo_cancion);
            values.put("letra_cancion", letra_cancion);
            values.put("imagen_cancion", imagen_cancion);
            values.put("id_idol", id_idol);

            id = db.insert(TABLE_SONGS, null, values);

            Cursor awa = db.rawQuery("SELECT * FROM " + TABLE_SONGS + " ORDER BY 1 DESC LIMIT 1", null);

            iniciarFirebase();

            if(awa.moveToFirst()){
                do {
                    Canciones cancion = new Canciones();
                    int idInt = awa.getInt(0);

                    cancion.setId(idInt);
                    Log.e("ID DE LA CANCIÓN CTM", String.valueOf(idInt));

                    cancion.setNombre(nombre);
                    Log.e("Nombre cancion", nombre);

                    cancion.setTipo_cancion(tipo_cancion);
                    Log.e("tipo cancion", tipo_cancion);

                    cancion.setLetra_cancion(letra_cancion);
                    Log.e("letra cancion", letra_cancion);

                    cancion.setImagen_cancionString(String.valueOf(imagen_cancion));
                    cancion.setId_idol(id_idol);

                    Log.e("idol id", String.valueOf(id_idol));

                    databaseReference.child("canciones").child(String.valueOf(cancion.getId())).setValue(cancion);

                } while (awa.moveToNext());
            }

        } catch (Exception ex) {
            ex.toString();
        }

        return id;

    }

    public void SubirCanciones(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();


        Cursor recorrerBase = db.rawQuery("SELECT * FROM " + TABLE_SONGS, null);

        Log.e("aaa", String.valueOf(recorrerBase));

        if (recorrerBase.moveToFirst()){
            do {
                Canciones cancion = new Canciones();
                int idInt = recorrerBase.getInt(0);
                cancion.setId(idInt);

                String nombre = recorrerBase.getString(1);
                cancion.setNombre(nombre);
                Log.e("Nombre cancion", nombre);

                String tipo_cancion = recorrerBase.getString(2);
                cancion.setTipo_cancion(tipo_cancion);
                Log.e("tipo cancion", tipo_cancion);

                String letra_cancion = recorrerBase.getString(3);
                cancion.setLetra_cancion(letra_cancion);
                Log.e("letra cancion", letra_cancion);

                cancion.setImagen_cancionString(String.valueOf(recorrerBase.getBlob(4)));

                int IdIdol = recorrerBase.getInt(5);
                cancion.setId_idol(IdIdol);
                Log.e("idol id", String.valueOf(IdIdol));

                databaseReference.child("canciones").child(String.valueOf(cancion.getId())).setValue(cancion);

            } while (recorrerBase.moveToNext());

        }

    }

    public Boolean VerExisteCancion(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SONGS + " WHERE id = ?", new String[] {String.valueOf(id)});
        if (cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<Canciones> mostrarCanciones() {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Canciones> listaCanciones = new ArrayList<>();
        Canciones cancion;
        Cursor cursorCancion;


        cursorCancion = db.rawQuery("SELECT * FROM " + TABLE_SONGS + " ORDER BY nombre ASC", null);

        if (cursorCancion.moveToFirst()) {
            do {
                cancion = new Canciones();
                cancion.setId(cursorCancion.getInt(0));
                cancion.setNombre(cursorCancion.getString(1));
                cancion.setTipo_cancion(cursorCancion.getString(2));
                cancion.setLetra_cancion(cursorCancion.getString(3));
                cancion.setImagen_cancion(cursorCancion.getBlob(4));
                cancion.setId_idol(cursorCancion.getInt(5));

                listaCanciones.add(cancion);

            } while (cursorCancion.moveToNext());
        }

        cursorCancion.close();

        return listaCanciones;
    }

    public Canciones verCancion(int id) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Canciones cancion = null;
        Cursor cursorCanciones;

        cursorCanciones = db.rawQuery("SELECT * FROM " + TABLE_SONGS + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorCanciones.moveToFirst()) {
            cancion = new Canciones();
            cancion.setId(cursorCanciones.getInt(0));
            cancion.setNombre(cursorCanciones.getString(1));
            cancion.setTipo_cancion(cursorCanciones.getString(2));
            cancion.setLetra_cancion(cursorCanciones.getString(3));
            cancion.setImagen_cancion(cursorCanciones.getBlob(4));
            cancion.setId_idol(cursorCanciones.getInt(5));
        }

        cursorCanciones.close();

        return cancion;
    }

    public boolean editarCancion(int id,
                              String nombre,
                              String tipo_cancion,
                              String letra_cancion,
                              byte[] imagen_cancion,
                             int id_Idol) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();

        try {
            db.execSQL("UPDATE " + TABLE_SONGS + " SET nombre = '" + nombre + "', tipo_cancion = '" + tipo_cancion + "'," +
                    " letra_cancion = '" + letra_cancion + "', imagen_cancion = '" + imagen_cancion + "'," +
                    "id_idol = '" + id_Idol + "' WHERE id = '" + id + "' ");

            correcto = true;

            Canciones cancion = new Canciones();

            cancion.setId(id);
            Log.e("id cancion", String.valueOf(id));

            cancion.setNombre(nombre);
            Log.e("Nombre cancion", nombre);

            cancion.setTipo_cancion(tipo_cancion);
            Log.e("tipo cancion", tipo_cancion);

            cancion.setLetra_cancion(letra_cancion);
            Log.e("letra cancion", letra_cancion);

            cancion.setImagen_cancionString(String.valueOf(imagen_cancion));
            Log.e("imagen cancion", String.valueOf(imagen_cancion));

            cancion.setId_idol(id_Idol);
            Log.e("idol id", String.valueOf(id_Idol));

            databaseReference.child("canciones").child(String.valueOf(cancion.getId())).setValue(cancion);

        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }

    public boolean eliminarCancion(int id) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();

        try {
            db.execSQL("DELETE FROM " + TABLE_SONGS + " WHERE id = '" + id + "'");
            correcto = true;

            String idString = String.valueOf(id);
            databaseReference.child("canciones").child(idString).removeValue();

        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }


}
