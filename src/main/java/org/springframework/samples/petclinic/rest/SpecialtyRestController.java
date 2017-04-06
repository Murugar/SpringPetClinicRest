package org.springframework.samples.petclinic.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api/specialties")
public class SpecialtyRestController {
	
	@Autowired
	private ClinicService clinicService;
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Collection<Specialty>> getAllSpecialtys(){
		Collection<Specialty> specialties = new ArrayList<Specialty>();
		specialties.addAll(this.clinicService.findAllSpecialties());
		if (specialties.isEmpty()){
			return new ResponseEntity<Collection<Specialty>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Specialty>>(specialties, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{specialtyId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Specialty> getSpecialty(@PathVariable("specialtyId") int specialtyId){
		Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
		if(specialty == null){
			return new ResponseEntity<Specialty>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Specialty>(specialty, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Specialty> addSpecialty(@RequestBody @Valid Specialty specialty, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (specialty == null)){
			System.out.println("Error Added Speciality");
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Specialty>(headers, HttpStatus.BAD_REQUEST);
		}
		this.clinicService.saveSpecialty(specialty);
		System.out.println("Added Speciality");
		headers.setLocation(ucBuilder.path("/api/specialtys/{id}").buildAndExpand(specialty.getId()).toUri());
		return new ResponseEntity<Specialty>(specialty, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{specialtyId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Specialty> updateSpecialty(@PathVariable("specialtyId") int specialtyId, @RequestBody @Valid Specialty specialty, BindingResult bindingResult){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (specialty == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Specialty>(headers, HttpStatus.BAD_REQUEST);
		}
		Specialty currentSpecialty = this.clinicService.findSpecialtyById(specialtyId);
		if(currentSpecialty == null){
			return new ResponseEntity<Specialty>(HttpStatus.NOT_FOUND);
		}
		currentSpecialty.setName(specialty.getName());
		this.clinicService.saveSpecialty(currentSpecialty);
		System.out.println("Updated Speciality");
		return new ResponseEntity<Specialty>(currentSpecialty, HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/{specialtyId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	//@Transactional
	public ResponseEntity<Void> deleteSpecialty(@PathVariable("specialtyId") int specialtyId){
		Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
		if(specialty == null){
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		System.out.println(specialty.getName());
		System.out.println("Before Deleted Speciality");
		try {
			this.clinicService.deleteSpecialty(specialty);
		} catch (Exception e) {
			this.clinicService.deleteSpecialty(specialty);
		}
		System.out.println("Deleted Speciality");
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
