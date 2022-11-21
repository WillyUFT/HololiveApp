package com.example.jololai.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.example.jololai.entidades.Idols;
import com.example.jololai.entidades.Usuarios;
import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Videos;
import com.example.jololai.entidades.ComCanciones;
import com.example.jololai.entidades.RepVideos;

import androidx.annotation.Nullable;


public class DbConsultas extends DbHelper{

    Context context;

    public DbConsultas(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public ComCanciones verMaximoSimp() {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ComCanciones comCanciones = null;

        Cursor simp = db.rawQuery("SELECT DISTINCT nombre_usuario, COUNT(*)" + " FROM " + TABLE_COM_CAN +
                " GROUP BY nombre_usuario ORDER BY count(*) DESC LIMIT 1", null);

        if (simp.moveToFirst()){

            comCanciones = new ComCanciones();
            comCanciones.setNombre_usuario(simp.getString(0));
        }

        simp.close();

        return comCanciones;

    }

    public Videos verVideoMaximo(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Videos Video = null;

        Cursor videoBrigido = db.rawQuery("SELECT nombreVideo, (SELECT COUNT (*) FROM " + TABLE_VIDEOS + " WHERE " + TABLE_VIDEOS + ".id = " + TABLE_REP_VID + ".id_video) AS total" +
                " FROM " + TABLE_REP_VID + " LEFT JOIN " + TABLE_VIDEOS + " ON " + TABLE_VIDEOS + ".id = " + TABLE_REP_VID + ".id_video" + " ORDER BY total DESC LIMIT 1", null);

        if (videoBrigido.moveToFirst()){

            Video = new Videos();
            Video.setNombreVideo(videoBrigido.getString(0));

        }

        videoBrigido.close();

        return Video;

    }

    public RepVideos verPersonaMirona(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        RepVideos repVideos = null;

        Cursor cursorSuscriptor = db.rawQuery("SELECT DISTINCT nombre_usuario, COUNT(*)" + " FROM " + TABLE_REP_VID +
                " GROUP BY nombre_usuario ORDER BY count(*) DESC LIMIT 1", null);

        if (cursorSuscriptor.moveToFirst()){

            repVideos = new RepVideos();
            repVideos.setNombre_usuario(cursorSuscriptor.getString(0));
        }

        cursorSuscriptor.close();

        return repVideos;

    }




}
