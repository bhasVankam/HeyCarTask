package com.heycar.controller;

import com.heycar.CarListingApplication;
import com.heycar.domain.CarListing;
import com.heycar.dto.CarListingDTO;
import com.heycar.repository.CarListingRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CarListingApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sql/create-test-data.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sql/delete-test-data.sql")
@ActiveProfiles("integration-test")
public class CarListingControllerIntegrationTest {

    private static final String BASE_URI = "/car-listing/";
    private static final String BASE_DEALER_URI = BASE_URI + "vehicle_listings/{dealerId}";
    private static final String BASE_DELER_CSV_UPLOAD = BASE_URI + "upload_csv/{dealerId}";

    @Autowired
    private CarListingRepository carListingRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void testShouldPostCarListings() {
        List<CarListingDTO> testDataForPostListings = getTestDataForPostListings();
        //@formatter:off
        String dealer = "31332d";
        given()
            .body(testDataForPostListings)
            .contentType(ContentType.JSON)
        .when()
            .pathParam("dealerId", dealer)
            .post(BASE_DEALER_URI)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.CREATED.value());
        //@formatter:on

        verifyCarListing(testDataForPostListings, dealer);
    }

    @Test
    void testShouldPostCarListingsToUpdateExistingListing() {
        List<CarListingDTO> testDataForPostListings = getTestDataForPostListingsToUpdateExistingListing();
        //@formatter:off
        String dealer = "tester";
        given()
            .body(testDataForPostListings)
            .contentType(ContentType.JSON)
        .when()
            .pathParam("dealerId", dealer)
            .post(BASE_DEALER_URI)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.CREATED.value());
        //@formatter:on

        verifyCarListing(testDataForPostListings, dealer);
    }

    @Test
    void testShouldUploadCSV() {
        String csvPath = getClass().getClassLoader().getResource("csv/test_data_full.csv").getFile();
        File csvFile = new File(csvPath);
        //@formatter:off
        String dealer = "tester1";
        given()
            .multiPart("file", csvFile)
        .when()
            .pathParam("dealerId", dealer)
            .post(BASE_DELER_CSV_UPLOAD)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.CREATED.value());
        //@formatter:on
        CarListing carListing1 = carListingRepository.findByCodeAndCreatedBy("1", dealer).get();
        assertEquals("black", carListing1.getColor());
        assertEquals("mercedes", carListing1.getMake());
        assertEquals("a 180", carListing1.getModel());
        assertEquals(90, carListing1.getPowerInKw());
        assertEquals(2014, carListing1.getYear());
        assertEquals(dealer, carListing1.getCreatedBy());
        assertNotNull(carListing1.getCreatedOn());

        CarListing carListing2 = carListingRepository.findByCodeAndCreatedBy("2", dealer).get();
        assertNull(carListing2.getColor());
        assertEquals("skoda", carListing2.getMake());
        assertEquals("octavia", carListing2.getModel());
        assertEquals(63, carListing2.getPowerInKw());
        assertEquals(2018, carListing2.getYear());
        assertEquals(dealer, carListing2.getCreatedBy());
        assertNotNull(carListing2.getCreatedOn());
    }

    @Test
    void testShouldThrowConflictForInvalidRow() {
        String csvPath = getClass().getClassLoader().getResource("csv/test_data_invalid_row.csv").getFile(); // Second row in the csv misses color column // correct value - 2,skoda/octavia,86,2018,,16990
        File csvFile = new File(csvPath);
        //@formatter:off
        given()
            .multiPart("file", csvFile)
        .when()
            .pathParam("dealerId", "tester10")
            .post(BASE_DELER_CSV_UPLOAD)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.CONFLICT.value());
        //@formatter:on
    }

    @Test
    void testShouldThrowConflictForInvalidMakeModelString() {
        String csvPath = getClass().getClassLoader().getResource("csv/test_data_invalid_make_model.csv").getFile(); // Second row in the csv misses color column // correct value - 2,skoda/octavia,86,2018,,16990
        File csvFile = new File(csvPath);
        //@formatter:off
        given()
            .multiPart("file", csvFile)
        .when()
            .pathParam("dealerId", "testing")
            .post(BASE_DELER_CSV_UPLOAD)
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.CONFLICT.value());
        //@formatter:on
    }

    @Test
    void testShouldSearchForAll() {
        //@formatter:off
        given()
        .when()
            .get(BASE_URI + "search")
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("entities.size()", is(4));
        //@formatter:on
    }

    @Test
    void testShouldSearchWithMakeAndColor() {
        //@formatter:off
        given()
            .queryParam("make", "ford")
            .queryParam("color", "black and white")
        .when()
            .get(BASE_URI + "search")
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("entities.size()", is(1))
            .body("entities[0].code", is("12030"))
            .body("entities[0].make", is("Ford"))
            .body("entities[0].model", is("b 309"));
        //@formatter:on
    }

    @Test
    void testShouldSearchWithMakeAndModel() {
        //@formatter:off
        given()
            .queryParam("make", "Ford")
            .queryParam("model", "c 309")
        .when()
            .get(BASE_URI + "search")
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("entities.size()", is(1))
            .body("entities[0].code", is("12031"))
            .body("entities[0].make", is("Ford"))
            .body("entities[0].model", is("c 309"));
        //@formatter:on
    }

    @Test
    void testShouldSearchWithYear() {
        //@formatter:off
        given()
            .queryParam("year", 2019)
        .when()
            .get(BASE_URI + "search")
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("entities.size()", is(1))
            .body("entities[0].code", is("12033"))
            .body("entities[0].make", is("BMW"))
            .body("entities[0].model", is("b 39"));
        //@formatter:on
    }

    private void verifyCarListing(List<CarListingDTO> testDataForPostListings, String dealer) {
        testDataForPostListings.forEach(expected -> {
            CarListing actual = carListingRepository.findByCodeAndCreatedBy(expected.getCode(), dealer).get();
            assertEquals(expected.getColor(), actual.getColor());
            assertEquals(expected.getMake(), actual.getMake());
            assertEquals(expected.getModel(), actual.getModel());
            assertEquals(expected.getPowerInKw(), actual.getPowerInKw());
            assertEquals(expected.getYear(), actual.getYear());
            assertEquals(expected.getPrice(), actual.getPrice());
            assertEquals(dealer, actual.getCreatedBy());
            assertNotNull(actual.getCreatedOn());
        });
    }

    private List<CarListingDTO> getTestDataForPostListings() {
        List<CarListingDTO> carListingDTOS = new ArrayList<>();
        carListingDTOS.add(CarListingDTO.builder()
                .code("224")
                .make("bmw")
                .model("okok")
                .color("red")
                .powerInKw(234)
                .price(BigDecimal.valueOf(2330000L))
                .year(1994)
                .build());
        carListingDTOS.add(CarListingDTO.builder()
                .code("24")
                .make("audi")
                .model("123 a")
                .color("white")
                .powerInKw(123)
                .price(BigDecimal.valueOf(2330000L))
                .build());
        return carListingDTOS;
    }

    private List<CarListingDTO> getTestDataForPostListingsToUpdateExistingListing() {
        List<CarListingDTO> carListingDTOS = new ArrayList<>();
        carListingDTOS.add(CarListingDTO.builder()
                .code("12030")
                .make("Ford")
                .model("b 309")
                .color("black")
                .powerInKw(310)
                .price(BigDecimal.valueOf(343403.03))
                .year(2019)
                .build());
        return carListingDTOS;
    }

}
