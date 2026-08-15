package me.mallahajay43.campaignflow.audience.repository;

import me.mallahajay43.campaignflow.audience.entity.ContactImportFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContactImportFileRepository extends JpaRepository<ContactImportFile, UUID> {

}
