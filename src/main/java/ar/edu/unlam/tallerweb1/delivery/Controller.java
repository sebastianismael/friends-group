package ar.edu.unlam.tallerweb1.delivery;

import javax.servlet.http.HttpServletRequest;

public interface Controller {
    Object invoke(HttpServletRequest request);
}
