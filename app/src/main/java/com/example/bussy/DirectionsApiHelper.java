package com.example.bussy;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DirectionsApiHelper  {

    private Context context;
    private static final String TAG = "DirectionsApiHelper";
    private Polyline drivingPolyline;
    private Polyline walkingPolyline;
    private List<LatLng> walkingPoints = new ArrayList<>();
    private List<LatLng> drivingPoints = new ArrayList<>();
    private LatLng lastDrivingPoint;
    private double Lat,Long;

    private GoogleMap mMap;

    public DirectionsApiHelper(Context context) {
        this.context = context;
    }


    public void onLocationChanged(Location location) {
        // Aquí recibes las actualizaciones de ubicación en tiempo real
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Haz lo que necesites con la ubicación
        Lat = latitude;
        Long = longitude;
    }

    double originLat = 25.665244;
    double originLng = -100.246258;

    // Coordenadas del destino (Montreal)
    double destLat = 25.665787;
    double destLng = -100.216023;


    public void requestDirections(String apiKey, String origin, String destination) {



        // URL de la solicitud para tránsito
        String transitUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin + "&destination=" + destination + "&mode=transit&key=" + apiKey;

        // Crear la solicitud JSON para tránsito
        JsonObjectRequest transitRequest = new JsonObjectRequest
                (Request.Method.GET, transitUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray routes = response.getJSONArray("routes");
                            if (routes.length() > 0) {
                                // Se encontró una ruta de tránsito, mostrarla en el mapa
                                ((MainActivity) context).displayRoute(response);
                            } else {
                                // No se encontró una ruta de tránsito, intentar obtener una a pie
                                requestWalkingDirections(apiKey, origin, destination);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.toString());
                    }
                });

        // Agregar la solicitud a la cola
        Volley.newRequestQueue(context).add(transitRequest);
    }

    private void requestWalkingDirections(String apiKey, String origin, String destination) {
        // URL de la solicitud para caminar
        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin + "&destination=" + destination + "&mode=walking&key=" + apiKey;

        // Crear la solicitud JSON para caminar
        JsonObjectRequest walkingRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray routes = response.getJSONArray("routes");
                            if (routes.length() > 0) {
                                // Se encontró una ruta a pie, guardar puntos y mostrar en el mapa
                                walkingPoints.clear();
                                walkingPoints.addAll(parseRoutePoints(routes.getJSONObject(0)));
                                showWalkingRoute();
                            } else {
                                // No se encontró una ruta a pie, mostrar mensaje de error
                                Log.e(TAG, "No se encontró una ruta a pie");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.toString());
                    }
                });

        // Agregar la solicitud a la cola
    }


    private void showWalkingRoute() {
        PolylineOptions options = new PolylineOptions().addAll(walkingPoints).color(R.color.light_blue_600).width(5);
        if (walkingPolyline != null) {
            walkingPolyline.remove();
        }
        walkingPolyline = mMap.addPolyline(options);
    }

    private List<LatLng> parseRoutePoints(JSONObject route) throws JSONException {
        List<LatLng> points = new ArrayList<>();
        JSONArray legs = route.getJSONArray("legs");
        for (int i = 0; i < legs.length(); i++) {
            JSONArray steps = legs.getJSONObject(i).getJSONArray("steps");
            for (int j = 0; j < steps.length(); j++) {
                JSONObject step = steps.getJSONObject(j);
                JSONObject startLocation = step.getJSONObject("start_location");
                double lat = startLocation.getDouble("lat");
                double lng = startLocation.getDouble("lng");
                points.add(new LatLng(lat, lng));
            }
        }
        return points;
    }

}