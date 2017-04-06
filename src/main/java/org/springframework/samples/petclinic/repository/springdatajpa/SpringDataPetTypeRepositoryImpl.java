
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;



@Profile("spring-data-jpa")
public class SpringDataPetTypeRepositoryImpl implements PetTypeRepositoryOverride {
	
	@PersistenceContext
    private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public void delete(PetType petType) {
		String petTypeId = petType.getId().toString();
		
		List<Pet> pets = new ArrayList<Pet>();
		pets = this.em.createQuery("SELECT pet FROM Pet pet WHERE type_id=" + petTypeId).getResultList();
		for (Pet pet : pets){
			List<Visit> visits = new ArrayList<Visit>();
			visits = pet.getVisits();
			for (Visit visit : visits){
				this.em.createQuery("DELETE FROM Visit visit WHERE id=" + visit.getId().toString()).executeUpdate();
			}
			this.em.createQuery("DELETE FROM Pet pet WHERE id=" + pet.getId().toString()).executeUpdate();
		}
		this.em.createQuery("DELETE FROM PetType pettype WHERE id=" + petTypeId).executeUpdate();
		if (em.contains(petType)) {
			em.remove(petType);
		}

	}

}
