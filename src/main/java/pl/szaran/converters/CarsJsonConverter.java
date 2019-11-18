package pl.szaran.converters;

import pl.szaran.model.Car;

import java.util.Set;

public class CarsJsonConverter extends JsonConverter<Set<Car>> {
    public CarsJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
