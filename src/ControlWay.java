import java.util.ArrayList;
import java.util.Iterator;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;


public class ControlWay {
	private ArrayList<Summit> road;
	private String resumeString;
	private PText resumeText;
	
	public ControlWay(ArrayList<Summit> road){
		this.road= new ArrayList<Summit>(road);
		doResume();
		resumeText=new PText(resumeString);
		resumeText.centerBoundsOnPoint(10, 10);
	}
	
	public static boolean isCorrectWay(ArrayList<Summit> testRoad){
		return false;
	}
	
	protected void doResume(){
		Iterator<Summit> it = road.iterator();
		
		Summit s ;
		while (it.hasNext()) {
			s = it.next();
			resumeString=resumeString+" + "+s.getText();
		}
	}
	
	public PText getResume(){
		return resumeText;
	}

}
