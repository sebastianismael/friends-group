package ar.edu.unlam.tallerweb1.delivery.controllers;

import ar.edu.unlam.tallerweb1.delivery.Controller;

import javax.servlet.http.HttpServletRequest;

public class GoToAddFriend implements Controller {
    @Override
    public String invoke(HttpServletRequest request) {
        String user = request.getParameter("user");
        return
                "<div>" +
                    "<form id=\"add-friend\" action=\"add-friend\" method=\"POST\"><br>" +
                        "<input id=\"friend\" name=\"friend\" type=\"text\" value=\"\"/><br><br>" +
                        "<input id=\"user\" name=\"user\" type=\"hidden\" value=\""+user+"\"/>" +
                        "<button Type=\"Submit\"/>Agregar Amigo</button>\n" +
                    "</form>\n" +
                "</div>";
    }
}
