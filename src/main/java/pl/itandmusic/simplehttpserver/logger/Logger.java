package pl.itandmusic.simplehttpserver.logger;

import java.util.Calendar;

public class Logger {

	private Class<?> clazz;
	
	private Logger(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public static Logger getLogger(Class<?> clazz) {
		return new Logger(clazz);
	}
	
	public void info(String text) {
		log(text, LogLevel.INFO);
	}
	
	public void warn(String text) {
		log(text, LogLevel.WARN);
	}
	
	public void error(String text) {
		log(text, LogLevel.ERROR);
	}
	
	public void log(String text, LogLevel logLevel) {
		System.out.println(Calendar.getInstance().getTime() + "  " + logLevel.toString() + "  " + clazz.getName() + ": " + text);
	}
	
}
