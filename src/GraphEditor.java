//package Generator.src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.nodes.PPath;
//import edu.umd.cs.piccolox.event.PStyledTextEventHandler;
//import edu.umd.cs.piccolox.nodes.PStyledText;
import edu.umd.cs.piccolo.nodes.PText;

public class GraphEditor extends PCanvas {

	private static final long serialVersionUID = 1L;
	private PLayer nodeLayer;
	private boolean saveNodes = false;
	private boolean deleteNodes = false;
	private boolean saveEdges = false;
	private boolean deleteEdges = false;
	private boolean changeColor = false;
	private int nbClick = 0;
	private PNode node1;
	private PNode node2;
	private PLayer edgeLayer;
	private JFrame popup = new JFrame();

	private HashMap<PPath, Edges> listOfEdge = new HashMap<PPath, Edges>();
	private HashMap<PPath, Summit> listOfSummit = new HashMap<PPath, Summit>();

	public GraphEditor(int width, int height) {
		setPreferredSize(new Dimension(width, height));

		// Initialize, and create a layer for the edges
		// (always underneath the nodes)

		nodeLayer = getLayer();
		edgeLayer = new PLayer();

		getRoot().addChild(edgeLayer);
		getCamera().addLayer(0, edgeLayer);
		nodeLayer.addInputEventListener(new PDragEventHandler() {
			{
				PInputEventFilter filter = new PInputEventFilter();
				filter.setOrMask(InputEvent.BUTTON1_MASK
						| InputEvent.BUTTON3_MASK);
				setEventFilter(filter);
			}

			public void mouseEntered(PInputEvent e) {
				super.mouseEntered(e);
				if (e.getButton() == MouseEvent.NOBUTTON) {
					if (!(e.getPickedNode() == node1 && nbClick > 0))
						e.getPickedNode().setPaint(Color.RED);
				}
			}

			public void mouseExited(PInputEvent e) {
				super.mouseExited(e);
				if (e.getButton() == MouseEvent.NOBUTTON) {
					if (e.getPickedNode() != node1 && nbClick > 0) {
						e.getPickedNode().setPaint(Color.WHITE);
					}
					else if (nbClick == 0){
						e.getPickedNode().setPaint(Color.WHITE);
					}
				}
			}

			public void mouseClicked(PInputEvent e) {
				super.mouseClicked(e);
				
				if (saveNodes && e.getPickedNode() instanceof PPath) {
					if (nbClick == 0) {
						nbClick++;
						node1 = e.getPickedNode();
						changeColor(true);
					}

					else {
						node2 = e.getPickedNode();
						changeColor(false);
						nbClick = 0;
						newEdge();
					}
				} else if (deleteNodes && e.getPickedNode() instanceof PPath) {
					PNode removedNode = e.getPickedNode();
					(listOfSummit.get(removedNode)).delete(nodeLayer,
							edgeLayer, listOfEdge);
					nodeLayer.removeChild(removedNode);
				}
			}

			protected void changeColor(boolean node) {
				if (node) {
					changeColor = true;
					node1.setPaint(Color.ORANGE);
				} else {
					changeColor = false;
					node1.setPaint(Color.WHITE);
					node2.setPaint(Color.RED);
				}
			}

			protected void startDrag(PInputEvent e) {
				super.startDrag(e);
				e.setHandled(true);
				e.getPickedNode().moveToFront();
			}

			protected void drag(PInputEvent e) {
				super.drag(e);
				if (e.getPickedNode() instanceof PPath) {
					ArrayList edges = (ArrayList) e.getPickedNode().getAttribute("edges");
					for (int i = 0; i < edges.size(); i++) {
						GraphEditor.this.updateEdge((PPath) edges.get(i));
					}
				}
			}
		});
	}

	public void newNode() {
		Summit node = new Summit();
		listOfSummit.put(node.getNode(), node);
		nodeLayer.addChild(node.getNode());
	}

	public void deleteNode() {
		this.deleteNodes = true;
		System.out.println("delete node");
	}

	public void stopDeleteNode() {
		this.deleteNodes = false;
		System.out.println("stop delete node");
	}

	public void startSaveNodes() {
		this.saveNodes = true;
		System.out.println("save node");
	}

	public void stopSaveNodes() {
		this.saveNodes = false;
	}

	public void changeSizeOfNodes(int size) {
		Set entry = listOfSummit.entrySet();
		Iterator it = entry.iterator();
		while (it.hasNext()) {
			Map.Entry value = (Map.Entry) it.next();
			Summit s = (Summit) value.getValue();
			double X = s.getNode().getX();
			double Y = s.getNode().getY();
			s.getNode().setBounds(X, Y, size, size);
			s.setSliderValue(size);
			System.out.println(X+"\t"+Y+"\t"+size);
		}
	}

	public void newEdge() {
		// node 1 and 2 are save in the function named mouseClicked
		Edges edge = new Edges(node1, node2);
		nodeLayer.addChild(edge.getText());
		listOfEdge.put(edge.getEdge(), edge);
		edgeLayer.addChild(edge.getEdge());
		updateEdge(edge.getEdge());
	}

	public void deleteEdge() {
		this.deleteEdges = true;
		System.out.println("delete edge");
	}

	public void stopDeleteEdge() {
		this.deleteEdges = false;
		System.out.println("stop delete edge");
	}

	public void startSaveEdges() {
		this.saveEdges = true;
		System.out.println("save edge");
	}

	public void stopSaveEdges() {
		this.saveEdges = false;
	}

	public void updateEdge(PPath edge) {
		// Note that the node's "FullBounds" must be used
		// (instead of just the "Bounds") because the nodes
		// have non-identity transforms which must be included
		// when determining their position.

		PNode node1 = (PNode) ((ArrayList) edge.getAttribute("nodes")).get(0);
		PNode node2 = (PNode) ((ArrayList) edge.getAttribute("nodes")).get(1);
		Point2D start = node1.getFullBoundsReference().getCenter2D();
		Point2D end = node2.getFullBoundsReference().getCenter2D();
//		edge.reset();
//		edge.moveTo((float) start.getX(), (float) start.getY());
//		edge.lineTo((float) end.getX(), (float) end.getY());
//		
	    final Point2D p1 = node1.getFullBoundsReference().getCenter2D();
	    final Point2D p2 = node2.getFullBoundsReference().getCenter2D();
	    final Shape newArrow = createArrow(
	        new Point2D.Double(p1.getX(), p1.getY()), new
	Point2D.Double(p2.getX(),
	            p2.getY()));
	    edge.setPathTo(newArrow);
		listOfEdge.get(edge).updateText(start, end);
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

		    return path;
		  }
}
