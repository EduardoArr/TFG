package com.example.gestordeinventario.Controlador;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.gestordeinventario.R;
import com.example.gestordeinventario.Vista.RegistrarDatosActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EquipoAdapter extends BaseAdapter {

        //Variables
        Context c;
        ArrayList<Equipo> equipos;
        LayoutInflater inflater;

        //Constructor
        public EquipoAdapter(Context c, ArrayList<Equipo> equipos) {
            this.c = c;
            this.equipos = equipos;
            inflater= (LayoutInflater)
                    c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return equipos.size();
        }
        @Override
        public Object getItem(int position) {
            return equipos.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.lista,parent,false);
            }
            //Asignamos a las variables la id de los textView del diseño
            TextView tipo = convertView.findViewById(R.id.txtTipo);
            TextView edificio = convertView.findViewById(R.id.txtEdificio);
            TextView aula = convertView.findViewById(R.id.txtAula);
            TextView puesto = convertView.findViewById(R.id.txtPuesto);
            ImageView img = convertView.findViewById(R.id.Imagen);
            ImageButton btnOpciones = convertView.findViewById(R.id.btn_opciones);


            //Cargamos dentro de los textView los datos almacenados en la clase Equipo.
            Equipo equipo = equipos.get(position);
            tipo.setText(equipo.getTipo());
            edificio.setText(equipo.getEdificio());
            aula.setText(equipo.getAula());
            puesto.setText(equipo.getPuesto());
            Picasso.get().load(equipo.getUrl()).into(img);

            //Almacenamos los datos en un String.
            String strtipo = equipo.getTipo();
            String id = equipo.getCodigo();
            String imagen = equipo.getUrl();
            String strAula = equipo.getAula();
            String strEdificio = equipo.getEdificio();
            String strPuesto = equipo.getPuesto();

            btnOpciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarOpcionesDialogo(imagen, id, strtipo, strAula, strEdificio, strPuesto);
                }
            });
            return convertView;
        }

    //Hacemos un método para mostrar el diálogo del botón de editar y borrar
    public void mostrarOpcionesDialogo(final String imagen, final String id, final String tipo, final String aula, final String edificio, final String puesto){

        //Creamos un array de Strings con las opciones que van a aparecer en el diálogo

        String[] opciones = {"Editar", "Eliminar"};

        //Creamos el AlertDialog con las opciones
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Selecciona una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //El which a 0 indica la primera opción que es Editar
                if(which == 0){
                    //Si clickamos en Editar vamos a la Actividad de Add_Libro para poder editar los datos
                    //le tenemos que mandar todos los datos que tiene ese libro para que los muestre
                    Intent intent = new Intent(c, RegistrarDatosActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("tipo", tipo);
                    intent.putExtra("puesto", puesto);
                    intent.putExtra("aula", aula);
                    intent.putExtra("edificio", edificio);


                    intent.putExtra("imagen", imagen);
                    //Añadimos otro dato para saber si viene de editar
                    intent.putExtra("REQUEST_EDICION", true);
                    c.startActivity(intent);
                }
                //Si which es 1 ha clickado en eliminar
                else if(which == 1){
                    //Creamos otro diálogo para ver si estamos seguros de borrarlo
                    AlertDialog.Builder eliminarDialogo = new AlertDialog.Builder(c);
                    eliminarDialogo.setTitle("Eliminarás un equipo");
                    eliminarDialogo.setMessage("¿Estás seguro de eliminarlo?");
                    eliminarDialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    eliminarDialogo.setNegativeButton("No", null);
                    eliminarDialogo.create().show();

                }
            }
        });

        //Creamos y mostramos el diálogo
        builder.create().show();

    }

    }



