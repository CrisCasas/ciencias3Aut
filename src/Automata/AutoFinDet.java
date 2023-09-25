package Automata;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
// maneja el automata Finito Determinista
public class AutoFinDet implements Cloneable, Procss {

    private HashSet<String> estadosFinales;
    private String estadoInicial = "";
    private HashSet<TransicionAFD> transiciones;

    public AutoFinDet() {
        this.transiciones = new HashSet();
        this.estadosFinales = new HashSet();
    }

   //Agrega la transición al autómata
    public void agregarTransicion(String e1, char simbolo, String e2) {
        this.transiciones.add(new TransicionAFD(e1, simbolo, e2));
    }

   //Agrega la transición pasada por parámetro al autómata
    public void aniadirTransicion(TransicionAFD trans) {
        this.transiciones.add(trans);
    }

   //Obtiene el estado destino de una transición del autómata
    public String getTransicion(String estado, char simbolo) {
        for (TransicionAFD t : this.transiciones) {
            if (t.getEstadoO().equals(estado) && t.getSimbolo() == simbolo) {
                return t.getEstadoD();
            }
        }

        return "";
    }

   //setters && getters
    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public void setEstadosFinales(HashSet<String> estadosFinales) {
        this.estadosFinales = estadosFinales;
    }

    public HashSet<String> getEstadosFinales() {
        return estadosFinales;
    }

    public HashSet<TransicionAFD> getTransiciones() {
        return transiciones;
    }

    //Agrega el estado final pasado por parámetro al autómata
    public void addEstadoFinal(String estadoFinal) {
        this.estadosFinales.add(estadoFinal);
    }

    @Override
    public boolean CheckEF(String estado) {
        return this.estadosFinales.contains(estado);//true si pertenece
    }

    //Elimina el símbolo de entrada pasado por parámetro del autómata
    public void eliminarSimbolo(char s) //Elimina las transiciones que usan ese simbolo
    {
        for (TransicionAFD t : this.transiciones) {
            if (t.getSimbolo() == s) {
                this.transiciones.remove(t);
            }
        }
    }

    //Elimina el estado pasado por parámetro del autómata
    public void eliminarEstado(String e) //Eliminar las transiciones que usan ese estado
    {
        HashSet<TransicionAFD> eliminar = new HashSet();
        for (TransicionAFD t : this.transiciones) {
            if (t.getEstadoO().equals(e) || t.getEstadoD().equals(e)) //Si aparece en el origen o destino de una transicion
            {
                eliminar.add(t);    //La eliminamos
            }
        }
        this.transiciones.removeAll(eliminar);
    }

    //Elimina la transición pasada por parámetro
    public void eliminarTransicion(TransicionAFD t) {
        this.transiciones.remove(t);
    }

    //simula como funciona el automata recorriendo utilizando  la cadena de entrada
    //y como llega al siguiente estado de acuerdo a las transiciones
    //si al llegar al último estado es de aceptación la cadena es reconocida
    @Override
    public boolean Check(String cadena) throws Exception {
        //CONTROL DE EXCEPCIONES
        if (this.estadoInicial.equals("")) {
            throw new Exception("Error: no ha indicado ningún estado inicial!");
        }
        if (this.getEstadosFinales().isEmpty()) {
            throw new Exception("Error: no ha indicado ningún estado final!");
        }
        
        char[] simbolo = cadena.toCharArray();
        String estado = this.getEstadoInicial();

        for (int i = 0; i < simbolo.length; i++) {
            estado = getTransicion(estado, simbolo[i]);
            if (estado.equals("")) {
                throw new Exception("Error: transicion con caracter '" + simbolo[i] + "' no válida!");
            }
        }

        return CheckEF(estado);//retorna true en caso tal que la cadena es reconocida
        //excepción sino funciona
    }

   //view automata txt
    @Override
    public String toString() {
        String mensaje = "";
        HashSet<String> estados = new HashSet();

        mensaje += "ESTADOS\n";

        for (TransicionAFD t : this.transiciones) {
            estados.add(t.getEstadoO());
            estados.add(t.getEstadoD());
        }

        mensaje += "ESTADO INICIAL: " + this.estadoInicial + "\n";
        mensaje += "ESTADOS FINALES: \n";
        for (String e : estadosFinales) {
            mensaje += e;
        }

        for (String e : estados) {
            mensaje += e + "\n";
        }

        mensaje += "\nTRANSICIONES:\n";
        for (TransicionAFD t : this.transiciones) {
            mensaje += t + "\n";
        }

        return mensaje;
    }
    // logica AFD
    public static void main(String[] args) {
        AutoFinDet automata = new AutoFinDet();

        automata.estadosFinales.add("q1");

        automata.agregarTransicion("q0", '1', "q0");
        automata.agregarTransicion("q0", '0', "q2");
        automata.agregarTransicion("q2", '0', "q2");
        automata.agregarTransicion("q2", '1', "q1");
        automata.agregarTransicion("q1", '0', "q1");
        automata.agregarTransicion("q1", '1', "q1");

        System.out.println(automata);

        automata.setEstadoInicial("q0");

        try {
            if (automata.Check("101")) {
                System.out.println("RECONOCIDO");
            } else {
                System.out.println("NO RECONOCIDO");
            }
        } catch (Exception ex) {
            Logger.getLogger(AutoFinDet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Devuelve una copia del autómata. Se crean nuevos objetos, no se clonan las referencias
    @Override
    public Object clone() throws CloneNotSupportedException {
        AutoFinDet aux = null;
        try {
            aux = (AutoFinDet) super.clone(); //Hace una copia binaria de los objetos
        } catch (CloneNotSupportedException ex) {
            System.out.println("Clone no soportado");
        }
        //Como el clone de HashSet hace solo una copia superficial, tenemos que copia a mano los elementos
        aux.estadosFinales = new HashSet<>();
        for (String estado : this.estadosFinales) {
            aux.estadosFinales.add(estado);
        }

        aux.transiciones = new HashSet<TransicionAFD>();
        for (TransicionAFD t : this.transiciones) {
            aux.transiciones.add(t);
        }

        return aux;
    }
}
