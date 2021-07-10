package wrzecond.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
@EqualsAndHashCode
public abstract class TjvEntity {

    @Id
    @GeneratedValue
    @Getter private int id;

}
