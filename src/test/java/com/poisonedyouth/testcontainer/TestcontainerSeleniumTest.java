package com.poisonedyouth.testcontainer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestcontainerSeleniumTest {

	@LocalServerPort
	protected int serverPort;

	@Autowired
	private PersonService personService;

	@Rule
	public BrowserWebDriverContainer chrome =
			new BrowserWebDriverContainer()
					.withDesiredCapabilities(DesiredCapabilities.chrome());
	private WebDriver driver;

	@Before
	public void setUp() {
		driver = chrome.getWebDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//This is not working if tests are run on windows machine
		driver.get("http://" + chrome.getTestHostIpAddress() + ":" + serverPort + "/");
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void deletePersonIsWorking() {
		driver.findElement(By.id("delete0")).click();

		Person person = personService.findPersonById(1);
		assertThat(person).isNull();
	}
}
