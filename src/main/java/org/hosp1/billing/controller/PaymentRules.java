package org.hosp1.billing.controller;

import org.hosp1.billing.model.Bill;
import org.hosp1.billing.model.PaymentStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Rules shared by the staff and patient controllers, so payment logic lives in one place. */
final class PaymentRules {

    private PaymentRules() { }

    /** Parses "123.4" to 123.40, or returns null if the text is not a number. */
    static BigDecimal parseMoney(String text) {
        try {
            return new BigDecimal(text.trim()).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    /** Applies a payment to the bill. Returns an error message, or null if it succeeded. */
    static String applyPayment(Bill bill, BigDecimal amount) {
        if (bill.getStatus() == PaymentStatus.CANCELLED) {
            return "This bill is cancelled.";
        }
        if (bill.getStatus() == PaymentStatus.PAID) {
            return "This bill is already fully paid.";
        }
        if (amount == null || amount.signum() <= 0) {
            return "Enter an amount greater than zero.";
        }
        if (amount.compareTo(bill.getBalance()) > 0) {
            return "Amount is more than the balance of " + bill.getBalance() + ".";
        }
        bill.setAmountPaid(bill.getAmountPaid().add(amount));
        bill.setStatus(bill.getBalance().signum() == 0 ? PaymentStatus.PAID : PaymentStatus.PARTIALLY_PAID);
        return null;
    }
}