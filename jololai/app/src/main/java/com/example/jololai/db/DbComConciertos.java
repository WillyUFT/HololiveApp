package com.example.jololai.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.jololai.entidades.ComConcierto;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DbComConciertos extends DbHelper {

    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DbComConciertos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    private void iniciarFirebase() {

        FirebaseApp.initializeApp(context.getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public long insertarComConcierto(String nombre_usuario,
                                   Integer id_concierto) {

        long id = 0;

        try {

            iniciarFirebase();

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre_usuario", nombre_usuario);
            values.put("id_cancion", id_concierto);


            id = db.insert(TABLE_COM_CON, null, values);

            Cursor buscarId = db.rawQuery("SELECT * FROM " + TABLE_COM_CON + " ORDER BY 1 DESC LIMIT 1", null);

            if (buscarId.moveToFirst()){
                do {
                    ComConcierto comConciertos = new ComConcierto();
                    int idInt = buscarId.getInt(0);

                    comConciertos.setId(idInt);
                    comConciertos.setNombre_usuario(nombre_usuario);
                    comConciertos.setId_concierto(id_concierto);

                    databaseReference.child("compra_conciertos").child(String.valueOf(comConciertos.getId())).setValue(comConciertos);

                } while (buscarId.moveToNext());
            }

        } catch (Exception ex) {

            ex.toString();
        }

        return id;

    }

    public ArrayList<ComConcierto> mostrarComConciertos() {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<ComConcierto> listaComConciertos = new ArrayList<>();
        ComConcierto comConcierto;
        Cursor cursorComCancion;

        cursorComCancion = db.rawQuery("SELECT * FROM " + TABLE_COM_CON + " ORDER BY id ASC", null);

        if (cursorComCancion.moveToFirst()) {
            do {

                comConcierto = new ComConcierto();
                comConcierto.setId(cursorComCancion.getInt(0));
                comConcierto.setNombre_usuario(cursorComCancion.getString(1));

                listaComConciertos.add(comConcierto);

            } while (cursorComCancion.moveToNext());
        }

        cursorComCancion.close();

        return listaComConciertos;

    }

    public ComConcierto verComCancion(int id) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ComConcierto ComConciertos = null;
        Cursor cursorComCancion;

        cursorComCancion = db.rawQuery("SELECT * FROM " + TABLE_COM_CON + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorComCancion.moveToFirst()) {

            ComConciertos = new ComConcierto();
            ComConciertos.setId(cursorComCancion.getInt(0));
            ComConciertos.setNombre_usuario(cursorComCancion.getString(1));
            ComConciertos.setId_concierto(cursorComCancion.getInt(2));

        }

        cursorComCancion.close();

        return ComConciertos;

    }

}
