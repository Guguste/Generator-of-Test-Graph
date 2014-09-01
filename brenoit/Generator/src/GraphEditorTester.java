
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.image.*;

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
        final JPanel mainPane = new JPanel();        
        JPanel boutonPane = new JPanel();
        boutonPane.setLayout(new GridLayout(7, 1));
        
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
              graphEditor.saveNodes2 = false;
            }
          });
        
        JButton newCurvedEdgesButton = new JButton("New Curved Edges");
        newCurvedEdgesButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
              graphEditor.stopDeleteNode();
              graphEditor.startSaveNodes2();
              graphEditor.saveNodes = false;
            }
          });
        
        /*JButton deleteEdgeButton = new JButton("Delete Edge");
        deleteNodeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                graphEditor.stopSaveEdges();
                graphEditor.deleteEdge();
            }
          });*/
        
        JButton buttonPrint = new JButton("Make Image");
        buttonPrint.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent ae){
          JFrame win = (JFrame)SwingUtilities.getWindowAncestor(mainPane);
          Dimension size = win.getSize();
          BufferedImage image = (BufferedImage)win.createImage(size.width, size.height);
          Graphics g = image.getGraphics();
          win.paint(g);
          g.dispose();
          try      {
            ImageIO.write(image, "jpg", new File("TryToMakeAnImage.jpg"));
            System.out.println("I print an Image");
          }
          catch (IOException e){
            e.printStackTrace();
          }
        }
      });
        
        // source : http://docs.oracle.com/javase/tutorial/uiswing/components/slider.html
        
        final JSlider sizeOfNodes = new JSlider(JSlider.HORIZONTAL, 20, 100, 40);
        sizeOfNodes.setMajorTickSpacing(20);
        sizeOfNodes.setMinorTickSpacing(5);
        sizeOfNodes.setPaintTicks(true);
        sizeOfNodes.setPaintLabels(true);
        Font font = new Font("Serif", Font.ITALIC, 15);
        sizeOfNodes.setFont(font);
        
        sizeOfNodes.addChangeListener(new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		graphEditor.changeSizeOfNodes((int)sizeOfNodes.getValue());
            	mainPane.revalidate();
            	System.out.println("Done");
        	}
        });
        
        // Add of differents buttons and menu on the panel
        boutonPane.add(listMenu);
        boutonPane.add(moveButton);
        boutonPane.add(newNodeButton);
        boutonPane.add(newEdgesButton);
        boutonPane.add(newCurvedEdgesButton);
        boutonPane.add(deleteNodeButton);
        //boutonPane.add(deleteEdgeButton);
        boutonPane.add(buttonPrint);
        boutonPane.add(sizeOfNodes);
        
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