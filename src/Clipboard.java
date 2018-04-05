
public class Clipboard {

	private int n;
	private List < Character > content;
	
	public Clipboard(int i){
		n = i;
		content = null;
	}
	
	public Clipboard(int i, List <Character> lc){
		n = i;
		content = lc;
	}
	
	public int getNumber(int index){
		return n;
	}
	
	public List< Character > getValue(){
		return content;
	}
	
	
}
