package ar.edu.unlam.tallerweb1.domain.model;

public class FriendsGroup {

    private Long id;
    private String name;
    public FriendsGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
