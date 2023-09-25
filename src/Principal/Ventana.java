package Principal;

import Automata.AutoFinNoDet;
import Automata.AutoFinDet;
import Automata.TransicionAFD;
import Automata.TransAutFinNDet;
import Automata.TransLamda;
import Grafo.ManejaGrafo;
import java.util.HashSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.mxgraph.swing.mxGraphComponent;
import java.util.ArrayList;

//Esta clase nos permité una visión general del programa
public class Ventana extends javax.swing.JFrame {

    private AutoFinDet AFDET = new AutoFinDet();
    private AutoFinNoDet AFNDet = new AutoFinNoDet();

    private HashSet<String> numEstados = new HashSet();
    private HashSet<String> numSimbolos = new HashSet();
    private DefaultTableModel tabla;
    


    ManejaGrafo grafo = new ManejaGrafo();
    
    //Form para la interfaz
    
    public Ventana() {
        initComponents();
        tabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla.setColumnIdentifiers(new Object[]{"Estados Y símbolos"});
        tablaDeTransicion.setModel(tabla);
        btnLlegadas.setVisible(false); //Inicia  en AFD y por ello no requiere varias llegadas
       

        this.setLocationRelativeTo(null); 
    }

    private void reiniciarTabla() {
        while (tabla.getRowCount() > 0) {
            tabla.removeRow(0);
        }
    }

