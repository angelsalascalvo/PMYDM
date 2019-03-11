package com.example.rutil.sendbox;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FilaPaquete extends RecyclerView.Adapter<FilaPaquete.EjemploFilaPaquete> {
    private ArrayList<PaqueteDatos> listadoPaquetes;
    private boolean editar;
    private MediaPlayer mpBorrar, mpClic;

    /**
     * CONSTRUCTOR PARAMETRIZADO
     * @param lp
     */
    public FilaPaquete(ArrayList<PaqueteDatos> lp, boolean editar, MediaPlayer mpBorrar, MediaPlayer mpClic){
        listadoPaquetes=lp;
        this.editar=editar;
        this.mpBorrar=mpBorrar;
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
    public EjemploFilaPaquete onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Crear la vista con el layout correspondiente a la plantilla de paquete
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.paquete, viewGroup, false);
        //Crear objeto de tipo viewHolder de la clase interna con la vista creada anteriormente
        EjemploFilaPaquete ejemplo = new EjemploFilaPaquete(view);
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
    public void onBindViewHolder(@NonNull final EjemploFilaPaquete fila, final int i) {
        //Obtener los datos del array y agregarlos a los elementos de la fila que se mostrará
        fila.tvCodigo.setText(listadoPaquetes.get(i).getCodigo());
        fila.tvDireccion.setText(listadoPaquetes.get(i).getDireccion());
        fila.tvNombre.setText(listadoPaquetes.get(i).getNombre());
        fila.tvEstado.setText(listadoPaquetes.get(i).getEntregado());
        fila.ivEstado.setImageResource(listadoPaquetes.get(i).getImgEstado());
        fila.ibEstado.setImageResource(listadoPaquetes.get(i).getImagenButtonEstado());
        fila.ibEstado.setBackgroundColor(listadoPaquetes.get(i).getColorEstado());
        //Si se puede editar activamos los botones
        if(editar) {
            //Accion al presionar la fila
            fila.lyPaquete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Ocultar las opciones de todos los elementos
                    for (int j = 0; j < listadoPaquetes.size(); j++) {
                        if (i != j)
                            listadoPaquetes.get(j).setMostrarOp(false);
                    }
                    //En funcion de como este el menu se oculta o se muestra
                    if (listadoPaquetes.get(i).isMostrarOp())
                        listadoPaquetes.get(i).setMostrarOp(false);
                    else
                        listadoPaquetes.get(i).setMostrarOp(true);
                    //Indicar que se han producido cambios
                    notifyDataSetChanged();
                }
            });

            //Mostrar opciones si esta activo
            if (listadoPaquetes.get(i).isMostrarOp())
                fila.lyAcciones.setVisibility(View.VISIBLE);
            else
                fila.lyAcciones.setVisibility(View.GONE);

            //Accion al presionar el boton estado
            fila.ibEstado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Reproducir sonido
                    mpClic.start();
                    //Si el paquete no esta entregado se marca como entregado y al contrario
                    if (listadoPaquetes.get(i).getEntregado().equalsIgnoreCase(ActivityTranspor.sPendiente))
                        ActivityTranspor.entregarPaquete(listadoPaquetes.get(i).getCodigo());
                    else
                        ActivityTranspor.pendientePaquete(listadoPaquetes.get(i).getCodigo());

                    //Indicar que se han producido cambios
                    notifyDataSetChanged();
                }
            });

            //Accion al presionar el boton borrado
            fila.ibBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mpBorrar.start();
                    ActivityTranspor.borrarPaquete(listadoPaquetes.get(i).getCodigo());
                }
            });
        }else{
            fila.lyAcciones.setVisibility(View.GONE);
        }
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
        public ImageButton ibEstado, ibBorrar;

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
            ibEstado =(ImageButton) itemView.findViewById(R.id.ibEstado);
            ibBorrar=(ImageButton) itemView.findViewById(R.id.ibBorrar);
        }
    }
}
