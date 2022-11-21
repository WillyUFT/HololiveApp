package com.example.jololai.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Videos;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DbVideos extends DbHelper {

    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DbVideos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    private void iniciarFirebase() {

        FirebaseApp.initializeApp(context.getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public long insertarVideo(String nombreVideo,
                              String genero_video,
                              String promocional,
                              byte[] imagen_video,
                              String link,
                              Integer id_idol) {

        long id = 0;

        try {

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombreVideo", nombreVideo);
            values.put("genero_video", genero_video);
            values.put("promocional", promocional);
            values.put("imagen_video", imagen_video);
            values.put("link", link);
            values.put("id_idol", id_idol);

            id = db.insert(TABLE_VIDEOS, null, values);

            Cursor buscarId = db.rawQuery("SELECT * FROM " + TABLE_VIDEOS + " ORDER BY 1 DESC LIMIT 1", null);

            iniciarFirebase();

            if (buscarId.moveToFirst()){
                do {
                    Videos video = new Videos();
                    int idInt = buscarId.getInt(0);

                    video.setId(idInt);
                    video.setNombreVideo(nombreVideo);
                    video.setGenero_video(genero_video);
                    video.setPromocional(promocional);
                    video.setImagenVideoString(String.valueOf(imagen_video));
                    video.setLink(link);
                    video.setId_idol(id_idol);

                    databaseReference.child("videos").child(String.valueOf(video.getId())).setValue(video);

                } while (buscarId.moveToNext());
            }



        } catch (Exception ex) {
            ex.toString();
        }

        return id;

    }

    public ArrayList<Videos> mostrarVideos() {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Videos> listaVideos = new ArrayList<>();
        Videos video;
        Cursor cursorVideo;


        cursorVideo = db.rawQuery("SELECT * FROM " + TABLE_VIDEOS + " ORDER BY nombreVideo ASC", null);

        if (cursorVideo.moveToFirst()) {
            do {
                video = new Videos();
                video.setId(cursorVideo.getInt(0));
                video.setNombreVideo(cursorVideo.getString(1));
                video.setGenero_video(cursorVideo.getString(2));
                video.setPromocional(cursorVideo.getString(3));
                video.setImagen_video(cursorVideo.getBlob(4));
                video.setLink(cursorVideo.getString(5));
                video.setId_idol(cursorVideo.getInt(6));

                listaVideos.add(video);

            } while (cursorVideo.moveToNext());
        }

        cursorVideo.close();

        return listaVideos;
    }

    public void SubirVideos(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();


        Cursor recorrerBase = db.rawQuery("SELECT * FROM " + TABLE_VIDEOS, null);

        if (recorrerBase.moveToFirst()){
            do {
                Videos videos = new Videos();
                int idInt = recorrerBase.getInt(0);
                videos.setId(idInt);

                String nombre = recorrerBase.getString(1);
                videos.setNombreVideo(nombre);

                String genero = recorrerBase.getString(2);
                videos.setGenero_video(genero);

                String promocional = recorrerBase.getString(3);
                videos.setPromocional(promocional);

                videos.setImagenVideoString(String.valueOf(recorrerBase.getBlob(4)));

                String link = recorrerBase.getString(5);
                videos.setLink(link);

                int idIdol = recorrerBase.getInt(6);
                videos.setId_idol(idIdol);

                databaseReference.child("videos").child(String.valueOf(videos.getId())).setValue(videos);

            } while (recorrerBase.moveToNext());

        }

    }

    public Videos verVideo(int id) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Videos video = null;
        Cursor cursorVideos;

        cursorVideos = db.rawQuery("SELECT * FROM " + TABLE_VIDEOS + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorVideos.moveToFirst()) {
            video = new Videos();
            video.setId(cursorVideos.getInt(0));
            video.setNombreVideo(cursorVideos.getString(1));
            video.setGenero_video(cursorVideos.getString(2));
            video.setPromocional(cursorVideos.getString(3));
            video.setImagen_video(cursorVideos.getBlob(4));
            video.setLink(cursorVideos.getString(5));
            video.setId_idol(cursorVideos.getInt(6));
        }

        cursorVideos.close();

        return video;
    }

    public boolean editarVideo(int id,
                               String nombreVideo,
                               String genero_video,
                               String promocional,
                               byte[] imagen_video,
                               String link,
                               int id_Idol) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        iniciarFirebase();

        try {

            db.execSQL("UPDATE " + TABLE_VIDEOS + " SET nombreVideo = '" + nombreVideo + "', genero_video = '" + genero_video + "'," +
                    " promocional = '" + promocional + "', imagen_video = '" + imagen_video + "'," +
                    "link = '" + link + "', id_idol = '" + id_Idol + "' WHERE id = '" + id + "' ");

            correcto = true;

            Videos video = new Videos();

            video.setId(id);
            video.setNombreVideo(nombreVideo);
            video.setGenero_video(genero_video);
            video.setPromocional(promocional);
            video.setImagenVideoString(String.valueOf(imagen_video));
            video.setLink(link);
            video.setId_idol(id_Idol);

            databaseReference.child("videos").child(String.valueOf(video.getId())).setValue(video);

        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }

    public Boolean VerExisteVideo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VIDEOS + " WHERE id = ?", new String[] {String.valueOf(id)});
        if (cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        else {
            return false;
        }
    }

    public boolean eliminarVideo(int id) {

        boolean correcto = false;

        iniciarFirebase();

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + TABLE_VIDEOS + " WHERE id = '" + id + "'");
            correcto = true;

            String idString = String.valueOf(id);
            databaseReference.child("videos").child(idString).removeValue();

        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }
}