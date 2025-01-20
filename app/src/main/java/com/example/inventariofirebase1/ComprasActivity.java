package com.example.inventariofirebase1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ComprasActivity extends AppCompatActivity {

    private EditText etCodigoCompra, etCantidadCompra;
    private TextView tvNombreProducto, tvStockProducto, tvPrecioCosto, tvTotalPagar;
    private Button btnBuscar, btnCalcularTotal, btnComprar;

    private DatabaseReference databaseReference;

    private String codigoProducto;
    private int stock;
    private double precioCosto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);

        etCodigoCompra = findViewById(R.id.et_codigo_compra);
        etCantidadCompra = findViewById(R.id.et_cantidad_compra);
        tvNombreProducto = findViewById(R.id.tv_nombre_producto_compra);
        tvStockProducto = findViewById(R.id.tv_stock_producto_compra);
        tvPrecioCosto = findViewById(R.id.tv_precio_compra);
        tvTotalPagar = findViewById(R.id.tv_total_pagar_compra);
        btnBuscar = findViewById(R.id.btn_buscar_compra);
        btnCalcularTotal = findViewById(R.id.btn_calcular_total_compra);
        btnComprar = findViewById(R.id.btn_comprar);

        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        btnBuscar.setOnClickListener(v -> buscarProducto());
        btnCalcularTotal.setOnClickListener(v -> calcularTotal());
        btnComprar.setOnClickListener(v -> realizarCompra());
    }

    private void buscarProducto() {
        codigoProducto = etCodigoCompra.getText().toString().trim();

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
                    precioCosto = snapshot.child("precioCosto").getValue(Double.class);

                    tvNombreProducto.setText("Nombre producto: " + nombre);
                    tvStockProducto.setText("Stock: " + stock);
                    tvPrecioCosto.setText("Precio de costo: $" + precioCosto);
                } else {
                    Toast.makeText(ComprasActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ComprasActivity.this, "Error al buscar producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calcularTotal() {
        String cantidadStr = etCantidadCompra.getText().toString().trim();

        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingrese una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);
        double total = cantidad * precioCosto;
        tvTotalPagar.setText("Total a pagar: $" + total);
    }

    private void realizarCompra() {
        String cantidadStr = etCantidadCompra.getText().toString().trim();

        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingrese una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);
        int nuevoStock = stock + cantidad;

        databaseReference.child(codigoProducto).child("stock").setValue(nuevoStock).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Compra realizada correctamente", Toast.LENGTH_SHORT).show();
                tvStockProducto.setText("Stock: " + nuevoStock);
                tvTotalPagar.setText("Total a pagar: $0.00");
                etCantidadCompra.setText("");
            } else {
                Toast.makeText(this, "Error al realizar la compra", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
