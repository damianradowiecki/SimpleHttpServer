package pl.itandmusic.simplehttpserver.session;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

public class SessionContainer {

	public static final Set<HttpSession> SESSIONS = new HashSet<>();
}
