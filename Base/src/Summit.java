import java.util.ArrayList;
import edu.umd.cs.piccolo.nodes.PPath;


public class Summit {

	private float x=1;
	private float y=1;
	private PPath node;

	public Summit(){
		 node = PPath.createEllipse(x, y, 30, 30);
         node.addAttribute("edges", new ArrayList());
	}
	
	public PPath getNode(){
		return node;
	}
	
	public void setXY(float x,float y){
		this.x=x;
		this.y=y;
	}
}
