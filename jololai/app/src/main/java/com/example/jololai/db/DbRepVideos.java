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

public class DbRepVideos extends DbHelper {

    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DbRepVideos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    private void iniciarFirebase() {

        FirebaseApp.initializeApp(context.getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public long insertarRepVideo(String nombre_usuario,
                                 Integer id_video,
                                 String mes) {

        long id = 0;

        try {

            iniciarFirebase();

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre_usuario", nombre_usuario);
            values.put("id_video", id_video);
            values.put("mes", mes);

            id = db.insert(TABLE_REP_VID, null, values);

            Cursor buscarId = db.rawQuery("SELECT * FROM " + TABLE_REP_VID + " ORDER BY 1 DESC LIMIT 1", null);

            if (buscarId.moveToFirst()){
                do {
                    RepVideos repVideos = new RepVideos();
                    int idInt = buscarId.getInt(0);

                    repVideos.setId(idInt);
                    repVideos.setNombre_usuario(nombre_usuario);
                    repVideos.setId_video(id_video);
                    repVideos.setMes(mes);

                    databaseReference.child("reproduccion_videos").child(String.valueOf(repVideos.getId())).setValue(repVideos);

                } while (buscarId.moveToNext());
            }

        } catch (Exception ex) {

            ex.toString();
        }

        return id;

    }

    public ArrayList<RepVideos> mostrarRepVideos() {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<RepVideos> listaRepVideos = new ArrayList<>();
        RepVideos RepVideo;
        Cursor cursorRepVideo;

        cursorRepVideo = db.rawQuery("SELECT * FROM " + TABLE_REP_VID + " ORDER BY id ASC", null);

        if (cursorRepVideo.moveToFirst()) {
            do {

                RepVideo = new RepVideos();
                RepVideo.setId(cursorRepVideo.getInt(0));
                RepVideo.setNombre_usuario(cursorRepVideo.getString(1));
                RepVideo.setId_video(cursorRepVideo.getInt(2));
                RepVideo.setMes(cursorRepVideo.getString(3));

                listaRepVideos.add(RepVideo);

            } while (cursorRepVideo.moveToNext());
        }

        cursorRepVideo.close();

        return listaRepVideos;

    }

    public void SubirRepVideos(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();


        Cursor recorrerBase = db.rawQuery("SELECT * FROM " + TABLE_REP_VID, null);

        if (recorrerBase.moveToFirst()){
            do {
                RepVideos repVideos = new RepVideos();

                int idInt = recorrerBase.getInt(0);
                repVideos.setId(idInt);

                String nombre = recorrerBase.getString(1);
                repVideos.setNombre_usuario(nombre);

                int idvideo = recorrerBase.getInt(2);
                repVideos.setId_video(idvideo);

                String mes = recorrerBase.getString(3);
                repVideos.setMes(mes);

                databaseReference.child("reproduccion_videos").child(String.valueOf(repVideos.getId())).setValue(repVideos);

            } while (recorrerBase.moveToNext());
        }
    }

    public RepVideos verRepVideo(int id) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        RepVideos repVideos = null;
        Cursor cursorRepVideo;

        cursorRepVideo = db.rawQuery("SELECT * FROM " + TABLE_REP_VID + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorRepVideo.moveToFirst()) {

            repVideos = new RepVideos();
            repVideos.setId(cursorRepVideo.getInt(0));
            repVideos.setNombre_usuario(cursorRepVideo.getString(1));
            repVideos.setId_video(cursorRepVideo.getInt(2));
            repVideos.setMes(cursorRepVideo.getString(3));

        }

        cursorRepVideo.close();

        return repVideos;

    }

}