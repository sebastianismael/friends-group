package ar.edu.unlam.tallerweb1.delivery.util;

import ar.edu.unlam.tallerweb1.delivery.Controller;
import ar.edu.unlam.tallerweb1.infrastructure.utils.MySqlDataSource;

import javax.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.SQLException;


public class CleanData implements Controller {
    @Override
    public String invoke(HttpServletRequest request) {
        try {
            Connection connection = MySqlDataSource.instance().getConnection();
            connection.createStatement().execute("delete from payment");
            connection.createStatement().execute("delete from shared_expenses");
            connection.createStatement().execute("delete from user");
            connection.createStatement().execute("delete from friends_group");
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Data deleted!";
    }
}
