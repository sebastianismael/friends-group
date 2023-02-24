package ar.edu.unlam.tallerweb1.domain.model;

import java.time.LocalDateTime;

public class SharedExpent {

    private Long id;
    private User owner;
    private Double amount;
    private String detail;
    private LocalDateTime date;
    private FriendsGroup friendsGroup;

    public SharedExpent(Long id, User owner, Double amount, String detail, LocalDateTime date, FriendsGroup friendsGroup) {
        this(owner, amount, detail, date, friendsGroup);
        this.id = id;
        this.friendsGroup = friendsGroup;
    }

    public SharedExpent(User owner, Double amount, String detail, LocalDateTime date, FriendsGroup friendsGroup) {
        this.owner = owner;
        this.amount = amount;
        this.detail = detail;
        this.date = date;
        this.friendsGroup = friendsGroup;
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDetail() {
        return detail;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public FriendsGroup getFriendsGroup() {
        return friendsGroup;
    }

    public String getPayer() {
        return owner.getName();
    }
}
