package me.mallahajay43.campaignflow.template.rederer;

import me.mallahajay43.campaignflow.audience.api.ContactProjection;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmailRenderer {

    public String render(String html, ContactProjection contact) {

        return html
                .replace(
                        "{{fullName}}",
                        Optional.ofNullable(contact.fullName()).orElse("")
                )
                .replace("{{email}}", contact.email());
    }
}