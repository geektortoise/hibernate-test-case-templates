package domain;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SqlFragmentAlias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "people")
public class People implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PPL_WEIGHT")
    private Integer weight;

    @Column(name = "PPL_ZIP_CODE", precision = 1)
    private String zipCode;

    @OneToMany
    @JoinColumns({
            @JoinColumn(name = "ZIP_CODE", referencedColumnName = "PPL_ZIP_CODE", insertable = false, updatable = false, nullable = false)
    })
    @Filter(name = "onlyUnsafe")
    @Filter(name = "onlyAuthorized",
            condition="{rc}.MAX_SUPPORTED_WEIGHT > {ppl}.PPL_WEIGHT",
            deduceAliasInjectionPoints = false,
            aliases = {
                    @SqlFragmentAlias(alias = "ppl",table = "people"),
                    @SqlFragmentAlias(alias = "rc",table = "roller_coaster")
            })
    private Set<RollerCoaster> authorizedRollerCoasters = new LinkedHashSet<>();


// Constructors, Getters, Setters, hashCode and so on...

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setAuthorizedRollerCoasters(Set<RollerCoaster> authorizedRollerCoasters) {
        this.authorizedRollerCoasters = authorizedRollerCoasters;
    }

    public Set<RollerCoaster> getAuthorizedRollerCoasters() {
        return authorizedRollerCoasters;
    }

    public String toString() {
        return "people[id="+this.id+", weight="+this.weight+",zipCode="+this.zipCode+"]";
    }
}

