package Principal;

import Automata.AFND;
import Automata.AFD;
import Automata.TransicionAFD;
import Automata.TransicionAFND;
import Automata.TransicionL;
import Grafo.ManejaGrafo;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.mxgraph.swing.mxGraphComponent;
import javax.swing.filechooser.FileFilter;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase Interfaz. Esta clase se encargará de darnos una visión gráfica del
 * automata.
 */
public class Interfaz extends javax.swing.JFrame {

    private AFD afd = new AFD();
    private AFND afnd = new AFND();

    private HashSet<String> cjtoEstados = new HashSet();
    private HashSet<String> cjtoSimbolos = new HashSet();
    private DefaultTableModel modeloTT;
    


    ManejaGrafo grafica = new ManejaGrafo();

    /**
     * Creates new form Interfaz
     */
    public Interfaz() {
        initComponents();
        modeloTT = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTT.setColumnIdentifiers(new Object[]{"Estados Y simbolos"});
        tablaTransicion.setModel(modeloTT);
        botonDestinos.setVisible(false); //Por defecto empieza en AFD y no necesita multples destinos

        

        this.setLocationRelativeTo(null); //Centrar ventana en la pantalla
    }

    private void vaciarTabla() {
        while (modeloTT.getRowCount() > 0) {
            modeloTT.removeRow(0);
        }
    }

    /**
     * Actualiza la tabla de transiciones según haya sido modificada a través de
     * la interfaz
     */
    public void actualizarTabla() {
        //TODO: editar nombre estados, si es incial -> delante y si es final * delante
        vaciarTabla();
        Object[] simbolos = cjtoSimbolos.toArray();
        ArrayList<Object> columna = new ArrayList();

        columna.add("Estados Y simbolos");
        for (int i = 0; i < cjtoSimbolos.size(); i++) {
            columna.add(simbolos[i]);
        }
        //Si es AFND le tenemos que añadir la columna de simbolo LAMBDA
        if (tipoAFND.isSelected()) {
            columna.add("L");
        }
        modeloTT.setColumnIdentifiers(columna.toArray()); //Añadimos la cabecera de la tabla

        for (String e : cjtoEstados) // Por cada estado
        {
            columna = new ArrayList<>(); //Reiniciamos la columna
            columna.add(e);

            if (tipoAFD.isSelected()) {
                for (int j = 0; j < cjtoSimbolos.size(); j++) // Rellena cada columna segun el simbolo de entrada 
                {
                    columna.add(afd.getTransicion(e, (tablaTransicion.getColumnName(j + 1)).charAt(0)));  // Obteniendo la transicion del AFD
                }
            } else {
                for (int j = 0; j < cjtoSimbolos.size() + 1; j++) //Tiene que contar con la columna L
                {
                    if ((tablaTransicion.getColumnName(j + 1)).equals("L")) {
                        columna.add(afnd.getTransicionL(e));
                    } else {
                        columna.add(afnd.getTransicion(e, (tablaTransicion.getColumnName(j + 1)).charAt(0)));
                    }
                }
            }

            modeloTT.addRow(columna.toArray());
        }

    }

