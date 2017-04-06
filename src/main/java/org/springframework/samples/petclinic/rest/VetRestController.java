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
import org.springframework.samples.petclinic.model.Vet;
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
@RequestMapping("api/vets")
public class VetRestController {
	
	@Autowired
	private ClinicService clinicService;
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Collection<Vet>> getAllVets(){
		Collection<Vet> vets = new ArrayList<Vet>();
		vets.addAll(this.clinicService.findAllVets());
		if (vets.isEmpty()){
			return new ResponseEntity<Collection<Vet>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Vet>>(vets, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{vetId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Vet> getVet(@PathVariable("vetId") int vetId){
		Vet vet = this.clinicService.findVetById(vetId);
		if(vet == null){
			return new ResponseEntity<Vet>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vet>(vet, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Vet> addVet(@RequestBody @Valid Vet vet, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		
		System.out.println("Vet Controller Add");
		
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (vet == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Vet>(headers, HttpStatus.BAD_REQUEST);
		}
		System.out.println(vet.getFirstName());
		System.out.println(vet.getLastName());
		
		this.clinicService.saveVet(vet);
		headers.setLocation(ucBuilder.path("/api/vets/{id}").buildAndExpand(vet.getId()).toUri());
		return new ResponseEntity<Vet>(vet, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{vetId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Vet> updateVet(@PathVariable("vetId") int vetId, @RequestBody @Valid Vet vet, BindingResult bindingResult){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (vet == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Vet>(headers, HttpStatus.BAD_REQUEST);
		}
		Vet currentVet = this.clinicService.findVetById(vetId);
		if(currentVet == null){
			return new ResponseEntity<Vet>(HttpStatus.NOT_FOUND);
		}
		currentVet.setFirstName(vet.getFirstName());
		currentVet.setLastName(vet.getLastName());
		currentVet.setSpecialtiesInternal(vet.getSpecialtiesInternal());
		this.clinicService.saveVet(currentVet);
		return new ResponseEntity<Vet>(currentVet, HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/{vetId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional
	public ResponseEntity<Void> deleteVet(@PathVariable("vetId") int vetId){
		Vet vet = this.clinicService.findVetById(vetId);
		if(vet == null){
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		try {
			this.clinicService.deleteVet(vet);
		} catch (Exception e) {
			this.clinicService.deleteVet(vet);
		}
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	

}
