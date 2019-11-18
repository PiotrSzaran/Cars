package pl.szaran.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.szaran.model.enums.Color;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    private String model;
    private Color color;
    private BigDecimal price;
    private Long mileage;
    private Set<String> components;

    @Override
    public String toString() {
        return
                "\nmodel:'" + model + '\'' +
                ", \n color:" + color +
                ", \n  price:" + price +
                ", \n   mileage:" + mileage +
                ", \n    components:" + components;
    }
}
