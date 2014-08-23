package Generator.src;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;


public class Edges {

        private PPath edge;
        private PText text;
        
        public Edges(PNode node1, PNode node2){
        	
                edge = new PPath();
                edge.addAttribute("nodes", new ArrayList<PPath>());
                
                text = new PText("blop");

                edge.addChild(text);
                
                ((ArrayList<PPath>)node1.getAttribute("edges")).add(edge);
                ((ArrayList<PPath>)node2.getAttribute("edges")).add(edge); 
                ((ArrayList<PNode>)edge.getAttribute("nodes")).add(node1);
                ((ArrayList<PNode>)edge.getAttribute("nodes")).add(node2);
        }
        
        public Edges(PPath edge){
            this.edge = edge;
        }
        
        public void updateText(Point2D start,Point2D end){
        	text.centerBoundsOnPoint((start.getX()+end.getX())/2, (start.getY()+end.getY())/2);
        }
        
        public PText getText(){
        	return text;
        }
        
        public PPath getEdge(){
                return edge;
        }
        
        public void deleteEdge(PLayer edgeLayer,PLayer nodeLayer){
                edgeLayer.removeChild((PNode)edge);
                nodeLayer.removeChild(text);
        }
}
