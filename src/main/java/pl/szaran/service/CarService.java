package pl.szaran.service;

import lombok.*;
import pl.szaran.converters.CarsJsonConverter;
import pl.szaran.exceptions.MyException;
import pl.szaran.model.Car;
import pl.szaran.model.enums.Color;
import pl.szaran.service.enums.SortType;
import pl.szaran.validation.CarValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    /**
     * Metoda zwraca mapę, której kluczem jest nazwa modelu samochodu,
     * natomiast wartością obiekt klasy Car, który reprezentuje
     * najdroższy samochód o tej nazwie modelu. Mapa powinna być
     * posortowana kluczami malejąco.
     */

    public Map<String, Car> getModelWithBiggestPrice() {

        Map<String, Car> map = new HashMap<>();

        var models = cars
                .stream()
                .map(Car::getModel)
                .collect(Collectors.toSet());

        for (String model : models
        ) {
            /**
             * Tutaj tworze liste samochodów konkretnego modelu, sortując ją po cenie od najwyższej do najniższej.
             * Najdroższy samochód na liście ma index 0.
             */
            var list = cars
                    .stream()
                    .filter(car -> car.getModel().matches(model))
                    .sorted(Comparator.comparing(Car::getPrice).reversed())
                    .collect(Collectors.toList());

            map.put(model, list.get(0));

        }

        return map;
    }

    /**
     * Metoda wypisuje statystykę samochodów w zestawieniu. W
     * statystyce powinny znajdować się wartość średnia, wartość
     * najmniejsza, wartość największa dla pól opisujących cenę oraz
     * przebieg samochodów.
     */
    public Map<String, BigDecimal> getStatistics() {

        Map<String, BigDecimal> map = new LinkedHashMap<>(); //aby zachować kolejność dodawania par

        // map.put("Min price", cars.stream().min(Comparator.comparing(Car::getPrice)).orElseThrow(NoSuchElementException::new).getPrice()); //można krócej
        map.put("Min price", cars.stream().map(Car::getPrice).reduce(BigDecimal::min).get());
        //map.put("Max price", cars.stream().max(Comparator.comparing(Car::getPrice)).orElseThrow(NoSuchElementException::new).getPrice());
        map.put("Max price", cars.stream().map(Car::getPrice).reduce(BigDecimal::max).get());
        //map.put("Min mileage", BigDecimal.valueOf(cars.stream().min(Comparator.comparing(Car::getMileage)).orElseThrow(NoSuchElementException::new).getMileage()));
        map.put("Min mileage", BigDecimal.valueOf(cars.stream().map(Car::getMileage).reduce(Long::min).get()));
        //map.put("Max mileage", BigDecimal.valueOf(cars.stream().max(Comparator.comparing(Car::getMileage)).orElseThrow(NoSuchElementException::new).getMileage()));
        map.put("Max mileage", BigDecimal.valueOf(cars.stream().map(Car::getMileage).reduce(Long::max).get()));

        BigDecimal priceSum = cars.stream().map(Car::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        map.put("Price sum", priceSum);
        map.put("Average price", priceSum.divide(BigDecimal.valueOf(cars.size()), 2, RoundingMode.CEILING));

        BigDecimal mileageSum = BigDecimal.valueOf(cars.stream().map(Car::getMileage).reduce((a, b) -> a + b).get());
        map.put("Mileage sum", mileageSum);
        map.put("Average milage", mileageSum.divide(BigDecimal.valueOf(cars.size()), 2, RoundingMode.CEILING));

        return map;
    }


    /**
     * Metoda zwraca samochód, którego cena jest największa. W
     * przypadku kiedy więcej niż jeden samochód posiada największą
     * cenę należy zwrócić kolekcję tych samochodów.
     */

    public List<Car> getCarWithBiggestPrice() {

        var biggestPrice = cars.stream().map(Car::getPrice).reduce(BigDecimal::max).get();

        return cars.stream().filter(car -> car.getPrice().equals(biggestPrice)).collect(Collectors.toList());
    }

    /**
     * Metoda zwraca kolekcję samochodów, w której każdy samochód
     * posiada posortowaną alfabetycznie kolekcję komponentów.
     */


    /**
     * Metoda zwraca mapę, której kluczem jest nazwa komponentu,
     * natomiast wartością jest kolekcja samochodów, które posiadają
     * ten komponent. Pary w mapie powinny być posortowane malejąco po
     * ilości elementów w kolekcji reprezentującej wartość pary.
     */

    public Map<String, Set<Car>> getCarsByComponent() {

        Map<String, Set<Car>> map = new LinkedHashMap<>();
        Map<String, Integer> componentOccurrenceMap = new HashMap<>();

        Set<String> components = new HashSet<>();

        //wypełniam kolekcję components komponentami
        cars
                .stream()
                .map(Car::getComponents)
                .forEach(c -> components.addAll(c));

        //mapa pomocnicza:
        for (String str : components
        ) {
            componentOccurrenceMap.put(str, cars
                    .stream()
                    .filter(car -> car.getComponents().contains(str))
                    .collect(Collectors.toSet()).size());
        }

        //sorotwanie malejąco mapy po wartościach:
        var sortedMap =
                componentOccurrenceMap
                        .entrySet()
                        .stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        System.out.println(sortedMap); //do podglądu

        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            map.put(entry.getKey(), cars
                    .stream()
                    .filter(car -> car.getComponents().contains(entry.getKey()))
                    .collect(Collectors.toSet()));
        }

        return map;
    }

    /**
     * Metoda zwraca kolekcję samochodów, których cena znajduje się w
     * przedziale cenowym <a, b>. Wartości a oraz b przekazywane są
     * jako argument metody. Kolekcja powinna być posortowana
     * alfabetycznie według nazw samochodów.
     */

    @Override
    public String toString() {
        return "Cars: \n"
                + cars;
    }
}
