package com.example.inventariofirebase1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductoActivity extends AppCompatActivity {

    private EditText etCodigo, etNombreProducto, etStock, etPrecioCosto, etPrecioVenta;
    private Button btnGuardar, btnActualizar, btnBuscar, btnBorrar, btnRegresarProducto;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        // Inicializar vistas
        etCodigo = findViewById(R.id.editTextCodigo);
        etNombreProducto = findViewById(R.id.etNombreProducto);
        etStock = findViewById(R.id.etStock);
        etPrecioCosto = findViewById(R.id.etPrecioCosto);
        etPrecioVenta = findViewById(R.id.etPrecioVenta);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnRegresarProducto = findViewById(R.id.btnRegresarProducto);

        // Inicializar referencia a Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Configurar botones
        btnGuardar.setOnClickListener(v -> guardarProducto());
        btnActualizar.setOnClickListener(v -> actualizarProducto());
        btnBuscar.setOnClickListener(v -> buscarProducto());
        btnBorrar.setOnClickListener(v -> borrarProducto());

        // Configurar el botón Regresar para redirigir a Login
        btnRegresarProducto.setOnClickListener(v -> irALogin());
    }

    private void guardarProducto() {
        String codigo = etCodigo.getText().toString().trim();
        String nombre = etNombreProducto.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String precioCostoStr = etPrecioCosto.getText().toString().trim();
        String precioVentaStr = etPrecioVenta.getText().toString().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || stockStr.isEmpty() || precioCostoStr.isEmpty() || precioVentaStr.isEmpty()) {
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock = Integer.parseInt(stockStr);
        double precioCosto = Double.parseDouble(precioCostoStr);
        double precioVenta = Double.parseDouble(precioVentaStr);

        Producto producto = new Producto(codigo, nombre, stock, precioCosto, precioVenta);

        databaseReference.child(codigo).setValue(producto).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProductoActivity.this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            } else {
                Toast.makeText(ProductoActivity.this, "Error al guardar el producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarProducto() {
        String codigo = etCodigo.getText().toString().trim();
        String nombre = etNombreProducto.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String precioCostoStr = etPrecioCosto.getText().toString().trim();
        String precioVentaStr = etPrecioVenta.getText().toString().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || stockStr.isEmpty() || precioCostoStr.isEmpty() || precioVentaStr.isEmpty()) {
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock = Integer.parseInt(stockStr);
        double precioCosto = Double.parseDouble(precioCostoStr);
        double precioVenta = Double.parseDouble(precioVentaStr);

        Producto producto = new Producto(codigo, nombre, stock, precioCosto, precioVenta);

        databaseReference.child(codigo).setValue(producto).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProductoActivity.this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProductoActivity.this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarProducto() {
        String codigo = etCodigo.getText().toString().trim();

        if (codigo.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un código", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(codigo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Producto producto = dataSnapshot.getValue(Producto.class);
                    etNombreProducto.setText(producto.getNombre());
                    etStock.setText(String.valueOf(producto.getStock()));
                    etPrecioCosto.setText(String.valueOf(producto.getPrecioCosto()));
                    etPrecioVenta.setText(String.valueOf(producto.getPrecioVenta()));
                    Toast.makeText(ProductoActivity.this, "Producto encontrado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductoActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProductoActivity.this, "Error al buscar el producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void borrarProducto() {
        String codigo = etCodigo.getText().toString().trim();

        if (codigo.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un código", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(codigo).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProductoActivity.this, "Producto borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            } else {
                Toast.makeText(ProductoActivity.this, "Error al borrar el producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        etCodigo.setText("");
        etNombreProducto.setText("");
        etStock.setText("");
        etPrecioCosto.setText("");
        etPrecioVenta.setText("");
    }

    private void irALogin() {
        Intent intent = new Intent(ProductoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finaliza la actividad actual para evitar que se regrese a ella con el botón "Atrás"
    }
}