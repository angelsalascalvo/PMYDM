package com.example.rutil.sendbox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FilaPaquete extends RecyclerView.Adapter<FilaPaquete.EjemploFilaPaquete> {
    ArrayList<PaqueteDatos> listadoPaquetes;

    /**
     * CONSTRUCTOR PARAMETRIZADO
     * @param lp
     */
    public FilaPaquete(ArrayList<PaqueteDatos> lp){
        listadoPaquetes=lp;
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
    public EjemploFilaPaquete onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Crear la vista con el layout correspondiente a la plantilla de productos
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.paquete, viewGroup, false);
        //Crear objeto de tipo viewHolder de la clase interna con la vista creada anteriormente
        EjemploFilaPaquete ejemplo = new EjemploFilaPaquete(view);
        //Devolver el objeto de la fila creado
        return ejemplo;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTA AL HACER SCROLL EN EL RECYCLER VIEW PARA CREAR LAS NUEVAS FILAS QUE SE VAN A MOSTRAR
     * Ya que recyclerView Elimina las filas al hacer scroll y las crea de nuevo para ahorrar memoria y así no tener
     * que crear todos los registros o filas de una vez
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull EjemploFilaPaquete fila, final int i) {
        //Obtener los datos del array y agregarlos a los elementos de la fila que se mostrará
        fila.tvCodigo.setText(listadoPaquetes.get(i).getCodigo());
        fila.tvDireccion.setText(listadoPaquetes.get(i).getDireccion());
        fila.tvNombre.setText(listadoPaquetes.get(i).getNombre());
        fila.tvEstado.setText(listadoPaquetes.get(i).getEstado());
        fila.ivEstado.setImageResource(listadoPaquetes.get(i).getImgEstado());

        //Poner a la escucha el elemento
        fila.lyPaquete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ocultar las opciones de todos los elementos
                for(int j = 0; j<listadoPaquetes.size(); j++){
                    if(i!=j)
                    listadoPaquetes.get(j).setMostrarOp(false);
                }
                //En funcion de como este el menu se oculta o se muestra
                if(listadoPaquetes.get(i).isMostrarOp())
                    listadoPaquetes.get(i).setMostrarOp(false);
                else
                    listadoPaquetes.get(i).setMostrarOp(true);
                //Indicar que se han producido cambios
                notifyDataSetChanged();
            }
        });

        //Mostrar opciones si esta activo
        if(listadoPaquetes.get(i).isMostrarOp())
            fila.lyAcciones.setVisibility(View.VISIBLE);
        else
            fila.lyAcciones.setVisibility(View.GONE);

    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER EL NUMERO DE ELEMENTOS DEL LISTADO
     * @return
     */
    @Override
    public int getItemCount() {
        return listadoPaquetes.size();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * CLASE INTERNA CON EJEMPLO DE UNA FILA DE UN PAQUETE
     */
    public static class EjemploFilaPaquete extends RecyclerView.ViewHolder{
        public ImageView ivEstado;
        public TextView tvCodigo, tvNombre, tvDireccion, tvEstado;
        public LinearLayout lyAcciones, lyPaquete;

        public EjemploFilaPaquete(@NonNull View itemView) {
            super(itemView);
            //Referencias
            ivEstado = (ImageView) itemView.findViewById(R.id.ivEstado);
            tvCodigo = (TextView) itemView.findViewById(R.id.tvCodigo);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
            tvDireccion = (TextView) itemView.findViewById(R.id.tvDireccion);
            tvEstado = (TextView) itemView.findViewById(R.id.tvEstado);
            lyAcciones = (LinearLayout) itemView.findViewById(R.id.lyAcciones);
            lyPaquete = (LinearLayout) itemView.findViewById(R.id.lyPaquete);
        }
    }
}
