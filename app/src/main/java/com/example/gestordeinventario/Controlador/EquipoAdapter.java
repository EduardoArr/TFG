package com.example.gestordeinventario.Controlador;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gestordeinventario.R;
import com.example.gestordeinventario.Vista.MostrarDatosActivity;
import com.example.gestordeinventario.Vista.RegistrarDatosActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EquipoAdapter extends BaseAdapter{

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
            TextView edtID = convertView.findViewById(R.id.txtID);
            TextView fecha = convertView.findViewById(R.id.txtFecha);
            ImageView img = convertView.findViewById(R.id.Imagen);
            ImageButton btnOpciones = convertView.findViewById(R.id.btn_opciones);


            //Cargamos dentro de los textView los datos almacenados en la clase Equipo.
            Equipo equipo = equipos.get(position);
            edtID.setText(equipo.getCodigo());
            tipo.setText(equipo.getTipo());
            edificio.setText(equipo.getEdificio());
            aula.setText(equipo.getAula());
            fecha.setText(equipo.getFecha());
            puesto.setText(equipo.getPuesto());
            Picasso.get().load(equipo.getUrl()).into(img);

            //Almacenamos los datos en un String.
            final String strtipo = equipo.getTipo();
            final String id = edtID.getText().toString();
            final String strfecha = equipo.getFecha();
            final String imagen = equipo.getUrl();
            final String strAula = equipo.getAula();
            final String strEdificio = equipo.getEdificio();
            final String strPuesto = equipo.getPuesto();


            btnOpciones.setOnClickListener(v -> mostrarOpcionesDialogo("" + position, imagen, id, strtipo, strAula, strEdificio, strPuesto, strfecha));
            return convertView;
        }


    //Hacemos un método para mostrar el diálogo del botón de editar y borrar
    public void mostrarOpcionesDialogo(final String posicion, final String imagen, final String id_equipo, final String tipo, final String aula, final String edificio, final String puesto, final String fecha){

        //Creamos un array de Strings con las opciones que van a aparecer en el diálogo

        String[] opciones = {"Editar", "Eliminar"};

        //Creamos el AlertDialog con las opciones
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Selecciona una opción");
        builder.setItems(opciones, (dialog, which) -> {
            //El which a 0 indica la primera opción que es Editar
            if(which == 0){
                //Si clickamos en Editar vamos a la Actividad de Add_Libro para poder editar los datos
                //le tenemos que mandar todos los datos que tiene ese libro para que los muestre
                Intent intent = new Intent(c, RegistrarDatosActivity.class);
                intent.putExtra("id", id_equipo);
                intent.putExtra("tipo", tipo);
                intent.putExtra("puesto", puesto);
                intent.putExtra("aula", aula);
                intent.putExtra("edificio", edificio);
                intent.putExtra("fecha_captura", fecha);

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
                eliminarDialogo.setPositiveButton("Sí", (dialog1, which1) -> FBorrarEquipo(id_equipo, "http://192.168.1.45/inventario/borrar_equipo.php"));
                eliminarDialogo.setNegativeButton("No", null);
                eliminarDialogo.create().show();

            }
        });

        //Creamos y mostramos el diálogo
        builder.create().show();

    }

    //Función que comprueba que el usuario y la contraseña esta correcto y si está correcto nos muestra el formulario principal.
    private void FBorrarEquipo(final String id, String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(c, "EQUIPO ELIMINADO CORRECTAMENTE", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Recojo los datos de los edit text y los cargo en un HashMap como parámetros para comprobar que el usuario existe.
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id_equipo", id);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
    }



}



