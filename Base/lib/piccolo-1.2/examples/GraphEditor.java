import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class GraphEditor extends PCanvas {

	private static final long serialVersionUID = 1L;
	private PLayer nodeLayer;
	private boolean saveNodes=false;
	private int nbClick=0;
	private PNode node1;
	private PNode node2;
	private PLayer edgeLayer;
	
	public GraphEditor(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        int numNodes = 10;
        int numEdges = 10;

        // Initialize, and create a layer for the edges
        // (always underneath the nodes)
        nodeLayer = getLayer();
        edgeLayer = new PLayer();
        getRoot().addChild(edgeLayer);
        getCamera().addLayer(0, edgeLayer);
        Random random = new Random();

        // Create some random nodes
        // Each node's attribute set has an
        // ArrayList to store associated edges
        for (int i = 0; i < numNodes; i++) {
            float x = random.nextInt(width);
            float y = random.nextInt(height);
            PPath node = PPath.createEllipse(x, y, 30, 30);
            node.addAttribute("edges", new ArrayList());
            
            nodeLayer.addChild(node);
        }
        
        // Create some random edges
        // Each edge's attribute set has an
        // ArrayList to store associated nodes
        for (int i = 0; i < numEdges; i++) {
            int n1 = random.nextInt(numNodes);
            int n2 = random.nextInt(numNodes);

            // Make sure we have two distinct nodes.
            while (n1 == n2) {
                n2 = random.nextInt(numNodes);
            }
            
            PNode node1 = nodeLayer.getChild(n1);
            PNode node2 = nodeLayer.getChild(n2);
            
            PPath edge = new PPath();
            ((ArrayList<PPath>)node1.getAttribute("edges")).add(edge);
            ((ArrayList<PPath>)node2.getAttribute("edges")).add(edge);
            edge.addAttribute("nodes", new ArrayList());
            ((ArrayList<PNode>)edge.getAttribute("nodes")).add(node1);
            ((ArrayList<PNode>)edge.getAttribute("nodes")).add(node2);
            edgeLayer.addChild(edge);
            updateEdge(edge);
        }
        
        nodeLayer.addInputEventListener(new PDragEventHandler() {
            {
                PInputEventFilter filter = new PInputEventFilter();
                filter.setOrMask(InputEvent.BUTTON1_MASK | InputEvent.BUTTON3_MASK);
                setEventFilter(filter);
            }

            public void mouseEntered(PInputEvent e) {
                super.mouseEntered(e);
                if (e.getButton() == MouseEvent.NOBUTTON) {
                    e.getPickedNode().setPaint(Color.RED);
                }
            }
            
            public void mouseExited(PInputEvent e) {
                super.mouseExited(e);
                if (e.getButton() == MouseEvent.NOBUTTON) {
                    e.getPickedNode().setPaint(Color.WHITE);
                }
            }
            
            public void mouseClicked(PInputEvent e){
            	super.mouseClicked(e);
            	if(saveNodes){
            		if(nbClick==0){
            			nbClick++;
            			node1=e.getPickedNode();
            		}
            		else{
            			node2=e.getPickedNode();
            			nbClick=0;
            			newEdge();
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
                if(e.getPickedNode() instanceof PPath){
                	ArrayList edges = (ArrayList) e.getPickedNode().getAttribute("edges");
                	for (int i = 0; i < edges.size(); i++) {
                		GraphEditor.this.updateEdge((PPath) edges.get(i));
                	}
                }
            }
        });
    }
	public void newNode(){
        Random random = new Random();

        PPath node = PPath.createEllipse(1, 1, 30, 30);
        PText text = new PText("blop");
        text.setBounds(1, 1, 20, 20);
        node.addAttribute("edges", new ArrayList());
        nodeLayer.addChild(node);
        nodeLayer.addChild(text);
	}
	
	public void newEdge(){
        PPath edge = new PPath();
        ((ArrayList<PPath>)this.node1.getAttribute("edges")).add(edge);
        ((ArrayList<PPath>)this.node2.getAttribute("edges")).add(edge);
        edge.addAttribute("nodes", new ArrayList());
        ((ArrayList<PNode>)edge.getAttribute("nodes")).add(this.node1);
        ((ArrayList<PNode>)edge.getAttribute("nodes")).add(this.node2);
        edgeLayer.addChild(edge);
        updateEdge(edge);
	}
	
	public void startSaveNodes(){
		this.saveNodes=true;
	}
	public void stopSaveNodes(){
		this.saveNodes=false;
	}
	

    public void updateEdge(PPath edge) {
        // Note that the node's "FullBounds" must be used
        // (instead of just the "Bounds") because the nodes
        // have non-identity transforms which must be included
        // when determining their position.

        PNode node1 = (PNode) ((ArrayList)edge.getAttribute("nodes")).get(0);
        PNode node2 = (PNode) ((ArrayList)edge.getAttribute("nodes")).get(1);
        Point2D start = node1.getFullBoundsReference().getCenter2D();
        Point2D end = node2.getFullBoundsReference().getCenter2D();
        edge.reset();
        edge.moveTo((float)start.getX(), (float)start.getY());
        edge.lineTo((float)end.getX(), (float)end.getY());
    }
}