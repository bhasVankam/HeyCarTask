package com.heycar.controller;

import com.heycar.dto.CarListingDTO;
import com.heycar.dto.PageDTO;
import com.heycar.service.CarListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CarListingController {

    private final CarListingService carListingService;

    @PostMapping(value = "/car-listing/vehicle_listings/{dealerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody List<CarListingDTO> carListingDTOS,
                       @PathVariable String dealerId) {
        carListingService.postListings(carListingDTOS, dealerId);
    }

    @PostMapping(value = "/car-listing/upload_csv/{dealerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadViaCsv(@RequestParam("file") MultipartFile csvFile, @PathVariable String dealerId) throws IOException {
        carListingService.uploadListings(csvFile, dealerId);
    }

    @GetMapping(value = "/car-listing/search")
    public PageDTO<CarListingDTO> search(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(required = false) String make,
                                         @RequestParam(required = false) String model,
                                         @RequestParam(required = false) Integer year,
                                         @RequestParam(required = false) String color) {
        // I like to use request body instead of param, as we are posting more information. Now it is only 4 fields
        // but in future we might add more fields and sorting as well (field names and sorting order)
        Map<String, String> searchRequest = new HashMap<>();
        if (make != null) {
            searchRequest.put("make", make);
        }
        if (model != null) {
            searchRequest.put("model", model);
        }
        if (year != null) {
            searchRequest.put("year", year.toString());
        }
        if (color != null) {
            searchRequest.put("color", color);
        }
        return carListingService.search(page, pageSize, searchRequest);
    }

}
