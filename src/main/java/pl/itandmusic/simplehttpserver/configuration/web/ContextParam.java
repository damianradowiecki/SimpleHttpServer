package pl.itandmusic.simplehttpserver.configuration.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="context-param")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContextParam {
	
	@XmlElement(name = "param-name")
	private String name;
	@XmlElement(name = "param-value")
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
