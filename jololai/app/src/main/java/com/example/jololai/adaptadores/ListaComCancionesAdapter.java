package com.example.jololai.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jololai.R;
import com.example.jololai.db.DbCanciones;
import com.example.jololai.db.DbVideos;
import com.example.jololai.entidades.ComCanciones;
import com.example.jololai.entidades.Canciones;

import java.util.ArrayList;

public class ListaComCancionesAdapter extends RecyclerView.Adapter<ListaComCancionesAdapter.comCancionViewHolder> {

    ArrayList<ComCanciones> listaComCanciones;
    ArrayList<ComCanciones> listaOriginal;

    Canciones Cancion;

    String nombreCancion;

    Context context;

    public ListaComCancionesAdapter(ArrayList<ComCanciones> listaComCanciones) {
        this.listaComCanciones = listaComCanciones;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaComCanciones);
    }

    private void nombreCancion(int idCancion) {

        final DbCanciones dbCanciones = new DbCanciones(context);

        if (dbCanciones.VerExisteCancion(idCancion) == true){
            Cancion = dbCanciones.verCancion(idCancion);
            nombreCancion = Cancion.getNombre();
        } else {
            nombreCancion = "Canción eliminada";
        }
    }

    @NonNull
    @Override
    public ListaComCancionesAdapter.comCancionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_com_cancion, null, false);
        context = parent.getContext();
        return new ListaComCancionesAdapter.comCancionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ListaComCancionesAdapter.comCancionViewHolder holder, int position) {

        nombreCancion(listaComCanciones.get(position).getId_cancion());
        holder.textoNombreCancion.setText(nombreCancion);

        holder.textoNombreUsuario.setText(listaComCanciones.get(position).getNombre_usuario());
        holder.textoMes.setText(listaComCanciones.get(position).getMes());

    }

    @Override
    public int getItemCount() {
        return listaComCanciones.size();
    }

    public class comCancionViewHolder extends RecyclerView.ViewHolder {

        TextView textoNombreCancion, textoNombreUsuario, textoMes;

        public comCancionViewHolder(@NonNull View itemView) {
            super(itemView);

            textoNombreCancion = itemView.findViewById(R.id.viewNombreCancionCom);
            textoNombreUsuario = itemView.findViewById(R.id.viewUsuarioCom);
            textoMes = itemView.findViewById(R.id.viewMesCom);

        }
    }
}
