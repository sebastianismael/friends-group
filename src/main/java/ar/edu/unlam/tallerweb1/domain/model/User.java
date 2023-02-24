package ar.edu.unlam.tallerweb1.domain.model;

public class User {

    private Long id;
    private String name;
    private FriendsGroup friendsGroup;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FriendsGroup getFriendsGroup() {
        return friendsGroup;
    }

    public void setFriendsGroup(FriendsGroup friendsGroup) {
        this.friendsGroup = friendsGroup;
    }

    public boolean hasFriendGroup() {
        return this.friendsGroup != null;
    }
}
