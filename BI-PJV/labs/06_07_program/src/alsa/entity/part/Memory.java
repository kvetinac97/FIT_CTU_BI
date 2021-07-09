package alsa.entity.part;

import java.io.Serializable;
import java.util.Objects;

public class Memory extends ComputerPart implements Serializable {

    private final int capacity;

    public Memory(int capacity) {
        this.capacity = capacity;
    }

    public int capacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Memory memory = (Memory) o;
        return capacity == memory.capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(capacity);
    }

    @Override
    public String toString() {
        return "Memory{" +
                "capacity=" + capacity +
                '}';
    }
}
