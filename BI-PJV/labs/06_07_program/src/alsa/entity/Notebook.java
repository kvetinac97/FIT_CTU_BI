package alsa.entity;

import alsa.entity.part.ComputerPart;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Notebook extends Product implements Serializable {

    private final NotebookCategory category;

    private final ComputerPart[] computerParts;

    public Notebook(int id, String name, double price, int count, NotebookCategory category, ComputerPart[] computerParts) {
        super(id, name, price, count);
        this.category = category;
        this.computerParts = computerParts;
    }

    public NotebookCategory category() {
        return category;
    }

    public ComputerPart[] computerParts() {
        return computerParts;
    }

    @Override
    public Notebook increaseCount() {
        return new Notebook(id, name, price, count + 1, category, computerParts);
    }

    @Override
    public Notebook decreaseCount() {
        return new Notebook(id, name, price, count - 1, category, computerParts);
    }

    @Override
    public boolean hasSpecialGuarantee() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Notebook notebook = (Notebook) o;
        return category == notebook.category &&
                Arrays.equals(computerParts, notebook.computerParts);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), category);
        result = 31 * result + Arrays.hashCode(computerParts);
        return result;
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "category=" + category +
                ", computerParts=" + Arrays.toString(computerParts) +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
