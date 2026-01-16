package com.example.proyectofinal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.adapters.LibroAdapter;
import com.example.proyectofinal.database.DatabaseHelper;
import com.example.proyectofinal.models.Libro;

import java.util.ArrayList;
import java.util.List;

public class BusquedaFragment extends Fragment {

    private EditText etBuscar;
    private RecyclerView recyclerView;
    private LibroAdapter adapter;
    private List<Libro> listaLibros;
    private List<Libro> listaFiltrada;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busqueda, container, false);

        etBuscar = view.findViewById(R.id.et_buscar);
        recyclerView = view.findViewById(R.id.recycler_busqueda);

        listaLibros = new ArrayList<>();
        listaFiltrada = new ArrayList<>();
        cargarDatos();

        adapter = new LibroAdapter(listaFiltrada, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarLibros(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void cargarDatos() {
        // Limpiar lista
        listaLibros.clear();

        // Cargar desde la base de datos
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        listaLibros.addAll(dbHelper.obtenerTodosLosLibros());
        dbHelper.close();
    }

    private void filtrarLibros(String texto) {
        listaFiltrada.clear();

        if (texto.isEmpty()) {
            // Si no hay texto, mostrar todos los libros de la BD
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            listaFiltrada.addAll(dbHelper.obtenerTodosLosLibros());
            dbHelper.close();
        } else {
            // Buscar en la base de datos
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            listaFiltrada.addAll(dbHelper.buscarLibros(texto));
            dbHelper.close();
        }

        adapter.notifyDataSetChanged();
    }
}