package Automata;

import java.util.HashSet;
import java.util.Objects;

//Lo estados son representados por cadenas de caracteres
public class TransLamda {

    private String estadoInicio;
    private HashSet<String> estadosLlegadas;

    //Crear la transición con los parámetro indicados
    public TransLamda(String estdadoInicio, HashSet<String> estadosLlegada) {
        this.estadoInicio = estdadoInicio;
        this.estadosLlegadas = estadosLlegada;
    }
  
    //Regresa el estado de inicio
    public String getEstadoInicio() {
        return estadoInicio;
    }
    
    //Regresa el grupo de estados llegada
    public HashSet<String> getEstadosLlegada() {
        return estadosLlegadas;
    }

    //utilizar toString para escribir atributos de la transición
    @Override
    public String toString() {
        String info = " " + this.estadoInicio;

        for (String valor : this.estadosLlegadas) {
            info += " " + valor;
        }

        return info;
    }

    //regresa el hashcode del objeto 
    //para poder compararlo con el objeto de las colecines tipo hash 
    //Hashset, hashMap
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.estadoInicio);
        hash = 59 * hash + Objects.hashCode(this.estadosLlegadas);
        return hash;
    }


    //regresa true si la transición de párametro es igual a la del método
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
        final TransLamda other = (TransLamda) objeto;
        if (!Objects.equals(this.estadoInicio, other.estadoInicio)) {
            return false;
        }
        if (!Objects.equals(this.estadosLlegadas, other.estadosLlegadas)) {
            return false;
        }
        return true;
    }

}
