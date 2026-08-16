package me.mallahajay43.campaignflow.audience.dto;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.apache.commons.collections4.MultiValuedMap;

@Data
public class ContactCsvRow {

    @CsvBindByName(column = "Email", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 320, message = "Email cannot exceed 320 characters")
    private String email;

    @CsvBindByName(column = "Full Name")
    @Size(max = 200, message = "Full name cannot exceed 200 characters")
    private String fullName;

    // This captures every other column in the CSV file
    @CsvBindAndJoinByName(column = ".*", elementType = String.class)
    private MultiValuedMap<String, String> remainingColumns;
}

