package pl.itandmusic.simplehttpserver.model;

import java.util.List;

public class RequestContent {

	private List<String> plainContent;
	private String POSTData;

	public List<String> getPlainContent() {
		return plainContent;
	}

	public void setPlainContent(List<String> plainContent) {
		this.plainContent = plainContent;
	}

	public String getPOSTData() {
		return POSTData;
	}

	public void setPOSTData(String POSTData) {
		this.POSTData = POSTData;
	}
	
	public boolean empty() {
		return plainContent == null || plainContent.size() == 0; 
	}

}
