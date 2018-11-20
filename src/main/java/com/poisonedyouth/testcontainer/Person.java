package com.poisonedyouth.testcontainer;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@NotEmpty
	@Column(name = "firstname")
	private String firstname;

	@NotEmpty
	@Column(name = "lastname")
	private String lastname;

	@Min(value = 1)
	@Max(value = 100)
	@Column(name = "age")
	private int age;
}
