package ar.edu.unlam.tallerweb1.delivery.controllers;

import ar.edu.unlam.tallerweb1.delivery.Controller;

import javax.servlet.http.HttpServletRequest;

public class Alive implements Controller {
    @Override
    public String invoke(HttpServletRequest request) {
        return "=)";
    }
}
