package pl.itandmusic.simplehttpserver.logger;

import java.util.Calendar;

public class Logger {

	public static void log(String text, LogLevel logLevel) {
		System.out.println(Calendar.getInstance().getTime() + "   " + logLevel.toString() + " : " + text);
	}
}
