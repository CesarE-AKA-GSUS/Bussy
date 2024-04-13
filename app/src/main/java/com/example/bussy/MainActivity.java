package com.example.bussy;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.Manifest.permission;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

import java.security.Permission;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    //Editar AQUI

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    EditText txtLatitud,txtLongitud;
    GoogleMap mMap;
    private Marker currentMarker;
    private Marker Tecnl;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//txtEditarTexto = findViewById(R.id.txtEditarTexto);
        SearchView BarraBusquedaMain = findViewById(R.id.BarraBusquedaMain);





        //ESTAS DOS LINEAS LLAMAN AL METODO "onMapReady"
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //ESTO CONFIGURA LA BARRA DE BUESQUEDA MAIN
        BarraBusquedaMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarraBusquedaMain.setIconified(false); // Expandir el SearchView
            }
        });

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
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();

    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    public int Datos(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(51); // Genera un número aleatorio del 0 al 50
        return randomNumber;
    }

    @SuppressLint("SetTextI18n")
    public void onInfoWindowClick(Marker marker) {
/*
        switch (Objects.requireNonNull(marker.getTitle())){
            case "Ruta 223":
               txtEditarTexto.setText(Datos() + " Personas Approx");
                break;
            case "Ruta 214":
               txtEditarTexto.setText(Datos() + " Personas Approx");
                break;
        }

        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();*/

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        if (currentMarker != null) {
            currentMarker.remove(); // Eliminar el marcador actual si existe
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.Nuevo_Marcador)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }

    }
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}