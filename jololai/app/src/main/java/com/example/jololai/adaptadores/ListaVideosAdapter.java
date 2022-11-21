package com.example.jololai.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jololai.R;
import com.example.jololai.crud_videos.VerVideoInfo;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Videos;
import com.example.jololai.entidades.Idols;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaVideosAdapter extends RecyclerView.Adapter<ListaVideosAdapter.videoViewHolder> {

    private static String UsuarioSesionActiva;

    ArrayList<Videos> listavideos;
    ArrayList<Videos> listaOriginal;
    Idols Idol;
    String nombree;
    Context context;

    public ListaVideosAdapter(ArrayList<Videos> listavideos) {
        this.listavideos = listavideos;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listavideos);
    }


    @NonNull
    @Override
    public videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_video, null, false);
        context = parent.getContext();
        return new videoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull videoViewHolder holder, int position) {

        holder.textoNombreVideo.setText(listavideos.get(position).getNombreVideo());
        holder.textoGenero.setText(listavideos.get(position).getGenero_video());

        nombreIdol(listavideos.get(position).getId_idol());

        holder.textoIdolVideo.setText(nombree);

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
            listavideos.clear();
            listavideos.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Videos> coleccion = listavideos.stream()
                        .filter(i -> i.getNombreVideo().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                listavideos.clear();
                listavideos.addAll(coleccion);
            } else {
                for (Videos c : listaOriginal) {
                    if (c.getNombreVideo().toLowerCase().contains(txtBuscar.toLowerCase())) {
                        listavideos.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listavideos.size();
    }

    public class videoViewHolder extends RecyclerView.ViewHolder {

        TextView textoNombreVideo, textoGenero, textoIdolVideo;

        public videoViewHolder(@NonNull View itemView) {
            super(itemView);

            textoNombreVideo = itemView.findViewById(R.id.viewNombreVideo);
            textoGenero = itemView.findViewById(R.id.viewGeneroVideo);
            textoIdolVideo = itemView.findViewById(R.id.viewIdolcitasVideo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VerVideoInfo.class);
                    intent.putExtra("ID", listavideos.get(getAdapterPosition()).getId());
                    intent.putExtra("SesionActual", UsuarioSesionActiva);
                    context.startActivity(intent);
                }
            });
        }
    }

}
