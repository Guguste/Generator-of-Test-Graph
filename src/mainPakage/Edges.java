package mainPakage;

//package Generator.src;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
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
					System.out.println(descriptor);
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
		this.textToPut = newText;
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
	  private Shape createArrow(Point2D start, Point2D end) {

		    // Arrow settings.
		    int b = 20;
		    double theta = Math.toRadians(30);

		    // Arrow Calculations.
		    double xs = start.getX();
		    double ys = start.getY();
		    double xe = end.getX();
		    double ye = end.getY();
		    double alpha = Math.atan2(ye - ys, xe - xs);
		    double dx1 = b * Math.cos(alpha + theta);
		    double dy1 = b * Math.sin(alpha + theta);
		    double dx2 = b * Math.cos(alpha - theta);
		    double dy2 = b * Math.sin(alpha - theta);

		    // Arrow Path.
		    GeneralPath path = new GeneralPath();
		    path.moveTo(xs, ys);
		    path.lineTo(xe, ye);
		    path.lineTo(xe - dx1, ye - dy1);
		    path.moveTo(xe, ye);
		    path.lineTo(xe - dx2, ye - dy2);

		    return path;
		  }
}
