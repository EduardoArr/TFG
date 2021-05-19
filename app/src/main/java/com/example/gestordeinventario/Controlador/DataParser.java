package com.example.gestordeinventario.Controlador;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

    public class DataParser extends AsyncTask<Void,Void,Integer> {
        Context c;
        String jsonData;
        ListView listaEquipos;
        ProgressDialog pd;
        public static final String
                URL_IMAGENES = "http://192.168.1.41/inventario/imagenes/";
        ArrayList<Equipo> equipos =new ArrayList<>();
        public DataParser(Context c, String jsonData, ListView listaEquipos) {
            this.c = c;
            this.jsonData = jsonData;
            this.listaEquipos = listaEquipos;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(c);
            pd.setTitle("Parse");
            pd.setMessage("Analizando ... Por favor espere");
            pd.show();
        }
        @Override
        protected Integer doInBackground(Void... params) {
            return this.parseData();
        }
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            pd.dismiss();
            if(result==0)
            {
                Toast.makeText(c,"No se puede analizar", Toast.LENGTH_SHORT).show();
            }else {
                EquipoAdapter adapter=new EquipoAdapter(c, equipos);
                listaEquipos.setAdapter(adapter);
            }
        }
        private int parseData()
        {
            try
            {
                JSONArray ja=new JSONArray(jsonData);
                JSONObject jo=null;
                equipos.clear();
                Equipo equipo;
                for(int i=0;i<ja.length();i++)
                {
                    jo=ja.getJSONObject(i);
                    String tipo =jo.getString("tipo");
                    String id_equipo = jo.getString("id_equipo");
                    String edificio =jo.getString("edificio");
                    String aula =jo.getString("aula");
                    String puesto =jo.getString("puesto");
                    String fecha_captura =jo.getString("fecha_captura");
                    String imagen=jo.getString("foto");

                    equipo =new Equipo();

                    equipo.setEdificio(edificio);
                    equipo.setCodigo(id_equipo);
                    equipo.setAula(aula);
                    equipo.setPuesto(puesto);
                    equipo.setTipo(tipo);
                    equipo.setFecha(fecha_captura);
                    equipo.setUrl(imagen);
                    equipos.add(equipo);
                }
                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

