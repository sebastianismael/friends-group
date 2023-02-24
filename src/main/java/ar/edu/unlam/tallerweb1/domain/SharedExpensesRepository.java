package ar.edu.unlam.tallerweb1.domain;

import ar.edu.unlam.tallerweb1.domain.model.SharedExpent;

import java.util.List;

public interface SharedExpensesRepository {

    List<SharedExpent> findBy(String user);

    void save(SharedExpent SharedExpent);
}
