package com.example.jololai.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jololai.R;
import com.example.jololai.crud_videos.VerVideoInfo;
import com.example.jololai.db.DbVideos;
import com.example.jololai.entidades.RepVideos;
import com.example.jololai.entidades.Usuarios;
import com.example.jololai.entidades.Videos;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

public class ListaRepVideosAdapter extends RecyclerView.Adapter<ListaRepVideosAdapter.repVideoViewHolder> {

    ArrayList<RepVideos> listaRepVideos;
    ArrayList<RepVideos> listaOriginal;

    Videos Video;

    String nombreVideo;

    Context context;

    public ListaRepVideosAdapter(ArrayList<RepVideos> listaRepVideos) {
        this.listaRepVideos = listaRepVideos;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaRepVideos);
    }

    private void nombreVideo(int idVideo) {

        final DbVideos dbVideos = new DbVideos(context);

        Log.e("id video", String.valueOf(idVideo));

        Log.e("existe o no", String.valueOf(dbVideos.VerExisteVideo(idVideo)));

        if (dbVideos.VerExisteVideo(idVideo) == true){
            Video = dbVideos.verVideo(idVideo);
            nombreVideo = Video.getNombreVideo();
        } else {
            nombreVideo = "Video eliminado";
        }
    }

    @NonNull
    @Override
    public ListaRepVideosAdapter.repVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_rep_video, null, false);
        context = parent.getContext();
        return new ListaRepVideosAdapter.repVideoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ListaRepVideosAdapter.repVideoViewHolder holder, int position) {

        nombreVideo(listaRepVideos.get(position).getId_video());
        holder.textoNombreVideo.setText(nombreVideo);

        holder.textoNombreUsuario.setText(listaRepVideos.get(position).getNombre_usuario());
        holder.textoMes.setText(listaRepVideos.get(position).getMes());

    }

    @Override
    public int getItemCount() {
        return listaRepVideos.size();
    }

    public class repVideoViewHolder extends RecyclerView.ViewHolder {

        TextView textoNombreVideo, textoNombreUsuario, textoMes;

        public repVideoViewHolder(@NonNull View itemView) {
            super(itemView);

            textoNombreVideo = itemView.findViewById(R.id.viewNombreVideoRep);
            textoNombreUsuario = itemView.findViewById(R.id.viewUsuarioRep);
            textoMes = itemView.findViewById(R.id.viewMesRep);

        }
    }
}
