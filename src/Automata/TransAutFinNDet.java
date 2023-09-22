package Automata;

import java.util.HashSet;
import java.util.Objects;

//Los estados son representados por Strings y los símbolos por char
public class TransAutFinNDet {

    private String estadoInicio;
    private HashSet<String> estadoLlegada;
    private char simbolo;
    
    //crear la transición con sus correspondientes parámetros
    public TransAutFinNDet(String estadoInicio, char simboloEntrada, HashSet<String> estadoLlegada) {
        this.estadoInicio = estadoInicio;
        this.estadoLlegada = estadoLlegada;
        this.simbolo = simboloEntrada;
    }

    //Regresa estado de inicio
    public String getEstadoInicio() {
        return estadoInicio;
    }

    //regresa el grupo de estados llegada
    public HashSet<String> getEstadosLlegada() {
        return estadoLlegada;
    }

    //Regresa el símbolo de transición
    public char getSimboloTrans() {
        return simbolo;
    }

    //utilizar toString para escribir atributos de la transición
    @Override
    public String toString() {
        String mensaje = " " + this.estadoInicio + " '" + this.simbolo + "'";

        for (String valor : this.estadoLlegada) {
            mensaje += " " + valor;
        }

        return mensaje;
    }

    //regresa el hashcode del objeto 
    //para poder compararlo con el objeto de las colecines tipo hash 
    //Hashset, hashMap
    @Override
    public int hashCode() {
        int hashCode = 5;
        hashCode = 71 * hashCode + Objects.hashCode(this.estadoInicio);
        hashCode = 71 * hashCode + Objects.hashCode(this.estadoLlegada);
        hashCode = 71 * hashCode + this.simbolo;
        return hashCode;
    }


    //regresa true sí la transición del parámetro es igual a la que invoca el método
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null) {
            return false;
        }
        if (getClass() != objeto.getClass()) {
            return false;
        }
        final TransAutFinNDet other = (TransAutFinNDet) objeto;
        if (this.simbolo != other.simbolo) {
            return false;
        }
        if (!Objects.equals(this.estadoInicio, other.estadoInicio)) {
            return false;
        }
        if (!Objects.equals(this.estadoLlegada, other.estadoLlegada)) {
            return false;
        }
        return true;
    }

}
