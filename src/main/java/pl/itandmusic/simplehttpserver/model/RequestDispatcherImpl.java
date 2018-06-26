package pl.itandmusic.simplehttpserver.model;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RequestDispatcherImpl implements RequestDispatcher {

	@Override
	public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		//przekazanie kontroli do nastepnego servletu/jsp/...
		//i tam jest zakończenie
	}

	@Override
	public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		//przekazanie kontroli do nastepnego servletu/jsp/...
		//po wykonaniu powyższego następuje powrót do tego miejsca
	}

}
