package Automata;

import java.util.Objects;

public class TransicionAFD {

    private String estadoO;
    private String estadoD;
    private char simbolo;

   //Crea
    public TransicionAFD(String e1, char simbolo, String e2) {
        this.estadoO = e1;
        this.estadoD = e2;
        this.simbolo = simbolo;
    }
    //devuelve el origen
    public String getEstadoO() {
        return estadoO;
    }

    //Devuelve destino
    public String getEstadoD() {
        return estadoD;
    }

    public char getSimbolo() {
        return simbolo;
    }
    //view txt de T
    @Override
    public String toString() {
        return (" " + this.estadoO + " '" + this.simbolo + "' " + this.estadoD);
    }

    //Devuvelve el código hash del objeto, usado para ser comparado con otro objeto en colecciones (HashSet, HashMap..)
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.estadoO);
        hash = 97 * hash + Objects.hashCode(this.estadoD);
        hash = 97 * hash + this.simbolo;
        return hash;
    }

    //Devuelve verdadero si la transición pasada por parámetro equivale a laque invoca el método
   
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TransicionAFD other = (TransicionAFD) obj;
        if (this.simbolo != other.simbolo) {
            return false;
        }
        if (!Objects.equals(this.estadoO, other.estadoO)) { //Además nos fijamos en los estado origen y destino
            return false;
        }
        if (!Objects.equals(this.estadoD, other.estadoD)) {
            return false;
        }
        return true;
    }

}
