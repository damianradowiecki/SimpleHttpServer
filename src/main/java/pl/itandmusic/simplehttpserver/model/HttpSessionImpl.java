package pl.itandmusic.simplehttpserver.model;

import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class HttpSessionImpl implements HttpSession {

	private Map<String, Object> attributes = new HashMap<>();
	private GregorianCalendar creationTime;
	private String id;
	private SessionStatus status;
	private ServletContext servletContext;
	
	public HttpSessionImpl(String id, ServletContext servletContext) {
		this.id = Objects.requireNonNull(id);
		this.servletContext = Objects.requireNonNull(servletContext);
		creationTime = new GregorianCalendar();
		status = SessionStatus.NEW;
		
	}
	
	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return new EnumerationImpl<String>(attributes.keySet());
	}

	@Override
	public long getCreationTime() {
		return creationTime.getTimeInMillis();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String name) {
		return getAttribute(name);
	}

	@Override
	public String[] getValueNames() {
		return attributes.keySet().toArray(new String[0]);
	}

	@Override
	public void invalidate() {
		status = SessionStatus.INVALIDATED;
	}

	@Override
	public boolean isNew() {
		boolean result = status.equals(SessionStatus.NEW);
		status = SessionStatus.WORKING;
		return result;
	}

	@Override
	public void putValue(String name, Object value) {
		attributes.put(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		Object object = attributes.remove(name);
		servletContext.getListenerManager().invokeValueUnbound(this, attributes.get(name).getClass());
		servletContext.getListenerManager().invokeAttributeRemoved(this, object);
	}

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		boolean exists = attributes.containsKey(name);
		attributes.put(name, value);
		if(exists) {
			servletContext.getListenerManager().invokeAttributeReplaced(this, value);
		}else {
			servletContext.getListenerManager().invokeAttributeAdded(this, value);
		}
		servletContext.getListenerManager().invokeValueBound(this, attributes.get(name).getClass());
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof HttpSession) {
			HttpSession session = (HttpSession) object;
			return session.getId().equals(this.id);
		}
		else {
			return false;
		}
	}
}
