package wrzecond.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TjvFood extends TjvEntity {

    @Column(nullable = false)
    @Getter @Setter private String name;

    @Column(nullable = false)
    @Getter @Setter private int price;

    @Column(nullable = false)
    @Getter @Setter private boolean cooked;

    @ManyToMany
    @JoinTable(name = "TJV_FOOD_ALLERGEN", joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id"))
    @Getter @Setter private List<TjvAllergen> allergens;

}
