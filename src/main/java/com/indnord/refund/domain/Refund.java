package com.indnord.refund.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * A refund raised against a {@link Capture}.
 *
 * <p>{@code amountMinor} is the refund amount in integer minor currency units,
 * matching {@link Capture#getAmountMinor()}.
 */
@Entity
@Table(name = "refunds")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "capture_id", nullable = false)
    private Capture capture;

    private long amountMinor;

    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    protected Refund() {
        // JPA
    }

    public Refund(Long id, Capture capture, long amountMinor, RefundStatus status) {
        this.id = id;
        this.capture = capture;
        this.amountMinor = amountMinor;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Capture getCapture() {
        return capture;
    }

    public long getAmountMinor() {
        return amountMinor;
    }

    public RefundStatus getStatus() {
        return status;
    }
}
