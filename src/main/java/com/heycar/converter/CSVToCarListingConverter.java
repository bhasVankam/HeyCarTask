package com.heycar.converter;

import com.heycar.exception.InvalidCSVException;
import com.heycar.domain.CarListing;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@Slf4j
public class CSVToCarListingConverter {

    private static final String CODE = "code";
    private static final String MAKE_MODEL = "make/model";
    private static final String POWER = "power-in-ps";
    private static final String YEAR = "year";
    private static final String COLOR = "color";
    private static final String PRICE = "price";
    private static final List<String> SUPPORTED_CSV_COLUMNS = Arrays.asList(CODE, MAKE_MODEL, POWER, YEAR, COLOR, PRICE);

    public List<CarListing> convert(MultipartFile file) throws IOException {
        try (CSVParser parser = getParser(file)) {
            List<String> headerNames = parser.getHeaderNames().stream().map(String::toLowerCase).collect(Collectors.toList());
            if (!headerNames.containsAll(SUPPORTED_CSV_COLUMNS)) {
                throw new RuntimeException("Invalid CSV");
            }
            Stream<CSVRecord> csvRecordStream = StreamSupport.stream(parser.spliterator(), false);
            return csvRecordStream
                    .filter(Objects::nonNull)
                    .filter(this::isConsistentRow)
                    .map(CSVRecord::toMap)
                    .map(record -> toCarListing(record, headerNames))
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new InvalidCSVException(ex.getMessage());
        }
    }

    private CarListing toCarListing(Map<String, String> record, List<String> headerNames) {
        if (isBlank(record.get(PRICE)) || isBlank(record.get(CODE)) || isBlank(record.get(MAKE_MODEL))) {
            throw new RuntimeException("Either price or code or make/model is absent for the record - " + record.toString());
        }
        CarListing carListing = new CarListing();
        carListing.setColor(isNotBlank(record.get(COLOR)) ? record.get(COLOR) : null);
        carListing.setPrice(new BigDecimal(record.get(PRICE)));
        carListing.setYear(parseInteger(record.get(YEAR)));

        String[] makeAndModel = record.get(MAKE_MODEL).split("/");
        if (makeAndModel.length != 2) {
            throw new RuntimeException("Invalid Make Model value for record - " + record.toString());
        }
        carListing.setMake(makeAndModel[0]);
        carListing.setModel(makeAndModel[1]);

        carListing.setPowerInKw(psToKw(parseInteger(record.get(POWER))));
        carListing.setCode(record.get(CODE));
        return carListing;
    }

    private CSVParser getParser(MultipartFile file) throws IOException {
        return CSVFormat.EXCEL.withHeader().parse(new InputStreamReader(file.getInputStream()));
    }

    private boolean isConsistentRow(CSVRecord record) {
        if (record.isConsistent()) {
            return true;
        }
        throw new RuntimeException("Inconsistent row - record size does not match the header size." +
                "Possible reasons - missing value or extra value. " +
                "Escape the field values which has commas using double quotes. for record -" + record.toMap().toString());
    }

    private static Integer parseInteger(final String s) {
        return isNotBlank(s) ? Integer.valueOf(s) : null;
    }

    private static Integer psToKw(final Integer ps) {
        return ps != null ? Math.round(ps * 0.73549875f) : null;
    }
}
