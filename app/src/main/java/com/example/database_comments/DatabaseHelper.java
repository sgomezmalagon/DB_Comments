package com.example.database_comments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Definición de constantes según el estilo de los apuntes
    private static final String DATABASE_NAME = "comentarios.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "comentarios";
    public static final String COL_ID = "_id"; // Necesario para CursorAdapters
    public static final String COL_NOMBRE = "nombre";
    public static final String COL_CONTENIDO = "contenido";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Uso de execSQL para crear la tabla
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOMBRE + " TEXT, " +
                COL_CONTENIDO + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Estrategia de actualizar borrando la tabla antigua
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Método para insertar usando ContentValues
    public void addComentario(String nombre, String contenido) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOMBRE, nombre);
        values.put(COL_CONTENIDO, contenido);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Método para obtener todos los comentarios usando rawQuery
    public Cursor getAllComentarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Se renombra el id si es necesario para el Spinner/Adapter
        return db.rawQuery("SELECT " + COL_ID + " AS _id, " + COL_NOMBRE + ", " + COL_CONTENIDO +
                            " FROM " + TABLE_NAME, null);
    }

    // Método para eliminar por ID usando delete()
    public void deleteComentario(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}

