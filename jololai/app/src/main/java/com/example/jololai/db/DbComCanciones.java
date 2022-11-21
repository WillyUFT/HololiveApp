package com.example.jololai.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.jololai.entidades.ComCanciones;
import com.example.jololai.entidades.RepVideos;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DbComCanciones extends DbHelper {

    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DbComCanciones(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    private void iniciarFirebase() {

        FirebaseApp.initializeApp(context.getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public long insertarComCancion(String nombre_usuario,
                                 Integer id_cancion,
                                 String mes) {

        long id = 0;

        try {

            iniciarFirebase();

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre_usuario", nombre_usuario);
            values.put("id_cancion", id_cancion);
            values.put("mes", mes);

            id = db.insert(TABLE_COM_CAN, null, values);

            Cursor buscarId = db.rawQuery("SELECT * FROM " + TABLE_COM_CAN + " ORDER BY 1 DESC LIMIT 1", null);

            if (buscarId.moveToFirst()){
                do {
                    ComCanciones comCanciones = new ComCanciones();
                    int idInt = buscarId.getInt(0);

                    comCanciones.setId(idInt);
                    comCanciones.setNombre_usuario(nombre_usuario);
                    comCanciones.setId_cancion(id_cancion);
                    comCanciones.setMes(mes);

                    databaseReference.child("compra_canciones").child(String.valueOf(comCanciones.getId())).setValue(comCanciones);

                } while (buscarId.moveToNext());
            }

        } catch (Exception ex) {

            ex.toString();
        }

        return id;

    }

    public void SubirComprasCanciones(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();


        Cursor recorrerBase = db.rawQuery("SELECT * FROM " + TABLE_COM_CAN, null);

        if (recorrerBase.moveToFirst()){
            do {
                ComCanciones comCanciones = new ComCanciones();

                int idInt = recorrerBase.getInt(0);
                comCanciones.setId(idInt);

                String nombre = recorrerBase.getString(1);
                comCanciones.setNombre_usuario(nombre);

                int idCanciones = recorrerBase.getInt(2);
                comCanciones.setId_cancion(idCanciones);

                String mes = recorrerBase.getString(3);
                comCanciones.setMes(mes);

                databaseReference.child("compras_canciones").child(String.valueOf(comCanciones.getId())).setValue(comCanciones);

            } while (recorrerBase.moveToNext());
        }
    }

    public ArrayList<ComCanciones> mostrarComCanciones() {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<ComCanciones> listaComCanciones = new ArrayList<>();
        ComCanciones ComCancion;
        Cursor cursorComCancion;

        cursorComCancion = db.rawQuery("SELECT * FROM " + TABLE_COM_CAN + " ORDER BY id ASC", null);

        if (cursorComCancion.moveToFirst()) {
            do {

                ComCancion = new ComCanciones();
                ComCancion.setId(cursorComCancion.getInt(0));
                ComCancion.setNombre_usuario(cursorComCancion.getString(1));
                ComCancion.setId_cancion(cursorComCancion.getInt(2));
                ComCancion.setMes(cursorComCancion.getString(3));

                listaComCanciones.add(ComCancion);

            } while (cursorComCancion.moveToNext());
        }

        cursorComCancion.close();

        return listaComCanciones;

    }

    public ComCanciones verComCancion(int id) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ComCanciones ComCanciones = null;
        Cursor cursorComCancion;

        cursorComCancion = db.rawQuery("SELECT * FROM " + TABLE_COM_CAN + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorComCancion.moveToFirst()) {

            ComCanciones = new ComCanciones();
            ComCanciones.setId(cursorComCancion.getInt(0));
            ComCanciones.setNombre_usuario(cursorComCancion.getString(1));
            ComCanciones.setId_cancion(cursorComCancion.getInt(2));
            ComCanciones.setMes(cursorComCancion.getString(3));

        }

        cursorComCancion.close();

        return ComCanciones;

    }

}

/*********** Esto posiblemente no se ocupe, al final son registros, pero igual los dejamos
 * para futuras referencias *****************/

/**************************

 public  boolean editarComCancion(int id,
 String nombre_usuario,
 Integer id_cancion,
 String mes){

 boolean correcto = false;

 DbHelper dbHelper = new DbHelper(context);
 SQLiteDatabase db = dbHelper.getWritableDatabase();

 try {

 db.execSQL("UPDATE " + TABLE_COM_CAN + " SET nombre_usuario '" + nombre_usuario + "'," +
 " id_cancion = '" + id_cancion + "', mes = '" + mes + "' WHERE id = ' " + id + "' ");

 correcto = true;

 } catch (Exception ex) {

 ex.toString();
 correcto = false;

 } finally {

 db.close();

 }

 return correcto;

 }

 public boolean eliminarComCancion(int id){

 boolean correcto = false;

 DbHelper dbHelper = new DbHelper(context);
 SQLiteDatabase db = dbHelper.getWritableDatabase();

 try {

 db.execSQL("DELETE FROM " + TABLE_COM_CAN + " WHERE id = ' " + id + "'");
 correcto = true;
 } catch (Exception ex) {

 ex.toString();
 correcto = false;

 } finally {

 db.close();

 }

 return correcto;

 }
 ********************/