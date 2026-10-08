package com.indnord.refund.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A captured card payment that refunds may be raised against.
 *
 * <p>{@code amountMinor} is the captured amount in integer minor currency units
 * (e.g. cents) so that refund arithmetic never involves floating point.
 */
@Entity
@Table(name = "captures")
public class Capture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long amountMinor;

    protected Capture() {
        // JPA
    }

    public Capture(Long id, long amountMinor) {
        this.id = id;
        this.amountMinor = amountMinor;
    }

    public Long getId() {
        return id;
    }

    public long getAmountMinor() {
        return amountMinor;
    }
}
