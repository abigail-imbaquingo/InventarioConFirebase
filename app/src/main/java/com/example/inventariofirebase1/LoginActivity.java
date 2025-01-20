package com.example.inventariofirebase1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etCorreo, etContraseña;
    private Spinner spOpciones;
    private Button btnIngresar, btnRegistrar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.etCorreo);
        etContraseña = findViewById(R.id.etContraseña);
        spOpciones = findViewById(R.id.spOpciones);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        firebaseAuth = FirebaseAuth.getInstance();

        // Configuración del Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_ingreso, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOpciones.setAdapter(adapter);

        // Botón de registro
        btnRegistrar.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
            startActivity(intent);
        });

        // Botón de inicio de sesión
        btnIngresar.setOnClickListener(view -> {
            String correo = etCorreo.getText().toString().trim();
            String contraseña = etContraseña.getText().toString().trim();
            String opcionSeleccionada = spOpciones.getSelectedItem().toString();

            if (correo.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Iniciar sesión con Firebase Authentication
            firebaseAuth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Navegación según el rol seleccionado
                            switch (opcionSeleccionada) {
                                case "Persona":
                                    startActivity(new Intent(LoginActivity.this, PersonaActivity.class));
                                    break;
                                case "Producto":
                                    startActivity(new Intent(LoginActivity.this, ProductoActivity.class));
                                    break;
                                case "Inventario":
                                    startActivity(new Intent(LoginActivity.this, InventarioActivity.class));
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this, "Opción no válida", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
