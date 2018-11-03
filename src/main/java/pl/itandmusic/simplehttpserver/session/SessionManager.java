package pl.itandmusic.simplehttpserver.session;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import pl.itandmusic.simplehttpserver.model.HttpSessionImpl;
import pl.itandmusic.simplehttpserver.service.CookieService;
import pl.itandmusic.simplehttpserver.utils.RandomString;

public class SessionManager {

	public final Set<HttpSession> sessions = new HashSet<>();
	private int sessionTimeout;
	private static final SessionManager sessionManager = new SessionManager();
	private static boolean throwExceptionOnConstructorCall = false;
	
	private SessionManager() {
		if(throwExceptionOnConstructorCall) {
			throw new RuntimeException("Singleton constructor call");
		}
		throwExceptionOnConstructorCall = true;
	}
	
	public static SessionManager getInstance() {
		return sessionManager;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public boolean sessionExists(String sessionId) {
		return getSessionById(sessionId).isPresent();
	}

	public boolean sessionExists(Cookie[] cookies) {
		return CookieService.getSessionIdCookie(cookies).isPresent();
	}

	public HttpSession createNewSession() {
		String newSessionId = generateSessionId();
		return new HttpSessionImpl(newSessionId);
	}

	public Optional<HttpSession> getSessionById(String sessionId) {
		return sessions.stream().filter(s -> s.getId().equalsIgnoreCase(sessionId)).findAny();
	}

	public HttpSession createIfNotExists(String sessionId) {
		if (sessionId == null) {
			return createNewSession();
		} else {
			Optional<HttpSession> optinalHttpSession = getSessionById(sessionId);
			if (optinalHttpSession.isPresent()) {
				return optinalHttpSession.get();
			} else {
				return createNewSession();
			}
		}
	}

	public void destorySession(HttpSession httpSession) {
		sessions.remove(httpSession);
	}

	private String generateSessionId() {
		String sessionId = null;
		while (!getInUseSessionIds().contains((sessionId = RandomString.generate())))
			;
		return sessionId;
	}

	private synchronized List<String> getInUseSessionIds() {
		return sessions.stream().map(s -> s.getId()).collect(Collectors.toList());
	}
}
