package pl.itandmusic.simplehttpserver.configuration.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "session-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class SessionConfig {

	@XmlElement(name = "session-timeout")
	private int sessionTimeout;

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

}
