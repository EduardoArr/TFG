package com.example.gestordeinventario.Vista;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.widget.Button;

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


            btnMostrarDatos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FMostrarDatosIntent(null);

                }
            });

            btnRegistrarEqipo.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FRegistrarEquipoIntent(null);

                }
            }));

            btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FCerrarSesionIntent(null);
                }
            });

        } catch (Exception e) {
        e.printStackTrace();
        }
    }

    // Función que cambia al activity de Mostrar Datos
    private void FMostrarDatosIntent(View v){
        try {
            Intent intent = new Intent(this, MostrarDatosActivity.class);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Función que cambia al activity de Registrar Equipo
    private void FRegistrarEquipoIntent(View v){
        try {
            Intent intent = new Intent(this, RegistrarDatosActivity.class);
            intent.putExtra("REQUEST_EDICION", false);
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Función que cambia al activity de Registrar Equipo
    private void FCerrarSesionIntent(View v){
        try {
            Intent intent = new Intent(ScrollingActivity.this, IniciarSesionActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}