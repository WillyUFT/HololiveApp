package com.example.jololai.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jololai.R;
import com.example.jololai.crud_idols.VerIdolInfo;
import com.example.jololai.entidades.Idols;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaIdolsAdapter extends RecyclerView.Adapter<ListaIdolsAdapter.idolViewHolder> {

    private static String UsuarioSesionActiva;
    ArrayList<Idols> listaidols;
    ArrayList<Idols> listaOriginal;

    public ListaIdolsAdapter(ArrayList<Idols> listaidols) {

        this.listaidols = listaidols;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaidols);

    }

    @NonNull
    @Override
    public idolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_idol, null, false);
        return new idolViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull idolViewHolder holder, int position) {
        holder.textoNombre.setText(listaidols.get(position).getNombre());
        holder.textoUnidad.setText(listaidols.get(position).getUnidad());
        holder.textoGeneracion.setText(listaidols.get(position).getGeneracion());

    }

    public static String obtenerUsuarioActivo(String UsuarioSesionActual) {

        UsuarioSesionActiva = UsuarioSesionActual;
        return UsuarioSesionActual;

    }

    public void filtrado(final String txtBuscar) {
        int longitud = txtBuscar.length();
        if (longitud == 0) {
            listaidols.clear();
            listaidols.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Idols> coleccion = listaidols.stream()
                        .filter(i -> i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                listaidols.clear();
                listaidols.addAll(coleccion);
            } else {
                for (Idols c : listaOriginal) {
                    if (c.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())) {
                        listaidols.add(c);
                    }
                }
            }
        }

        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return listaidols.size();
    }

    public class idolViewHolder extends RecyclerView.ViewHolder {

        TextView textoNombre, textoUnidad, textoGeneracion;

        public idolViewHolder(@NonNull View itemView) {
            super(itemView);

            textoNombre = itemView.findViewById(R.id.viewNombre);
            textoUnidad = itemView.findViewById(R.id.viewUnidad);
            textoGeneracion = itemView.findViewById(R.id.viewGeneracion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VerIdolInfo.class);
                    intent.putExtra("ID", listaidols.get(getAdapterPosition()).getId());
                    intent.putExtra("SesionActual", UsuarioSesionActiva);
                    context.startActivity(intent);
                }
            });
        }
    }
}
