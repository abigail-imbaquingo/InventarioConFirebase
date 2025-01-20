package com.example.inventariofirebase1;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonaActivity extends AppCompatActivity {

    private EditText etCedula, etNombre, etProvincia;
    private RadioGroup rgGenero;
    private RadioButton rbHombre, rbMujer;
    private Spinner spPais;
    private Button btnActualizar, btnSalir;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ArrayAdapter<String> adapter; // Variable global para el adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona);

        // Referencias de UI
        etCedula = findViewById(R.id.etCedula);
        etNombre = findViewById(R.id.etNombre);
        etProvincia = findViewById(R.id.etProvincia);
        rgGenero = findViewById(R.id.rgGenero);
        rbHombre = findViewById(R.id.rbHombre);
        rbMujer = findViewById(R.id.rbMujer);
        spPais = findViewById(R.id.spPais);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnSalir = findViewById(R.id.btnSalir);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("personas");

        // Configuración del Spinner
        String[] paises = {"Seleccione país", "Ecuador", "Colombia", "Venezuela"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paises); // Inicializar adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPais.setAdapter(adapter);

        // Cargar datos existentes del usuario
        cargarDatosUsuario();

        // Botón Actualizar
        btnActualizar.setOnClickListener(view -> actualizarDatosUsuario());

        // Botón Salir
        btnSalir.setOnClickListener(view -> finish());
    }

    private void cargarDatosUsuario() {
        String userId = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Cargar datos del nodo
                    String cedula = dataSnapshot.child("cedula").getValue(String.class);
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    String provincia = dataSnapshot.child("provincia").getValue(String.class);
                    String pais = dataSnapshot.child("pais").getValue(String.class);
                    String genero = dataSnapshot.child("genero").getValue(String.class);

                    // Prellenar los campos
                    if (cedula != null) etCedula.setText(cedula);
                    if (nombre != null) etNombre.setText(nombre);
                    if (provincia != null) etProvincia.setText(provincia);

                    if (pais != null) {
                        int spinnerPosition = adapter.getPosition(pais); // Usar el adapter global
                        spPais.setSelection(spinnerPosition);
                    }

                    if (genero != null) {
                        if (genero.equals("Hombre")) {
                            rgGenero.check(R.id.rbHombre);
                        } else if (genero.equals("Mujer")) {
                            rgGenero.check(R.id.rbMujer);
                        }
                    }
                } else {
                    Toast.makeText(PersonaActivity.this, "Por favor, complete el formulario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PersonaActivity", "Error al cargar datos: " + databaseError.getMessage());
            }
        });
    }

    private void actualizarDatosUsuario() {
        String userId = firebaseAuth.getCurrentUser().getUid();

        // Obtener datos del formulario
        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String provincia = etProvincia.getText().toString().trim();
        String pais = spPais.getSelectedItem().toString();
        String genero = rgGenero.getCheckedRadioButtonId() == R.id.rbHombre ? "Hombre" : "Mujer";

        // Validar datos
        if (cedula.isEmpty() || nombre.isEmpty() || provincia.isEmpty() || pais.equals("Seleccione país")) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar en Firebase
        databaseReference.child(userId).child("cedula").setValue(cedula);
        databaseReference.child(userId).child("nombre").setValue(nombre);
        databaseReference.child(userId).child("provincia").setValue(provincia);
        databaseReference.child(userId).child("pais").setValue(pais);
        databaseReference.child(userId).child("genero").setValue(genero);

        Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
    }
}