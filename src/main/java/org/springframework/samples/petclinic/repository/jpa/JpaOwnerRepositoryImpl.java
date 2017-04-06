
package org.springframework.samples.petclinic.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Repository;


@Repository
@Profile("jpa")
public class JpaOwnerRepositoryImpl implements OwnerRepository {

    @PersistenceContext
    private EntityManager em;


    /**
     * Important: in the current version of this method, we load Owners with all their Pets and Visits while
     * we do not need Visits at all and we only need one property from the Pet objects (the 'name' property).
     * There are some ways to improve it such as:
     * - creating a Ligtweight class (example here: https://community.jboss.org/wiki/LightweightClass)
     * - Turning on lazy-loading and using {@link OpenSessionInViewFilter}
     */
    @SuppressWarnings("unchecked")
    public Collection<Owner> findByLastName(String lastName) {
        // using 'join fetch' because a single query should load both owners and pets
        // using 'left join fetch' because it might happen that an owner does not have pets yet
        Query query = this.em.createQuery("SELECT DISTINCT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE :lastName");
        query.setParameter("lastName", lastName + "%");
        return query.getResultList();
    }

    @Override
    public Owner findById(int id) {
        // using 'join fetch' because a single query should load both owners and pets
        // using 'left join fetch' because it might happen that an owner does not have pets yet
        Query query = this.em.createQuery("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.id =:id");
        query.setParameter("id", id);
        return (Owner) query.getSingleResult();
    }


    @Override
    public void save(Owner owner) {
        if (owner.getId() == null) {
            this.em.persist(owner);
        } else {
            this.em.merge(owner);
        }

    }
    
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Owner> findAll() throws DataAccessException {
		Query query = this.em.createQuery("SELECT owner FROM Owner owner");
        return query.getResultList();
	}

	@Override
	public void delete(Owner owner) throws DataAccessException {
		this.em.remove(this.em.contains(owner) ? owner : this.em.merge(owner));
	}

}
