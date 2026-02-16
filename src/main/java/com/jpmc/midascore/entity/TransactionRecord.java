package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private float amount;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    // Default constructor (required by JPA)
    protected TransactionRecord() {
    }

    // Constructor for creating new transaction records
    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public float getAmount() {
        return amount;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    // Setters
    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return String.format("TransactionRecord[id=%d, sender=%s, recipient=%s, amount=%.2f]",
                id,
                sender != null ? sender.getName() : "null",
                recipient != null ? recipient.getName() : "null",
                amount);
    }
}