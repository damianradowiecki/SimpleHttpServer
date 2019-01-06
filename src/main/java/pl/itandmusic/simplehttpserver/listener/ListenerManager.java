package pl.itandmusic.simplehttpserver.listener;

import java.util.EventListener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.ServletContext;

public class ListenerManager {

	private static final Logger LOGGER = Logger.getLogger(ListenerManager.class);
	private ServletContext servletContext;

	public ListenerManager(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void invokeContextInitialized() {
		try {
			for (Class<? extends EventListener> l : servletContext.getListeners()) {
				for (Class<?> i : l.getInterfaces()) {
					if (i == ServletContextListener.class) {
						ServletContextListener servletContextListener = ServletContextListener.class
								.cast(l.newInstance());
						ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);
						servletContextListener.contextInitialized(servletContextEvent);
					}
				}
			}

		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.logException(e, LogLevel.WARN);
		}

	}

	public void invokeSessionDestroyed(HttpSession httpSession) {
		try {
			for (Class<? extends EventListener> l : servletContext.getListeners()) {
				for (Class<?> i : l.getInterfaces()) {
					if (i == HttpSessionListener.class) {
						HttpSessionListener httpSessionListener = HttpSessionListener.class.cast(l.newInstance());
						HttpSessionEvent httpSessionEvent = new HttpSessionEvent(httpSession);
						httpSessionListener.sessionDestroyed(httpSessionEvent);
					}
				}
			}

		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.logException(e, LogLevel.WARN);
		}

	}

	public void invokeSessionCreated(HttpSession httpSession) {
		try {
			for (Class<? extends EventListener> l : servletContext.getListeners()) {
				for (Class<?> i : l.getInterfaces()) {
					if (i == HttpSessionListener.class) {
						HttpSessionListener httpSessionListener = HttpSessionListener.class.cast(l.newInstance());
						HttpSessionEvent httpSessionEvent = new HttpSessionEvent(httpSession);
						httpSessionListener.sessionCreated(httpSessionEvent);
					}
				}
			}

		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.logException(e, LogLevel.WARN);
		}
	}

	public void invokeValueBound(HttpSession httpSession, Object object) {
		if(object instanceof HttpSessionBindingListener) {
			try {
				HttpSessionBindingEvent event = 
						new HttpSessionBindingEvent(httpSession, httpSession.getId(), object);
				((HttpSessionBindingListener)object).valueBound(event);
			}catch(Throwable exception) {
				LOGGER.logException(exception, LogLevel.WARN);
			}
		}
	}

	public void invokeValueUnbound(HttpSession httpSession, Object object) {
		if(object instanceof HttpSessionBindingListener) {
			try {
				HttpSessionBindingEvent event = 
						new HttpSessionBindingEvent(httpSession, httpSession.getId(), object);
				((HttpSessionBindingListener)object).valueUnbound(event);
			}catch(Throwable exception) {
				LOGGER.logException(exception, LogLevel.WARN);
			}
		}
	}
	
	public void invokeAttributeAdded(HttpSession httpSession, Object object) {
		try {
			for (Class<? extends EventListener> l : servletContext.getListeners()) {
				for (Class<?> i : l.getInterfaces()) {
					if (i == HttpSessionAttributeListener.class) {
						HttpSessionAttributeListener httpSessionAttributeListener = 
								HttpSessionAttributeListener.class.cast(l.newInstance());
						HttpSessionBindingEvent event = 
								new HttpSessionBindingEvent(httpSession, httpSession.getId(), object);
						httpSessionAttributeListener.attributeAdded(event);
					}
				}
			}

		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.logException(e, LogLevel.WARN);
		}
	}
	
	public void invokeAttributeRemoved(HttpSession httpSession, Object object) {
		try {
			for (Class<? extends EventListener> l : servletContext.getListeners()) {
				for (Class<?> i : l.getInterfaces()) {
					if (i == HttpSessionAttributeListener.class) {
						HttpSessionAttributeListener httpSessionAttributeListener = 
								HttpSessionAttributeListener.class.cast(l.newInstance());
						HttpSessionBindingEvent event = 
								new HttpSessionBindingEvent(httpSession, httpSession.getId(), object);
						httpSessionAttributeListener.attributeRemoved(event);
					}
				}
			}

		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.logException(e, LogLevel.WARN);
		}
	}
	
	public void invokeAttributeReplaced(HttpSession httpSession, Object object) {
		try {
			for (Class<? extends EventListener> l : servletContext.getListeners()) {
				for (Class<?> i : l.getInterfaces()) {
					if (i == HttpSessionAttributeListener.class) {
						HttpSessionAttributeListener httpSessionAttributeListener = 
								HttpSessionAttributeListener.class.cast(l.newInstance());
						HttpSessionBindingEvent event = 
								new HttpSessionBindingEvent(httpSession, httpSession.getId(), object);
						httpSessionAttributeListener.attributeReplaced(event);
					}
				}
			}

		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.logException(e, LogLevel.WARN);
		}
	}
}
