package com.example.inventariofirebase1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.inventariofirebase1.R;

import androidx.appcompat.app.AppCompatActivity;

public class InventarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        // Establece el título de la actividad
        setTitle("Inventario Firebase1");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú desde inventario.xml
        getMenuInflater().inflate(R.menu.inventario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ventas) {
            startActivity(new Intent(this, VentasActivity.class));
            return true;
        } else if (id == R.id.compras) {
            startActivity(new Intent(this, ComprasActivity.class));
            return true;
        } else if (id == R.id.lista_productos) {
            startActivity(new Intent(this, ListaDeProductosActivity.class));
            return true;
        } else if (id == R.id.cerrar_sesion) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}