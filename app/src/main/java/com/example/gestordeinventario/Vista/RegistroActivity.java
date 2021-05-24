package com.example.gestordeinventario.Vista;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gestordeinventario.R;

import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    //Variables
    EditText edtUsua;
    EditText edtPassw;
    EditText edtConfirmPass;
    Button btnRegistrarNuevo;
    ActionBar actionBar;

    private String UPLOAD_URL ="http://192.168.11.71/inventario/insertar_usuario.php";
    private String KEY_USUARIO = "email";
    private String KEY_CONTRASEÑA = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtUsua = findViewById(R.id.edtRegistrarUsuario);
        edtPassw = findViewById(R.id.edtRegistrarContra);
        btnRegistrarNuevo = findViewById(R.id.btnRegistrarNuevo);
        edtConfirmPass = findViewById(R.id.edtConfirmarContra);

        FActionBar();

        btnRegistrarNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FSubirUsuario();
            }
        });
    }


    //Función que sube los datos al servidor.
    private void FSubirUsuario(){

        if (FComprobarErroresRegistro()) {

            //Mostrar el diálogo de progreso
            final ProgressDialog loading = ProgressDialog.show(this,"REGISTRANDO...","Espere por favor...",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            //Descartar el diálogo de progreso
                            loading.dismiss();
                            //Mostrando el mensaje de la respuesta
                            Toast.makeText(RegistroActivity.this, s , Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Descartar el diálogo de progreso
                            loading.dismiss();

                            //Showing toast
                            Toast.makeText(RegistroActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    String email = edtUsua.getText().toString();
                    String password = edtPassw.getText().toString();


                    //Creación de parámetros
                    Map<String,String> params = new Hashtable<String, String>();

                    //Agregando de parámetros
                    params.put(KEY_USUARIO, email);
                    params.put(KEY_CONTRASEÑA, password);

                    //Parámetros de retorno
                    return params;
                }
            };

            //Creación de una cola de solicitudes
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //Agregar solicitud a la cola
            requestQueue.add(stringRequest);

        }

    }


    //Función que comprueba que el email cumpla con los requisitos
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    //Función para comprobar los errrores de registro.
    private boolean FComprobarErroresRegistro(){

        String confirmarPass = edtConfirmPass.getText().toString();
        String password = edtPassw.getText().toString();
        String email = edtUsua.getText().toString();

        if(!password.equals(confirmarPass)) {
            Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return false;
        }

        if(password.length() < 6 || confirmarPass.length() < 6){
            Toast.makeText(RegistroActivity.this, "La contraseña tiene que tener mínimo 6 dígitos", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!validarEmail(email)){
            Toast.makeText(RegistroActivity.this, "Email no valido", Toast.LENGTH_LONG).show();
            return false;
        }

        //sin errores
        return true;
    }


    //Función para cambiar el nombre del ActionBar y mostrar una opción de regreso al anterior activity
    public void FActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setTitle("Registrar Usuario Nuevo");
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