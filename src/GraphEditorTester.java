
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class GraphEditorTester extends JFrame {

        private static final long serialVersionUID = 1L;
        private GraphEditor graphEditor;
        private JComboBox<String> listMenu;
        private DefaultComboBoxModel<String> contentMenu = new DefaultComboBoxModel<String>();

        public GraphEditorTester() {
        setTitle("Piccolo2D Graph Editor");
        
                Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                int hauteur = (int)tailleEcran.getHeight();
                int largeur = (int)tailleEcran.getWidth();
            this.setTitle("Generator of Test Graph");
            this.setSize(largeur, hauteur);
            
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graphEditor = new GraphEditor(500, 500);
        JPanel mainPane = new JPanel();        
        JPanel boutonPane = new JPanel();
        boutonPane.setLayout(new GridLayout(5, 1));
        
        // listMenu's content 
        contentMenu.addElement("Menu");
        contentMenu.addElement("Save");
        contentMenu.addElement("Test");
        
        // Initialization of listMenu with content
        listMenu = new JComboBox<String>(contentMenu);
        
        // Manage of actions from listMenu
        listMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println((String)listMenu.getSelectedItem());
				listMenu.setSelectedIndex(0); //Reset the default value to "Menu" after click
			}
		});        

        JButton moveButton = new JButton("Move Element");
        moveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                graphEditor.stopSaveNodes();
                graphEditor.stopDeleteNode();

            }
          });
        
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

        // Add of differents buttons and menu on te panel
        boutonPane.add(listMenu);
        boutonPane.add(moveButton);
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