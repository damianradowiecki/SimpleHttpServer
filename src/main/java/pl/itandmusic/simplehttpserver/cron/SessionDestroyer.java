package pl.itandmusic.simplehttpserver.cron;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.ServletContext;
import pl.itandmusic.simplehttpserver.session.SessionManager;


public class SessionDestroyer {

	public static final int SESSION_TIMEOUT_CHECKING_IN_MINUTES_PERIOD = 1;
	
	private SessionDestroyer() {}
	
	public static void start() {
		for(ServletContext sc : Configuration.applications.values()) {
			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			executorService.scheduleAtFixedRate(new SessionDestroyerAction(sc.getSessionManager(), 
					sc.getSessionManager().getSessionTimeout()), 
					SESSION_TIMEOUT_CHECKING_IN_MINUTES_PERIOD, 
					1, 
					TimeUnit.MINUTES);
		}
	}
}

class SessionDestroyerAction implements Runnable{

	private final Logger logger = Logger.getLogger(SessionDestroyerAction.class);
	private SessionManager sessionManager;
	private int maxSessionInactiveInMinutesPeriod;
	
	public SessionDestroyerAction(SessionManager sessionManager, int maxSessionInactiveInMinutesPeriod) {
		this.sessionManager = sessionManager;
		this.maxSessionInactiveInMinutesPeriod = maxSessionInactiveInMinutesPeriod;
	}
	
	@Override
	public void run() {
		for(HttpSession session : sessionManager.sessions) {
			if(maxSessionInactiveInMinutesPeriod != 0 && System.currentTimeMillis() - session.getLastAccessedTime() > maxSessionInactiveInMinutesPeriod * 60 * 1000) {
				String sessionId = session.getId();
				sessionManager.destorySession(session);
				logger.info("Destroyed session with id " + sessionId);
			}
		}
	}
	
}
