package com.example.gestordeinventario.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gestordeinventario.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class RegistrarDatosActivity extends AppCompatActivity {

    //Variables
    ActionBar actionBar;
    private Button btnRegistrar;
    private ImageView imageView;
    private Spinner spTipo;
    private Spinner spEdificio;
    private Spinner spAula;
    private Spinner spPuesto;
    private String fecha_captura;
    Uri uri;
    boolean editar;
    String txt_tipo, txt_aula, txt_puesto, txt_edificio, txt_imagen, txt_id, txt_fecha;
    Bundle bundle;

    private Bitmap bitmap;

    //Declaramos códigos que nos servirán para comprobar permisos cámara y almacenamiento
    private static final int REQUEST_CAMERA_CODE = 1;
    private static final int REQUEST_STORAGE_CODE = 2;

    //Declaramos códigos que nos servirán para comprobar si la imagen viene de galería o de cámara
    private static final int PICK_CAMERA_CODE = 3;
    private final int PICK_IMAGE_REQUEST = 4;

    private final String UPLOAD_URL ="http://192.168.1.42/inventario/insertar_equipo.php";
    private final String UPLOAD_URL2 = "http://192.168.1.42/inventario/editar_equipo.php";
    private final String KEY_IMAGEN = "foto";
    private final String KEY_IDEQUIPO = "id_equipo";
    private final String KEY_TIPO = "tipo";
    private final String KEY_AULA = "aula";
    private final String KEY_EDIFICIO = "edificio";
    private final String KEY_PUESTO = "puesto";
    private final String KEY_FECHA = "fecha_captura";

    //Creamos arrays de permisos (el primero para cámara y almacenamiento y el segundo sólo para almacenamiento)
    private String[] cameraPermissions;
    private String[] storagePermissions;



    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos);

        btnRegistrar =  findViewById(R.id.btnCrearEquipo);
        imageView  =  findViewById(R.id.imgAñadirFotoEquipo);
        spTipo = findViewById(R.id.spTipo);
        spEdificio = findViewById(R.id.spEdificio);
        spAula = findViewById(R.id.spAula);
        spPuesto = findViewById(R.id.spPuesto);
        fecha_captura = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        //Inicializamos los arrays de permisos
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        bundle = getIntent().getExtras();
        editar = bundle.getBoolean("REQUEST_EDICION");


        //Funciones
        FActionBar();
        FCargarArraySpinner();
        FEsEditar();


        btnRegistrar.setOnClickListener(v -> {
            if(editar){
                FEditarEquipo();
            }else{
                uploadImage();
            }
        });
        imageView.setOnClickListener(v -> mostrarOpcionesImagen());
    }

    private void FEsEditar(){
        if (editar) {
            //cambiamos el título del ActionBar
            actionBar.setTitle("Editar Equipo");

            //si viene de editar cojo los datos del libro
            txt_id = bundle.getString("id");
            txt_tipo = bundle.getString("tipo");
            txt_aula = bundle.getString("aula");
            txt_puesto = bundle.getString("puesto");
            txt_edificio = bundle.getString("edificio");
            txt_fecha = bundle.getString("fecha_captura");

            //y los muestro

            ArrayAdapter<CharSequence> TipoAdapter = ArrayAdapter.createFromResource(this,
                    R.array.arrayTipo, android.R.layout.simple_spinner_item);

            // Especificar el layout que vamos a usar cuando pinchemos en  el spinner
            TipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Aplicamos el adaptador en el spinner
            spTipo.setAdapter(TipoAdapter);

            //Cargamos los datos de la bd en el spinner
            int intTipo = TipoAdapter.getPosition(txt_tipo);
            spTipo.setSelection(intTipo);

            ArrayAdapter<CharSequence> EdificioAdapter = ArrayAdapter.createFromResource(this,
                    R.array.arrayEdificio, android.R.layout.simple_spinner_item);
            EdificioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spEdificio.setAdapter(EdificioAdapter);

            //Cargamos los datos de la bd en el spinner
            int intEdificio = EdificioAdapter.getPosition(txt_edificio);
            spEdificio.setSelection(intEdificio);

            ArrayAdapter<CharSequence> AulaAdapter = ArrayAdapter.createFromResource(this,
                    R.array.arrayAula, android.R.layout.simple_spinner_item);
            AulaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spAula.setAdapter(AulaAdapter);

            //Cargamos los datos de la bd en el spinner
            int intAula = AulaAdapter.getPosition(txt_aula);
            spAula.setSelection(intAula);

            ArrayAdapter<CharSequence> PuestoAdapter = ArrayAdapter.createFromResource(this,
                    R.array.arrayPuesto, android.R.layout.simple_spinner_item);
            PuestoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPuesto.setAdapter(PuestoAdapter);

            //Cargamos los datos de la bd en el spinner
            int intPuesto = PuestoAdapter.getPosition(txt_puesto);
            spPuesto.setSelection(intPuesto);

            if (txt_imagen == null) {
                imageView.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                //Cuando pinchamos en agregar imagen de libro
            }

        } else {
            //cambiamos el título dek ActionBar
            actionBar.setTitle("Registrar Equipo Nuevo");
        }

    }

    private void mostrarOpcionesImagen() {
        //Tenemos dos opciones, sacar una foto con la cámara o seleccionarla de galería
        String[] opciones = {"Cámara", "Galería"};

        //Creamos el AlertDialog con el texto de Selecccionar imagen y las dos opciones
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen");
        builder.setItems(opciones, (dialog, which) -> {
            //manejamos los click dependiendo de si el usuario elige galería o cámara
            //el entero wich será cero si se elige la primera opción y 1 si se elige la segunda

            //Si hace click en cámara
            if(which==0){
                //comprobamos si tiene los permisos de cámara
                //si no los tiene se los pedimos
                if(!comprobarPermisosCamara()){
                    pedirPermisosCamara();
                }
                //si los tiene, vamos al método irCamara() para sacar la foto
                else{
                    irCamara();
                }
            }
            //Si hace click en galería
            else if(which==1){
                //comprobamos si tiene los permisos de almacenamiento
                //si no los tiene se los pedimos
                if(!comprobarPermisosAlmacenamiento()){
                    pedirPermisosAlmacenamiento();
                }
                //si los tiene, vamos al método irGaleria() para seleccionar la foto de galería
                else{
                    showFileChooser();
                }
            }
        });
        //Creamos y mostramos el diálogo con lo establecido
        builder.create().show();

    }

    private boolean comprobarPermisosCamara() {
        boolean permisosCamara = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean permisosAlmacenamiento = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return permisosCamara && permisosAlmacenamiento;

    }


    private void pedirPermisosCamara() {
        //Solicitamos el permiso de cámara
        ActivityCompat.requestPermissions(this, cameraPermissions, REQUEST_CAMERA_CODE);
    }

    private boolean comprobarPermisosAlmacenamiento() {
        boolean permisos = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return permisos;
    }
    private void pedirPermisosAlmacenamiento() {
        //Solicitamos el permiso de almacenamiento
        ActivityCompat.requestPermissions(this, storagePermissions, REQUEST_STORAGE_CODE);
    }

    private void irCamara() {
        //Creamos un ContentValues para almacenar el título de la imagen escogida y su descripción
        ContentValues valores = new ContentValues();
        valores.put(MediaStore.Images.Media.TITLE, "Título de la imagen");
        valores.put(MediaStore.Images.Media.DESCRIPTION, "Descripción de la imagen");

        //En la uri correpsondiente a la imagen escogida insertamos los valores de título y descripción
        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valores);

        //lanzamos el intent para sacar foto desde la cámara con la uri y el código correspondiente a la cámara para que desde
        //onActivityResult se sepa que es desde aquí
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, PICK_CAMERA_CODE);
    }

    //Con este método pedimos los permisos, el requestCode será el REQUEST_CAMERA_CODE o el REQUEST_STORAGE_CODE
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_CODE){
            //Si el usuario permite esos permisos, vamos a la cámara
            if(permissions.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                irCamara();
            }
            //si no le mostramos un mensaje para que habilite los permisos
            else{
                Toast.makeText(this, "Es necesario que habilites los permisos", Toast.LENGTH_SHORT).show();
            }

        }
        if(requestCode == REQUEST_STORAGE_CODE){
            //Si el usuario permite esos permisos, vamos a la galería
            if(permissions.length>0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                showFileChooser();
            }
            //si no le mostramos un mensaje para que habilite los permisos
            else{
                Toast.makeText(this, "Es necesario que habilites los permisos", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //Función que comprime la imagen para subirla correctamente al servidor
    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //Función que sube los datos al servidor.
    private void uploadImage(){
        //Mostrar el diálogo de progreso
        final ProgressDialog loading = ProgressDialog.show(this,"Subiendo...","Espere por favor...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        Toast.makeText(RegistrarDatosActivity.this, s , Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(RegistrarDatosActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);

                //Obtener el nombre de la imagen
                String tipo = spTipo.getItemAtPosition(spTipo.getSelectedItemPosition()).toString();
                String edificio = spEdificio.getItemAtPosition(spEdificio.getSelectedItemPosition()).toString();
                String aula = spAula.getItemAtPosition(spAula.getSelectedItemPosition()).toString();
                String puesto = spPuesto.getItemAtPosition(spPuesto.getSelectedItemPosition()).toString();
                String fecha = fecha_captura;

                    //Creación de parámetros
                    Map<String,String> params = new Hashtable<>();

                    //Agregando de parámetros
                    params.put(KEY_IMAGEN, imagen);
                    params.put(KEY_TIPO, tipo);
                    params.put(KEY_EDIFICIO, edificio);
                    params.put(KEY_AULA, aula);
                    params.put(KEY_PUESTO, puesto);
                    params.put(KEY_FECHA, fecha);

                    //Parámetros de retorno
                    return params;
                }
        };

        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    //Función que comprueba que el usuario y la contraseña esta correcto y si está correcto nos muestra el formulario principal.
    private void FEditarEquipo(){
        final ProgressDialog loading = ProgressDialog.show(this,"Subiendo...","Espere por favor...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL2,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                Toast.makeText(RegistrarDatosActivity.this, "EQUIPO EDITADO CORRECTAMENTE", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                Toast.makeText(RegistrarDatosActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Creación de parámetros
                Map<String,String> params = new Hashtable<>();

                //Obtener el nombre de la imagen
                String imagen = getStringImagen(bitmap);
                String tipo = spTipo.getItemAtPosition(spTipo.getSelectedItemPosition()).toString();
                String edificio = spEdificio.getItemAtPosition(spEdificio.getSelectedItemPosition()).toString();
                String aula = spAula.getItemAtPosition(spAula.getSelectedItemPosition()).toString();
                String puesto = spPuesto.getItemAtPosition(spPuesto.getSelectedItemPosition()).toString();
                String fecha = fecha_captura;

                //Agregando de parámetros
                params.put(KEY_IDEQUIPO, txt_id);
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_TIPO, tipo);
                params.put(KEY_EDIFICIO, edificio);
                params.put(KEY_AULA, aula);
                params.put(KEY_PUESTO, puesto);
                params.put(KEY_FECHA, fecha);

                //Parámetros de retorno
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrarDatosActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri filePath = data.getData();
                try {
                    //Cómo obtener el mapa de bits de la Galería
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    //Configuración del mapa de bits en ImageView
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (requestCode == PICK_CAMERA_CODE ) {
                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);


            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //si se ha recibido correctamente la imagen crop, cambiamos el CircleImageView por ésta
                CropImage.ActivityResult resultado = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    showFileChooser();
                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //Si hay un error lo mostramos
                    Exception error = resultado.getError();
                    Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();

                }
            }
        }

    }
    //Función para cargar todos los Array
        private void FCargarArraySpinner(){
        try {
        ArrayAdapter<CharSequence> TipoAdapter = ArrayAdapter.createFromResource(this,
                R.array.arrayTipo, android.R.layout.simple_spinner_item);
        // Especificar el layaut que vamos a usar cuando pinchemos en  el spinner
        TipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Aplicamos el adaptador en el spinner
        spTipo.setAdapter(TipoAdapter);

        ArrayAdapter<CharSequence> EdificioAdapter = ArrayAdapter.createFromResource(this,
                R.array.arrayEdificio, android.R.layout.simple_spinner_item);
        EdificioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEdificio.setAdapter(EdificioAdapter);

        ArrayAdapter<CharSequence> AulaAdapter = ArrayAdapter.createFromResource(this,
                R.array.arrayAula, android.R.layout.simple_spinner_item);
        AulaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAula.setAdapter(AulaAdapter);

        ArrayAdapter<CharSequence> PuestoAdapter = ArrayAdapter.createFromResource(this,
                R.array.arrayPuesto, android.R.layout.simple_spinner_item);
        PuestoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPuesto.setAdapter(PuestoAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Función para cambiar el nombre del ActionBar y mostrar una opción de regreso al anterior activity
    public void FActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setTitle("Registrar Equipo Nuevo");
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