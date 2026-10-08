package com.indnord.refund.refund;

import com.indnord.refund.domain.Capture;
import com.indnord.refund.domain.Refund;

import java.util.List;

/**
 * Validates a requested refund amount against the capture's refundable balance.
 *
 * <p>Rejects amounts that are zero or negative, or that exceed the refundable
 * balance (captured amount minus refunds already executed or pending). Amounts
 * are compared as integer minor currency units so no floating point arithmetic
 * is involved.
 */
public final class RefundAmountValidator {

    private RefundAmountValidator() {
    }

    /**
     * @throws InvalidRefundRequestException if the requested amount is non-positive
     *         or exceeds the capture's refundable balance
     */
    public static void validate(Capture capture, List<Refund> existingRefunds, long requestedAmountMinor) {
        if (requestedAmountMinor <= 0) {
            throw new InvalidRefundRequestException(
                    "Refund amount must be greater than zero.");
        }

        long refundableBalanceMinor = RefundableBalanceCalculator.calculate(capture, existingRefunds);

        if (requestedAmountMinor > refundableBalanceMinor) {
            throw new InvalidRefundRequestException(
                    "Refund amount of " + requestedAmountMinor
                            + " exceeds the refundable balance of " + refundableBalanceMinor
                            + " for capture " + capture.getId() + ".");
        }
    }
}
