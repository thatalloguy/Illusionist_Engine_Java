package Editor;

import java.util.ArrayList;
import java.util.List;

public class Console {

	private List<String> log = new ArrayList<String>();
	
	public Console() {
		
	}
	
	public void addWarning(String string) {
		log.add("[WARNING: ]" + string);
	}
	
	public void addMessage(String string) {
		log.add("[MESSAGE: ]" + string);
	}
	
	public void addError(String string) {
		log.add("[ERROR: ]" + string);
	}
	
	
	public List<String> getLog() {
		return this.log;
	}
}
