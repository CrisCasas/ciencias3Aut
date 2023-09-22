package Automata;

import java.util.HashSet;

public class AutoFinNoDet implements Cloneable, Proceso {

    private HashSet<String> estadosDeFin;
    private String estadoDeInicio = "";
    private HashSet<TransAutFinNDet> transicion;
    private HashSet<TransLamda> transLamda;

    //Método Constructor
    public AutoFinNoDet() {
        this.estadosDeFin = new HashSet();
        this.transicion = new HashSet();
        this.transLamda = new HashSet();
    }

    //Fijar los estados de inicio del automata
    public void setEstadoInicio(String estadoInicial) {
        this.estadoDeInicio = estadoInicial;
    }

    //Fijar los estados de fin del automata
    public void setEstadosDeFin(HashSet<String> estadosFinales) {
        this.estadosDeFin = estadosFinales;
    }

    
    
    // Fijar las transiciones pasadas por parámtero a el autómata
    public void setTransicion(HashSet<TransAutFinNDet> transicion) {
        this.transicion = transicion;
    }

    
    // Fijar las transiciones lamda pasadas por parámtero a el autómata
    public void setTransLamda(HashSet<TransLamda> transicionesL) {
        this.transLamda = transicionesL;
    }

    //añade la transición creada en los parámetros del autómata 
    public void aniadirTransicion(String estadoIni, char simbolo, HashSet estadoFin) {
        this.transicion.add(new TransAutFinNDet(estadoIni, simbolo, estadoFin));
    }

    //añade la transición al autómata por parámero.
    public void aniadirTransicion(TransAutFinNDet trans) {
        this.transicion.add(trans);
    }


    //Añade la transición lamda al autómata pasada por parámetro
    public void aniadirTransicionL(String e1, HashSet e2) {
        this.transLamda.add(new TransLamda(e1, e2));
    }

    //añadé a él automata la transición lamda pasada por parámetro
    public void aniadirTransLamda(TransLamda trans) {
        this.transLamda.add(trans);
    }


    //trae el estado de inicio del autómata
    public String getEstInicio() {
        return estadoDeInicio;
    }

    //trae las transiciónes del autómata 
    public HashSet<TransAutFinNDet> getTransiciones() {
        return transicion;
    }

    //trae las transiciones lamda del autómata
    public HashSet<TransLamda> getTransLamda() {
        return transLamda;
    }

    //trae los estados finales
    public HashSet<String> getEstadosFin() {
        return estadosDeFin;
    }


    //trae el estado de llegada de la transición
    public HashSet<String> getTransicion(String estInicio, char simboloEntrada) {
        for (TransAutFinNDet t : this.transicion) {
            if (t.getEstadoInicio().equals(estInicio) && t.getSimboloTrans() == simboloEntrada) {
                return t.getEstadosLlegada();
            }
        }

        return new HashSet<>(); 
    }


    //Regresa los estados de inicio en los que avanza el autómata
    public HashSet<String> getTransicion(HashSet<String> estadosInicio, char simboloEntrada) {
        HashSet<String> respuesta = new HashSet();

        for (String estado : estadosInicio) {
            for (String estado2 : this.getTransicion(estado, simboloEntrada)) {
                respuesta.add(estado2);
            }
        }

        return respuesta;//devuelve los estados llegada
    }

    //regresa el estado inicio del autómata con una transicion lamda desde el inicio pasado por parámetro
    public HashSet<String> getTransicionL(String estadoInicio) {
        for (TransLamda respuesta : this.transLamda) {
            if (respuesta.getEstadoInicio().equals(estadoInicio)) {
                return respuesta.getEstadosLlegada();
            }
        }

        return new HashSet(); //estadoLlegada
    }


    //Regresa true sí el estado pasado en el parámetro es un estadoFinalo de Aceptación
    public boolean esFin(String estado) {
        return this.estadosDeFin.contains(estado);
    }


    //Regresa True si los estados pasados por parámetro
    //son estados finales o de aceptación
    public boolean esFin(HashSet<String> estadosAevaluar) {
        for (String estado : estadosAevaluar) {
            if (this.esFin(estado)) {
                return true;
            }
        }

        return false;
    }

    //Regresa la clausuarLamda del estado anteriormente pasado por parámetro
    //que son los estados de llegada, por los que pasa con transiciones lamda
    //desde el estado de inicio pasado por parametro
    public HashSet<String> lamdaClausura(String estadoInicio) {
        HashSet<String> respuesta = new HashSet<>();
        respuesta.add(estadoInicio);                   //Agregamos el estado actual

        transLamda.forEach((transLamda) -> { //Recorremos las transiciones lamda
            if (transLamda.getEstadoInicio().equals(estadoInicio)) { //que tienen inicio en este estado
                transLamda.getEstadosLlegada().forEach((estadoLlegada) -> {      
                    //Y agregamos la respuesta a todos los estados 
                    if(!estadoLlegada.equals(transLamda.getEstadoInicio()))//Condicional para evitar bucle infinito
                        respuesta.addAll(lamdaClausura(estadoLlegada));
                });
            }
        });

        return respuesta;//grupo de estados de llegada.
    }


    //Regresa la clausuraLamda de los estados que han sido pasados por parámetro
    //de estados llegada a los que avanza el autómata con transiciones Lamda
    public HashSet<String> clausuraLamda(HashSet<String> estadosOrigen) { //Regresa el grupo de estados 
        HashSet<String> respuesta = new HashSet();

        estadosOrigen.forEach((estado) -> {
            HashSet<String> valores = lamdaClausura(estado); //Aplicamos la clausura a cada estado del grupo
            
            for (String valor : valores) {
                respuesta.add(valor); //Añadimos cada llegada obtenida
            }

        });

        return respuesta;//grupo de estados de llegada
    }


