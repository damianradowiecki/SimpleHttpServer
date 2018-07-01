@javax.xml.bind.annotation.XmlSchema(elementFormDefault = javax.xml.bind.annotation.XmlNsForm.UNQUALIFIED)
package pl.itandmusic.simplehttpserver.configuration.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "server-config")
@XmlAccessorType(XmlAccessType.FIELD)
class ServerConfig {

	@XmlElement(name = "port")
	private int port;
	@XmlElement(name = "apps-directory")
	private String appsDirectory;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAppsDirectory() {
		return appsDirectory;
	}

	public void setAppsDirectory(String appsDirectory) {
		this.appsDirectory = appsDirectory;
	}

}
