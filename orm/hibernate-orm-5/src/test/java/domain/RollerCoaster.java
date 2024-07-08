package domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roller_coaster")
public class RollerCoaster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ZIP_CODE", precision = 1)
    private String zipCode;

    @Column(name = "MAX_SUPPORTED_WEIGHT")
    private Integer maxWeight;

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setMaxWeight(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String toString() {
        return "roller_coaster[id="+this.id+", maxWeight="+this.maxWeight+",zipCode="+this.zipCode+"]";
    }
}