    //Recrea como funciona el autómata, Recorriendo la cadena ingresada
    // y avanza el estado acutal según las transcciones establecidas del autómata
    //Sí el estado al que llega al leer el último simbolo de la cadena ingresada
    //es el estado final o de adaptación entonces la cadena es aceptada.
    @Override
    public boolean aceptarCadena(String cadena) throws Exception {
        //verificar estados de inicio y fin
        if (this.estadoDeInicio.equals("")) {
            throw new Exception("ERROR: indique el estado de INICIO");
        }
        if (this.getEstadosFin().isEmpty()) {
            throw new Exception("ERROR: indique el estado(s) FINAL");
        }
        
        char[] simbolo = cadena.toCharArray();
        HashSet<String> estadoInicio = new HashSet();
        estadoInicio.add(this.getEstInicio());
        estadoInicio = clausuraLamda(estadoInicio); //iniciar desde la clausura del estado de inicio

        for (int i = 0; i < simbolo.length; i++) {
            
            estadoInicio = getTransicion(estadoInicio, simbolo[i]); //se avanza a través del símbolo
            
            for (String estadoDisponible : clausuraLamda(estadoInicio)) {
                estadoInicio.add(estadoDisponible); //Se agrega la clausura a todos los estados llegada
            }
            
            if (estadoInicio.isEmpty()) {
                throw new Exception("ERROR: transición con Símbolo '" + simbolo[i] + "' NO aceptada");
            }

        }

        return esFin(estadoInicio);
    }

    //Borra las transiciones que utilizan un simbolo pasado por parámetro
    public void borrarSimbolo(char simbolo) {
        for (TransAutFinNDet t : this.transicion) {
            if (t.getSimboloTrans() == simbolo) {
                this.transicion.remove(t);
            }
        }
    }

    //Borrar las transiciones que utilizan el estado pasado por parámetro
    public void borrarEstado(String estados) //
    {
        //borra las transiciones con estado inicio
        HashSet<TransAutFinNDet> borrar = new HashSet();
        for (TransAutFinNDet trans : this.transicion) {
            if (trans.getEstadoInicio().equals(estados)) {
                borrar.add(trans); //Eliminar transicion
            }
            //borra las ocurrencias en las llegadas de la transicion
            HashSet<String> borrarDestino = new HashSet();
            for (String estado : trans.getEstadosLlegada()) {
                if (estado.equals(estados)) {
                    borrarDestino.add(estados); //Borrar estado de llegada
                }
            }
            trans.getEstadosLlegada().removeAll(borrarDestino);
        }
        this.transicion.removeAll(borrar);

        //Borra las transiciones lamda con estado inicio
        HashSet<TransLamda> borraTransLamda = new HashSet();
        for (TransLamda trans : this.transLamda) {
            if (trans.getEstadoInicio().equals(estados)) {
                borraTransLamda.add(trans);
            }

            //Borra las ocurrencias en los estados llegada de las transicionse lamda
            HashSet<String> borrarLlegadaLamda = new HashSet();
            for (String estadoLlegada : trans.getEstadosLlegada()) {
                if (estadoLlegada.equals(estados)) {
                    borrarLlegadaLamda.add(estados); //Borrar estado de Llegada
                }
            }
            trans.getEstadosLlegada().removeAll(borrarLlegadaLamda);
        }
        this.transLamda.removeAll(borraTransLamda);
    }

    //Borra la transición del autómata recorrida pasada por parámetro
    public void eliminarTransicion(TransAutFinNDet trans) {
        this.transicion.remove(trans);
    }


    //Borra la transición lamda del autómata que se pasa por parámetro
    public void eliminarTransicionL(TransLamda trans) {
        this.transLamda.remove(trans);
    }


    //Escribe el texto en el autómata
    @Override
    public String toString() {
        String info = "";
        HashSet<String> estados = new HashSet();

        info += "\nEstados:";

        for (TransAutFinNDet t : this.transicion) {
            estados.add(t.getEstadoInicio());
        }

        for (String e : estados) {
            info += e + "\n";
        }

        info += "\nEstado de Inicio: " + this.estadoDeInicio;
        info += "\nEstados de Fin: ";
        for (String estado : estadosDeFin) {
            info += estado+" ";
        }
        info += "\nTransición:\n";
        for (TransAutFinNDet trans : this.transicion) {
            info += trans+"\n";
        }

        info += "\nTransiciones_Lamda:\n";
        for (TransLamda t : this.transLamda) {
            info += t+"\n";
        }

        return info;
    }

    //Regresa una copia del autómata y se crean nuevos objetos.
    @Override
    public Object clone() {//sobre escribir clone de object
        AutoFinNoDet copia = null;
        try {
            copia = (AutoFinNoDet) super.clone(); //Copia método clone de los objetos
        } catch (CloneNotSupportedException ex) {
            System.out.println("Clone no soportado");
        }
        //Copia de los elementos
        copia.estadosDeFin = new HashSet<String>();
        for (String estado : this.estadosDeFin) {
            copia.estadosDeFin.add(estado);
        }

        copia.transicion = new HashSet<TransAutFinNDet>();
        for (TransAutFinNDet trans : this.transicion) {
            copia.transicion.add(trans);
        }

        copia.transLamda = new HashSet<TransLamda>();
        for (TransLamda transLamda : this.transLamda) {
            copia.transLamda.add(transLamda);
        }

        return copia;
    }
}
