package iuh.fit.se.ace_store.service;

public interface EmailService {
    void sendHtmlEmail(String to, String subject, String htmlBody);
}
