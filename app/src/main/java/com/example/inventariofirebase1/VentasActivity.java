package com.example.inventariofirebase1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventariofirebase1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VentasActivity extends AppCompatActivity {

    private EditText etCodigoVenta, etCantidadVenta;
    private TextView tvNombreProducto, tvStockProducto, tvPrecioVenta, tvTotalPagar;
    private Button btnBuscar, btnCalcularTotal, btnVender;

    private DatabaseReference databaseReference;

    private String codigoProducto;
    private int stock;
    private double precioVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        // Referencias de vistas
        etCodigoVenta = findViewById(R.id.editTextCodigo);
        etCantidadVenta = findViewById(R.id.editTextCantidad);
        tvNombreProducto = findViewById(R.id.textViewNombreProducto);
        tvStockProducto = findViewById(R.id.textViewStock);
        tvPrecioVenta = findViewById(R.id.textViewPrecioVenta);
        tvTotalPagar = findViewById(R.id.textViewTotalPagar);
        btnBuscar = findViewById(R.id.buttonBuscar);
        btnCalcularTotal = findViewById(R.id.buttonCalcularTotal);
        btnVender = findViewById(R.id.buttonVender);

        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        btnBuscar.setOnClickListener(v -> buscarProducto());
        btnCalcularTotal.setOnClickListener(v -> calcularTotal());
        btnVender.setOnClickListener(v -> realizarVenta());
    }

    private void buscarProducto() {
        codigoProducto = etCodigoVenta.getText().toString().trim();

        if (codigoProducto.isEmpty()) {
            Toast.makeText(this, "Ingrese un código de producto", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(codigoProducto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    stock = snapshot.child("stock").getValue(Integer.class);
                    precioVenta = snapshot.child("precioVenta").getValue(Double.class);

                    tvNombreProducto.setText("Nombre producto: " + nombre);
                    tvStockProducto.setText("Stock: " + stock);
                    tvPrecioVenta.setText("Precio de venta: $" + precioVenta);
                } else {
                    Toast.makeText(VentasActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(VentasActivity.this, "Error al buscar producto: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calcularTotal() {
        String cantidadStr = etCantidadVenta.getText().toString().trim();

        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingrese una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);

        if (cantidad > stock) {
            Toast.makeText(this, "No hay stock suficiente", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = cantidad * precioVenta;
        tvTotalPagar.setText("Total a pagar: $" + total);
    }

    private void realizarVenta() {
        String cantidadStr = etCantidadVenta.getText().toString().trim();

        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingrese una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);

        if (cantidad > stock) {
            Toast.makeText(this, "No hay stock suficiente", Toast.LENGTH_SHORT).show();
            return;
        }

        int nuevoStock = stock - cantidad;

        databaseReference.child(codigoProducto).child("stock").setValue(nuevoStock).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Venta realizada correctamente", Toast.LENGTH_SHORT).show();
                tvStockProducto.setText("Stock: " + nuevoStock);
                tvTotalPagar.setText("Total a pagar: $0.00");
                etCantidadVenta.setText("");
            } else {
                Toast.makeText(this, "Error al realizar la venta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}