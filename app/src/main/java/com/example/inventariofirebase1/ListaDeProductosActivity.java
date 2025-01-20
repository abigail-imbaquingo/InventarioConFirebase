package com.example.inventariofirebase1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListaDeProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProductos;
    private DatabaseReference productosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        // Referencia a la base de datos
        productosRef = FirebaseDatabase.getInstance().getReference("productos");

        // Configurar RecyclerView
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

        // Configurar FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Producto> options = new FirebaseRecyclerOptions.Builder<Producto>()
                .setQuery(productosRef, Producto.class)
                .build();

        // Configurar FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<Producto, ProductoViewHolder> adapter =
                new FirebaseRecyclerAdapter<Producto, ProductoViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductoViewHolder holder, int position, @NonNull Producto model) {
                        holder.tvCodigoProducto.setText("Código: " + model.getCodigo());
                        holder.tvNombreProducto.setText("Nombre: " + model.getNombre());
                        holder.tvStockProducto.setText("Stock: " + model.getStock());
                        holder.tvPrecioVentaProducto.setText("Precio de Venta: $" + model.getPrecioVenta());
                        holder.tvPrecioCompraProducto.setText("Precio de Costo: $" + model.getPrecioCosto());
                    }

                    @NonNull
                    @Override
                    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_producto, parent, false);
                        return new ProductoViewHolder(view);
                    }
                };

        recyclerViewProductos.setAdapter(adapter);
        adapter.startListening();
    }

    // ViewHolder para productos
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigoProducto, tvNombreProducto, tvStockProducto, tvPrecioVentaProducto, tvPrecioCompraProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigoProducto = itemView.findViewById(R.id.tvCodigoProducto);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvStockProducto = itemView.findViewById(R.id.tvStockProducto);
            tvPrecioVentaProducto = itemView.findViewById(R.id.tvPrecioVentaProducto);
            tvPrecioCompraProducto = itemView.findViewById(R.id.tvPrecioCompraProducto);
        }
    }
}