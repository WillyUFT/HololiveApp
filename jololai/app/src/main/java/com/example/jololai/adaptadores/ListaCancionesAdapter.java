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
import com.example.jololai.crud_canciones.VerCancionInfo;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Canciones;
import com.example.jololai.entidades.Idols;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ListaCancionesAdapter extends RecyclerView.Adapter<ListaCancionesAdapter.cancionViewHolder> {

    private static String UsuarioSesionActiva;

    ArrayList<Canciones> listacanciones;
    ArrayList<Canciones> listaOriginal;
    Idols Idol;
    String nombree;
    Context context;

    public ListaCancionesAdapter(ArrayList<Canciones> listacanciones) {
        this.listacanciones = listacanciones;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listacanciones);
    }


    @NonNull
    @Override
    public cancionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_cancion, null, false);
        context = parent.getContext();
        return new cancionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull cancionViewHolder holder, int position) {

        holder.textoNombre.setText(listacanciones.get(position).getNombre());
        holder.textoTipoCancion.setText(listacanciones.get(position).getTipo_cancion());

        nombreIdol(listacanciones.get(position).getId_idol());

        holder.textoIdol.setText(nombree);

    }

    public static String obtenerUsuarioActivo(String UsuarioSesionActual) {

        UsuarioSesionActiva = UsuarioSesionActual;
        return UsuarioSesionActual;

    }

    private void nombreIdol(int idIdol) {

        final DbIdols dbIdols = new DbIdols(context);
        Idol = dbIdols.verIdol(idIdol);

        nombree = Idol.getNombre();
    }

    public void filtrado(final String txtBuscar) {
        int longitud = txtBuscar.length();
        if (longitud == 0) {
            listacanciones.clear();
            listacanciones.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Canciones> coleccion = listacanciones.stream()
                        .filter(i -> i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                listacanciones.clear();
                listacanciones.addAll(coleccion);
            } else {
                for (Canciones c : listaOriginal) {
                    if (c.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())) {
                        listacanciones.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listacanciones.size();
    }

    public class cancionViewHolder extends RecyclerView.ViewHolder {

        TextView textoNombre, textoTipoCancion, textoIdol;

        public cancionViewHolder(@NonNull View itemView) {
            super(itemView);

            textoNombre = itemView.findViewById(R.id.viewNombreCancion);
            textoTipoCancion = itemView.findViewById(R.id.viewTipoCancion);
            textoIdol = itemView.findViewById(R.id.viewCantante);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VerCancionInfo.class);
                    intent.putExtra("ID", listacanciones.get(getAdapterPosition()).getId());
                    intent.putExtra("SesionActual", UsuarioSesionActiva);
                    context.startActivity(intent);
                }
            });
        }
    }

}
