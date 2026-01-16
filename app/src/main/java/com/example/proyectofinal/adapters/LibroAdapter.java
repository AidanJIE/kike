package com.example.proyectofinal.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.database.DatabaseHelper;
import com.example.proyectofinal.models.Libro;

import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> {

    private List<Libro> listaLibros;
    private Context context;

    public LibroAdapter(List<Libro> listaLibros, Context context) {
        this.listaLibros = listaLibros;
        this.context = context;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro, parent, false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = listaLibros.get(position);

        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText("Autor: " + libro.getAutor());
        holder.tvPrecio.setText(String.format("$%.2f", libro.getPrecio()));
        holder.tvCantidad.setText("Stock: " + libro.getCantidad());
        holder.tvCategoria.setText(libro.getCategoria());

        // Click para ver detalles
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context,
                    "📖 " + libro.getTitulo() +
                            "\n💵 Precio: $" + libro.getPrecio() +
                            "\n📦 Stock: " + libro.getCantidad(),
                    Toast.LENGTH_LONG).show();
        });

        // Long click para eliminar
        holder.itemView.setOnLongClickListener(v -> {
            mostrarDialogoEliminar(libro, position);
            return true;
        });
    }

    /**
     * Mostrar diálogo de confirmación para eliminar
     */
    private void mostrarDialogoEliminar(Libro libro, int position) {
        new AlertDialog.Builder(context)
                .setTitle("🗑️ Eliminar Libro")
                .setMessage("¿Estás seguro de eliminar \"" + libro.getTitulo() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    // 1. Obtener DatabaseHelper
                    DatabaseHelper dbHelper = new DatabaseHelper(context);

                    // 2. Eliminar de la base de datos
                    dbHelper.eliminarLibro(libro.getId());
                    dbHelper.close();

                    // 3. Eliminar de la lista local
                    listaLibros.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listaLibros.size());

                    Toast.makeText(context, "✅ Libro eliminado permanentemente", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    @Override
    public int getItemCount() {
        return listaLibros.size();
    }

    public static class LibroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor, tvPrecio, tvCantidad, tvCategoria;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tv_titulo_libro);
            tvAutor = itemView.findViewById(R.id.tv_autor_libro);
            tvPrecio = itemView.findViewById(R.id.tv_precio_libro);
            tvCantidad = itemView.findViewById(R.id.tv_cantidad_libro);
            tvCategoria = itemView.findViewById(R.id.tv_categoria_libro);
        }
    }
}