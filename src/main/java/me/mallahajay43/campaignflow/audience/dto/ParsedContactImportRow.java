package me.mallahajay43.campaignflow.audience.dto;

import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.audience.state.ImportRowStatus;
import me.mallahajay43.campaignflow.common.enums.ImportStatus;

public record ParsedContactImportRow(
        long rowNumber,
        Contact contact,
        String email,
        ImportRowStatus status,
        String errorMessage
) {

    public static ParsedContactImportRow imported(
            long rowNumber,
            Contact contact
    ) {
        return new ParsedContactImportRow(
                rowNumber,
                contact,
                contact.getEmail(),
                ImportRowStatus.IMPORTED,
                null
        );
    }

    public static ParsedContactImportRow rejected(
            long rowNumber,
            String email,
            String errorMessage
    ) {
        return new ParsedContactImportRow(
                rowNumber,
                null,
                email,
                ImportRowStatus.REJECTED,
                errorMessage
        );
    }

    public boolean valid() {
        return status == ImportRowStatus.IMPORTED;
    }
}