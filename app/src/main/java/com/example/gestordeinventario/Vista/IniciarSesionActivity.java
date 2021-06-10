package com.example.gestordeinventario.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gestordeinventario.Controlador.Usuario;
import com.example.gestordeinventario.R;

import java.util.HashMap;
import java.util.Map;

public class IniciarSesionActivity extends AppCompatActivity {

    EditText edtUsuario, edtPassw;
    Button btnLogin;
    Button btnRegistro;
    private final String UPLOAD_URL ="http://192.168.1.45/inventario/validar_usuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //SplashScreen cuando iniciamos el programa.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);


        edtUsuario = findViewById(R.id.edtUsuario);
        edtPassw = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.btnRegistrarse);

        btnLogin.setOnClickListener(v -> FvalidarUsuario());
        btnRegistro.setOnClickListener(v -> FIntentRegistro());
    }

    //Función que no cambia al formulario de registro cuando pulsamos en el botón de registrar.
    private void FIntentRegistro(){
        Intent intent = new Intent(IniciarSesionActivity.this, RegistroActivity.class);
        startActivity(intent);
    }

    //Función que comprueba que el usuario y la contraseña esta correcto y si está correcto nos muestra el formulario principal.
    private void FvalidarUsuario(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(IniciarSesionActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_LONG).show();
                }
            }
        }, error -> Toast.makeText(IniciarSesionActivity.this, error.toString(), Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() {
                //Recojo los datos de los edit text y los cargo en un HashMap como parámetros para comprobar que el usuario existe.
                Map<String, String> parametros = new HashMap<>();
                parametros.put("email", edtUsuario.getText().toString());
                parametros.put("password", edtPassw.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}