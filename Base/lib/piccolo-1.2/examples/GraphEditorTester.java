import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphEditorTester extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private GraphEditor graphEditor;


	public GraphEditorTester() {
        setTitle("Piccolo2D Graph Editor");
        
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int hauteur = (int)tailleEcran.getHeight();
		int largeur = (int)tailleEcran.getWidth();
	    this.setTitle("Ma première fenêtre Java");
	    this.setSize(largeur, hauteur);
	    
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graphEditor = new GraphEditor(500, 500);

        JPanel boutonPane = new JPanel();
        
        JButton newNodeButton = new JButton("New Node");
        newNodeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
            	graphEditor.stopSaveNodes();
            	graphEditor.newNode();
            }
          });
        
        JButton newEdgesButton = new JButton("New Edges");
        newEdgesButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
              graphEditor.startSaveNodes();
            }
          });
        
        boutonPane.add(newNodeButton);
        boutonPane.add(newEdgesButton);
        boutonPane.add(graphEditor);
        getContentPane().add(boutonPane);
        pack();
        setVisible(true);
        

    }

    public static void main(String args[]) {
        new GraphEditorTester();
    }

}