package pl.itandmusic.simplehttpserver.cron;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.session.SessionContainer;
import pl.itandmusic.simplehttpserver.session.SessionManager;

public class SessionDestroyer {

	public static final int MAX_SESSION_INACTIVE_PERIOD_MINUTES = 1;
	
	public static void start() {
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(new SessionDestroyerAction(), MAX_SESSION_INACTIVE_PERIOD_MINUTES, 1, TimeUnit.MINUTES);
	}
}

class SessionDestroyerAction implements Runnable{

	private final Logger logger = Logger.getLogger(SessionDestroyerAction.class);
	
	@Override
	public void run() {
		for(HttpSession session : SessionContainer.SESSIONS) {
			if(System.currentTimeMillis() - session.getLastAccessedTime() > SessionDestroyer.MAX_SESSION_INACTIVE_PERIOD_MINUTES * 60 * 1000) {
				SessionManager sessionManager = SessionManager.getSessionManager();
				String sessionId = session.getId();
				sessionManager.destorySession(session);
				logger.info("Destroyed session with id " + sessionId);
			}
		}
	}
	
}
