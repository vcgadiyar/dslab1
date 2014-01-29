import java.io.Serializable;

public class Message implements Serializable{
	private String src;
	private String dest;
	private String kind;
	private Object data;
	private int seqNum;
	private boolean isDupe;
	
	public Message(String dest, String kind, Object data){
		this.dest = dest;
		this.kind = kind;	
		this.data = data;
	}
	
	public Message(Message msg){
		this.src = msg.getSrc();
		this.dest = msg.getDest();
		this.kind = msg.getKind();
		this.data = msg.getData();
		this.seqNum = msg.getSeqNum();
		this.isDupe = msg.isDupe();
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDest() {
		return dest;
	}

	public String getKind() {
		return kind;
	}

	public Object getData() {
		return data;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public boolean isDupe() {
		return isDupe;
	}

	public void setDupe(boolean dupe) {
		this.isDupe = dupe;
	}	
}
