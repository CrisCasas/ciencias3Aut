package Principal;

import java.util.HashSet;
import javax.swing.DefaultListModel;

//Muestra los estados del autómata
public class PnlEstado extends javax.swing.JPanel {
   
    //Crear el form de pnlEstados
    public PnlEstado() {
        initComponents();
    }
 
    //Llenar la lista de estados
    public void setEstados(HashSet<String> estados)
    {
        DefaultListModel Lista = new DefaultListModel();
        Lista.addAll(estados);
        listaEstados.setModel(Lista);
    }

    //Trae los estados elegidos de la lista mostrada al usuario
    public HashSet<String> getEstados()
    {
        HashSet<String> estadosElegidos = new HashSet();
        estadosElegidos.addAll(listaEstados.getSelectedValuesList()); 
        return estadosElegidos;
    }
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlScrollEstados = new javax.swing.JScrollPane();
        listaEstados = new javax.swing.JList<>();

        listaEstados.setFont(new java.awt.Font("Rockwell", 0, 18)); // NOI18N
        listaEstados.setForeground(new java.awt.Color(0, 0, 0));
        pnlScrollEstados.setViewportView(listaEstados);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlScrollEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlScrollEstados, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> listaEstados;
    private javax.swing.JScrollPane pnlScrollEstados;
    // End of variables declaration//GEN-END:variables
}
