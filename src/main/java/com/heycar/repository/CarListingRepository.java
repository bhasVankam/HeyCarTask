package com.heycar.repository;

import com.heycar.domain.CarListing;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarListingRepository extends CrudRepository<CarListing, UUID>, JpaSpecificationExecutor<CarListing> {

    Optional<CarListing> findByCodeAndCreatedBy(@NotNull String code, @NotNull String createdBy);
}
