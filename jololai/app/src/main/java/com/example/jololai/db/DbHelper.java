package com.example.jololai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 57;
    private static final String DATABASE_NOMBRE = "holoDexDB.db";
    public static final String TABLE_IDOLS = "t_miembros";
    public static final String TABLE_SONGS = "t_canciones";
    public static final String TABLE_USERS = "t_usuarios";
    public static final String TABLE_VIDEOS = "t_videos";
    public static final String TABLE_REP_VID = "t_rep_videos";
    public static final String TABLE_COM_CAN = "t_com_canciones";
    public static final String TABLE_CONCIERTO = "t_concierto";
    public static final String TABLE_COM_CON = "t_com_concierto";


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_IDOLS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "nombre_original TEXT NOT NULL," +
                "estado TEXT NOT NULL," +
                "unidad TEXT NOT NULL," +
                "generacion TEXT NOT NULL," +
                "debut TEXT NOT NULL," +
                "nickname TEXT NOT NULL," +
                "cumple TEXT NOT NULL," +
                "altura TEXT NOT NULL," +
                "disenador TEXT NOT NULL," +
                "bio TEXT NOT NULL," +
                "imagen BLOB)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_SONGS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "tipo_cancion TEXT NOT NULL," +
                "letra_cancion TEXT," +
                "imagen_cancion BLOB," +
                "id_idol INTEGER)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USERS + "(" +
                "usuario TEXT PRIMARY KEY," +
                "compras_realizadas," +
                "contraseña TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_VIDEOS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreVideo TEXT NOT NULL," +
                "genero_video TEXT NOT NULL," +
                "promocional TEXT NOT NULL," +
                "imagen_video BLOB," +
                "link TEXT NOT NULL," +
                "id_idol INTEGER)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_REP_VID + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre_usuario TEXT NOT NULL," +
                "id_video INTEGER," +
                "mes TEXT NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_COM_CAN + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre_usuario TEXT NOT NULL," +
                "id_cancion INTEGER," +
                "mes TEXT NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONCIERTO + "(" +
                "ID_Concierto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nombre_Concierto TEXT NOT NULL," +
                "ID_Idol INTEGER," +
                "Fecha_Concierto TEXT NOT NULL," +
                "Duracion_Minutos INTEGER)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_COM_CON + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre_usuario TEXT NOT NULL," +
                "id_concierto INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_IDOLS);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_SONGS);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_VIDEOS);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_REP_VID);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_COM_CAN);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_CONCIERTO);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_COM_CON);
        onCreate(sqLiteDatabase);

    }
}
