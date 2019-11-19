package pl.szaran.service;

import pl.szaran.exceptions.MyException;
import pl.szaran.model.Car;

public class MenuService {

     private final CarService carService;

    public MenuService(CarService carService) {
        this.carService = carService;
    }

    private void showMenu() {
        System.out.println("\n");
        System.out.println("1. Sortowanie kolekcji samochodów według wybranego kryterium");
        System.out.println("2.");
        System.out.println("3.");
        System.out.println("4.");
        System.out.println("5.");
        System.out.println("6.");
        System.out.println("7.");
        System.out.println("8.");
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
                        showSortedCars();
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:

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

    public void showSortedCars() {
        var list = carService.sortBy();

        for (Car c: list
             ) {
            System.out.println(c);
        }
    }

}
