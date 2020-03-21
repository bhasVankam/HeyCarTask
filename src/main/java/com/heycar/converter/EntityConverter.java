package com.heycar.converter;

import com.heycar.domain.CarListing;
import com.heycar.dto.CarListingDTO;
import org.springframework.beans.BeanUtils;

public final class EntityConverter {

    public static CarListing carListingDTOToCarListingConverter(CarListingDTO carListingDTO) {
        CarListing carListing = new CarListing();
        BeanUtils.copyProperties(carListingDTO, carListing);
        return carListing;
    }

    public static CarListingDTO carListingToCarListingDTOConverter(CarListing carListing) {
        CarListingDTO carListingDTO = new CarListingDTO();
        BeanUtils.copyProperties(carListing, carListingDTO);
        return carListingDTO;
    }
}
