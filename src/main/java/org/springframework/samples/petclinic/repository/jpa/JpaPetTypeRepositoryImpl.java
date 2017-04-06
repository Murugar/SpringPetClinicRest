
package org.springframework.samples.petclinic.repository.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.stereotype.Repository;



@Repository
@Profile("jpa")
public class JpaPetTypeRepositoryImpl implements PetTypeRepository {
	
    @PersistenceContext
    private EntityManager em;

	@Override
	public PetType findById(int id) {
		return this.em.find(PetType.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<PetType> findAll() throws DataAccessException {
		return this.em.createQuery("SELECT ptype FROM PetType ptype").getResultList();
	}

	@Override
	public void save(PetType petType) throws DataAccessException {
		if (petType.getId() == null) {
            this.em.persist(petType);
        } else {
            this.em.merge(petType);
        }

	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(PetType petType) throws DataAccessException {
		//this.em.remove(this.em.contains(petType) ? petType : this.em.merge(petType));
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
