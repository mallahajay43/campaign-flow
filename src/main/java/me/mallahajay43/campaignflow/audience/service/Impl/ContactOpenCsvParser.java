package me.mallahajay43.campaignflow.audience.service.Impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.dto.ContactCsvRow;
import me.mallahajay43.campaignflow.audience.dto.ParsedContactImportRow;
import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.common.exceptions.ImportStorageException;
import me.mallahajay43.campaignflow.common.exceptions.InvalidCsvStructureException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContactOpenCsvParser {

    private final Validator validator;
    private final ContactCsvMapper contactCsvMapper;

    public void parse(InputStream inputStream, long checkpoint, int chunkSize,
            Consumer<List<ParsedContactImportRow>> chunkConsumer
    ) {
        try (
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).build()
        ) {

            HeaderColumnNameMappingStrategy<ContactCsvRow> strategy = createMappingStrategy();

            // Reads and captures the first CSV record as the header.
            strategy.captureHeader(csvReader);

            validateRequiredHeaders(strategy);

            List<ParsedContactImportRow> chunk = new ArrayList<>(chunkSize);

            long rowNumber = 0;
            String[] columns;

            while ((columns = csvReader.readNext()) != null) {

                // Skip header
                rowNumber++;
                if (rowNumber <= checkpoint) {
                    continue;
                }

                ParsedContactImportRow parsedRow =
                        parseRow(strategy, columns, rowNumber);

                chunk.add(parsedRow);

                if (chunk.size() == chunkSize) {
                    chunkConsumer.accept(List.copyOf(chunk));
                    chunk.clear();
                }
            }

            if (!chunk.isEmpty()) {
                chunkConsumer.accept(List.copyOf(chunk));
            }

        } catch (CsvValidationException exception) {
            throw new InvalidCsvStructureException(
                    "Unable to parse CSV file",
                    exception
            );

        } catch (IOException exception) {
            throw new ImportStorageException(
                    "Unable to read CSV file",
                    exception
            );
        } catch (CsvRequiredFieldEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    private HeaderColumnNameMappingStrategy<ContactCsvRow> createMappingStrategy() {

        HeaderColumnNameMappingStrategy<ContactCsvRow> strategy =
                new HeaderColumnNameMappingStrategy<>();

        strategy.setType(ContactCsvRow.class);
        strategy.setErrorLocale(Locale.ENGLISH);

        return strategy;
    }

    private ParsedContactImportRow parseRow(
            HeaderColumnNameMappingStrategy<ContactCsvRow> strategy,
            String[] columns,
            long rowNumber
    ) {
        ContactCsvRow csvRow = null;

        try {

            strategy.verifyLineLength(columns.length);
            csvRow = strategy.populateNewBean(columns);
            Set<ConstraintViolation<ContactCsvRow>> violations = validator.validate(csvRow);

            if (!violations.isEmpty()) {
                return ParsedContactImportRow.rejected(rowNumber, csvRow.getEmail(), buildValidationMessage(violations)
                );
            }

            Contact contact = contactCsvMapper.toEntity(csvRow);

            return ParsedContactImportRow.imported(rowNumber, contact);

        } catch (CsvRequiredFieldEmptyException exception) {
            return ParsedContactImportRow.rejected(
                    rowNumber,
                    extractEmail(csvRow),
                    "Required value is missing: " + exception.getMessage()
            );

        } catch (CsvFieldAssignmentException |
                 CsvBeanIntrospectionException exception) {

            return ParsedContactImportRow.rejected(
                    rowNumber,
                    extractEmail(csvRow),
                    "Could not map CSV row: " +
                            exception.getMessage()
            );

        } catch (RuntimeException exception) {
            return ParsedContactImportRow.rejected(
                    rowNumber,
                    extractEmail(csvRow),
                    "Unexpected row validation error: " +
                            exception.getMessage()
            );
        } catch (CsvChainedException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildValidationMessage(
            Set<ConstraintViolation<ContactCsvRow>> violations
    ) {
        return violations.stream()
                .sorted(
                        Comparator.comparing(violation ->
                                violation.getPropertyPath().toString()
                        )
                )
                .map(violation ->
                        violation.getPropertyPath() +
                                ": " +
                                violation.getMessage()
                )
                .collect(Collectors.joining("; "));
    }

    private String extractEmail(ContactCsvRow row) {
        return row == null ? null : row.getEmail();
    }

    private void validateRequiredHeaders(
            HeaderColumnNameMappingStrategy<ContactCsvRow> strategy
    ) {
        String[] headers;

        try {
            headers = strategy.generateHeader(new ContactCsvRow());
        } catch (CsvRequiredFieldEmptyException exception) {
            throw new InvalidCsvStructureException(
                    "Could not validate CSV headers",
                    exception
            );
        }
    }
}