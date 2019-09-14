package com.sport.repository;

import com.sport.entity.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

    Address findByNameAndTownAndPostalCode(String name, String town, String postalCode);

    List<Address> findByTypeAndPostalCodeStartsWith(String type, String postalCode);
}
