package fr.remiguitreau.aroeven.barcode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AroEquipment {

    private String type;

    private String size;

    private String identifier;

    private boolean pair;

}
