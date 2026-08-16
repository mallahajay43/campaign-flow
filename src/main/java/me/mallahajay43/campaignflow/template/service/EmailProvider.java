package me.mallahajay43.campaignflow.template.service;

public interface EmailProvider {
    void send(String email, String subject, String html, String from);
}
