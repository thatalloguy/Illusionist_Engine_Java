package Editor;

public class StateManager {

	
	private String MainState;
	private String ChildState;
	private String TagState;
	
	private Boolean isIdle;
	
	public StateManager() {}

	public String getMainState() {
		return MainState;
	}

	public void setMainState(String mainState) {
		MainState = mainState;
	}

	public String getChildState() {
		return ChildState;
	}

	public void setChildState(String childState) {
		ChildState = childState;
	}

	public String getTagState() {
		return TagState;
	}

	public void setTagState(String tagState) {
		TagState = tagState;
	}

	public Boolean getIsIdle() {
		return isIdle;
	}

	public void setIsIdle(Boolean isIdle) {
		this.isIdle = isIdle;
	}
	
	
	
}
