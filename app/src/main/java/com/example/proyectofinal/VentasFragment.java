package com.example.proyectofinal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.adapters.VentaAdapter;
import com.example.proyectofinal.database.DatabaseHelper;
import com.example.proyectofinal.models.Venta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentasFragment extends Fragment {

    private RecyclerView recyclerView;
    private VentaAdapter adapter;
    private List<Venta> listaVentas;
    private FloatingActionButton fabNuevaVenta;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ventas, container, false);

        recyclerView = view.findViewById(R.id.recycler_ventas);
        fabNuevaVenta = view.findViewById(R.id.fab_nueva_venta);

        databaseHelper = new DatabaseHelper(getContext());
        listaVentas = new ArrayList<>();

        adapter = new VentaAdapter(listaVentas, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        cargarVentasDesdeBD();

        fabNuevaVenta.setOnClickListener(v -> mostrarDialogoNuevaVenta());

        return view;
    }

    private void cargarVentasDesdeBD() {
        listaVentas.clear();
        listaVentas.addAll(databaseHelper.obtenerTodasLasVentas());
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogoNuevaVenta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_nueva_venta, null);

        EditText etLibro = dialogView.findViewById(R.id.et_libro_venta);
        EditText etCantidad = dialogView.findViewById(R.id.et_cantidad_venta);
        EditText etPrecio = dialogView.findViewById(R.id.et_precio_venta);
        EditText etCliente = dialogView.findViewById(R.id.et_cliente);

        Button btnBuscarLibro = dialogView.findViewById(R.id.btn_buscar_libro);
        TextView tvStock = dialogView.findViewById(R.id.tv_stock_disponible);

        btnBuscarLibro.setOnClickListener(v -> {
            String titulo = etLibro.getText().toString().trim();
            if (titulo.isEmpty()) {
                Toast.makeText(getContext(), "Escribe el título", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = databaseHelper.obtenerPrecioLibro(titulo);
            if (precio > 0) {
                etPrecio.setText(String.valueOf(precio));
                tvStock.setText("Libro encontrado");
            } else {
                tvStock.setText("Libro no encontrado");
            }
        });

        builder.setView(dialogView)
                .setTitle("Registrar venta")
                .setPositiveButton("Registrar", (d, w) ->
                        procesarVenta(etLibro, etCantidad, etPrecio, etCliente))
                .setNegativeButton("Cancelar", null)
                .show();
    }


    private void procesarVenta(EditText etLibro,
                               EditText etCantidad,
                               EditText etPrecio,
                               EditText etCliente) {

        String libro = etLibro.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String cliente = etCliente.getText().toString().trim();

        if (libro.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(getContext(),
                    "Completa los campos obligatorios",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fecha = sdf.format(new Date());

            Venta venta = new Venta(0, libro, cantidad, precio, fecha, cliente);

            long id = databaseHelper.agregarVenta(venta);
            venta.setId((int) id);

            listaVentas.add(0, venta);
            adapter.notifyItemInserted(0);
            recyclerView.scrollToPosition(0);

            Toast.makeText(getContext(),
                    "Venta registrada correctamente",
                    Toast.LENGTH_LONG).show();

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(),
                    "Cantidad y precio inválidos",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}
