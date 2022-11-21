package com.example.jololai.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Conciertos;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DbConciertos extends DbHelper {

    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DbConciertos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    private void iniciarFirebase() {

        FirebaseApp.initializeApp(context.getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public long insertarConcierto(String Nombre_Concierto,
                                  Integer ID_Idol,
                                  String Fecha_Concierto,
                                  Integer Duracion_Minutos){

        long id = 0;

        try{

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("Nombre_Concierto", Nombre_Concierto);
            values.put("ID_Idol", ID_Idol);
            values.put("Fecha_Concierto", Fecha_Concierto);
            values.put("Duracion_Minutos", Duracion_Minutos);

            id = db.insert(TABLE_CONCIERTO, null, values);

            Cursor buscarId = db.rawQuery("SELECT * FROM " + TABLE_CONCIERTO + " ORDER BY 1 DESC LIMIT 1", null);

            iniciarFirebase();

            if (buscarId.moveToFirst()){
                do {
                    Conciertos conciertos = new Conciertos();
                    int idInt = buscarId.getInt(0);

                    conciertos.setID_Concierto(idInt);
                    conciertos.setNombre_Concierto(Nombre_Concierto);
                    conciertos.setID_Idol(ID_Idol);
                    conciertos.setFecha_Concierto(Fecha_Concierto);
                    conciertos.setDuracion_Minutos(Duracion_Minutos);

                    databaseReference.child("conciertos").child(String.valueOf(conciertos.getID_Concierto())).setValue(conciertos);

                } while (buscarId.moveToNext());
            }

        } catch (Exception ex) {
            ex.toString();
        }

        return id;

    }

    public void SubirConciertos(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();


        Cursor recorrerBase = db.rawQuery("SELECT * FROM " + TABLE_CONCIERTO, null);

        if (recorrerBase.moveToFirst()){
            do {
                Conciertos conciertos = new Conciertos();
                int idInt = recorrerBase.getInt(0);
                conciertos.setID_Concierto(idInt);

                String nombre = recorrerBase.getString(1);
                conciertos.setNombre_Concierto(nombre);

                int idIdol = recorrerBase.getInt(2);
                conciertos.setID_Idol(idIdol);

                String fecha = recorrerBase.getString(3);
                conciertos.setFecha_Concierto(fecha);

                int duracion = recorrerBase.getInt(4);
                conciertos.setDuracion_Minutos(duracion);

                databaseReference.child("conciertos").child(String.valueOf(conciertos.getID_Concierto())).setValue(conciertos);

            } while (recorrerBase.moveToNext());

        }

    }

    public ArrayList<Conciertos> mostrarConciertos() {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Conciertos> ListaConcierto = new ArrayList<>();
        Conciertos conciertos;
        Cursor cursorConcierto;


        cursorConcierto = db.rawQuery("SELECT * FROM " + TABLE_CONCIERTO + " ORDER BY ID_Idol ASC", null);

        if (cursorConcierto.moveToFirst()) {
            do {
                conciertos = new Conciertos();
                conciertos.setID_Concierto(cursorConcierto.getInt(0));
                conciertos.setNombre_Concierto(cursorConcierto.getString(1));
                conciertos.setID_Idol(cursorConcierto.getInt(2));
                conciertos.setFecha_Concierto(cursorConcierto.getString(3));
                conciertos.setDuracion_Minutos(cursorConcierto.getInt(4));

                ListaConcierto.add(conciertos);

            } while (cursorConcierto.moveToNext());
        }

        cursorConcierto.close();

        return ListaConcierto;
    }

    public Conciertos verConcierto(int ID_Concierto) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Conciertos conciertos = null;
        Cursor cursorConcierto;

        cursorConcierto = db.rawQuery("SELECT * FROM " + TABLE_CONCIERTO + " WHERE ID_Concierto = " + ID_Concierto + " LIMIT 1", null);

        if (cursorConcierto.moveToFirst()) {
            conciertos = new Conciertos();
            conciertos.setID_Concierto(cursorConcierto.getInt(0));
            conciertos.setNombre_Concierto(cursorConcierto.getString(1));
            conciertos.setID_Idol(cursorConcierto.getInt(2));
            conciertos.setFecha_Concierto(cursorConcierto.getString(3));
            conciertos.setDuracion_Minutos(cursorConcierto.getInt(4));
        }

        cursorConcierto.close();

        return conciertos;
    }

    public boolean editarConcierto(int ID_Concierto,
                                   String Nombre_Concierto,
                                   int ID_Idol,
                                   String Fecha_Concierto,
                                   int Duracion_Minutos) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_CONCIERTO + " SET ID_Concierto = '" + ID_Concierto + "', Nombre_Concierto = '" + Nombre_Concierto + "'," +
                    " ID_Idol = '" + ID_Idol + "', Fecha_Concierto = '" + Fecha_Concierto + "'," +
                    "Duracion_Minutos = '" + Duracion_Minutos + "' WHERE ID_Concierto = '" + ID_Concierto + "' ");

            correcto = true;

        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }

    public boolean eliminarConcierto(int ID_Concierto) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + TABLE_CONCIERTO + " WHERE ID_Concierto = '" + ID_Concierto + "'");

            String idString = String.valueOf(ID_Concierto);
            databaseReference.child("concierto").child(idString).removeValue();

            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }


}
