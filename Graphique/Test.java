/**
 * Created by Monarque on 09/04/2014.
*/

/**
 * Created by Monarque on 09/04/2014.
 */
/*  Ne pas oublier d'installer la librairie Jgraphx
    Voiçi le lien de dl: http://www.jgraph.com/jgraphdownload.html
    Pour l'installer sous intellij, allez dans File > projet structure > librairies >
     cliquez sur le "+" et ajouter la lib se trouvant dans le dossier précédemment téléchargé
*/

/*
    User Manual : http://jgraph.github.io/mxgraph/docs/manual_javavis.html
*/

import javax.swing.JFrame;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class Test extends JFrame {

    /** Pour éviter un warning venant du JFrame */
    private static final long serialVersionUID = -8123406571694511514L;

    public Test() {
        super("JGrapghX tutoriel: Exemple 1");

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30);
            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150, 80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);
        } finally {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Test frame = new Test();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
    }
}
