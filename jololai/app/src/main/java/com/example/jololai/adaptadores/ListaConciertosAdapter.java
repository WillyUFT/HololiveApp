package com.example.jololai.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.system.StructUtsname;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jololai.R;
import com.example.jololai.crud_canciones.VerCancionInfo;
import com.example.jololai.crud_conciertos.VerConciertoInfo;
import com.example.jololai.db.DbIdols;
import com.example.jololai.entidades.Conciertos;
import com.example.jololai.entidades.Idols;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaConciertosAdapter extends RecyclerView.Adapter<ListaConciertosAdapter.conciertoViewHolder> {

    private static String UsuarioSesionActiva;

    ArrayList<Conciertos> listaConciertos;
    ArrayList<Conciertos> listaOriginal;

    Idols Idol;
    String nombree;
    Context context;

    public ListaConciertosAdapter(ArrayList<Conciertos> listaConciertos) {
        this.listaConciertos = listaConciertos;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaConciertos);
    }

    @NonNull
    @Override
    public conciertoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_conciertos, null, false);
        context = parent.getContext();
        return new conciertoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull conciertoViewHolder holder, int position) {

        holder.textoNombre.setText(listaConciertos.get(position).getNombre_Concierto());
        Log.e("aa", listaConciertos.get(position).getNombre_Concierto().toString());
        holder.textoDuracion.setText(listaConciertos.get(position).getDuracion_Minutos().toString());
        Log.e("bb", listaConciertos.get(position).getDuracion_Minutos().toString());
        nombreIdol(listaConciertos.get(position).getID_Idol());

        holder.textoIdol.setText(nombree);

    }

    public static String obtenerUsuarioActivo(String UsuarioSesionActual) {

        UsuarioSesionActiva = UsuarioSesionActual;
        Log.e("usuarioActivoAdapter", String.valueOf(UsuarioSesionActiva));
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
            listaConciertos.clear();
            listaConciertos.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Conciertos> coleccion = listaConciertos.stream()
                        .filter(i -> i.getNombre_Concierto().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                listaConciertos.clear();
                listaConciertos.addAll(coleccion);
            } else {
                for (Conciertos c : listaOriginal) {
                    if (c.getNombre_Concierto().toLowerCase().contains(txtBuscar.toLowerCase())) {
                        listaConciertos.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaConciertos.size();
    }

    public class conciertoViewHolder extends RecyclerView.ViewHolder {

        TextView textoNombre, textoDuracion, textoIdol;

        public conciertoViewHolder(@NonNull View itemView) {
            super(itemView);

            textoNombre = itemView.findViewById(R.id.viewNombreConcierto);
            textoDuracion = itemView.findViewById(R.id.viewDuracionConcierto);
            textoIdol = itemView.findViewById(R.id.viewCantanteConcierto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VerConciertoInfo.class);
                    intent.putExtra("ID", listaConciertos.get(getAdapterPosition()).getID_Concierto());
                    intent.putExtra("SesionActual", UsuarioSesionActiva);
                    context.startActivity(intent);
                }
            });
        }
    }

}
