package pl.itandmusic.simplehttpserver.jsp;

import java.io.File;
import java.util.List;

public interface ScripletSearcher {
		
	List<Scriplet> search(File file);
	
}	
