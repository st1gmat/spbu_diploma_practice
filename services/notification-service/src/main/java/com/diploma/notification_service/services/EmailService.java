package com.diploma.notification_service.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.diploma.notification_service.models.Product;

import java.math.BigDecimal;
import java.util.List;
// import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmailService {

    public void sendPaymentSuccessEmail(String recipient, String customerName, BigDecimal amount, String orderReference) {
        // CompletableFuture.runAsync(() -> {
            try {
                log.info("Simulating email sending... to {} (Payment Confirmation)", recipient);
                TimeUnit.MILLISECONDS.sleep(300); // Имитация задержки в 700 мс
                log.info("Pseudo-email sent to: {} | Subject: Payment Confirmation | Body: Dear {}, your payment of ${} for order {} was successful!",
                        recipient, customerName, amount, orderReference);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Email sending was interrupted", e);
            }
        // });
    }

    public void sendOrderConfirmationEmail(String recipient, String customerName, BigDecimal totalAmount, String orderReference, List<Product> products) {
        // CompletableFuture.runAsync(() -> {
            try {
                log.info("Simulating email sending... to {} (Order Confirmation)", recipient);
                TimeUnit.MILLISECONDS.sleep(300); // Имитация задержки в 700 мс
                log.info("Pseudo-email sent to: {} | Subject: Order Confirmation | Body: Dear {}, your order {} with total amount ${} has been placed successfully! Products: {}",
                        recipient, customerName, orderReference, totalAmount, products);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Email sending was interrupted", e);
            }
        // });
    }
}
