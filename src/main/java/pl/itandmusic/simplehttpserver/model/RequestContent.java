package pl.itandmusic.simplehttpserver.model;

import java.util.List;

public class RequestContent {

	private List<String> plainContent;
	private String postData;

	public List<String> getPlainContent() {
		return plainContent;
	}

	public void setPlainContent(List<String> plainContent) {
		this.plainContent = plainContent;
	}

	public String getPostData() {
		return postData;
	}

	public void setPostData(String postData) {
		this.postData = postData;
	}
	
	public boolean empty() {
		return plainContent == null || plainContent.size() == 0; 
	}

}
