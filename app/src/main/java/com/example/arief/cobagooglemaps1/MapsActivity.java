package com.example.arief.cobagooglemaps1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.internal.PlaceEntity;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener ,GoogleMap.OnMarkerDragListener{

    private GoogleMap mMap;
    private Button bAutoComplete,bPlacePicker;

    private static final int KODE_REQUEST_PLACE_AUTOCOMPLETE = 1;
    private static final int KODE_REQUEST_PLACE_PICKER =  1;

    private boolean iniAutoComplete ;
    private boolean iniPlacePicker ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        bPlacePicker = (Button)findViewById(R.id.bPlacePicker);
        bAutoComplete = (Button)findViewById(R.id.bAutoComplete);
        bAutoComplete.setOnClickListener(this);
        bPlacePicker.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        mMap.addMarker(new MarkerOptions().draggable(true).position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMarkerDragListener(this);
    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {


        LatLng latLng  = marker.getPosition();

        String lat = String.valueOf(latLng.latitude);
        String lng = String.valueOf(latLng.longitude);

        Log.e("MarkerLatLng","Latitude : " + lat + "\t" + "Longitude : " + lng);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bPlacePicker :
                placePickerIntent();
                iniPlacePicker = true;
                break;
            case R.id.bAutoComplete :
                findPlace();
                iniAutoComplete  = true;
                break;
        }
    }

    public void placePickerIntent(){
        try{
            Intent inten = new PlacePicker.IntentBuilder().build(MapsActivity.this);
            startActivityForResult(inten,KODE_REQUEST_PLACE_PICKER);
        }catch (Exception ex){
            Log.e("Error placePicker",ex.getMessage());
        }
    }

    public void findPlace(){
        try{
            Intent inten = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(MapsActivity.this);
            startActivityForResult(inten,KODE_REQUEST_PLACE_AUTOCOMPLETE);
        }catch (Exception ex){
            Log.e("ErrorFindPlace",ex.getMessage());
        }
    }

    private void displayPlaceResult(Place place,String judul){

        String placeName = place.getName().toString();
        String placeAddress = place.getAddress().toString();
        String phoneNumber = place.getPhoneNumber().toString();

        LatLng latng = place.getLatLng();
        Log.e("LatLngOfPlace","Latitude : " + String.valueOf(latng.latitude)+"\t"+
                "Longitude : " +String.valueOf(latng.longitude));

        buatAlertDialog(judul,placeName,placeAddress,phoneNumber);

    }

    private void buatAlertDialog(String judul,String nama,String alamat,String no){

        AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);
        View v = getLayoutInflater().inflate(R.layout.dialog_ui,null);

        dialog.setView(v);

        TextView tNama = (TextView)v.findViewById(R.id.tampilNama);
        TextView tAlamat = (TextView)v.findViewById(R.id.tampilAlamat);
        TextView tNo = (TextView)v.findViewById(R.id.tampilTelp);
        TextView tJudul = (TextView)v.findViewById(R.id.judul);

        try{
            tJudul.setText(judul);
            tNama.setText(nama);
            tAlamat.setText(alamat);
            tNo.setText(no);
        }catch (Exception ex){
            Log.e("Error ex" ,ex.getMessage());
        }

        dialog.setNeutralButton("Close", null);

        dialog.show();

    }

     /*if(requestCode == KODE_REQUEST_PLACE_AUTOCOMPLETE && resultCode == RESULT_OK){
        Place place = PlaceAutocomplete.getPlace(MapsActivity.this,data);

        String placeName = place.getName().toString();
        String placeAddress = place.getAddress().toString();
        String phoneNumber = place.getPhoneNumber().toString();

        //Toast.makeText(this, "Name ", Toast.LENGTH_SHORT).show();


        buatAlertDialog(placeName,placeAddress,phoneNumber);

        LatLng latng = place.getLatLng();
        Log.e("LatLngOfPlace","Latitude : " + String.valueOf(latng.latitude)+"\t"+
                "Longitude : " +String.valueOf(latng.longitude));


    }else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
        Log.e("Result error","PlaceAutoComplete Result error");
    }else if(resultCode == RESULT_CANCELED){
        Log.e("Result cancel","PlaceAutoComplete Result cancelled");
    }*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(iniPlacePicker){
            Log.e("boolean","INI place picker onAction");

            if(requestCode == KODE_REQUEST_PLACE_PICKER && resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(MapsActivity.this,data);

                displayPlaceResult(place,"PlacePicker Result");

            }else if(resultCode == RESULT_CANCELED){
                Log.e("ResultCode Cancel","Cancelled PlacePicker");
            }else if(resultCode == PlacePicker.RESULT_ERROR){
                Log.e("ResultCode ERROR","Error PlacePicker");
            }

        }else if (iniAutoComplete){
            Log.e("boolean","INI place autoComplete onAction");
         if(requestCode == KODE_REQUEST_PLACE_AUTOCOMPLETE && resultCode == RESULT_OK){

             Place place = PlaceAutocomplete.getPlace(MapsActivity.this,data);

             displayPlaceResult(place,"PlaceAutoComplete Result");

         }else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
             Log.e("Result error","PlaceAutoComplete Result error");
         }else if(resultCode == RESULT_CANCELED){
             Log.e("Result cancel","PlaceAutoComplete Result cancelled");
         }

     }

        iniPlacePicker = false;
        iniAutoComplete = false;

    }

}
