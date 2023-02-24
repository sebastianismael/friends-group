package ar.edu.unlam.tallerweb1.domain;

import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.User;

import java.util.List;

public interface FriendsGroupRepository {
    List<User> getMembers(FriendsGroup group);

    FriendsGroup getGroupOf(User user);
}
