package pl.szaran.service;

import pl.szaran.exceptions.MyException;
import pl.szaran.model.Car;
import pl.szaran.model.enums.Color;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuService {

    private final CarService carService;

    public MenuService(CarService carService) {
        this.carService = carService;
    }

    private void showMenu() {
        System.out.println("\n");
        System.out.println("1. Sortowanie kolekcji samochodów według wybranego kryterium");
        System.out.println("2. Pokaż samochody o większym niż podany przebiegu");
        System.out.println("3. Pokaż ile samochodów w kolekcji jest danego koloru");
        System.out.println("4. Pokaż najdroższy model samochodu");
        System.out.println("5. Statystyka cen i przebiegu");
        System.out.println("6. Wyświetl najdroższy samochód");
        System.out.println("7.");
        System.out.println("8. Pokaż samochody zawierające konkretny komponent");
        System.out.println("9. Wyświetl samochody z podanego przedziału cenowego");
        System.out.println("99. WYJSCIE");
    }

    public void mainMenu() {
        boolean quit = false;
        int option;
        try {
            do {
                showMenu();
                option = UserDataService.getInt("Wybierz opcję:");

                switch (option) {
                    case 1:
                        showList(carService.sortBy());
                        break;
                    case 2:
                        showList(carService.getWithMileageGreaterThan());
                        break;
                    case 3:
                        showHowManyCarsWithColor();
                        break;
                    case 4:
                        showModelWithBiggestPrice();
                        break;
                    case 5:
                        showStatistics();
                        break;
                    case 6:
                        showCarWithBiggestPrice();
                        break;
                    case 7:

                        break;
                    case 8:
                        showCarsByComponent();
                        break;
                    case 9:
                        showList(carService.getCarsWithPriceBetween());
                        break;
                    case 99:
                        quit = true;
                        break;
                    default:
                        System.out.println("\n NIEPOPRAWNY WYBÓR \n");
                }
            }
            while (!quit);
            System.out.println("DO ZOBACZENIA!");
        } catch (MyException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    private void showList(List<Car> list) {
        if (list.equals(null)) {
            throw new MyException("CAR COLLECTION ERROR");
        } else {
            list.forEach(car -> System.out.println(car));
        }
    }

    private void showHowManyCarsWithColor() {
        Map<Color, Long> map = carService.howManyCarsWithColor();
        for (Map.Entry<Color, Long> entry : map.entrySet()) {
            System.out.println(entry.getKey().toString() + " - " + entry.getValue());
        }
    }

    private void showModelWithBiggestPrice() {
        Map<String, Car> map = carService.getModelWithBiggestPrice();
        for (Map.Entry<String, Car> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
        }
    }

    private void showStatistics() {
        var map = carService.getStatistics();
        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void showCarWithBiggestPrice() {

        var list = carService.getCarWithBiggestPrice();

        if (list.size() == 1) {
            System.out.println("kolekcja posiada jeden samochód o największej cenie:");
            System.out.println(list.get(0));
        } else {
            System.out.println("liczba samochodów o największej cenie: " + list.size());
            list.stream().forEach(car -> System.out.println(car));
        }
    }

    private void showCarsByComponent() {
        var map = carService.getCarsByComponent();
        for (Map.Entry<String, Set<Car>> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": \n" + entry.getValue() + "\n");
        }
    }
}
