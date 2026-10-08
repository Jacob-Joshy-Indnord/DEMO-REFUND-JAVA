package com.indnord.refund.refund;

/**
 * Thrown when a refund request fails validation and must be rejected.
 */
public class InvalidRefundRequestException extends RuntimeException {

    public InvalidRefundRequestException(String message) {
        super(message);
    }
}
