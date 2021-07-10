package wrzecond.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import lombok.*;
import wrzecond.table.TjvTableType;

@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TjvTable extends TjvEntity {

    @Column(nullable = false)
    @Getter @Setter private TjvTableType type;

}