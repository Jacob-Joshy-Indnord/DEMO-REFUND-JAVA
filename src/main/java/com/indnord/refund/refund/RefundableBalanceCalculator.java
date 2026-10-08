package com.indnord.refund.refund;

import com.indnord.refund.domain.Capture;
import com.indnord.refund.domain.Refund;

import java.util.List;

/**
 * Computes how much of a capture is still available to refund.
 */
public final class RefundableBalanceCalculator {

    private RefundableBalanceCalculator() {
    }

    /**
     * Refundable balance = captured amount minus refunds already Executed, Approved
     * or Pending approval for that capture. Rejected and Failed refunds do not count.
     */
    public static long calculate(Capture capture, List<Refund> existingRefunds) {
        long alreadyRefundedMinor = existingRefunds.stream()
                .filter(refund -> refund.getStatus().countsAgainstRefundableBalance())
                .mapToLong(Refund::getAmountMinor)
                .sum();

        return capture.getAmountMinor() - alreadyRefundedMinor;
    }
}
