package Grafo;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Clase Grafo. Hereda de mxGraph.
 * @author Víctor M. Rodríguez y Fran J. Beltrán
 */
public class Grafo extends mxGraph {

    /**
     * Crea el objeto grafo, que hereda de mxGraph y establece su configuración
     * estética
     */
    public Grafo() {
        super();

        Hashtable<String, Object> estiloEstado = new Hashtable<>();
        estiloEstado.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        estiloEstado.put(mxConstants.STYLE_FONTSIZE, 20);

        Hashtable<String, Object> estiloEFinal = new Hashtable<String, Object>();
        estiloEFinal.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_DOUBLE_ELLIPSE);
        estiloEFinal.put(mxConstants.STYLE_FONTSIZE, 20);
        estiloEFinal.put(mxConstants.STYLE_FILLCOLOR, "black");

        Hashtable<String, Object> estiloEInicial = new Hashtable<String, Object>();
        estiloEInicial.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_DOUBLE_ELLIPSE);
        estiloEInicial.put(mxConstants.STYLE_FONTSIZE, 20);
        estiloEInicial.put(mxConstants.STYLE_FILLCOLOR, "white");

        Map<String, Object> edgeStyle = new HashMap<String, Object>();
        edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        stylesheet.setDefaultEdgeStyle(edgeStyle);

        stylesheet.putCellStyle("ESTADOFINAL", estiloEFinal);
        stylesheet.putCellStyle("ESTADO", estiloEstado);
        stylesheet.putCellStyle("ESTADOINICIAL", estiloEInicial);

        this.setStylesheet(stylesheet);

        this.getModel().beginUpdate();
        this.setCellsLocked(true);
        this.setVertexLabelsMovable(false);
        this.setEdgeLabelsMovable(false);
    }

}
