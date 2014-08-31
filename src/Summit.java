//package Generator.src;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Summit implements Serializable{
	private static final int defaulXY = 10;
	private static double size=40;
	private PPath node;
	private PText text;
	private static int count = 0;

	public Summit() {
		text = new PText("" + (char) (65 + (int) (count % 26)) + (char) ((int) (48 + count++ / 26)));
		text.setPickable(false);
		text.centerBoundsOnPoint(size/2+defaulXY, size/2+defaulXY);
		node = PPath.createEllipse((float)defaulXY, (float)defaulXY, (float)size, (float)size);
		node.addAttribute("edges", new ArrayList());
		node.addChild(text);
	}	
	public PPath getNode(){return node;}
	public static double getSize(){return size;}
	
	public void reset(double x, double y, int width, int height) {
		this.node.reset();
		this.node.setBounds(x, y, width, height);
	}
	
	public void changeBounds(int size){
		this.size=size;
		double X = node.getX();
		double Y = node.getY();
		node.setBounds(X, Y, size, size);		
		text.centerBoundsOnPoint(X + size/2 , Y + size/2);


	}

	public void delete(PLayer nodeLayer, PLayer edgeLayer,
			HashMap<PPath, Edges> listOfEdge) {
		ArrayList edges = (ArrayList) node.getAttribute("edges"); // List of nodes that we want to  delete

		for (int i = edges.size() - 1; i >= 0; i--) {

			PPath edge = (PPath) edges.get(i); // One edge that we want delete

			ArrayList nodesList = (ArrayList) edge.getAttribute("nodes"); // List the two current node parents

			PNode nodeElem = (PNode) nodesList.get(0);
			ArrayList nodeElemListEdges = (ArrayList) nodeElem
					.getAttribute("edges");
			nodeElemListEdges.remove(edge);

			PNode nodeElem2 = (PNode) nodesList.get(1);
			ArrayList nodeElemListEdges2 = (ArrayList) nodeElem2
					.getAttribute("edges");
			nodeElemListEdges2.remove(edge);

			((Edges) listOfEdge.get(edge)).deleteEdge(edgeLayer, nodeLayer);
			listOfEdge.remove(edge);

			System.out.println(edges.size());
		}
	}
	public void changeColor(boolean changeColor) {
		if (changeColor) {
			node.setPaint(Color.ORANGE);
		} else {
			node.setPaint(Color.WHITE);
		}
	}
	
	//This function search if an edge is betwin two node and he return null if he don't exist and the node otherwise
	public PPath haveLinkWhith(PPath node2){
		ArrayList listEdge = ((ArrayList<PPath>) node.getAttribute("edges"));
		Iterator<PPath> it = listEdge.iterator();
		
		while(it.hasNext()){
			PPath s = it.next();
			ArrayList listNode = ((ArrayList<PPath>) s.getAttribute("nodes"));
			Iterator<PPath> it2 = listNode.iterator();
			if(it2.next()==node && it2.next()==node2)
				return s;
		}
		return null;
	}
}