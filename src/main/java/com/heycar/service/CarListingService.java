package com.heycar.service;

import com.heycar.converter.CSVToCarListingConverter;
import com.heycar.converter.EntityConverter;
import com.heycar.domain.CarListing;
import com.heycar.dto.CarListingDTO;
import com.heycar.dto.PageDTO;
import com.heycar.repository.CarListingRepository;
import com.heycar.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarListingService {

    private final CSVToCarListingConverter csvToCarListingConverter;
    private final TransactionHelper transactionHelper;
    private final CarListingRepository carListingRepository;
    private final CarListingSpecificationCreator carListingSpecificationCreator;

    public void uploadListings(final MultipartFile file, final String dealerId) throws IOException {
        List<CarListing> carListings = csvToCarListingConverter.convert(file);
        save(carListings, dealerId);
    }


    public void postListings(final List<CarListingDTO> dealerCarListings, final String dealerId) {
        List<CarListing> carListings = dealerCarListings.stream()
                .map(EntityConverter::carListingDTOToCarListingConverter)
                .collect(Collectors.toList());
        save(carListings, dealerId);
    }


    private void save(final List<CarListing> carListings, final String dealerId) {
        transactionHelper.inTransaction(() -> {
            for (CarListing carListing : carListings) {
                carListing.setCreatedBy(dealerId);
                Optional<CarListing> existingListing = carListingRepository.findByCodeAndCreatedBy(carListing.getCode(), dealerId);
                existingListing.ifPresent(listing -> {
                    carListing.setId(listing.getId());
                    carListing.setCreatedOn(listing.getCreatedOn());
                });
                carListingRepository.save(carListing);
            }
        });
    }

    public PageDTO<CarListingDTO> search(final int page, final int pageSize, final Map<String, String> searchRequest) {
        PageRequest pageRequest = createPageRequest(page, pageSize);
        Specification<CarListing> specification = carListingSpecificationCreator.create(searchRequest);
        Page<CarListing> pages = carListingRepository.findAll(specification, pageRequest);
        return PagingUtils.createPageDTO(pages, EntityConverter::carListingToCarListingDTOConverter);
    }

    private PageRequest createPageRequest(final int page, final int pageSize) {
        Sort sort = Sort.by(
                Sort.Order.desc("modifiedOn"),
                Sort.Order.desc("createdOn")
        );
        return PageRequest.of(page, pageSize, sort);
    }
}
