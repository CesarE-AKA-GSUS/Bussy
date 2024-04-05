package com.example.bussy;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {

    //Editar AQUI
    EditText txtLatitud,txtLongitud, txtEditarTexto;
    GoogleMap mMap;
    private Marker currentMarker;
    private Marker Tecnl;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtEditarTexto = findViewById(R.id.txtEditarTexto);
        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);

        txtEditarTexto.setEnabled(false);
        txtEditarTexto.setTextColor(Color.WHITE);

        //ESTAS DOS LINEAS LLAMAN AL METODO "onMapReady"
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //AJUSTA EL TAMAñO DEL MARCADOR
        int height = 50;
        int width = 50;
        //R.drawable.n ES LA UBICACION DE LA IMAGEN
        @SuppressLint("UseCompatLoadingForDrawables")
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.n);
        Bitmap b = bitmapdraw.getBitmap();
        //SMALLMARKER ES EL NOMBRE DE LA VARIBALE DEL MARCADOR
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        //LLAMA A LAS CLASES ONMAPCLICK Y CONMAPLONG
        mMap=googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        //OBTIENE LAS CORDENADAS DONDE SE ABRIRA EL MAPA
        LatLng TecNL = new LatLng(25.664895, -100.245138);
        //CREA UN MARCADOR
        Tecnl = mMap.addMarker(new MarkerOptions().position(TecNL).title(getString(R.string.Ruta_223)).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .flat(true).snippet(getString(R.string.Click_aqui_para_mas_informacion)));
        googleMap.setOnInfoWindowClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TecNL));
        mMap.setMinZoomPreference(12.0f); // Zoom mínimo permitido
        mMap.setMaxZoomPreference(16.0f); // Zoom máximo permitido

        //UBICACION TIEMPO REAL

    }



    public int Datos(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(51); // Genera un número aleatorio del 0 al 50
        return randomNumber;
    }

    @SuppressLint("SetTextI18n")
    public void onInfoWindowClick(Marker marker) {

        switch (Objects.requireNonNull(marker.getTitle())){
            case "Ruta 223":
                txtEditarTexto.setText(Datos() + " Personas Approx");
                break;
            case "Ruta 214":
                txtEditarTexto.setText(Datos() + " Personas Approx");
                break;
        }

        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        txtLatitud.setText(" " + latLng.latitude);
        txtLongitud.setText(" " + latLng.longitude);
        if (currentMarker != null) {
            currentMarker.remove(); // Eliminar el marcador actual si existe
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.Nuevo_Marcador)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        txtLatitud.setText(" " + latLng.latitude);
        txtLongitud.setText(" " + latLng.longitude);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}