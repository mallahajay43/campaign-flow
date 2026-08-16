package me.mallahajay43.campaignflow.audience.service.Impl;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.dto.ContactCsvRow;
import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.common.enums.ContactStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ContactCsvMapper {

    public Contact toEntity(ContactCsvRow row) {
        Map<String, Object> jsonAttributes = new HashMap<>();

        if (row.getRemainingColumns() != null) {
            row.getRemainingColumns()
                    .asMap()
                    .forEach((key, values) -> {

                        if ("Email".equalsIgnoreCase(key) ||
                                "Full Name".equalsIgnoreCase(key)) {
                            return;
                        }

                        List<String> nonBlankValues = values.stream()
                                .filter(Objects::nonNull)
                                .map(String::trim)
                                .filter(value -> !value.isBlank())
                                .toList();

                        if (nonBlankValues.size() == 1) {
                            jsonAttributes.put(
                                    key,
                                    nonBlankValues.getFirst()
                            );
                        } else if (!nonBlankValues.isEmpty()) {
                            jsonAttributes.put(
                                    key,
                                    nonBlankValues
                            );
                        }
                    });
        }

        return Contact.builder()
                .email(normalizeEmail(row.getEmail()))
                .fullName(trimToNull(row.getFullName()))
                .status(ContactStatus.ACTIVE)
                .attributes(jsonAttributes)
                .build();
    }

    private String normalizeEmail(String email) {
        return email == null
                ? null
                : email.trim().toLowerCase(Locale.ROOT);
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}

