package com.heycar.service;

import com.heycar.domain.CarListing;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CarListingSpecificationCreator {

    Specification<CarListing> create(Map<String, String> request) {
        Specification<CarListing> spec = Specification.where(null);
        for (Map.Entry<String, String> entry : request.entrySet()) {
            spec = spec.and(getSpecByField(entry.getKey(), entry.getValue()));
        }
        return spec;
    }

    private Specification<CarListing> getSpecByField(String name, String value) {
        switch (name) {
            case "model":
                return getIgnoreCaseSpec("model", value);
            case "make":
                return getIgnoreCaseSpec("make", value);
            case "color":
                return getIgnoreCaseSpec("color", value);
            case "year":
                return (root, query, cb) -> cb.equal(root.get("year"), Integer.valueOf(value));
        }
        return null;
    }

    private static Specification<CarListing> getIgnoreCaseSpec(String field, String value) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get(field)), value.toLowerCase());
    }
}
