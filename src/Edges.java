//package Generator.src;

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Edges implements Serializable{

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
	
	public void  upDateArrow( Point2D start, Point2D end) {

	    // Arrow settings.
	    int b = 20;
	    double theta = Math.toRadians(30);

	    // Arrow Calculations.
	    double xs = start.getX();
	    double ys = start.getY();
	    double xe = end.getX();
	    double ye = end.getY();
	    
	    double ab = Math.sqrt(Math.pow(xe - xs,2) + Math.pow(Math.abs(ye - ys),2));
	    double ae =  ye - ys ;
	    double eb =  xe - xs ;
	    double r = 20;
	    double xend = xs + eb * (ab - r) / ab;
	    double yend = ye + (ae * ( ab - r )/ ab) - ae;

	    double alpha = Math.atan2(yend - ys, xend - xs);
	    double dx1 = b * Math.cos(alpha + theta);
	    double dy1 = b * Math.sin(alpha + theta);
	    double dx2 = b * Math.cos(alpha - theta);
	    double dy2 = b * Math.sin(alpha - theta);

	    // Arrow Path.
	    GeneralPath path = new GeneralPath();
	    path.moveTo(xs, ys);
	    path.lineTo(xend, yend);
	    path.lineTo(xend - dx1, yend - dy1);
	    path.moveTo(xend, yend);
	    path.lineTo(xend - dx2, yend - dy2);

	    edge.setPathTo(path);
	  }
	public void changeColor(boolean changeColor) {
//		if (changeColor) {
//			edge.setPaint(Color.ORANGE);
//		} else {
//			edge.setPaint(Color.WHITE);
//		}
		System.out.println("blopy");
	}
}
