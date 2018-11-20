package com.poisonedyouth.testcontainer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

	List<Person> findPersonsByFirstname(String firstname);
}
