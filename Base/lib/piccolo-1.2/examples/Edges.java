import java.util.ArrayList;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;


public class Edges {
	
	private PPath edge;

	public Edges(PNode node1, PNode node2){
		edge = new PPath();
		edge.addAttribute("nodes", new ArrayList());
        ((ArrayList<PPath>)node1.getAttribute("edges")).add(edge);
        ((ArrayList<PPath>)node2.getAttribute("edges")).add(edge);
        ((ArrayList<PNode>)edge.getAttribute("nodes")).add(node1);
        ((ArrayList<PNode>)edge.getAttribute("nodes")).add(node2);
	}
	public Edges(PPath edge){
		this.edge = edge;

	}
	
	public PPath getEdge(){
		return edge;
	}
}
