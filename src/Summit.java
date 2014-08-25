//package Generator.src;

import java.util.ArrayList;
import java.util.HashMap;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Summit {
	private double x = 10;
	private double y = 10;
	private double width = 40;
	private double height = 40;
	private PPath node;
	private PText text;
	private static int count = 0;

	public Summit() {
		text = new PText("" + (char) (65 + (int) (count % 26))
				+ (char) ((int) (48 + count++ / 26)));
		text.setPickable(false);
		text.centerBoundsOnPoint(30, 30);
		node = PPath.createEllipse((float)x, (float)y, (float)width, (float)height);
		node.addAttribute("edges", new ArrayList());
		node.addChild(text);
	}

	public void setSliderValue(double value){
		this.width = this.height = value;
	}
	
	public PPath getNode() {
		return node;
	}
	
	public void reset(double x, double y, int width, int height) {
		this.node.reset();
		this.node.setBounds(x, y, width, height);
	}

	public void delete(PLayer nodeLayer, PLayer edgeLayer,
			HashMap<PPath, Edges> listOfEdge) {
		ArrayList edges = (ArrayList) node.getAttribute("edges"); // List of
																	// nodes
																	// that we
																	// want to
																	// delete

		for (int i = edges.size() - 1; i >= 0; i--) {

			PPath edge = (PPath) edges.get(i); // One edge that we want delete

			ArrayList nodesList = (ArrayList) edge.getAttribute("nodes"); // List
																			// of
																			// the
																			// two
																			// current
																			// node
																			// parents

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
}