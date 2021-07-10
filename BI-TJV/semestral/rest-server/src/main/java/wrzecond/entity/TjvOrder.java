package wrzecond.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TjvOrder extends TjvEntity {

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="table_id", nullable=false)
    @Getter @Setter private TjvTable table;

    @Column(nullable = false)
    @Getter @Setter private Timestamp datetime;

    @Column(nullable = false)
    @Getter @Setter private boolean completed;

}
