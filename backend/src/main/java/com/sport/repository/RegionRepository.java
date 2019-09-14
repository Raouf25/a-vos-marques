package com.sport.repository;

import com.sport.entity.Region;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends CrudRepository<Region, Integer> {
    public List<Region> findAllByOrderByTownAsc();;
}
