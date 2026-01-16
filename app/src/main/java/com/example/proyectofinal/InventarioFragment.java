package com.example.proyectofinal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.adapters.LibroAdapter;
import com.example.proyectofinal.database.DatabaseHelper;
import com.example.proyectofinal.models.Libro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class InventarioFragment extends Fragment {

    private RecyclerView recyclerView;
    private LibroAdapter adapter;
    private List<Libro> listaLibros;
    private FloatingActionButton fabAgregar;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventario, container, false);

        recyclerView = view.findViewById(R.id.recycler_inventario);
        fabAgregar = view.findViewById(R.id.fab_agregar_libro);

        // 1. INICIALIZAR LA BASE DE DATOS
        databaseHelper = new DatabaseHelper(getContext());

        // 2. CARGAR DATOS DESDE LA BD (no más listas temporales)
        listaLibros = new ArrayList<>();
        cargarDatosDesdeBD();

        // 3. CONFIGURAR EL ADAPTADOR
        adapter = new LibroAdapter(listaLibros, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // 4. BOTÓN PARA AGREGAR LIBRO
        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregarLibro());

        return view;
    }

    /**
     * Cargar libros desde la base de datos
     */
    private void cargarDatosDesdeBD() {
        // Limpiar lista actual
        listaLibros.clear();

        // Obtener libros de la base de datos
        List<Libro> librosBD = databaseHelper.obtenerTodosLosLibros();

        // Agregar a nuestra lista
        listaLibros.addAll(librosBD);

        // Notificar al adaptador
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refrescar la lista cada vez que se muestre el fragmento
        cargarDatosDesdeBD();
    }

    /**
     * Mostrar diálogo para agregar un nuevo libro
     */
    private void mostrarDialogoAgregarLibro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_agregar_libro, null);

        EditText etTitulo = dialogView.findViewById(R.id.et_titulo);
        EditText etAutor = dialogView.findViewById(R.id.et_autor);
        EditText etIsbn = dialogView.findViewById(R.id.et_isbn);
        EditText etPrecio = dialogView.findViewById(R.id.et_precio);
        EditText etCantidad = dialogView.findViewById(R.id.et_cantidad);
        EditText etCategoria = dialogView.findViewById(R.id.et_categoria);

        builder.setView(dialogView)
                .setTitle("➕ Agregar Nuevo Libro")
                .setPositiveButton("Guardar", (dialog, which) -> {
                    // Obtener valores de los campos
                    String titulo = etTitulo.getText().toString().trim();
                    String autor = etAutor.getText().toString().trim();
                    String isbn = etIsbn.getText().toString().trim();
                    String precioStr = etPrecio.getText().toString().trim();
                    String cantidadStr = etCantidad.getText().toString().trim();
                    String categoria = etCategoria.getText().toString().trim();

                    // Validar campos obligatorios
                    if (titulo.isEmpty() || autor.isEmpty() ||
                            precioStr.isEmpty() || cantidadStr.isEmpty()) {
                        Toast.makeText(getContext(), "⚠️ Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        // Convertir valores
                        double precio = Double.parseDouble(precioStr);
                        int cantidad = Integer.parseInt(cantidadStr);

                        // Crear objeto Libro (ID será 0, la BD lo auto-genera)
                        Libro nuevoLibro = new Libro();
                        nuevoLibro.setTitulo(titulo);
                        nuevoLibro.setAutor(autor);
                        nuevoLibro.setIsbn(isbn);
                        nuevoLibro.setPrecio(precio);
                        nuevoLibro.setCantidad(cantidad);
                        nuevoLibro.setCategoria(categoria);

                        // GUARDAR EN BASE DE DATOS
                        long idGenerado = databaseHelper.agregarLibro(nuevoLibro);

                        // Asignar el ID generado
                        nuevoLibro.setId((int) idGenerado);

                        // Agregar a la lista local
                        listaLibros.add(nuevoLibro);

                        // Notificar al adaptador
                        adapter.notifyItemInserted(listaLibros.size() - 1);

                        // Mensaje de éxito
                        Toast.makeText(getContext(),
                                "✅ Libro '" + titulo + "' agregado exitosamente",
                                Toast.LENGTH_SHORT).show();

                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(),
                                "❌ Error: Precio y cantidad deben ser números válidos",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    /**
     * Cuando se destruye el fragmento, cerrar la BD
     */
    @Override
    public void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }

    /**
     * Método público para actualizar la lista (puedes llamarlo desde otras partes)
     */
    public void actualizarLista() {
        cargarDatosDesdeBD();
    }
}