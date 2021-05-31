package com.example.gestordeinventario.Vista;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.SearchView;


import com.example.gestordeinventario.Controlador.DescargaImagen;
import com.example.gestordeinventario.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MostrarDatosActivity extends AppCompatActivity {

    //Variables
    ActionBar actionBar;
    final static String
    URL_WEB_SERVICE="http://192.168.1.42/inventario/lista_equipos.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos);
        FloatingActionButton fab = findViewById(R.id.fab);

        final ListView lv = findViewById(R.id.lvEquipos);
        fab.setOnClickListener(view -> new DescargaImagen(MostrarDatosActivity.this,URL_WEB_SERVICE,lv).execute());

        //Llamamos a la función del ActionBar
        FActionBar();
    }


    //Función que muestra las opciones del menú
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mostrardatos, menu);

        //Asociamos el SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
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