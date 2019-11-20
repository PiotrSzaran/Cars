package pl.szaran.service;

import lombok.*;
import pl.szaran.converters.CarsJsonConverter;
import pl.szaran.exceptions.MyException;
import pl.szaran.model.Car;
import pl.szaran.model.enums.Color;
import pl.szaran.service.enums.SortType;
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

    /**
     * Metoda, która zwraca nową kolekcję elementów Car posortowaną
     * według podanego jako argument metody kryterium. Metoda powinna
     * mieć możliwość sortowania po nazwie modelu, kolorze, cenie oraz
     * przebiegu. Dodatkowo należy określić czy sortowanie ma odbywać
     * się malejąco czy rosnąco.
     **/


    public List<Car> sortBy() {

        SortType sortType = UserDataService.getSortType();
        boolean ascending = UserDataService.getSortOrder();

        //turn on preview for switch expressions
        var sortedCars = switch (sortType) {
            case COLOR -> cars.stream().sorted(Comparator.comparing(Car::getColor));
            case MODEL -> cars.stream().sorted(Comparator.comparing(Car::getModel));
            case PRICE -> cars.stream().sorted(Comparator.comparing(Car::getPrice));
            default -> cars.stream().sorted(Comparator.comparing(Car::getMileage));
        };

        var sortedCarsByOrder = sortedCars.collect(Collectors.toList());
        if (!ascending) {
            Collections.reverse(sortedCarsByOrder);
        }
        return sortedCarsByOrder;

    }

    /**
     * Metoda zwraca kolekcję elementów typu Car, które posiadają
     * przebieg o wartości większej niż wartość podana jako argument
     * metody.
     **/

    public List<Car> getWithMileageGreaterThan() {
        Long mileage = UserDataService.getLong("Podaj przebieg:");
        return cars
                .stream()
                .filter(o -> o.getMileage() > mileage)
                .collect(Collectors.toList());
    }

    /**
     * Metoda zwraca mapę, której kluczem jest kolor, natomiast
     * wartością ilość samochodów, które posiadają taki kolor. Mapa
     * powinna być posortowana malejąco po wartościach.
     **/

    public Map<Color, Long> howManyCarsWithColor() {

        //var colors = cars.stream().map(car -> car.getColor()).collect(Collectors.toList());
        //zamiast listy możemy posłużyć się kolekcją Set:
        EnumSet<Color> colorSet = EnumSet.allOf(Color.class);

        Map<Color, Long> map = new HashMap<>();

        for (Color color : colorSet) {
            map.put(color, cars.stream().filter(car -> car.getColor().equals(color)).count());
        }

        //sortowanie mapy
        LinkedHashMap<Color, Long> linkedHashMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> linkedHashMap.put(x.getKey(), x.getValue()));

        return linkedHashMap;
    }

    @Override
    public String toString() {
        return "Cars: \n"
                + cars;
    }
}