    /**
     * Actualiza la representación gráfica del autómata según su situación
     */
    public void actualizarGrafica() {
        try {
            mxGraphComponent grafica_generada;
            if (tipoAFD.isSelected()) {
                grafica_generada = grafica.generarAFD(afd, cjtoEstados);
            } else {
                grafica_generada = grafica.generarAFND(afnd, cjtoEstados);
            }

            //scroll.removeAll();
            scroll.add(grafica_generada);
            scroll.getViewport().add(grafica_generada);
            scroll.revalidate();
            scroll.repaint();

        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, ex.getMessage(),
                    "Error al generar grafica", JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Comprueba si un AFD tiene para cada estado y simbolo una transición. Es
     * decir, tiene la tabla de transiciones completa.
     *
     * @param afd Automata pasado por parámetro
     * @param cjtoEstados Conjunto de estados
     * @param cjtoSimbolos Conjunto de simbolos
     * @return
     */
    public boolean comprobarDeterminismo(AFD afd, HashSet<String> cjtoEstados, HashSet<String> cjtoSimbolos) {
        int nTransiciones = afd.getTransiciones().size(); //Numero de transiciones del afd
        return (nTransiciones == cjtoEstados.size() * cjtoSimbolos.size()); //Existe una transicion para cada estado y simbolo
        //Si no, tenemos que crear un nuevo estado de absorcion con las transiciones que faltan
    }

    /**
     * Agrega un estado muerto de absorción, para corregir la falta de determinismo de un AFD
     */
    public void agregarEstadoMuerto() {
        int nCol = modeloTT.getColumnCount();
        int nFil = modeloTT.getRowCount();

        //Añadimos el estado muerto o de absorcion
        this.cjtoEstados.add("M");

        for (int i = 0; i < nFil; i++) {
            for (int j = 1; j < nCol; j++) { //La primera columna es el nombre del estado
                if (modeloTT.getValueAt(i, j).equals("")) {
                    TransicionAFD t = new TransicionAFD(modeloTT.getValueAt(i, 0).toString(), modeloTT.getColumnName(j).charAt(0), "M");
                    afd.agregarTransicion(t); //Añadimos la transicion al estado muerto o de absorcion
                    System.out.println("AÑADIDA TRANSICION: " + t);

                    modeloTT.setValueAt("M", i, j);
                }
            }
        }
        ArrayList<String> nuevoEstado = new ArrayList<>();
        nuevoEstado.add("M");
        for (int i = 1; i < modeloTT.getColumnCount(); i++) {
            nuevoEstado.add("M");
            afd.agregarTransicion("M", modeloTT.getColumnName(i).charAt(0), "M");
        }
        modeloTT.addRow(nuevoEstado.toArray());

        actualizarTabla();
        actualizarGrafica();
    }

    /**        labelFin.setVisible(false);
        labelResultado.setVisible(false);
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor        labelFin.setVisible(false);
        labelResultado.setVisible(false);.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTransicion = new javax.swing.JTable();
        botonSimular = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        botonAddEstado = new javax.swing.JButton();
        textEstado = new javax.swing.JTextField();
        textSimbolo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        comboOrigen = new javax.swing.JComboBox<>();
        comboDestino = new javax.swing.JComboBox<>();
        botonAddSimbolo = new javax.swing.JButton();
        botonAddT = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        comboEstadoI = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        comboSimbolo = new javax.swing.JComboBox<>();
        tipoAFD = new javax.swing.JRadioButton();
        tipoAFND = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        textCadena = new javax.swing.JTextField();
        botonEliminarT = new javax.swing.JButton();
        botonEstadosF = new javax.swing.JButton();
        labelEstadoI = new javax.swing.JLabel();
        labelEstadosF = new javax.swing.JLabel();
        botonEliminarSimbolo = new javax.swing.JButton();
        botonEliminarEstado = new javax.swing.JButton();
        botonDestinos = new javax.swing.JButton();
        botonLimpiar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Autómatas ");
        setBackground(new java.awt.Color(255, 255, 255));
        setLocation(new java.awt.Point(0, 0));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        tablaTransicion.setFont(new java.awt.Font("Rockwell", 0, 20)); // NOI18N
        tablaTransicion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "ESTADO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaTransicion.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaTransicion.setName("Tabla de Transiciones"); // NOI18N
        tablaTransicion.setRowHeight(30);
        tablaTransicion.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(tablaTransicion);
        if (tablaTransicion.getColumnModel().getColumnCount() > 0) {
            tablaTransicion.getColumnModel().getColumn(0).setResizable(false);
            tablaTransicion.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        botonSimular.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        botonSimular.setText("Evaluar");
        botonSimular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSimularActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel2.setText("Creación Símbolos de entrada");

        jLabel3.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel3.setText("Creación de Estados");
        jLabel3.setToolTipText("");

        botonAddEstado.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        botonAddEstado.setText("Crear");
        botonAddEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddEstadoActionPerformed(evt);
            }
        });

        textEstado.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        textEstado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textEstado.setText("q0");

        textSimbolo.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        textSimbolo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textSimbolo.setText("1");

        jLabel6.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel6.setText("Conjunto transiciones");

        comboOrigen.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        comboOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "inicio" }));

        comboDestino.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        comboDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "llegada" }));
        comboDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboDestinoActionPerformed(evt);
            }
        });

        botonAddSimbolo.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        botonAddSimbolo.setText("Crear");
        botonAddSimbolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddSimboloActionPerformed(evt);
            }
        });

        botonAddT.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        botonAddT.setText("Crear");
        botonAddT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAddTActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel7.setText("Estado inicial");

        comboEstadoI.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        comboEstadoI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEstadoIActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel8.setText("Estados finales");

        comboSimbolo.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        comboSimbolo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Símbolo" }));

        buttonGroup1.add(tipoAFD);
        tipoAFD.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        tipoAFD.setSelected(true);
        tipoAFD.setText("AFD");
        tipoAFD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoAFDActionPerformed(evt);
            }
        });

        buttonGroup1.add(tipoAFND);
        tipoAFND.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        tipoAFND.setText("AFND");
        tipoAFND.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoAFNDActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel9.setText("Elegir autómata");

        textCadena.setFont(new java.awt.Font("Rockwell", 0, 36)); // NOI18N
        textCadena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textCadenaActionPerformed(evt);
            }
        });

        botonEliminarT.setText("Eliminar Transiciones");
        botonEliminarT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarTActionPerformed(evt);
            }
        });

        botonEstadosF.setText("Estados");
        botonEstadosF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEstadosFActionPerformed(evt);
            }
        });

        labelEstadoI.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        labelEstadoI.setText("Estado inicio: SELECCIONAR");

        labelEstadosF.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        labelEstadosF.setText("Estados finales: SELECCIONAR");

        botonEliminarSimbolo.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        botonEliminarSimbolo.setText("Eliminar");
        botonEliminarSimbolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarSimboloActionPerformed(evt);
            }
        });

        botonEliminarEstado.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        botonEliminarEstado.setText("Eliminar");
        botonEliminarEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarEstadoActionPerformed(evt);
            }
        });

        botonDestinos.setText("Llegadas");
        botonDestinos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDestinosActionPerformed(evt);
            }
        });

        botonLimpiar.setText("Reiniciar");
        botonLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLimpiarActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel13.setText("Expresión a evaluar");

        scroll.setBackground(new java.awt.Color(255, 255, 255));
        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(botonEliminarT, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textCadena, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(botonSimular, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonAddEstado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonEliminarEstado))
                            .addComponent(jLabel6)
                            .addComponent(jLabel13)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(comboOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(comboSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(botonEstadosF)
                                        .addGap(18, 18, 18)
                                        .addComponent(labelEstadosF))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(comboDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(botonDestinos)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(botonAddT))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(comboEstadoI, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(labelEstadoI))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonAddSimbolo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonEliminarSimbolo)
                                .addGap(60, 60, 60)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tipoAFD)
                                        .addGap(18, 18, 18)
                                        .addComponent(tipoAFND))))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(143, 143, 143)
                        .addComponent(botonLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(textSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(botonAddSimbolo)
                                    .addComponent(botonEliminarSimbolo)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tipoAFD)
                                    .addComponent(tipoAFND))))
                        .addGap(13, 13, 13)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botonAddEstado)
                            .addComponent(textEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonEliminarEstado))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonAddT)
                            .addComponent(comboSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonDestinos))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(comboEstadoI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelEstadoI))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(botonEstadosF)
                            .addComponent(labelEstadosF))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textCadena, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonSimular, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(scroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonEliminarT)
                    .addComponent(botonLimpiar)
                    .addComponent(jLabel11))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("Automatas");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboEstadoIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEstadoIActionPerformed
        if (comboEstadoI.getItemCount() > 0) {
            if (tipoAFD.isSelected()) {
                afd.setEstadoInicial(comboEstadoI.getSelectedItem().toString());
            } else {
                afnd.setEstadoInicial(comboEstadoI.getSelectedItem().toString());
            }
            labelEstadoI.setText("Estado inicial: " + comboEstadoI.getSelectedItem().toString());
            actualizarGrafica();
        }
    }//GEN-LAST:event_comboEstadoIActionPerformed

    private void botonAddEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAddEstadoActionPerformed
        if (!textEstado.getText().isBlank() && cjtoEstados.add(textEstado.getText())) { //Para evitar repetidos
            comboOrigen.addItem(textEstado.getText());
            comboDestino.addItem(textEstado.getText());
            comboEstadoI.addItem(textEstado.getText());

            actualizarTabla();
            actualizarGrafica();
        }
    }//GEN-LAST:event_botonAddEstadoActionPerformed

    private void tipoAFDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoAFDActionPerformed
        this.afd = new AFD(); //Reseteamos el AFD
        botonLimpiar.doClick(); //Limpiamos todo
        comboSimbolo.removeItem("LAMBDA");
        botonDestinos.setVisible(false);
        comboDestino.setVisible(true);
        this.botonAddT.setVisible(true);
    }//GEN-LAST:event_tipoAFDActionPerformed

    private void tipoAFNDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoAFNDActionPerformed
        this.afnd = new AFND(); //Reseteamos el AFND
        botonLimpiar.doClick(); //Limpiamos todo
        comboSimbolo.addItem("LAMBDA");
        botonAddT.setVisible(false);
        comboDestino.setVisible(false);
        botonDestinos.setVisible(true);
    }//GEN-LAST:event_tipoAFNDActionPerformed

    private void botonAddSimboloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAddSimboloActionPerformed
        if (!textSimbolo.getText().isBlank() && cjtoSimbolos.add(textSimbolo.getText())) {
            comboSimbolo.addItem(textSimbolo.getText());
            actualizarTabla();
        }
    }//GEN-LAST:event_botonAddSimboloActionPerformed

    private void botonAddTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAddTActionPerformed
        if (tipoAFD.isSelected()) //Obtenemos la transicion de los valores del comboBox
        {
            TransicionAFD t = new TransicionAFD(comboOrigen.getSelectedItem().toString(), comboSimbolo.getSelectedItem().toString().charAt(0), comboDestino.getSelectedItem().toString());
            afd.agregarTransicion(t);
            System.out.println("AÑADIDA TRANSICION:" + t);
        }
        actualizarTabla();
        actualizarGrafica();
    }//GEN-LAST:event_botonAddTActionPerformed

    private void botonEliminarSimboloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarSimboloActionPerformed
        this.cjtoSimbolos.remove(textSimbolo.getText());
        comboSimbolo.removeItem(textSimbolo.getText());

        if (tipoAFD.isSelected()) {
            afd.eliminarSimbolo(textSimbolo.getText().charAt(0)); //Elimina las transiciones que usan el simbolo
        } else {
            afnd.eliminarSimbolo(textSimbolo.getText().charAt(0));
        }
        actualizarTabla();
    }//GEN-LAST:event_botonEliminarSimboloActionPerformed

    private void botonEliminarEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarEstadoActionPerformed
        String estado = textEstado.getText();
        this.cjtoEstados.remove(estado);
        comboEstadoI.removeItem(estado);
        comboOrigen.removeItem(estado);

        if (tipoAFD.isSelected()) {
            comboDestino.removeItem(estado);
            afd.eliminarEstado(estado);
        } else {
            afnd.eliminarEstado(estado);
        }
        actualizarTabla();
        actualizarGrafica();
    }//GEN-LAST:event_botonEliminarEstadoActionPerformed

    private void botonEliminarTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarTActionPerformed
        if (tipoAFD.isSelected()) {
            for (int i : tablaTransicion.getSelectedRows()) {
                System.out.println("Eliminando fila: " + i);
                String origen = modeloTT.getValueAt(i, 0).toString();

                for (int j = 1; j < modeloTT.getColumnCount(); j++) {//Por cada simbolo
                    String destino = modeloTT.getValueAt(i, j).toString();
                    TransicionAFD t = new TransicionAFD(origen, modeloTT.getColumnName(j).charAt(0), destino);
                    System.out.println("Eliminar transicion " + t);
                    afd.eliminarTransicion(t); //Borramos la transicion 
                }

            }
        }//TODO eliminar AFND

        actualizarTabla();
        actualizarGrafica();
    }//GEN-LAST:event_botonEliminarTActionPerformed

    private void botonEstadosFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEstadosFActionPerformed
        panelEstados pE = new panelEstados();
        pE.setEstados(cjtoEstados);

        int res = JOptionPane.showConfirmDialog(null, pE,
                "SELECCIONAR ESTADOS", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (tipoAFD.isSelected()) {
                afd.setEstadosFinales(pE.getEstados());
            } else {
                afnd.setEstadosFinales(pE.getEstados());
            }

            labelEstadosF.setText("Estados finales: " + pE.getEstados());
            actualizarGrafica();
        }

    }//GEN-LAST:event_botonEstadosFActionPerformed

    private void botonDestinosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDestinosActionPerformed
        panelEstados pE = new panelEstados();
        pE.setEstados(cjtoEstados);

        int res = JOptionPane.showConfirmDialog(null, pE,
                "SELECCIONAR ESTADOS", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (comboSimbolo.getSelectedItem().equals("LAMBDA")) //Es una L-T             
            {
                TransicionL lt = new TransicionL(comboOrigen.getSelectedItem().toString(), pE.getEstados());
                afnd.agregarTransicionL(lt);
                System.out.println("AÑADIDA LAMBDA-T:" + lt);
            } else {
                TransicionAFND t = new TransicionAFND(comboOrigen.getSelectedItem().toString(), comboSimbolo.getSelectedItem().toString().charAt(0), pE.getEstados());
                afnd.agregarTransicion(t);
                System.out.println("AÑADIDA T: " + t);
            }

        }
        actualizarTabla();
        actualizarGrafica();
    }//GEN-LAST:event_botonDestinosActionPerformed

    private void botonLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLimpiarActionPerformed
        this.cjtoEstados.clear();
        this.cjtoSimbolos.clear();
        this.afd = new AFD();
        this.afnd = new AFND();
      
        textCadena.setText("");
     
        labelEstadoI.setText("Estado inicial: NO SELECCIONADO");
        labelEstadosF.setText("Estados finales: NO SELECCIONADOS");
    
        this.comboDestino.removeAllItems();
        this.comboOrigen.removeAllItems();
        this.comboSimbolo.removeAllItems();
        this.comboEstadoI.removeAllItems();

        this.comboOrigen.addItem("Origen");
        this.comboSimbolo.addItem("Símbolo");
        this.comboDestino.addItem("Destino");
        if (this.tipoAFND.isSelected()) {
            this.comboSimbolo.addItem("LAMBDA");
        }
        vaciarTabla();
        actualizarTabla(); //Volver a pintar la tabla vacia
        actualizarGrafica();

    }//GEN-LAST:event_botonLimpiarActionPerformed

    private void textCadenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCadenaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textCadenaActionPerformed

    private void botonSimularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSimularActionPerformed
        try {

            if (tipoAFD.isSelected()) {
                if (comprobarDeterminismo(this.afd, this.cjtoEstados, this.cjtoSimbolos)) {
                    System.out.println("DETERMINISMO DEL AFD CORRECTO");
                } else {
                    System.out.println("INDETERMINISMO EN EL AFD! -> SE CREARÁ ESTADO MUERTO M");
                    JOptionPane.showMessageDialog(this, "Indeterminismo en el AFD!\n(No ha indicado todas las transiciones para cada estado y símbolo)\n"
                        + "Se procederá a crear un nuevo estado de absorción o muerto M", "Error", JOptionPane.WARNING_MESSAGE);
                    this.agregarEstadoMuerto();
                }

                if (afd.reconocer(textCadena.getText())) {
                    JOptionPane.showConfirmDialog(rootPane, "CADENA RECONOCIDA", "RESULTADO DEL AUTOMATA", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showConfirmDialog(rootPane, "CADENA NO RECONOCIDA", "RESULTADO DEL AUTOMATA", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (afnd.reconocer(textCadena.getText())) {
                    JOptionPane.showConfirmDialog(rootPane, "CADENA RECONOCIDA", "RESULTADO DEL AUTOMATA", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showConfirmDialog(rootPane, "CADENA NO RECONOCIDA", "RESULTADO DEL AUTOMATA", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en la siulación!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonSimularActionPerformed

    private void comboDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboDestinoActionPerformed

    /**
     * Muestra y ejecuta la interfaz
     *
     * @param args
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAddEstado;
    private javax.swing.JButton botonAddSimbolo;
    private javax.swing.JButton botonAddT;
    private javax.swing.JButton botonDestinos;
    private javax.swing.JButton botonEliminarEstado;
    private javax.swing.JButton botonEliminarSimbolo;
    private javax.swing.JButton botonEliminarT;
    private javax.swing.JButton botonEstadosF;
    private javax.swing.JButton botonLimpiar;
    private javax.swing.JButton botonSimular;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> comboDestino;
    private javax.swing.JComboBox<String> comboEstadoI;
    private javax.swing.JComboBox<String> comboOrigen;
    private javax.swing.JComboBox<String> comboSimbolo;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelEstadoI;
    private javax.swing.JLabel labelEstadosF;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable tablaTransicion;
    private javax.swing.JTextField textCadena;
    private javax.swing.JTextField textEstado;
    private javax.swing.JTextField textSimbolo;
    private javax.swing.JRadioButton tipoAFD;
    private javax.swing.JRadioButton tipoAFND;
    // End of variables declaration//GEN-END:variables
}
