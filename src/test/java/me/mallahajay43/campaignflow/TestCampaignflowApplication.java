package me.mallahajay43.campaignflow;

import org.springframework.boot.SpringApplication;

public class TestCampaignflowApplication {

	public static void main(String[] args) {
		SpringApplication.from(CampaignflowApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
