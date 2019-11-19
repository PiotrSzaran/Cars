package pl.szaran.main;

import pl.szaran.service.CarService;
import pl.szaran.service.MenuService;

public class App {
    public static void main(String[] args) {

        CarService carService = new CarService("cars.json");

        MenuService menuService = new MenuService(carService);

        menuService.mainMenu();

    }
}