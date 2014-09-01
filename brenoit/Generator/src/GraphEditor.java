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

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPickPath;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolo.util.PBounds;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GraphEditor extends PCanvas {

        private static final long serialVersionUID = 1L;
        public boolean saveNodes=false;
        public boolean saveNodes2=false;
        private boolean deleteNodes=false;
        private boolean saveEdges=false; 
        private boolean deleteEdges=false;
        private boolean changeColor=false;
        private int nbClick=0;
        private PNode node1;
        private PNode node2;
        private PLayer nodeLayer;
        private PLayer edgeLayer;
        
        
        private HashMap<PPath, Edges>  listOfEdge = new HashMap<PPath, Edges> ();
        private HashMap<PPath, Summit> listOfSummit = new HashMap<PPath, Summit> ();

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
                	if (changeColor == false){
                		e.getPickedNode().setPaint(Color.WHITE);
                	}
                }
            }
            
            public void mouseClicked(PInputEvent e){
                super.mouseClicked(e);
                
                if(saveNodes && e.getPickedNode() instanceof PPath){
                        if(nbClick==0){
                            nbClick++;
                            node1=e.getPickedNode();
                            changeColor(true); 
                        }
                        
                        else{
                            node2=e.getPickedNode();
                            changeColor(false);
                            nbClick=0;
                            newEdge();
                        }
                }
                
                else if(saveNodes2 && e.getPickedNode() instanceof PPath){
                    if(nbClick==0){
                        nbClick++;
                        node1=e.getPickedNode();
                        changeColor(true); 
                    }
                    
                    else{
                        node2=e.getPickedNode();
                        changeColor(false);
                        nbClick=0;
                        newEdge2();
                    }
                }
                
                else if(deleteNodes && e.getPickedNode() instanceof PPath){
                        PNode removedNode = e.getPickedNode();
                        (listOfSummit.get(removedNode)).delete(nodeLayer, edgeLayer, listOfEdge);
                        nodeLayer.removeChild(removedNode);            
                }
            }
            
            protected void changeColor(boolean node){
            		if (node){
            			changeColor = true;
            			node1.setPaint(Color.ORANGE);
            		}
            		else {
            			changeColor = false;
            			node1.setPaint(Color.WHITE);
            			node2.setPaint(Color.WHITE);
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
                        	if(saveNodes){
                                GraphEditor.this.updateEdge((PPath) edges.get(i));
                                System.out.println("JE TEST LES ARETES NORMALES !!!");
                                //saveNodes2 = false;
                        	}	
                        	else if(saveNodes2){
                        		GraphEditor.this.updateEdge2((PPath) edges.get(i));
                        		System.out.println("J'ESSAYE DE TESTER LES ARETES COURBEES !!!");
                        		//saveNodes = false;
                        	}
                        }                               
                }
            }
        });
    }

        public void newNode(){
                Summit node = new Summit();
                listOfSummit.put(node.getNode(), node);
                nodeLayer.addChild(node.getNode());  
        }

        public void deleteNode(){this.deleteNodes=true;System.out.println("delete node");}
        public void stopDeleteNode(){this.deleteNodes=false;System.out.println("stop delete node");}
        public void startSaveNodes(){this.saveNodes=true;System.out.println("save node");}
        public void startSaveNodes2(){this.saveNodes2=true;System.out.println("save node curve");}
        public void stopSaveNodes(){this.saveNodes=false;}
        public void changeSizeOfNodes(int size){
        	System.out.println(size);
        	
        	Set entry = listOfSummit.entrySet();
        	Iterator it = entry.iterator();
        	while(it.hasNext()){
        	  Map.Entry value = (Map.Entry)it.next();
        	  System.out.println(value.getValue());
        	  Summit s = (Summit)value.getValue();
        	  s.printWidth();
        	  s.setWH(size, size);
        	  System.out.println("NEW");
        	  s.printWidth();
        	}
        }

        public void newEdge(){        
        // node 1 and 2 are save in the function named  mouseClicked 
            Edges edge = new Edges(node1,node2);
            listOfEdge.put(edge.getEdge(), edge );
            edgeLayer.addChild(edge.getEdge());   
            updateEdge(edge.getEdge());
        }
        
        public void newEdge2(){        
            // node 1 and 2 are save in the function named  mouseClicked 
                Edges edge = new Edges(node1,node2);
                listOfEdge.put(edge.getEdge(), edge );
                edgeLayer.addChild(edge.getEdge()); 
                System.out.println("ON TEST BLABLA !");
                updateEdge2(edge.getEdge());
            }

        public void deleteEdge(){this.deleteEdges=true;System.out.println("delete edge");}
        public void stopDeleteEdge(){this.deleteEdges=false;System.out.println("stop delete edge");}
        public void startSaveEdges(){this.saveEdges=true;System.out.println("save edge");}
        public void stopSaveEdges(){this.saveEdges=false;}
        
        
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
        
        public void updateEdge2(PPath edge) {
        // Note that the node's "FullBounds" must be used
        // (instead of just the "Bounds") because the nodes
        // have non-identity transforms which must be included
        // when determining their position.
            /***
            PNode node1 = (PNode) ((ArrayList)edge.getAttribute("nodes")).get(0);
            PNode node2 = (PNode) ((ArrayList)edge.getAttribute("nodes")).get(1);
            Point2D start = node1.getFullBoundsReference().getCenter2D();
            Point2D end = node2.getFullBoundsReference().getCenter2D();
            edge.reset();
            edge.moveTo((float)start.getX(), (float)start.getY());
            edge.lineTo((float)end.getX(), (float)end.getY());
            ***/
        edge.reset();	
        PNode node1 = (PNode) ((ArrayList)edge.getAttribute("nodes")).get(0);
        PNode node2 = (PNode) ((ArrayList)edge.getAttribute("nodes")).get(1);
        PBounds b1 = node1.getFullBounds();
        PBounds b2 = node2.getFullBounds();
        Point2D c1 = b1.getCenter2D();
        Point2D c2 = b2.getCenter2D();
        double half = Math.abs(c1.getX() - c2.getX()) / 2;
        Point2D i1 = new Point2D.Double(Math.min(c1.getX(), c2.getX()) + half, Math.min(b1.getY(), b2.getY()));
        edge.moveTo((float) c1.getX(), (float) c1.getY());
        edge.curveTo((float) i1.getX(), (float) i1.getY(), (float) i1.getX(), (float) i1.getY(), (float) c2.getX(), (float) c2.getY());
    }
} //end of class