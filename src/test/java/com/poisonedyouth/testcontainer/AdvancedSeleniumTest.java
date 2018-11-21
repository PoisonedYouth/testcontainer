package com.poisonedyouth.testcontainer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdvancedSeleniumTest {

	@LocalServerPort
	protected int serverPort;

	@Autowired
	private PersonService personService;

	private WebDriver driver;

	@Before
	public void setUp() {
		URL resource = SimpleSeleniumTest.class.getClassLoader().getResource("chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", resource.getPath());

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://localhost:" + serverPort + "/persons");
	}

	@After
	public void tearDown(){
		driver.quit();
	}

	/**
	 * Asserts that persons page contains table with 4 columns and initial data
	 */
	@Test
	public void checkThatAllDataIsAvailable() {
		Person person = personService.findPersonById(1);

		String id0 = driver.findElement(By.id("id0")).getText();
		String firstname0 = driver.findElement(By.id("firstname0")).getText();
		String lastname0 = driver.findElement(By.id("lastname0")).getText();
		String age0 = driver.findElement(By.id("age0")).getText();

		assertThat(id0).isEqualTo(person.getId());
		assertThat(firstname0).isEqualTo(person.getFirstname());
		assertThat(lastname0).isEqualTo(person.getLastname());
		assertThat(age0).isEqualTo(person.getAge());
	}

	/**
	 * Asserts that adding new person is adding new row to persons page
	 */
	@Test
	public void addNewPersonIsWorking() {
		Person newPerson = new Person();
		newPerson.setId(5);
		newPerson.setFirstname("Matthias");
		newPerson.setLastname("Schenk");
		newPerson.setAge(34);

		driver.findElement(By.id("addPerson")).click();

		driver.findElement(By.id("firstname")).sendKeys(newPerson.getFirstname());
		driver.findElement(By.id("lastname")).sendKeys(newPerson.getLastname());
		driver.findElement(By.id("age")).sendKeys(newPerson.getAge() + "");
		driver.findElement(By.id("addPerson")).click();

		List<Person> persons = personService.getAllPersons();
		assertThat(persons).contains(newPerson);
	}

	/**
	 * Asserts that if validation of adding new person fails, no element is added to persons page
	 */
	@Test
	public void addNewPersonValidationIsWorking() {
		List<Person> personsBefore = personService.getAllPersons();

		driver.findElement(By.id("addPerson")).click();

		driver.findElement(By.id("firstname")).sendKeys("");
		driver.findElement(By.id("lastname")).sendKeys("");
		driver.findElement(By.id("age")).sendKeys("");
		driver.findElement(By.id("addPerson")).click();

		assertThat(driver.getTitle()).isEqualTo("New Person");

		assertThat(driver.findElements(By.className("error")).size()).isEqualTo(3);

		List<Person> personsAfter = personService.getAllPersons();
		assertThat(personsBefore.size()).isEqualTo(personsAfter.size());
	}

	/**
	 * Asserts that deleting existing person is removing row from persons page
	 */
	@Test
	public void deletePersonIsWorking() {
		driver.findElement(By.id("deletePerson0")).click();

		Person person = personService.findPersonById(1);
		assertThat(person).isNull();
	}

	/**
	 * Asserts that editing existing person is changing row on persons page
	 */
	@Test
	public void editPersonIsWorking() {
		driver.findElement(By.id("editPerson0")).click();

		WebElement firstname = driver.findElement(By.id("firstname"));
		firstname.clear();
		firstname.sendKeys("Max");
		WebElement lastname = driver.findElement(By.id("lastname"));
		lastname.clear();
		lastname.sendKeys("Mustermann");
		WebElement age = driver.findElement(By.id("age"));
		age.clear();
		age.sendKeys("12");
		driver.findElement(By.id("updatePerson")).click();

		Person existingPerson = personService.findPersonById(1);
		assertThat(existingPerson.getFirstname()).isEqualTo("Max");
		assertThat(existingPerson.getLastname()).isEqualTo("Mustermann");
		assertThat(existingPerson.getAge()).isEqualTo(12);
	}

	@Test
	public void editPersonValidationIsWorking() {
		Person personBefore = personService.findPersonById(1);

		driver.findElement(By.id("editPerson0")).click();

		WebElement firstname = driver.findElement(By.id("firstname"));
		firstname.clear();
		WebElement lastname = driver.findElement(By.id("lastname"));
		lastname.clear();
		WebElement age = driver.findElement(By.id("age"));
		age.clear();
		driver.findElement(By.id("updatePerson")).click();

		assertThat(driver.getTitle()).isEqualTo("Edit Person");

		Person personAfter = personService.findPersonById(1);
		assertThat(personBefore).isEqualTo(personAfter);
	}
}
