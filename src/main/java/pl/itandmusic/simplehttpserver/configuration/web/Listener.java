package pl.itandmusic.simplehttpserver.configuration.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "listener")
@XmlAccessorType(XmlAccessType.FIELD)
public class Listener {

	@XmlElement(name = "listener-class")
	private String listenerClass;

	public String getListenerClass() {
		return listenerClass;
	}

	public void setListenerClass(String listenerClass) {
		this.listenerClass = listenerClass;
	}

}
