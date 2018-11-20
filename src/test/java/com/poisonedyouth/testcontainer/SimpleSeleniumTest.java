package com.poisonedyouth.testcontainer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleSeleniumTest {

	private WebDriver driver;

	@Before
	public void setUp() {
		URL resource = SimpleSeleniumTest.class.getClassLoader().getResource("chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", resource.getPath());

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get("http://localhost:8080/persons");
	}

	@After
	public void tearDown(){
		driver.quit();
	}

	/**
	 * Asserts that persons page contains table with 4 columns
	 */
	@Test
	public void checkThatAllColumnsAreAvailable() {
		String id0 = driver.findElement(By.id("id0")).getText();
		String firstname0 = driver.findElement(By.id("firstname0")).getText();
		String lastname0 = driver.findElement(By.id("lastname0")).getText();
		String age0 = driver.findElement(By.id("age0")).getText();
		assertThat(id0).isEqualTo("1");
		assertThat(firstname0).isEqualTo("John");
		assertThat(lastname0).isEqualTo("Doe");
		assertThat(age0).isEqualTo("45");
	}

	/**
	 * Asserts that persons page contains table with initial data when loading first time
	 */
	@Test
	public void checkAllRowsAreAvailable() {
		List<WebElement> elements = driver.findElements(By.xpath("html/body/div/table/tbody/tr"));
		assertThat(elements).hasSize(4);
	}

	/**
	 * Asserts that adding new person is adding new row to persons page
	 */
	@Test
	public void addNewPersonIsWorking() {
		List<WebElement> elementsBefore = driver.findElements(By.xpath("html/body/div/table/tbody/tr"));
		driver.findElement(By.id("addPerson")).click();

		driver.findElement(By.id("firstname")).sendKeys("Matthias");
		driver.findElement(By.id("lastname")).sendKeys("Schenk");
		driver.findElement(By.id("age")).sendKeys("34");
		driver.findElement(By.id("addPerson")).click();

		List<WebElement> elementsAfter = driver.findElements(By.xpath("html/body/div/table/tbody/tr"));
		assertThat(elementsBefore.size()).isLessThan(elementsAfter.size());
	}

	/**
	 * Asserts that deleting existing person is removing row from persons page
	 */
	@Test
	public void deletePersonIsWorking() {
		List<WebElement> elementsBefore = driver.findElements(By.xpath("html/body/div/table/tbody/tr"));
		driver.findElement(By.id("deletePerson0")).click();

		List<WebElement> elementsAfter = driver.findElements(By.xpath("html/body/div/table/tbody/tr"));
		assertThat(elementsBefore.size()).isGreaterThan(elementsAfter.size());
	}
}
