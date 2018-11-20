package com.poisonedyouth.testcontainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
		return "persons";
	}

	@GetMapping("persons/new")
	public String addNewPersonShow(Model model) {
		model.addAttribute("person", new Person());
		return "newperson";
	}

	@PostMapping("/persons/new")
	public String addNewPerson(@ModelAttribute @Valid Person newPerson, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "newperson";
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

	@GetMapping("persons/edit/{id}")
	public String editPersonShoe(@PathVariable(name = "id") Integer id, Model model) {
		Person existingPerson = personService.findPersonById(id);
		if(existingPerson == null) {
			return "persons";
		}
		model.addAttribute("person", existingPerson);
		return "editperson";
	}

	@PostMapping("persons/edit")
	public String editPerson(@ModelAttribute Person person, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "editperson";
		}
		personService.savePerson(person);

		return "redirect:/persons";
	}
}
