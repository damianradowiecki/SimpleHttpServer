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
	@XmlElement(name = "log-level")
	private String logLevel;

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

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	
	

}
