//package Generator.src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Edges {

	private PPath edge;
	private PText text;
	private String textToPut = JOptionPane.showInputDialog(popup, "What text do you want to put on this edge ?", null);
	private static JFrame popup = new JFrame();

	public Edges(PNode node1, PNode node2) {

		edge = new PPath();
		edge.addAttribute("nodes", new ArrayList<PPath>());

		text = new PText(textToPut);

		edge.addChild(text);
		
		text.addInputEventListener(new PInputEventListener(){
			public void processEvent(PInputEvent e, int descriptor) {
				if (e.getPickedNode() instanceof PText && descriptor == 500) {
					updateText((PText)e.getPickedNode());
				}
			}
		});

		((ArrayList<PPath>) node1.getAttribute("edges")).add(edge);
		((ArrayList<PPath>) node2.getAttribute("edges")).add(edge);
		((ArrayList<PNode>) edge.getAttribute("nodes")).add(node1);
		((ArrayList<PNode>) edge.getAttribute("nodes")).add(node2);
	}

	public Edges(PPath edge) {
		this.edge = edge;
	}

	public void updateText(Point2D start, Point2D end) {
		text.centerBoundsOnPoint((start.getX() + end.getX()) / 2,
				(start.getY() + end.getY()) / 2);
	}
	
	public void updateText(PText ptext) {
		String newText = JOptionPane.showInputDialog(popup, "What text do you want to put on this edge ?", null);
		double X = ptext.getX();
		double Y = ptext.getY();
		ptext.resetBounds();
		this.textToPut = newText;
		ptext.setText(textToPut);
		ptext.setX(X);
		ptext.setY(Y);
	}

	public PText getText() {
		return text;
	}

	public PPath getEdge() {
		return edge;
	}

	public void deleteEdge(PLayer edgeLayer, PLayer nodeLayer) {
		edgeLayer.removeChild((PNode) edge);
		nodeLayer.removeChild(text);
	}
}
