package ar.edu.unlam.tallerweb1.domain.model;

public class Payment {

    private Long id;
    private User payer;
    private Double amount;
    private SharedExpent sharedExpent;

    public Payment(Long id, User payer, Double amount, SharedExpent sharedExpent) {
        this.id = id;
        this.payer = payer;
        this.amount = amount;
        this.sharedExpent = sharedExpent;
    }

    public Long getId() {
        return id;
    }

    public User getPayer() {
        return payer;
    }

    public Double getAmount() {
        return amount;
    }

    public SharedExpent getSharedExpent() {
        return sharedExpent;
    }
}
