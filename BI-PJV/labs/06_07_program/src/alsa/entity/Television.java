package alsa.entity;

import java.io.Serializable;

public class Television extends Product implements Serializable {

    public Television(int id, String name, double price, int count) {
        super(id, name, price, count);
    }

    @Override
    public Television increaseCount() {
        return new Television(id, name, price, count + 1);
    }

    @Override
    public Television decreaseCount() {
        return new Television(id, name, price, count - 1);
    }

    @Override
    public boolean hasSpecialGuarantee() {
        return false;
    }

    @Override
    public String toString() {
        return "Television{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
