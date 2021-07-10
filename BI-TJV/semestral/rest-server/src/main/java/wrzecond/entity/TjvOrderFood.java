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
public class TjvOrderFood extends TjvEntity {

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "order_id", nullable = false)
    @Getter @Setter private TjvOrder order;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "food_id", nullable = false)
    @Getter @Setter private TjvFood food;

    @Getter @Setter private Timestamp datetime;

    @Column(nullable = false)
    @Getter @Setter private int count;

}
