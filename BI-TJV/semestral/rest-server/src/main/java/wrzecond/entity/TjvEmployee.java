package wrzecond.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TjvEmployee extends TjvEntity {

    @Column(nullable = false)
    @Getter @Setter private String username;

    @Column(nullable = false)
    @Getter @Setter private String password;

    @Column(nullable = false)
    @Getter @Setter private String firstName;

    @Column(nullable = false)
    @Getter @Setter private String lastName;

    @Column(nullable = false)
    @Getter @Setter private Boolean admin;

}
