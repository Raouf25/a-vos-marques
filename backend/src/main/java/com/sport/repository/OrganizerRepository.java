package com.sport.repository;

import com.sport.entity.Organizer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends CrudRepository<Organizer, Integer> {

    Organizer findByOrganisationAndTownAndPostalCode(String organisation, String town, String postalCode);
}
