package pl.itandmusic.simplehttpserver.logger;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

import pl.itandmusic.simplehttpserver.configuration.Configuration;

public class Logger {

	private Class<?> clazz;
	private String pattern = "H:m:s";
	private SimpleDateFormat logDateFormat = new SimpleDateFormat(pattern);

	private Logger(Class<?> clazz) {
		this.clazz = Objects.requireNonNull(clazz);
	}

	public static Logger getLogger(Class<?> clazz) {
		return new Logger(clazz);
	}
	
	public void debug(String text) {
		log(text, LogLevel.DEBUG);
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
		if(isLevelEnabled(logLevel)) {
			String date_ = prepareDateString();
			String logLevel_ = logLevel.toString();
			String className = clazz.getName();
			System.out.println(date_ + "  " + logLevel_ + "  " + className + ": " + text);
		}
	}
	
	public void logException(Throwable exception, LogLevel logLevel) {
		log("Error message: " + exception.getMessage(), logLevel);
	}
	
	public void logException(String context, Exception exception, LogLevel logLevel) {
		log("Error thrown in context " + context + ". Error message: " + exception.getMessage(), logLevel);
	}

	private String prepareDateString() {
		return logDateFormat.format(Calendar.getInstance().getTime());
	}

	private boolean isLevelEnabled(LogLevel logLevel) {
		return Arrays.asList(Configuration.logLevels).contains(logLevel.toString());
	}
}
