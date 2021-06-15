package com.example.gestordeinventario.Vista;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gestordeinventario.Controlador.DescargaImagen;
import com.example.gestordeinventario.Controlador.Usuario;
import com.example.gestordeinventario.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class MostrarDatosActivity extends AppCompatActivity {

    //Variables
    ActionBar actionBar;
    final static String
    URL_WEB_SERVICE="http://192.168.43.68/inventario/";
    String tipo;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos);
        FloatingActionButton fab = findViewById(R.id.fab);
        lv = findViewById(R.id.lvEquipos);

        fab.setOnClickListener(v -> FFiltrarDatosTipo());
        //Llamamos a la función del ActionBar
        FActionBar();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mostrardatos, menu);

        MenuItem item = menu.findItem(R.id.spinnerMenu);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arrayFiltro, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipo = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }

    //Filtramos los equipos dependiendo de su tipo
    private void FFiltrarDatosTipo(){
    try {
        if(tipo.equals("Todo")){
            new DescargaImagen(MostrarDatosActivity.this,URL_WEB_SERVICE + "lista_equipos.php",lv).execute();
        }else if(tipo.equals("Monitor")){
            new DescargaImagen(MostrarDatosActivity.this,URL_WEB_SERVICE + "filtrar_monitor.php",lv).execute();
        }else if (tipo.equals("Canon")){
            new DescargaImagen(MostrarDatosActivity.this,URL_WEB_SERVICE + "filtrar_canon.php",lv).execute();
        }else if (tipo.equals("Teclado")){
            new DescargaImagen(MostrarDatosActivity.this,URL_WEB_SERVICE + "filtrar_teclado.php",lv).execute();
        }else if (tipo.equals("Ordenador")){
            new DescargaImagen(MostrarDatosActivity.this,URL_WEB_SERVICE + "filtrar_ordenador.php",lv).execute();
        }else if (tipo.equals("Raton")){
            new DescargaImagen(MostrarDatosActivity.this,URL_WEB_SERVICE + "filtrar_raton.php",lv).execute();
        }
    }catch (Exception e){
        //Si pulsamos el botón sin seleccionar ningún filtro, nos mostrá todos los equipos.
        new DescargaImagen(MostrarDatosActivity.this,URL_WEB_SERVICE + "lista_equipos.php",lv).execute();
    }
    }
    //Función para cambiar el nombre del ActionBar y mostrar una opción de regreso al anterior activity
    public void FActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setTitle("Equipos");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    //Función para que desde el ActionBar te vaya a la actividad anterior
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}