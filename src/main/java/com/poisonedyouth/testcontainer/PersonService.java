package com.poisonedyouth.testcontainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PersonService {

	private PersonRepository personRepository;

	@Autowired
	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public List<Person> getAllPersons() {
		return StreamSupport.stream(personRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	public Person savePerson(Person newPerson) {
		return personRepository.save(newPerson);
	}

	public Person findPersonById(Integer id) {
		Optional<Person> person = personRepository.findById(id);
		return person.get();
	}

	public void deletePerson(Person existingPerson) {
		personRepository.delete(existingPerson);
	}
}
