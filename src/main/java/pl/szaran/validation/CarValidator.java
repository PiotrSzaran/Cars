package pl.szaran.validation;

import pl.szaran.model.Car;

import java.math.BigDecimal;
import java.util.Map;

public class CarValidator extends AbstractValidator<Car> {

    @Override
    public Map<String, String> validate(Car car) {

        if ( car == null ) {
            errors.put("car", "is null");
        }

        if ( !isModelValid(car) ) {
            errors.put("model", "not valid: " + car.getModel());
        }

        if ( !areComponentsValid(car) ) {
            errors.put("components", "not valid: " + car.getComponents());
        }

        if ( !isMileageValid(car) ) {
            errors.put("mileage", "not valid: " + car.getMileage());
        }

        if ( !isPriceValid(car) ) {
            errors.put("price", "not valid: " + car.getPrice());
        }

        return errors;
    }

    private boolean isModelValid(Car car) {
        return car.getModel() != null && car.getModel().matches("[A-Z\\s]+");
    }

    private boolean areComponentsValid(Car car) {
        return car.getComponents() != null && car.getComponents().stream().allMatch(comp -> comp.matches("[A-Z\\s]+"));
    }

    private boolean isMileageValid(Car car) {
        return car.getMileage() != null && car.getMileage() >= 0;
    }

    private boolean isPriceValid(Car car) {
        return car.getPrice() != null && car.getPrice().compareTo(BigDecimal.ZERO) > -1; //metoda compareTo zwraca -1 kiedy vartosc w () jest większa, 0 - kiedy sa rowne i 1 kiedy wartosc w () jest mniejsza
    }
}
