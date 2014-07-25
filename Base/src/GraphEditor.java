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
	private boolean deleteNodes=false;
	private int nbClick=0;
	private PNode node1;
	private PNode node2;
	private PLayer edgeLayer;
	
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
                filter.setOrMask(InputEvent.BUTTON1_MASK | InputEvent.BUTTON3_MASK);
                setEventFilter(filter);
            }        int numNodes = 10;
            int numEdges = 10;


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
            	if(saveNodes && e.getPickedNode() instanceof PPath){
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
            	else if(deleteNodes && e.getPickedNode() instanceof PPath){
            		System.out.print("blop");
            		PNode removedNode = e.getPickedNode();
            		nodeLayer.removeChild(removedNode);
            		
            		// List of edges which link with removed node
            		ArrayList edges = (ArrayList) removedNode.getAttribute("edges");
            		
            		for(int i=0;i<edges.size();i++){            	
            			PPath edge=(PPath) edges.get(i);
            			ArrayList nodesList = (ArrayList) edge.getAttribute("nodes");
            			
           				PNode nodeElem= (PNode) nodesList.get(0);
           				ArrayList nodeElemListEdges = (ArrayList)nodeElem.getAttribute("edges");
           				nodeElemListEdges.remove(edge);
           				
           				PNode nodeElem2= (PNode) nodesList.get(1);
           				ArrayList nodeElemListEdges2 = (ArrayList)nodeElem.getAttribute("edges");
           				nodeElemListEdges2.remove(edge);
           				System.out.println(i);
           				edge.reset();            				
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
		Summit node = new Summit();
        nodeLayer.addChild(node.getNode());
	}
	
	public void deleteNode(){this.deleteNodes=true;System.out.println("delete");}
	public void stopDeleteNode(){this.deleteNodes=false;System.out.println("stop delete");}
	public void startSaveNodes(){this.saveNodes=true;System.out.println("save node");}
	public void stopSaveNodes(){this.saveNodes=false;}
	
	public void newEdge(){        
        // node 1 and 2 are save in the function named  mouseClicked 
	    Edges edge = new Edges(node1,node2);
        edgeLayer.addChild(edge.getEdge());   
        updateEdge(edge.getEdge());
    
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