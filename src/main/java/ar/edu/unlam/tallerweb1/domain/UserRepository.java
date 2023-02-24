package ar.edu.unlam.tallerweb1.domain;

import ar.edu.unlam.tallerweb1.domain.model.User;

public interface UserRepository {
    void save(User user);
    void saveWithFriendGroup(User user);
    User findByName(String name);
}
