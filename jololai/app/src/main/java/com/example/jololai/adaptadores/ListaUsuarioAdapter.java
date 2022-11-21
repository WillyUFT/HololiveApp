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
import com.example.jololai.crud_usuarios.VerUsuarioInfo;
import com.example.jololai.entidades.Usuarios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaUsuarioAdapter extends RecyclerView.Adapter<ListaUsuarioAdapter.usuarioViewHolder>{

    ArrayList<Usuarios> listaUsuarios;
    ArrayList<Usuarios> listaOriginal;

    public ListaUsuarioAdapter(ArrayList<Usuarios> listaUsuarios){

        this.listaUsuarios = listaUsuarios;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaUsuarios);

    }

    @NonNull
    @Override
    public ListaUsuarioAdapter.usuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_usuarios, null, false);
        return new usuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull usuarioViewHolder holder, int position) {

        holder.textoUserName.setText(listaUsuarios.get(position).getUsuario());
        holder.textoComprasRealizadas.setText(String.valueOf(listaUsuarios.get(position).getCompras_realizadas()));

      }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public void filtrado(final String txtBuscar) {
        int longitud = txtBuscar.length();
        if (longitud == 0) {
            listaUsuarios.clear();
            listaUsuarios.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Usuarios> coleccion = listaUsuarios.stream()
                        .filter(i -> i.getUsuario().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                listaUsuarios.clear();
                listaUsuarios.addAll(coleccion);
            } else {
                for (Usuarios c : listaOriginal) {
                    if (c.getUsuario().toLowerCase().contains(txtBuscar.toLowerCase())) {
                        listaUsuarios.add(c);
                    }
                }
            }
        }

        notifyDataSetChanged();

    }

    public class usuarioViewHolder extends RecyclerView.ViewHolder {

        TextView textoUserName, textoComprasRealizadas;

        public usuarioViewHolder(@NonNull View itemView) {
            super(itemView);

            textoUserName = itemView.findViewById(R.id.viewUsername);
            textoComprasRealizadas = itemView.findViewById(R.id.viewCompras);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VerUsuarioInfo.class);
                    intent.putExtra("Usuariooo", listaUsuarios.get(getAdapterPosition()).getUsuario());
                    context.startActivity(intent);
                }
            });
        }
    }

}
