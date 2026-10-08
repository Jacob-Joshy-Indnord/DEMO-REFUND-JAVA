package com.indnord.refund.domain;

/**
 * Lifecycle status of a refund raised against a capture.
 */
public enum RefundStatus {
    PENDING_APPROVAL,
    APPROVED,
    REJECTED,
    EXECUTED,
    FAILED;

    /**
     * Executed, Approved and Pending approval refunds still consume the capture's
     * refundable balance; Rejected and Failed refunds release it back.
     */
    public boolean countsAgainstRefundableBalance() {
        return this == PENDING_APPROVAL || this == APPROVED || this == EXECUTED;
    }
}
