package pl.itandmusic.simplehttpserver.buffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;

public class ResponseOutputStreamBuffer extends ServletOutputStream {

	private List<Integer> buffer;
	private boolean commited;
	
	public ResponseOutputStreamBuffer() {
		buffer = new ArrayList<>();
	}

	@Override
	public void write(int b) throws IOException {
		buffer.add(b);
	}

	public boolean isCommited() {
		return commited;
	}

	public void setCommited(boolean commited) {
		this.commited = commited;
	}

	public List<Integer> getBuffer() {
		return buffer;
	}

}
