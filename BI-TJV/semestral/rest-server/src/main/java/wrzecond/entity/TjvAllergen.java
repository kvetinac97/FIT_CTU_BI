package wrzecond.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TjvAllergen extends TjvEntity {

    @Column(nullable = false)
    @Getter @Setter private String name;

}
