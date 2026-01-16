package com.example.proyectofinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.models.Venta;

import java.util.List;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.VentaViewHolder> {

    private List<Venta> listaVentas;
    private Context context;

    public VentaAdapter(List<Venta> listaVentas, Context context) {
        this.listaVentas = listaVentas;
        this.context = context;
    }

    @NonNull
    @Override
    public VentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta, parent, false);
        return new VentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VentaViewHolder holder, int position) {
        Venta venta = listaVentas.get(position);

        holder.tvLibro.setText(venta.getTituloLibro());
        holder.tvCliente.setText("Cliente: " + venta.getCliente());
        holder.tvCantidad.setText("Cantidad: " + venta.getCantidad());
        holder.tvTotal.setText(String.format("Total: $%.2f", venta.getTotal()));
        holder.tvFecha.setText(venta.getFecha());
    }

    @Override
    public int getItemCount() {
        return listaVentas.size();
    }

    public static class VentaViewHolder extends RecyclerView.ViewHolder {
        TextView tvLibro, tvCliente, tvCantidad, tvTotal, tvFecha;

        public VentaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLibro = itemView.findViewById(R.id.tv_libro_venta);
            tvCliente = itemView.findViewById(R.id.tv_cliente_venta);
            tvCantidad = itemView.findViewById(R.id.tv_cantidad_venta);
            tvTotal = itemView.findViewById(R.id.tv_total_venta);
            tvFecha = itemView.findViewById(R.id.tv_fecha_venta);
        }
    }
}