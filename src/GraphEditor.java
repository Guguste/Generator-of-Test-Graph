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

public class GraphEditor extends PCanvas implements Serializable {

	private static final long serialVersionUID = 1L;
	private PLayer nodeLayer;
	
	private boolean saveNodes = false;
	private boolean deleteNodes = false;
	private boolean newRoad = false;
	private boolean saveEdges = false;
	private boolean deleteEdges = false;
	
	private int nbClick = 0;
	private PNode node1;
	private PNode node2;
	private PLayer edgeLayer;
	private JFrame popup = new JFrame();
	
	private ArrayList<Summit> road = new ArrayList<Summit>();

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
				if (e.getButton() == MouseEvent.NOBUTTON && e.getPickedNode().getPaint()!=Color.ORANGE) {
					if (e.getPickedNode() != node1 && nbClick > 0 ) {
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
				} else if (deleteNodes && e.getPickedNode() instanceof PPath) {
					PNode removedNode = e.getPickedNode();
					(listOfSummit.get(removedNode)).delete(nodeLayer,
							edgeLayer, listOfEdge);
					nodeLayer.removeChild(removedNode);
				}
				else if (newRoad && e.getPickedNode() instanceof PPath){//Permite to save all the node that the user select		
					road.add(listOfSummit.get(e.getPickedNode()));
					listOfSummit.get(e.getPickedNode()).changeColor(true);
					System.out.print("save a node");
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

	public void reset() {
		this.deleteNodes = false;
		this.saveNodes = false;
		this.saveEdges = false;
		this.deleteEdges = false;
	}

	public void startSaveNodes() {
		this.saveNodes = true;
		System.out.println("save node");
	}

	public void changeSizeOfNodes(int size) {
		Set<Entry<PPath, Summit>> entry = listOfSummit.entrySet();
		Iterator<Entry<PPath, Summit>> it = entry.iterator();
		while (it.hasNext()) {
			Entry<PPath, Summit> value = it.next();
			Summit s = (Summit) value.getValue();
			s.changeBounds(size);
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

	public void startSaveEdges() {
		this.saveEdges = true;
		System.out.println("save edge");
	}
	
	public void newRoad(){
	if(!newRoad)
		newRoad=true;
	else{
		newRoad=false;
		System.out.print("stop saving node");
		System.out.print(road.size());
		newRoad=false;
		for(int i = 0 ; i < road.size() ; road.get(i++).changeColor(false)); //Change color of the nodes which are selected to the road 
		road.removeAll(road);	
		}
	}
	
	public void updateEdge(PPath edge) {
	    final Point2D p1 = node1.getFullBoundsReference().getCenter2D();
	    final Point2D p2 = node2.getFullBoundsReference().getCenter2D();
	    listOfEdge.get(edge).upDateArrow( new Point2D.Double(p1.getX(), p1.getY()), new	Point2D.Double(p2.getX(), p2.getY()));	    
		listOfEdge.get(edge).updateText(p1, p2);
	}


}
