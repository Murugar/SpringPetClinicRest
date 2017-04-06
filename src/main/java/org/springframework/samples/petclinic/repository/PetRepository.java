
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;


public interface PetRepository {

    /**
     * Retrieve all <code>PetType</code>s from the data store.
     *
     * @return a <code>Collection</code> of <code>PetType</code>s
     */
    List<PetType> findPetTypes() throws DataAccessException;

    /**
     * Retrieve a <code>Pet</code> from the data store by id.
     *
     * @param id the id to search for
     * @return the <code>Pet</code> if found
     * @throws org.springframework.dao.DataRetrievalFailureException if not found
     */
    Pet findById(int id) throws DataAccessException;

    /**
     * Save a <code>Pet</code> to the data store, either inserting or updating it.
     *
     * @param pet the <code>Pet</code> to save
     * @see BaseEntity#isNew
     */
    void save(Pet pet) throws DataAccessException;
    
    /**
     * Retrieve <code>Pet</code>s from the data store, returning all owners 
     *
     * @return a <code>Collection</code> of <code>Pet</code>s (or an empty <code>Collection</code> if none
     * found)
     */
	Collection<Pet> findAll() throws DataAccessException;

    /**
     * Delete an <code>Pet</code> to the data store by <code>Pet</code>.
     *
     * @param pet the <code>Pet</code> to delete
     * 
     */
	void delete(Pet pet) throws DataAccessException;

}
