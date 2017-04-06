package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.PetType;

@Profile("spring-data-jpa")
public interface PetTypeRepositoryOverride {
	
	public void delete(PetType petType);

}
