package me.mallahajay43.campaignflow;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTest {

    private final ApplicationModules modules =
            ApplicationModules.of(CampaignflowApplication.class);

    @Test
    void verifiesModuleBoundaries() {
        modules.verify();
    }
}