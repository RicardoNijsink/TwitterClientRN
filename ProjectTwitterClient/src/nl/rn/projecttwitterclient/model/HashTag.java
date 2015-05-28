package nl.rn.projecttwitterclient.model;

public class HashTag extends Entity {
	private int beginPosition, endPosition;
	
	public HashTag(int begin, int end) {
		this.beginPosition = begin;
		this.endPosition = end;
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
