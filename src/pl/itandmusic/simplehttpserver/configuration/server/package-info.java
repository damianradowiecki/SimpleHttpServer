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
	@XmlElement(name = "app-directory")
	private String appDirectory;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAppDirectory() {
		return appDirectory;
	}

	public void setAppDirectory(String appDirectory) {
		this.appDirectory = appDirectory;
	}

}
