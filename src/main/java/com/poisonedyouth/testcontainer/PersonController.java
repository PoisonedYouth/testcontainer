package com.poisonedyouth.testcontainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PersonController {

	private PersonService personService;

	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/persons")
	public String getAllPersons(Model model) {
		model.addAttribute("persons", personService.getAllPersons());
		model.addAttribute("newPerson", new Person());

		return "persons";
	}

	@PostMapping("/persons")
	public String addNewPerson(@ModelAttribute Person newPerson, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {

		}
		personService.savePerson(newPerson);

		return "redirect:/persons";
	}

	@DeleteMapping("persons/delete/{id}")
	public String deletePerson(@PathVariable(name = "id") Integer id) {
		Person existingPerson = personService.findPersonById(id);
		if(existingPerson != null) {
			personService.deletePerson(existingPerson);
		}
		return "redirect:/persons";
	}
}
