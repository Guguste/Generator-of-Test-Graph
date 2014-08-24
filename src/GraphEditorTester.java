//package Generator.src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GraphEditorTester extends JFrame {

	private static final long serialVersionUID = 1L;
	private GraphEditor graphEditor;
	private JComboBox<String> listMenu;
	private DefaultComboBoxModel<String> contentMenu = new DefaultComboBoxModel<String>();

	public GraphEditorTester() {
		setTitle("Piccolo2D Graph Editor");

		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		int hauteur = (int) tailleEcran.getHeight();
		int largeur = (int) tailleEcran.getWidth();
		this.setTitle("Generator of Test Graph");
		this.setSize(largeur, hauteur);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graphEditor = new GraphEditor(500, 500);
		final JPanel mainPane = new JPanel();
		JPanel boutonPane = new JPanel();
		boutonPane.setLayout(new GridLayout(7, 1));

		/* Replaced with JMenu
		 * 
		// listMenu's content
		contentMenu.addElement("Menu");
		contentMenu.addElement("Save");
		contentMenu.addElement("Test");

		// Initialization of listMenu with content
		listMenu = new JComboBox<String>(contentMenu);

		// Manage of actions from listMenu
		listMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println((String) listMenu.getSelectedItem());
				listMenu.setSelectedIndex(0); // Reset the default value to
												// "Menu" after click
			}
		}); */

		JButton moveButton = new JButton("Move Element");
		moveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				graphEditor.stopSaveNodes();
				graphEditor.stopDeleteNode();
			}
		});

		JButton newNodeButton = new JButton("New Node");
		newNodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				graphEditor.stopSaveNodes();
				graphEditor.stopDeleteNode();
				graphEditor.newNode();
			}
		});

		JButton deleteNodeButton = new JButton("Delete Node");
		deleteNodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				graphEditor.stopSaveNodes();
				graphEditor.deleteNode();
			}
		});

		JButton newEdgesButton = new JButton("New Edges");
		newEdgesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				graphEditor.stopDeleteNode();
				graphEditor.startSaveNodes();
				mainPane.repaint();
			}
		});

		JButton deleteEdgeButton = new JButton("Delete Edge");
		deleteNodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				graphEditor.stopSaveEdges();
				graphEditor.deleteEdge();
			}
		});

		// source :
		// http://docs.oracle.com/javase/tutorial/uiswing/components/slider.html

		final JSlider sizeOfNodes = new JSlider(JSlider.HORIZONTAL, 20, 100, 40);
		sizeOfNodes.setMajorTickSpacing(20);
		sizeOfNodes.setMinorTickSpacing(5);
		sizeOfNodes.setPaintTicks(true);
		sizeOfNodes.setPaintLabels(true);
		Font font = new Font("Serif", Font.ITALIC, 15);
		sizeOfNodes.setFont(font);

		sizeOfNodes.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				graphEditor.changeSizeOfNodes((int) sizeOfNodes.getValue());
				mainPane.revalidate();
				System.out.println("Done");
			}
		});
		
		JPanel panelBar = new JPanel();
		panelBar.setLayout(new BorderLayout());
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");

		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");

		file.add(open);
		file.add(save);
		
		menuBar.add(file);
		menuBar.add(edit);
		
		// Add of differents buttons and menu on the panel
		//boutonPane.add(listMenu);
		boutonPane.add(moveButton);
		boutonPane.add(newNodeButton);
		boutonPane.add(newEdgesButton);
		boutonPane.add(deleteNodeButton);
		boutonPane.add(deleteEdgeButton);
		boutonPane.add(sizeOfNodes);

		//mainPane.setLayout(new BorderLayout());
		mainPane.add(boutonPane);
		mainPane.add(graphEditor);
		
		panelBar.add(menuBar);
		
		getContentPane().add(panelBar, BorderLayout.NORTH);
		getContentPane().add(mainPane);
		pack();
		setVisible(true);
	}

	public static void main(String args[]) {
		new GraphEditorTester();
	}
}