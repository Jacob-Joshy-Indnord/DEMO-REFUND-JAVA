package com.indnord.refund.refund;

import com.indnord.refund.domain.Capture;
import com.indnord.refund.domain.Refund;
import com.indnord.refund.domain.RefundStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RefundAmountValidatorTest {

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -500L})
    void rejectsZeroOrNegativeAmount(long requestedAmountMinor) {
        Capture capture = new Capture(1L, 10_000L);

        assertThatThrownBy(() ->
                RefundAmountValidator.validate(capture, List.of(), requestedAmountMinor))
                .isInstanceOf(InvalidRefundRequestException.class)
                .hasMessageContaining("greater than zero");
    }

    @Test
    void rejectsAmountExceedingRefundableBalance() {
        // 100.00 capture with a 30.00 refund already executed leaves a 70.00 balance.
        Capture capture = new Capture(1L, 10_000L);
        Refund alreadyExecuted = new Refund(1L, capture, 3_000L, RefundStatus.EXECUTED);

        assertThatThrownBy(() ->
                RefundAmountValidator.validate(capture, List.of(alreadyExecuted), 8_000L))
                .isInstanceOf(InvalidRefundRequestException.class)
                .hasMessageContaining("exceeds the refundable balance");
    }

    @Test
    void acceptsAmountExactlyAtRefundableBalance() {
        Capture capture = new Capture(1L, 10_000L);
        Refund alreadyExecuted = new Refund(1L, capture, 3_000L, RefundStatus.EXECUTED);

        assertThatCode(() ->
                RefundAmountValidator.validate(capture, List.of(alreadyExecuted), 7_000L))
                .doesNotThrowAnyException();
    }

    @Test
    void acceptsAmountBelowRefundableBalance() {
        Capture capture = new Capture(1L, 10_000L);

        assertThatCode(() ->
                RefundAmountValidator.validate(capture, List.of(), 2_500L))
                .doesNotThrowAnyException();
    }

    @Test
    void rejectedAndFailedRefundsDoNotReduceRefundableBalance() {
        Capture capture = new Capture(1L, 10_000L);
        Refund rejected = new Refund(1L, capture, 5_000L, RefundStatus.REJECTED);
        Refund failed = new Refund(2L, capture, 5_000L, RefundStatus.FAILED);

        assertThatCode(() ->
                RefundAmountValidator.validate(capture, List.of(rejected, failed), 10_000L))
                .doesNotThrowAnyException();
    }

    @Test
    void pendingAndApprovedRefundsReduceRefundableBalance() {
        Capture capture = new Capture(1L, 10_000L);
        Refund pending = new Refund(1L, capture, 4_000L, RefundStatus.PENDING_APPROVAL);
        Refund approved = new Refund(2L, capture, 3_000L, RefundStatus.APPROVED);

        assertThatThrownBy(() ->
                RefundAmountValidator.validate(capture, List.of(pending, approved), 4_000L))
                .isInstanceOf(InvalidRefundRequestException.class)
                .hasMessageContaining("exceeds the refundable balance");
    }
}
