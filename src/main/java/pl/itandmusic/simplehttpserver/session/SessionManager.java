package pl.itandmusic.simplehttpserver.session;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import pl.itandmusic.simplehttpserver.model.HttpSessionImpl;
import pl.itandmusic.simplehttpserver.utils.RandomString;

public class SessionManager {

	private static SessionManager sessionManager;
	
	private SessionManager() {};
	
	public static SessionManager getSessionManager() {
		if(sessionManager == null) {
			sessionManager = new SessionManager();
		}
		return sessionManager;
	}
	
	public HttpSession createNewSession() {
		String newSessionId = null;
		while(!getInUseSessionIds().contains((newSessionId = RandomString.generate())));
		return new HttpSessionImpl(newSessionId);
	}
	
	public Optional<HttpSession> getExistingSession(String sessionId) { 
			return SessionContainer
			.SESSIONS
			.stream()
			.filter(s -> s.getId().equalsIgnoreCase(sessionId))
			.findAny();
	}
	
	public HttpSession createIfNotExists(String sessionId) {
		if(sessionId == null) {
			return createNewSession();
		}
		else{
			Optional<HttpSession> optinalHttpSession = getExistingSession(sessionId);
			if(optinalHttpSession.isPresent()) {
				return optinalHttpSession.get();
			}
			else {
				return createNewSession();
			}
		}
	}
	
	private synchronized List<String> getInUseSessionIds(){
		return SessionContainer
		.SESSIONS
		.stream()
		.map(s -> s.getId())
		.collect(Collectors.toList());
	}
}
