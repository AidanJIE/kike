package com.example.proyectofinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectofinal.database.DatabaseHelper;

public class EstadisticasFragment extends Fragment {

    private TextView tvTotalLibros;
    private TextView tvTotalVentas;
    private TextView tvIngresoTotal;
    private TextView tvLibroMasVendido;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);

        tvTotalLibros = view.findViewById(R.id.tv_total_libros);
        tvTotalVentas = view.findViewById(R.id.tv_total_ventas);
        tvIngresoTotal = view.findViewById(R.id.tv_ingreso_total);
        tvLibroMasVendido = view.findViewById(R.id.tv_libro_mas_vendido);

        cargarEstadisticas();

        return view;
    }

    private void cargarEstadisticas() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        int totalLibros = dbHelper.obtenerTotalLibros();
        int totalVentas = dbHelper.obtenerTotalVentas();
        double ingresoTotal = dbHelper.obtenerIngresosTotales();

        dbHelper.close();

        tvTotalLibros.setText(String.valueOf(totalLibros));
        tvTotalVentas.setText(String.valueOf(totalVentas));
        tvIngresoTotal.setText(String.format("$%.2f", ingresoTotal));
        tvLibroMasVendido.setText("Cálculo pendiente"); // Más adelante implementar
    }
}