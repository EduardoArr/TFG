package com.example.gestordeinventario.Vista;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;

import com.example.gestordeinventario.Controlador.Usuario;
import com.example.gestordeinventario.R;

public class ScrollingActivity extends AppCompatActivity {

    //Variables
    Button btnCerrarSesion;
    Button btnRegistrarEqipo;
    Button btnMostrarDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scrolling);

            btnMostrarDatos = findViewById(R.id.btnMostrarDatos);
            btnRegistrarEqipo = findViewById(R.id.btnRegistrarEquipo);
            btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            btnMostrarDatos.setOnClickListener(v -> FMostrarDatosIntent());

            btnRegistrarEqipo.setOnClickListener((v -> FRegistrarEquipoIntent()));

            btnCerrarSesion.setOnClickListener(v -> FCerrarSesionIntent());

        } catch (Exception e) {
        e.printStackTrace();
        }
    }

    // Función que cambia al activity de Mostrar Datos
    private void FMostrarDatosIntent(){
        try {
            Intent intent = new Intent(this, MostrarDatosActivity.class);
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Función que cambia al activity de Registrar Equipo
    private void FRegistrarEquipoIntent(){
        try {
            Intent intent = new Intent(this, RegistrarDatosActivity.class);
            intent.putExtra("REQUEST_EDICION", false);
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Función que cambia al activity de Registrar Equipo
    private void FCerrarSesionIntent(){
        try {
            Intent intent = new Intent(ScrollingActivity.this, IniciarSesionActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}