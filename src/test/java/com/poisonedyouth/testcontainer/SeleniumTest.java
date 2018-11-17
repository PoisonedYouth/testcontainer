package com.poisonedyouth.testcontainer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class SeleniumTest {

	private WebDriver driver;

	@Before
	public void setUp() {
		URL resource = SeleniumTest.class.getClassLoader().getResource("chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", resource.getPath());

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@After
	public void tearDown(){
		driver.quit();
	}

	@Test
	public void checkThatAllColumnsAreAvailable() {
		driver.get("http://localhost:8080/persons");
		String id0 = driver.findElement(By.id("id0")).getText();
		String firstname0 = driver.findElement(By.id("firstname0")).getText();
		String lastname0 = driver.findElement(By.id("lastname0")).getText();
		String age0 = driver.findElement(By.id("age0")).getText();
		assertThat(id0).isEqualTo("1");
		assertThat(firstname0).isEqualTo("John");
		assertThat(lastname0).isEqualTo("Doe");
		assertThat(age0).isEqualTo("45");
	}

	@Test
	public void checkAllRowsAreAvailable() {
		driver.get("http://localhost:8080/persons");
		List<WebElement> elements = driver.findElements(By.xpath("html/body/div/table/tbody/tr"));
		assertThat(elements).hasSize(4);
	}

	@Test
	public void addNewPersonIsWorking() {
		driver.get("http://localhost:8080/persons");
		driver.findElement(By.id("firstname")).sendKeys("Matthias");
		driver.findElement(By.id("lastname")).sendKeys("Schenk");
		driver.findElement(By.id("age")).sendKeys("34");
		driver.findElement(By.id("addPerson")).click();

		List<WebElement> elements = driver.findElements(By.xpath("html/body/div/table/tbody/tr"));
		assertThat(elements).hasSize(5);
	}

	@Test
	public void deletePersonIsWorking() {
		driver.get("http://localhost:8080/persons");

		List<WebElement> elementsBefore = driver.findElements(By.xpath("html/body/div/table/tbody/tr"));
		driver.findElement(By.id("delete0")).click();

		List<WebElement> elementsAfter = driver.findElements(By.xpath("html/body/div/table/tbody/tr"));
		assertThat(elementsBefore.size()).isGreaterThan(elementsAfter.size());
	}
}
