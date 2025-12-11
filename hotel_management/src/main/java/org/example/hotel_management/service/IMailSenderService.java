package org.example.hotel_management.service;

import java.util.concurrent.CompletableFuture;

public interface IMailSenderService {

    CompletableFuture<Boolean> sendMail(String to, String subject);

    String loadHtmlTemplate(String otpCode);
}
