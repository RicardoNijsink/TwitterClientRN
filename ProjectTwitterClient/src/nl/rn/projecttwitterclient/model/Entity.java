package nl.rn.projecttwitterclient.model;

public abstract class Entity {
	private int beginPosition, endPosition;
	
	public Entity(int begin, int end) {
		beginPosition = begin;
		endPosition = end;
	}

	public int getBeginPosition() {
		return beginPosition;
	}

	public void setBeginPosition(int beginPosition) {
		this.beginPosition = beginPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}
	
	

}
