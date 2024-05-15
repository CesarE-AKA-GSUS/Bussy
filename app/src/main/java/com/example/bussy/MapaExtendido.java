package com.example.bussy;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapaExtendido extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private Marker Tecnl;
    private Marker currentMarker, marcador2;

    private Marker Tecnl2;
    private Handler handler;
    private Runnable runnable;
    private LinearLayout linearLayout;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> itemList;

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mapa_extendido);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

            //ESTAS DOS LINEAS LLAMAN AL METODO "onMapReady"

        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapas);
        mapFragment.getMapAsync(this);
        itemList = new ArrayList<>();

        int i = 0;
        // Agregar elementos a la lista dinámicamente usando un bucle while
        while (i < 10) {
            itemList.add("Elemento " + (i+12));
            i++;
        }


        // Configurar el adaptador
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);

        // Configurar el ListView
        listView = (ListView) findViewById(R.id.lista);
        listView.setAdapter(adapter);

        // Opcional: añadir un clic en el elemento de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemList.get(position);
                Toast.makeText(MapaExtendido.this, "Elemento seleccionado: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
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
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        //OBTIENE LAS CORDENADAS DONDE SE ABRIRA EL MAPA
        LatLng TecNL = new LatLng(25.664895, -100.245138);
        LatLng TecNL2 = new LatLng(25.66470371562547, -100.24338602091686);

        //CREA UN MARCADOR
        Tecnl = mMap.addMarker(new MarkerOptions().position(TecNL).title(getString(R.string.Ruta_223)).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .flat(true).snippet(getString(R.string.Click_aqui_para_mas_informacion)));
        googleMap.setOnInfoWindowClickListener(this);
        Tecnl2 = mMap.addMarker(new MarkerOptions().position(TecNL2).title(getString(R.string.Ruta_224)).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
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

        mMap.setOnMarkerClickListener(this);
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. verifica si se concedio permiso
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            // Cambiar el icono de la posición del usuario
            if (mMap != null) {
                try {
                    // Obtiene el icono personalizado
                    int height = 70;
                    int width = 70;
                    // R.drawable.iconlive ES LA UBICACION DE LA IMAGEN
                    @SuppressLint("UseCompatLoadingForDrawables")
                    BitmapDrawable customIcon = (BitmapDrawable) getResources().getDrawable(R.drawable.iconolive);
                    Bitmap b = customIcon.getBitmap();
                    // SMALLMARKER ES EL NOMBRE DE LA VARIABLE DEL MARCADOR
                    Bitmap smallcustomIcon = Bitmap.createScaledBitmap(b, width, height, false);

                    // Inicia la actualización periódica de la ubicación
                    handler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            updateLastKnownLocation(smallcustomIcon);
                            handler.postDelayed(this, 5000); // 5000 milisegundos = 5 segundos
                        }
                    };
                    handler.postDelayed(runnable, 5000); // Inicia la actualización después de 5 segundos
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

            return;
        }
    }

    private void updateLastKnownLocation(Bitmap smallcustomIcon) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Crea un objeto LocationManager para obtener la última ubicación conocida
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                // Si ya hay un marcador, elimínalo
                if (marcador2 != null) {
                    marcador2.remove();
                }
                // Crea un marcador en la última ubicación conocida con el icono personalizado
                LatLng conocida = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                marcador2 = mMap.addMarker(new MarkerOptions()
                        .position(conocida)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallcustomIcon)));
            }
        }

    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}