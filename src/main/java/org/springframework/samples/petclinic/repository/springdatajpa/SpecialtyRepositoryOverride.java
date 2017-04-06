
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Specialty;


@Profile("spring-data-jpa")
public interface SpecialtyRepositoryOverride {
	
	public void delete(Specialty specialty);

}
