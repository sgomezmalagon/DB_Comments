package com.example.database_comments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private EditText etNombre, etContenido, etMostrar;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        etNombre = findViewById(R.id.etNombre);
        etContenido = findViewById(R.id.etContenido);
        etMostrar = findViewById(R.id.etMostrarComentario);
        spinner = findViewById(R.id.spinnerComentarios);

        refreshSpinner(); // Llenar el spinner al iniciar

        // Botón CREAR
        findViewById(R.id.btnCrear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = etNombre.getText().toString().trim();
                String con = etContenido.getText().toString().trim();
                if (!nom.isEmpty()) {
                    dbHelper.addComentario(nom, con);
                    refreshSpinner();
                    etNombre.setText("");
                    etContenido.setText("");
                    Toast.makeText(MainActivity.this, "Comentario guardado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Escribe un nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botón VEURE (Ver) usando el Cursor
        findViewById(R.id.btnVeure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = (Cursor) spinner.getSelectedItem();
                if (c != null) {
                    // Obtener contenido de la columna correspondiente
                    String contenido = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_CONTENIDO));
                    etMostrar.setText(contenido);
                }
            }
        });

        // Botón ELIMINAR
        findViewById(R.id.btnEliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = spinner.getSelectedItemId(); // Obtiene el _id del cursor
                if (id != -1) {
                    dbHelper.deleteComentario(id);
                    refreshSpinner();
                    etMostrar.setText("");
                    Toast.makeText(MainActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void refreshSpinner() {
        Cursor cursor = dbHelper.getAllComentarios();

        // Configuración del adaptador siguiendo el Sample 9.1
        String[] from = new String[]{DatabaseHelper.COL_NOMBRE};
        int[] to = new int[]{android.R.id.text1};

        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item, cursor, from, to, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