    //Refresca la tabla de transiciones de acuerdo a lo gestionado por el usuario
    public void refrescarTabla() {
        //Editar estados y sus cualidades en general
        reiniciarTabla();
        Object[] simbolos = numSimbolos.toArray();
        ArrayList<Object> columna = new ArrayList();

        columna.add("Estados Y simbolos");
        for (int i = 0; i < numSimbolos.size(); i++) {
            columna.add(simbolos[i]);
        }
        //Si es Autómata finito no determinista entonces
        //se debe añadarir la columna para el simbolo LAMDA
        if (tipoAFND.isSelected()) {
            columna.add("L");
        }
        tabla.setColumnIdentifiers(columna.toArray()); //Agregar la cabecera  a la tabla

        for (String estadoInicio : numEstados) //para cada estado
        {
            columna = new ArrayList<>(); //Restablecer la columna
            columna.add(estadoInicio);

            if (tipoAutFinDet.isSelected()) {
                for (int j = 0; j < numSimbolos.size(); j++) // coloca en cada columna del simbolo 
                {
                    columna.add(AFDET.getTransicion(estadoInicio, (tablaDeTransicion.getColumnName(j + 1)).charAt(0)));  //traer la transicion para AFD
                }
            } else {
                for (int j = 0; j < numSimbolos.size() + 1; j++) //Acá cuenta con la columna L
                {
                    if ((tablaDeTransicion.getColumnName(j + 1)).equals("L")) {
                        columna.add(AFNDet.getTransicionL(estadoInicio));
                    } else {
                        columna.add(AFNDet.getTransicion(estadoInicio, (tablaDeTransicion.getColumnName(j + 1)).charAt(0)));
                    }
                }
            }

            tabla.addRow(columna.toArray());
        }

    }
    //refresca el grafo del autómata 
    public void refrescarGrafo() {
        try {
            mxGraphComponent grafoGen;
            if (tipoAutFinDet.isSelected()) {
                grafoGen = grafo.generarAFD(AFDET, numEstados);
            } else {
                grafoGen = grafo.generarAFND(AFNDet, numEstados);
            }

            //scroll.removeAll();
            scroll.add(grafoGen);
            scroll.getViewport().add(grafoGen);
            scroll.revalidate();
            scroll.repaint();

        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, ex.getMessage(),
                    "Error no se puede crear GRAFO", JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    //Verifica si la tabla de transición esta completa
    public boolean comprobarDeterminismo(AutoFinDet automata, HashSet<String> numEstados, HashSet<String> numSimbolos) {
        int numTransi = automata.getTransiciones().size(); 
        return (numTransi == numEstados.size() * numSimbolos.size()); //Transición para estado y simbolo
        //Cuando no hay nuevo estado se crean estados de absorción con las transiciones que restan
    }


    //añadé un estado para la incompletitud del afd
    public void aniadirEstadoMuerto() {
        int numColum = tabla.getColumnCount();
        int numFila = tabla.getRowCount();

        //agrega el estado de absorción
        this.numEstados.add("A"
                + "");

        for (int fila = 0; fila < numFila; fila++) {
            for (int columna = 1; columna < numColum; columna++) { //nombre del estado
                if (tabla.getValueAt(fila, columna).equals("")) {
                    TransicionAFD t = new TransicionAFD(tabla.getValueAt(fila, 0).toString(), tabla.getColumnName(columna).charAt(0), "A");
                    AFDET.aniadirTransicion(t); //Añadimos la transicion al estado muerto o de absorcion
                    System.out.println("Transición añadida: " + t);

                    tabla.setValueAt("A", fila, columna);
                }
            }
        }
        ArrayList<String> nuevoEstado = new ArrayList<>();
        nuevoEstado.add("A");
        for (int columna = 1; columna < tabla.getColumnCount(); columna++) {
            nuevoEstado.add("A");
            AFDET.agregarTransicion("A", tabla.getColumnName(columna).charAt(0), "A");
        }
        tabla.addRow(nuevoEstado.toArray());

        refrescarTabla();
        refrescarGrafo();
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
        jPnlPrincipal = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        textSimbolo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        textEstado = new javax.swing.JTextField();
        tipoAutFinDet = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        btnEstFin = new javax.swing.JButton();
        cmbLlegada = new javax.swing.JComboBox<>();
        btnBorrarTrans = new javax.swing.JButton();
        btnAddT = new javax.swing.JButton();
        btnAddSimb = new javax.swing.JButton();
        btnAddEst = new javax.swing.JButton();
        lblCreEst = new javax.swing.JLabel();
        cmbEstadoL = new javax.swing.JComboBox<>();
        lblConjTransiciones = new javax.swing.JLabel();
        tipoAFND = new javax.swing.JRadioButton();
        cmbSimb = new javax.swing.JComboBox<>();
        cmbInicio = new javax.swing.JComboBox<>();
        textCadena = new javax.swing.JTextField();
        btnEvaluar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDeTransicion = new javax.swing.JTable();
        lblSimbEnt = new javax.swing.JLabel();
        lblEstadoL = new javax.swing.JLabel();
        labelEstadosI = new javax.swing.JLabel();
        btnBorrarSimb = new javax.swing.JButton();
        btnBorrarEst = new javax.swing.JButton();
        btnLlegadas = new javax.swing.JButton();
        btnReiniciar = new javax.swing.JButton();
        scroll = new javax.swing.JScrollPane();
        jLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Autómatas ");
        setBackground(new java.awt.Color(0, 51, 102));
        setForeground(new java.awt.Color(0, 51, 102));
        setLocation(new java.awt.Point(0, 0));

        jPnlPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        jPnlPrincipal.setForeground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Noto Serif CJK JP", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 102));
        jLabel9.setText("Elegir autómata");

        textSimbolo.setBackground(new java.awt.Color(0, 102, 204));
        textSimbolo.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 14)); // NOI18N
        textSimbolo.setForeground(new java.awt.Color(255, 255, 255));
        textSimbolo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textSimbolo.setText("1");

        jLabel7.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 102));
        jLabel7.setText("Estado inicial");

        textEstado.setBackground(new java.awt.Color(0, 102, 204));
        textEstado.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 14)); // NOI18N
        textEstado.setForeground(new java.awt.Color(255, 255, 255));
        textEstado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textEstado.setText("q0");

        buttonGroup1.add(tipoAutFinDet);
        tipoAutFinDet.setFont(new java.awt.Font("Noto Sans CJK JP", 2, 14)); // NOI18N
        tipoAutFinDet.setForeground(new java.awt.Color(0, 102, 204));
        tipoAutFinDet.setSelected(true);
        tipoAutFinDet.setText("AFD");
        tipoAutFinDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoAutFinDetActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 102));
        jLabel8.setText("Estados finales");

        btnEstFin.setBackground(new java.awt.Color(0, 51, 102));
        btnEstFin.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 15)); // NOI18N
        btnEstFin.setForeground(new java.awt.Color(255, 255, 255));
        btnEstFin.setText("Estados");
        btnEstFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstFinActionPerformed(evt);
            }
        });

        cmbLlegada.setBackground(new java.awt.Color(0, 102, 204));
        cmbLlegada.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 14)); // NOI18N
        cmbLlegada.setForeground(new java.awt.Color(255, 255, 255));
        cmbLlegada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "llegada" }));
        cmbLlegada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLlegadaActionPerformed(evt);
            }
        });

        btnBorrarTrans.setBackground(new java.awt.Color(0, 51, 102));
        btnBorrarTrans.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 15)); // NOI18N
        btnBorrarTrans.setForeground(new java.awt.Color(255, 255, 255));
        btnBorrarTrans.setText("Eliminar Transiciones");
        btnBorrarTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarTransActionPerformed(evt);
            }
        });

        btnAddT.setBackground(new java.awt.Color(0, 51, 102));
        btnAddT.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 14)); // NOI18N
        btnAddT.setForeground(new java.awt.Color(255, 255, 255));
        btnAddT.setText("Crear");
        btnAddT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTActionPerformed(evt);
            }
        });

        btnAddSimb.setBackground(new java.awt.Color(0, 51, 102));
        btnAddSimb.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 14)); // NOI18N
        btnAddSimb.setForeground(new java.awt.Color(255, 255, 255));
        btnAddSimb.setText("Crear");
        btnAddSimb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSimbActionPerformed(evt);
            }
        });

        btnAddEst.setBackground(new java.awt.Color(0, 51, 102));
        btnAddEst.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 14)); // NOI18N
        btnAddEst.setForeground(new java.awt.Color(255, 255, 255));
        btnAddEst.setText("Crear");
        btnAddEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEstActionPerformed(evt);
            }
        });

        lblCreEst.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 14)); // NOI18N
        lblCreEst.setForeground(new java.awt.Color(0, 51, 102));
        lblCreEst.setText("Creación de Estados");
        lblCreEst.setToolTipText("");

        cmbEstadoL.setBackground(new java.awt.Color(0, 102, 204));
        cmbEstadoL.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 14)); // NOI18N
        cmbEstadoL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoLActionPerformed(evt);
            }
        });

        lblConjTransiciones.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 14)); // NOI18N
        lblConjTransiciones.setForeground(new java.awt.Color(0, 51, 102));
        lblConjTransiciones.setText("Conjunto transiciones");

        buttonGroup1.add(tipoAFND);
        tipoAFND.setFont(new java.awt.Font("Noto Sans CJK JP", 2, 14)); // NOI18N
        tipoAFND.setForeground(new java.awt.Color(0, 102, 204));
        tipoAFND.setText("AFND");
        tipoAFND.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoAFNDActionPerformed(evt);
            }
        });

        cmbSimb.setBackground(new java.awt.Color(0, 102, 204));
        cmbSimb.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 14)); // NOI18N
        cmbSimb.setForeground(new java.awt.Color(255, 255, 255));
        cmbSimb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Símbolo" }));

        cmbInicio.setBackground(new java.awt.Color(0, 102, 204));
        cmbInicio.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 14)); // NOI18N
        cmbInicio.setForeground(new java.awt.Color(255, 255, 255));
        cmbInicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "inicio" }));

        textCadena.setBackground(new java.awt.Color(0, 102, 204));
        textCadena.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 30)); // NOI18N
        textCadena.setForeground(new java.awt.Color(255, 255, 255));
        textCadena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textCadenaActionPerformed(evt);
            }
        });

        btnEvaluar.setBackground(new java.awt.Color(0, 51, 102));
        btnEvaluar.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 14)); // NOI18N
        btnEvaluar.setForeground(new java.awt.Color(255, 255, 255));
        btnEvaluar.setText("Evaluar");
        btnEvaluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEvaluarActionPerformed(evt);
            }
        });

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        tablaDeTransicion.setBackground(new java.awt.Color(0, 51, 102));
        tablaDeTransicion.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 20)); // NOI18N
        tablaDeTransicion.setForeground(new java.awt.Color(255, 255, 255));
        tablaDeTransicion.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaDeTransicion.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaDeTransicion.setName("Tabla de Transiciones"); // NOI18N
        tablaDeTransicion.setRowHeight(30);
        tablaDeTransicion.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(tablaDeTransicion);
        if (tablaDeTransicion.getColumnModel().getColumnCount() > 0) {
            tablaDeTransicion.getColumnModel().getColumn(0).setResizable(false);
            tablaDeTransicion.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        lblSimbEnt.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 14)); // NOI18N
        lblSimbEnt.setForeground(new java.awt.Color(0, 51, 102));
        lblSimbEnt.setText("Creación Símbolos de entrada");

        lblEstadoL.setBackground(new java.awt.Color(0, 51, 102));
        lblEstadoL.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 12)); // NOI18N
        lblEstadoL.setForeground(new java.awt.Color(0, 51, 102));
        lblEstadoL.setText("Estado inicio: SELECCIONAR");

        labelEstadosI.setBackground(new java.awt.Color(0, 51, 102));
        labelEstadosI.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 12)); // NOI18N
        labelEstadosI.setForeground(new java.awt.Color(0, 51, 102));
        labelEstadosI.setText("Estados finales: SELECCIONAR");

        btnBorrarSimb.setBackground(new java.awt.Color(0, 51, 102));
        btnBorrarSimb.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 14)); // NOI18N
        btnBorrarSimb.setForeground(new java.awt.Color(255, 255, 255));
        btnBorrarSimb.setText("Eliminar");
        btnBorrarSimb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarSimbActionPerformed(evt);
            }
        });

        btnBorrarEst.setBackground(new java.awt.Color(0, 51, 102));
        btnBorrarEst.setFont(new java.awt.Font("Noto Sans CJK JP", 0, 14)); // NOI18N
        btnBorrarEst.setForeground(new java.awt.Color(255, 255, 255));
        btnBorrarEst.setText("Eliminar");
        btnBorrarEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarEstActionPerformed(evt);
            }
        });

        btnLlegadas.setBackground(new java.awt.Color(0, 51, 102));
        btnLlegadas.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 15)); // NOI18N
        btnLlegadas.setForeground(new java.awt.Color(255, 255, 255));
        btnLlegadas.setText("Llegadas");
        btnLlegadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLlegadasActionPerformed(evt);
            }
        });

        btnReiniciar.setBackground(new java.awt.Color(0, 51, 102));
        btnReiniciar.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 15)); // NOI18N
        btnReiniciar.setForeground(new java.awt.Color(255, 255, 255));
        btnReiniciar.setText("Reiniciar");
        btnReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReiniciarActionPerformed(evt);
            }
        });

        scroll.setBackground(new java.awt.Color(255, 255, 255));
        scroll.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        scroll.setForeground(new java.awt.Color(255, 255, 255));
        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jLabel13.setFont(new java.awt.Font("Noto Sans CJK JP", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 102));
        jLabel13.setText("Expresión a evaluar");

        jLabel11.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPnlPrincipalLayout = new javax.swing.GroupLayout(jPnlPrincipal);
        jPnlPrincipal.setLayout(jPnlPrincipalLayout);
        jPnlPrincipalLayout.setHorizontalGroup(
            jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                        .addGap(435, 435, 435)
                        .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                .addComponent(textCadena, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEvaluar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                .addComponent(textEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddEst)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBorrarEst))
                            .addComponent(lblConjTransiciones)
                            .addComponent(jLabel13)
                            .addComponent(lblSimbEnt)
                            .addComponent(lblCreEst)
                            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                        .addComponent(cmbInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbSimb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                        .addComponent(btnEstFin)
                                        .addGap(18, 18, 18)
                                        .addComponent(labelEstadosI))
                                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                        .addComponent(cmbLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnLlegadas)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnAddT))
                                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                        .addComponent(cmbEstadoL, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblEstadoL))))
                            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                .addComponent(textSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddSimb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBorrarSimb)
                                .addGap(60, 60, 60)
                                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                        .addComponent(tipoAutFinDet)
                                        .addGap(18, 18, 18)
                                        .addComponent(tipoAFND))))))
                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                        .addGap(565, 565, 565)
                        .addComponent(btnReiniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
            .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                            .addGap(86, 86, 86)
                            .addComponent(btnBorrarTrans, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(218, 218, 218)
                    .addComponent(jLabel11)
                    .addContainerGap(804, Short.MAX_VALUE)))
        );
        jPnlPrincipalLayout.setVerticalGroup(
            jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(lblSimbEnt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(textSimbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAddSimb)
                                    .addComponent(btnBorrarSimb)))
                            .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tipoAutFinDet)
                                    .addComponent(tipoAFND))))
                        .addGap(13, 13, 13)
                        .addComponent(lblCreEst)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddEst)
                            .addComponent(textEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBorrarEst))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblConjTransiciones)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddT)
                            .addComponent(cmbSimb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLlegadas))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEstadoL, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(cmbEstadoL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelEstadosI)
                            .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(btnEstFin)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addGap(4, 4, 4)
                        .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textCadena, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEvaluar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(btnReiniciar)
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPnlPrincipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBorrarTrans)
                        .addComponent(jLabel11))
                    .addContainerGap(26, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("Automatas");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbEstadoLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstadoLActionPerformed
        if (cmbEstadoL.getItemCount() > 0) {
            if (tipoAutFinDet.isSelected()) {
                AFDET.setEstadoInicial(cmbEstadoL.getSelectedItem().toString());
            } else {
                AFNDet.setEstadoInicio(cmbEstadoL.getSelectedItem().toString());
            }
            lblEstadoL.setText("Estado inicial: " + cmbEstadoL.getSelectedItem().toString());
            refrescarGrafo();
        }
    }//GEN-LAST:event_cmbEstadoLActionPerformed

    private void btnAddEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEstActionPerformed
        if (!textEstado.getText().isBlank() && numEstados.add(textEstado.getText())) { //Para evitar repetidos
            cmbInicio.addItem(textEstado.getText());
            cmbLlegada.addItem(textEstado.getText());
            cmbEstadoL.addItem(textEstado.getText());

            refrescarTabla();
            refrescarGrafo();
        }
    }//GEN-LAST:event_btnAddEstActionPerformed

    private void tipoAutFinDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoAutFinDetActionPerformed
        this.AFDET = new AutoFinDet(); //Reseteamos el AFD
        btnReiniciar.doClick(); //Limpiamos todo
        cmbSimb.removeItem("Lamda");
        btnLlegadas.setVisible(false);
        cmbLlegada.setVisible(true);
        this.btnAddT.setVisible(true);
    }//GEN-LAST:event_tipoAutFinDetActionPerformed

    private void tipoAFNDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoAFNDActionPerformed
        this.AFNDet = new AutoFinNoDet(); //Reinciar el AFND
        btnReiniciar.doClick(); //Reiniciar
        cmbSimb.addItem("Lamda");
        btnAddT.setVisible(false);
        cmbLlegada.setVisible(false);
        btnLlegadas.setVisible(true);
    }//GEN-LAST:event_tipoAFNDActionPerformed

    private void btnAddSimbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSimbActionPerformed
        if (!textSimbolo.getText().isBlank() && numSimbolos.add(textSimbolo.getText())) {
            cmbSimb.addItem(textSimbolo.getText());
            refrescarTabla();
        }
    }//GEN-LAST:event_btnAddSimbActionPerformed

    private void btnAddTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTActionPerformed
        if (tipoAutFinDet.isSelected()) //Traer las traansiciones del comboBox
        {
            TransicionAFD t = new TransicionAFD(cmbInicio.getSelectedItem().toString(), cmbSimb.getSelectedItem().toString().charAt(0), cmbLlegada.getSelectedItem().toString());
            AFDET.aniadirTransicion(t);
            System.out.println("Transición Añadida:" + t);
        }
        refrescarTabla();
        refrescarGrafo();
    }//GEN-LAST:event_btnAddTActionPerformed

    private void btnBorrarSimbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarSimbActionPerformed
        this.numSimbolos.remove(textSimbolo.getText());
        cmbSimb.removeItem(textSimbolo.getText());

        if (tipoAutFinDet.isSelected()) {
            AFDET.eliminarSimbolo(textSimbolo.getText().charAt(0)); //Borra  las transiciones que consumen simbolo
        } else {
            AFNDet.borrarSimbolo(textSimbolo.getText().charAt(0));
        }
        refrescarTabla();
    }//GEN-LAST:event_btnBorrarSimbActionPerformed

    private void btnBorrarEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarEstActionPerformed
        String estado = textEstado.getText();
        this.numEstados.remove(estado);
        cmbEstadoL.removeItem(estado);
        cmbInicio.removeItem(estado);

        if (tipoAutFinDet.isSelected()) {
            cmbLlegada.removeItem(estado);
            AFDET.eliminarEstado(estado);
        } else {
            AFNDet.borrarEstado(estado);
        }
        refrescarTabla();
        refrescarGrafo();
    }//GEN-LAST:event_btnBorrarEstActionPerformed

    private void btnBorrarTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarTransActionPerformed
        if (tipoAutFinDet.isSelected()) {
            for (int i : tablaDeTransicion.getSelectedRows()) {
                System.out.println("Eliminando fila: " + i);
                String origen = tabla.getValueAt(i, 0).toString();

                for (int j = 1; j < tabla.getColumnCount(); j++) {//Por cada simbolo
                    String destino = tabla.getValueAt(i, j).toString();
                    TransicionAFD t = new TransicionAFD(origen, tabla.getColumnName(j).charAt(0), destino);
                    System.out.println("Eliminar transicion " + t);
                    AFDET.eliminarTransicion(t); //Borramos la transicion 
                }

            }
        }//TODO eliminar AFND

        refrescarTabla();
        refrescarGrafo();
    }//GEN-LAST:event_btnBorrarTransActionPerformed

    private void btnEstFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstFinActionPerformed
        PnlEstado pnlEstado = new PnlEstado();
        pnlEstado.setEstados(numEstados);

        int respuesta = JOptionPane.showConfirmDialog(null, pnlEstado,
                "Elegir Estados", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (respuesta == JOptionPane.OK_OPTION) {
            if (tipoAutFinDet.isSelected()) {
                AFDET.setEstadosFinales(pnlEstado.getEstados());
            } else {
                AFNDet.setEstadosDeFin(pnlEstado.getEstados());
            }

            labelEstadosI.setText("Estados finales: " + pnlEstado.getEstados());
            refrescarGrafo();
        }

    }//GEN-LAST:event_btnEstFinActionPerformed

    private void btnLlegadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLlegadasActionPerformed
        PnlEstado pE = new PnlEstado();
        pE.setEstados(numEstados);

        int res = JOptionPane.showConfirmDialog(null, pE,
                "Elegir Estados", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (cmbSimb.getSelectedItem().equals("Lamda")) //Es una L-T             
            {
                TransLamda transLamda = new TransLamda(cmbInicio.getSelectedItem().toString(), pE.getEstados());
                AFNDet.aniadirTransLamda(transLamda);
                System.out.println("Transición lamda añadida:" + transLamda);
            } else {
                TransAutFinNDet trans = new TransAutFinNDet(cmbInicio.getSelectedItem().toString(), cmbSimb.getSelectedItem().toString().charAt(0), pE.getEstados());
                AFNDet.aniadirTransicion(trans);
                System.out.println("Transición añadida: " + trans);
            }

        }
        refrescarTabla();
        refrescarGrafo();
    }//GEN-LAST:event_btnLlegadasActionPerformed

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        this.numEstados.clear();
        this.numSimbolos.clear();
        this.AFDET = new AutoFinDet();
        this.AFNDet = new AutoFinNoDet();
      
        textCadena.setText("");
     
        lblEstadoL.setText("Estado inicial: NO SELECCIONADO");
        labelEstadosI.setText("Estados finales: NO SELECCIONADOS");
    
        this.cmbLlegada.removeAllItems();
        this.cmbInicio.removeAllItems();
        this.cmbSimb.removeAllItems();
        this.cmbEstadoL.removeAllItems();

        this.cmbInicio.addItem("Origen");
        this.cmbSimb.addItem("Símbolo");
        this.cmbLlegada.addItem("Destino");
        if (this.tipoAFND.isSelected()) {
            this.cmbSimb.addItem("LAMBDA");
        }
        reiniciarTabla();
        refrescarTabla(); //Volver a pintar la tabla vacia
        refrescarGrafo();

    }//GEN-LAST:event_btnReiniciarActionPerformed

    private void textCadenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCadenaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textCadenaActionPerformed

    private void btnEvaluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEvaluarActionPerformed
        try {

            if (tipoAutFinDet.isSelected()) {
                if (comprobarDeterminismo(this.AFDET, this.numEstados, this.numSimbolos)) {
                    System.out.println("Determinismo del AFD correcto");
                } else {
                    System.out.println("Indeterminismo ne el automata, se añadé estado absorción");
                    JOptionPane.showMessageDialog(this, "Indeterminismo en el AFD!\n(No ha indicado todas las transiciones para cada estado y símbolo)\n"
                        + "Se procederá a crear un nuevo estado de absorción A", "Error", JOptionPane.WARNING_MESSAGE);
                    this.aniadirEstadoMuerto();
                }

                if (AFDET.Check(textCadena.getText())) {
                    JOptionPane.showConfirmDialog(rootPane, "Cadena Válida", "Respuesta autótama", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showConfirmDialog(rootPane, "Cadena NO Válida", "Respuesta autótama", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (AFNDet.Check(textCadena.getText())) {
                    JOptionPane.showConfirmDialog(rootPane, "Cadena Válida", "Respuesta autótama", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showConfirmDialog(rootPane, "Cadena NO Válida", "Respuesta autótama", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en la siulación!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEvaluarActionPerformed

    private void cmbLlegadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLlegadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbLlegadaActionPerformed

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
            java.util.logging.Logger.getLogger(Ventana.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddEst;
    private javax.swing.JButton btnAddSimb;
    private javax.swing.JButton btnAddT;
    private javax.swing.JButton btnBorrarEst;
    private javax.swing.JButton btnBorrarSimb;
    private javax.swing.JButton btnBorrarTrans;
    private javax.swing.JButton btnEstFin;
    private javax.swing.JButton btnEvaluar;
    private javax.swing.JButton btnLlegadas;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbEstadoL;
    private javax.swing.JComboBox<String> cmbInicio;
    private javax.swing.JComboBox<String> cmbLlegada;
    private javax.swing.JComboBox<String> cmbSimb;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPnlPrincipal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelEstadosI;
    private javax.swing.JLabel lblConjTransiciones;
    private javax.swing.JLabel lblCreEst;
    private javax.swing.JLabel lblEstadoL;
    private javax.swing.JLabel lblSimbEnt;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable tablaDeTransicion;
    private javax.swing.JTextField textCadena;
    private javax.swing.JTextField textEstado;
    private javax.swing.JTextField textSimbolo;
    private javax.swing.JRadioButton tipoAFND;
    private javax.swing.JRadioButton tipoAutFinDet;
    // End of variables declaration//GEN-END:variables
}
