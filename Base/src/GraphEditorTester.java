import java.awt.Dimension;
import java.awt.GridLayout;
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
        JPanel mainPane = new JPanel();
        
        JPanel boutonPane = new JPanel();
        boutonPane.setLayout(new GridLayout(3, 1));
        
        JButton newNodeButton = new JButton("New Node");
        newNodeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
            	graphEditor.stopSaveNodes();
            	graphEditor.stopDeleteNode();
            	graphEditor.newNode();
            }
          });
        
        JButton deleteNodeButton = new JButton("Delete Node");
        deleteNodeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
            	graphEditor.stopSaveNodes();
            	graphEditor.deleteNode();
            }
          });
        
        JButton newEdgesButton = new JButton("New Edges");
        newEdgesButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
              graphEditor.stopDeleteNode();
              graphEditor.startSaveNodes();
            }
          });
        
        boutonPane.add(newNodeButton);
        boutonPane.add(newEdgesButton);
        boutonPane.add(deleteNodeButton);
        mainPane.add(boutonPane);
        mainPane.add(graphEditor);
        getContentPane().add(mainPane);
        pack();
        setVisible(true);
        

    }

    public static void main(String args[]) {
        new GraphEditorTester();
    }

}