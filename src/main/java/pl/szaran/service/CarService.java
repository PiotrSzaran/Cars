package pl.szaran.service;

import lombok.*;
import pl.szaran.converters.CarsJsonConverter;
import pl.szaran.exceptions.MyException;
import pl.szaran.model.Car;
import pl.szaran.validation.CarValidator;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


//dodałem gettera, ktorym moge pobrac kolekcję set
@Getter
public class CarService {

    private final Set<Car> cars;

    public CarService(String jsonFilename) {
        CarsJsonConverter carsJsonConverter = new CarsJsonConverter(jsonFilename);
        AtomicInteger counter = new AtomicInteger(1);
        this.cars = carsJsonConverter
                .fromJson()
                .orElseThrow(() -> new MyException("JSON PARSE EXCEPTION IN CarService CONSTRUCTOR"))
                .stream()
                .filter(car -> {
                    CarValidator carValidator = new CarValidator();
                    Map<String, String> errors = carValidator.validate(car);
                    if (carValidator.hasErrors()) {
                        System.out.println("--------------- Car no. " + counter.get() + " ----------");
                        errors.forEach((k, v) -> System.out.println(k + " " + v));
                    }
                    counter.incrementAndGet();
                    return !carValidator.hasErrors();
                }).collect(Collectors.toSet());
    }


}
