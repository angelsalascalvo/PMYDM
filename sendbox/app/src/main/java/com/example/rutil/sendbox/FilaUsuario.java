package com.example.rutil.sendbox;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FilaUsuario extends RecyclerView.Adapter<FilaUsuario.EjemploFilaUsuario> {
    private ArrayList<TransporDatos> listadoUsuarios;
    private Context context;
    MediaPlayer mpClic;

    /**
     * CONSTRUCTOR PARAMETRIZADO
     * @param lu
     */
    public FilaUsuario(ArrayList<TransporDatos> lu, Context context, MediaPlayer mpClic){
        listadoUsuarios=lu;
        this.context=context;
        this.mpClic=mpClic;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA CREAR UN OBJETO O FILA QUE ASIGNAREMOS AL RECYCLER VIEW
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public EjemploFilaUsuario onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Crear la vista con el layout correspondiente a la plantilla de transportista
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transportista, viewGroup, false);
        //Crear objeto de tipo viewHolder de la clase interna con la vista creada anteriormente
        EjemploFilaUsuario ejemplo = new EjemploFilaUsuario(view);
        //Devolver el objeto de la fila creado
        return ejemplo;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTA PARA CREAR LAS FILAS QUE SE VAN A MOSTRAR
     * Ya que recyclerView Elimina las filas al hacer scroll y las crea de nuevo para ahorrar memoria y así no tener
     * que crear todos los registros o filas de una vez
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull final EjemploFilaUsuario fila, final int i) {
        //Obtener los datos del array y agregarlos a los elementos de la fila que se mostrará
        fila.tvNombre.setText(listadoUsuarios.get(i).getNombre());
        fila.tvDni.setText(listadoUsuarios.get(i).getDni());
        fila.tvMatricula.setText(listadoUsuarios.get(i).getMatricula());
        Picasso.get().load(listadoUsuarios.get(i).getFoto()).into(fila.ivFoto); //Establecer imagen perfil

        //Acción al pulsar sobre un elemento del listado
        fila.cvTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reproduci sonido
                mpClic.start();
                //Abrir Activity del usuario
                Intent intent = new Intent(context, ActivityUsuarioInfo.class);
                intent.putExtra("uid", listadoUsuarios.get(i).getUid());
                context.startActivity(intent);
            }
        });
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER EL NUMERO DE ELEMENTOS DEL LISTADO
     * @return
     */
    @Override
    public int getItemCount() {
        return listadoUsuarios.size();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * CLASE INTERNA CON EJEMPLO DE UNA FILA DE UN USUARIO TRANSPORTISTA
     */
    public static class EjemploFilaUsuario extends RecyclerView.ViewHolder{
        public ImageView ivFoto;
        public TextView tvNombre, tvDni, tvMatricula;
        public CardView cvTransportista;

        public EjemploFilaUsuario(@NonNull View itemView) {
            super(itemView);
            //Referencias
            ivFoto = (ImageView) itemView.findViewById(R.id.ivFotoPerUsu);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombreUsu);
            tvDni = (TextView) itemView.findViewById(R.id.tvDNIUsu);
            tvMatricula = (TextView) itemView.findViewById(R.id.tvMatriculaUsu);
            cvTransportista = (CardView) itemView.findViewById(R.id.cvTransportista);
        }
    }
}