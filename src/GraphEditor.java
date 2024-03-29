//package Generator.src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JFrame;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class GraphEditor extends PCanvas implements Serializable {

	private static final long serialVersionUID = 1L;
	private PLayer nodeLayer;
	private String actionToDo = new String("");
	private int nbClick = 0;
	private PNode node1;
	private PNode node2;
	private PLayer edgeLayer;
	private Summit startGraph = new Summit("start", 100, 20);
	private Summit endGraph = new Summit("end", 100, 100);
	private ArrayList<Summit> road = new ArrayList<Summit>();
	private HashMap<PPath, Edges> listOfEdge = new HashMap<PPath, Edges>();
	private HashMap<PPath, Summit> listOfSummit = new HashMap<PPath, Summit>();
	private HashMap<PText, ControlWay> listOfControlWay = new HashMap<PText, ControlWay>();

	public GraphEditor(int width, int height) {
		setPreferredSize(new Dimension(width, height));

		// Initialize, and create a layer for the edges
		// (always underneath the nodes)

		nodeLayer = getLayer();
		edgeLayer = new PLayer();

		nodeLayer.addChild(startGraph.getNode());
		nodeLayer.addChild(endGraph.getNode());

		listOfSummit.put(startGraph.getNode(), startGraph);
		listOfSummit.put(endGraph.getNode(), endGraph);

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
				if (e.getButton() == MouseEvent.NOBUTTON && e.getPickedNode().getPaint() != Color.ORANGE) {
					if (!(e.getPickedNode() == node1 && nbClick > 0))
						e.getPickedNode().setPaint(Color.RED);
				}
			}

			public void mouseExited(PInputEvent e) {
				super.mouseExited(e);
				if (e.getButton() == MouseEvent.NOBUTTON
						&& e.getPickedNode().getPaint() != Color.ORANGE) {
					if (e.getPickedNode() != node1 && nbClick > 0) {
						e.getPickedNode().setPaint(Color.WHITE);
					} else if (nbClick == 0) {
						e.getPickedNode().setPaint(Color.WHITE);
					}
				}
			}

			public void mouseClicked(PInputEvent e) {
				super.mouseClicked(e);

				if (actionToDo.equals("saveNodes")
						&& e.getPickedNode() instanceof PPath) {
					if (nbClick == 0) {
						nbClick++;
						node1 = e.getPickedNode();
						listOfSummit.get(node1).changeColor(true);
					}

					else {
						node2 = e.getPickedNode();
						nbClick = 0;
						listOfSummit.get(node2).changeColor(true);
						newEdge();
						listOfSummit.get(node1).changeColor(false);
						listOfSummit.get(node2).changeColor(false);
					}
				} else if (actionToDo.equals("deleteNode")
						&& e.getPickedNode() instanceof PPath) {
					PNode removedNode = e.getPickedNode();
					(listOfSummit.get(removedNode)).delete(nodeLayer,
							edgeLayer, listOfEdge);
					nodeLayer.removeChild(removedNode);
				} else if (actionToDo.equals("newRoad")
						&& e.getPickedNode() instanceof PPath) {
					// if conditions test the existing of edge between the two
					// selected nodes
					// Permite to save all the node that the user select
						if (road.size() > 0) {
							PPath edge = road.get(road.size() - 1)
									.haveLinkWhith((PPath) e.getPickedNode());
							if (edge != null) {
								listOfEdge.get(edge).changeColor(true);
								road.add(listOfSummit.get(e.getPickedNode()));
								listOfSummit.get(e.getPickedNode()).changeColor(true);
							}
						}
						else if (road.size()==0){
							road.add(listOfSummit.get(e.getPickedNode()));
							listOfSummit.get(e.getPickedNode()).changeColor(true);
							System.out.println("save a node");
						}
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
					ArrayList edges = (ArrayList) e.getPickedNode()
							.getAttribute("edges");
					for (int i = 0; i < edges.size(); i++) {
						updateEdge((PPath) edges.get(i));
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

	public void reset() {
		actionToDo = new String("");
	}

	public void setAction(String text) {
		actionToDo = new String(text);
	}

	public void changeSizeOfNodes(int size) {
		Set<Entry<PPath, Summit>> entry = listOfSummit.entrySet();
		Iterator<Entry<PPath, Summit>> it = entry.iterator();
		while (it.hasNext()) {
			Entry<PPath, Summit> value = it.next();
			Summit s = (Summit) value.getValue();
			s.changeBounds(size);
		}
		fullUpdateEdge();
	}

	public void newEdge() {
		// node 1 and 2 are save in the function named mouseClicked
		if (node1 != endGraph.getNode() && node2 != startGraph.getNode()
				&& (listOfSummit.get(node1).haveLinkWhith(node2)) == null) {
			Edges edge = new Edges(node1, node2);
			nodeLayer.addChild(edge.getText());
			listOfEdge.put(edge.getEdge(), edge);
			edgeLayer.addChild(edge.getEdge());
			updateEdge(edge.getEdge());
		}

	}

	public void newRoad() {
		if (road.size() > 0) {
			actionToDo = new String("");
			Iterator<Summit> it = road.iterator();
			Summit sp = null;// previous node
			Summit s = it.hasNext() ? it.next() : null;// Current node on the
														// list
			while (it.hasNext()) {
				// Change color of the nodes which are selected to the road
				if (sp != null) {
					sp.changeColor(false);
					PPath edge = sp.haveLinkWhith(s.getNode());// getting of the
																// edge between
																// previous node
																// and current
																// node
					listOfEdge.get(edge).changeColor(false);
				}
				sp = s;
				s = it.next();
				s.changeColor(false);
			}
			PPath edge = sp.haveLinkWhith(s.getNode());
			listOfEdge.get(edge).changeColor(false);
			ControlWay way = new ControlWay(road);
			listOfControlWay.put(way.getResume(),way );
			nodeLayer.addChild(way.getResume());
			road.removeAll(road); // reinitialisation of the listRoad which
									// contain the node which are selected
		}
	}

	public void updateEdge(PPath edge) {// This function repaint or creat One
										// edges
		node1 = (PNode) ((ArrayList<PPath>) edge.getAttribute("nodes")).get(0);
		node2 = (PNode) ((ArrayList<PPath>) edge.getAttribute("nodes")).get(1);
		Point2D p1 = node1.getFullBoundsReference().getCenter2D();
		Point2D p2 = node2.getFullBoundsReference().getCenter2D();
		listOfEdge.get(edge).upDateArrow(
				new Point2D.Double(p1.getX(), p1.getY()),
				new Point2D.Double(p2.getX(), p2.getY()));
		listOfEdge.get(edge).updateText(p1, p2);
	}

	public void fullUpdateEdge() {// This function repaint all the edges when
									// the user modify the size of the nodes
		Set<Entry<PPath, Edges>> entry = listOfEdge.entrySet();
		Iterator<Entry<PPath, Edges>> it = entry.iterator();
		while (it.hasNext()) {
			Entry<PPath, Edges> value = it.next();
			Edges s = (Edges) value.getValue();
			node1 = (PNode) ((ArrayList) (s.getEdge().getAttribute("nodes")))
					.get(0);
			node2 = (PNode) ((ArrayList) (s.getEdge().getAttribute("nodes")))
					.get(1);
			updateEdge(s.getEdge());
		}
	}

}
