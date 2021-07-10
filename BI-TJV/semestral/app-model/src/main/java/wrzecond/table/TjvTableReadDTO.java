package wrzecond.table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import wrzecond.IReadDTO;

@EqualsAndHashCode(callSuper = true)

public class TjvTableReadDTO extends TjvTableDTO implements IReadDTO {
    @Accessors(chain = true)
    @Getter @Setter private boolean empty;

    public TjvTableReadDTO (Integer id, TjvTableType type) {
        super (id, type);
        this.empty = true;
    }

    @Override
    public int getId () {
        return id;
    }

    @Override
    public String getDisplayName() {
        return getType().name();
    }

}
