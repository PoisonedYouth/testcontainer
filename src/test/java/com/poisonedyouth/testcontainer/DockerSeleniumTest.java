package com.poisonedyouth.testcontainer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AcceptanceTestsConfiguration.class })
public class DockerSeleniumTest {

	@Autowired
	private WebDriver driver;

	@Autowired
	private URI testcontainerBaseUri;

	@Autowired
	private PersonService personService;

	@Before
	public void setUp() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(testcontainerBaseUri.toString());
	}


	//docker co
	// ntainer run -d --name selenium-hub -p 4444:4444 selenium/hub:3.4.0

	//docker container run -d --name chrome -e HUB_PORT_4444_TCP_ADDR=selenium-hub -e
	//HUB_PORT_4444_TCP_PORT=4444 -e DISPLAY=99.0 -e SE_OPTS="-port5556"
	//--link selenium-hub:selenium-hub selenium/node-chrome:3.4.0

	//mvn verify -Pacceptance-tests -Dacceptance.testcontainer.url=http://IP_ADDRESS:8080
	//-Dselenium-browser=chrome

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
	 * Asserts that deleting existing person is removing row from persons page
	 */
	@Test
	public void deletePersonIsWorking() {
		driver.findElement(By.id("deletePerson0")).click();

		Person person = personService.findPersonById(1);
		assertThat(person).isNull();
	}
}
