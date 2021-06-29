package com.example.foodstand;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    String orderId;
    Order currentOrder= null;
    ListenerRegistration orders = null;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firestore = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        orderId = sharedPreferences.getString("id", null);
        if (orderId != null){
            listenToChanges();
            firestore.collection("Orders").document(orderId).get().addOnSuccessListener(v->{
                currentOrder = v.toObject(Order.class);
                if(currentOrder == null){
                    newOrder();
                }
                else{
                    if (currentOrder.getStatus().equals("waiting")){
                        inWaitingStatus();
                    }
                    if (currentOrder.getStatus().equals("in-progress")){
                        inProgress();
                    }
                    if (currentOrder.getStatus().equals("ready")){
                        ReadyStatus();
                    }
                }
            });

        } else{
            newOrder();
        }

        listenToChanges();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("id", orderId);
        edit.apply();

    }

    public void inWaitingStatus(){
        setContentView(R.layout.activity_edit_order);
        Switch hummus = findViewById(R.id.switchHummus2);
        Switch tahini = findViewById(R.id.switchTahini2);
        EditText comments = findViewById(R.id.editTextComments2);
        EditText name = findViewById(R.id.editTextTextPersonName2);
        TextView textPickles = findViewById(R.id.textPickles2);
        SeekBar seekBar = findViewById(R.id.seekBarPickles2);
        Button updateOrder = findViewById(R.id.updateOrder);
        Button deleteOrder = findViewById(R.id.deleteOrder);
        hummus.setChecked(currentOrder.isHummus());
        tahini.setChecked(currentOrder.isTahini());
        comments.setText(currentOrder.getComment());
        name.setText(currentOrder.getCustomerName());
        String text = "Number of pickles: " + currentOrder.getPickles();
        textPickles.setText(text);
        seekBar.setProgress(currentOrder.getPickles());

//        String t1 = "Number of pickles: 0";
//        textPickles.setText(t1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = "Number of pickles: ";

                text += progress;
                textPickles.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int numOfPickles = Integer.parseInt(textPickles.getText().toString().substring(19));
                currentOrder = new Order( comments.getText().toString(), name.getText().toString(),  hummus.isChecked(), numOfPickles, "waiting", tahini.isChecked());
                firestore.collection("Orders").document(orderId).set(currentOrder).addOnSuccessListener(res->{
                    Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_LONG).show();
                });


            }
        });

        deleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Orders").document(orderId).delete().addOnSuccessListener(res->{
                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_LONG).show();
                });
                orderId = null;
                currentOrder =null;
                newOrder();
            }
        });
        listenToChanges();
    }


    public void fetchingOrder(){
        setContentView(R.layout.activity_getting_order);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView text = findViewById(R.id.fetch);
        text.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }
    
    public void newOrder()
    {
        setContentView(R.layout.activity_new_order);
        SeekBar seekBar = findViewById(R.id.seekBarPickles);
        TextView textPickles = findViewById(R.id.textPickles);
        EditText comments = findViewById(R.id.editTextComments);
        EditText name = findViewById(R.id.editTextTextPersonName);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch hummus = findViewById(R.id.switchHummus);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch tahini = findViewById(R.id.switchTahini);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = "Number of pickles: ";

                text += progress;
                textPickles.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        FloatingActionButton addOrder = findViewById(R.id.floatingActionButton);
        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderId = UUID.randomUUID().toString();
                int numPickles = Integer.parseInt(textPickles.getText().toString().substring(19));
                String status = "waiting";
                currentOrder = new Order( comments.getText().toString(), name.getText().toString(),  hummus.isChecked(), numPickles, status, tahini.isChecked());
//                currentOrder = new Order(name.getText().toString(), numPickles, hummus.isChecked(),
//                        tahini.isChecked(), comments.getText().toString(), status);
                firestore.collection("Orders").document(orderId).set(currentOrder);
                inWaitingStatus();
            }
        });

    }
    public void inProgress(){
        setContentView(R.layout.activity_in_progress_order);
    }

    public void ReadyStatus(){

        setContentView(R.layout.activity_order_ready);

        Button confirmButton = findViewById(R.id.confirmOrder);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Orders").document(orderId).delete().addOnSuccessListener(res->{
                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_LONG).show();
                });
                orderId = null;
                currentOrder =null;
                newOrder();
            }
        });

    }

    private void listenToChanges(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        if(orderId == null){
            return;
        }
        orders = firebaseFirestore.collection("Orders").document(orderId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    // todo
                }
                else if(value == null){

                }
                else if (!value.exists()){
                    // delete
                }
                else{
                    currentOrder  = value.toObject(Order.class);
                    if(currentOrder.getStatus().equals("in-progress")){
                        inProgress();
                    }
                    if(currentOrder.getStatus().equals("ready")){
                        ReadyStatus();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}




