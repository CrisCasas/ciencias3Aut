package Automata;

public interface Procss {
    public abstract boolean CheckEF(String estado); // True si estado es un estado final
    public abstract boolean Check(String cadena) throws Exception; // True si la cadena es reconocida
    public abstract String toString(); //NO TOCAR Muestra las transiciones y estados finales
}
