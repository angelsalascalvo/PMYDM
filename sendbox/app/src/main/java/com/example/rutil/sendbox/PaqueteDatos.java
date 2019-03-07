package com.example.rutil.sendbox;

public class PaqueteDatos {
    private int imgEstado;
    private String codigo;
    private String nombre;
    private String direccion;
    private String estado;
    private boolean mostrarOp;

    /**
     * CONSTRUCTOR POR DEFECTO
     */
    public PaqueteDatos(){
    }

    /**
     * CONSTRUCTOR PARAMETRIZADO
     * @param imgEstado
     * @param codigo
     * @param nombre
     * @param direccion
     * @param estado
     */
    public PaqueteDatos(int imgEstado, String codigo, String nombre, String direccion, String estado) {
        this.imgEstado = imgEstado;
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.estado = estado;
        this.mostrarOp = false;
    }

    //----------------------------------------------------------------------------------------------

    //METODOS GET AND SET

    public int getImgEstado() {
        return imgEstado;
    }

    public void setImgEstado(int imgEstado) {
        this.imgEstado = imgEstado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isMostrarOp() {
        return mostrarOp;
    }

    public void setMostrarOp(boolean mostrarOp) {
        this.mostrarOp = mostrarOp;
    }
}
