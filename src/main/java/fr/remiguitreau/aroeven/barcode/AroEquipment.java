package fr.remiguitreau.aroeven.barcode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AroEquipment {

    private String brand;

    private String size;

    private String number;

    private boolean pair;

}
