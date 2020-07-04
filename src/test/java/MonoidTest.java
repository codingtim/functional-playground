import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class MonoidTest {

    @Test
    void mapReduceWithMonoid_sum() {
        List<Money> money = List.of(new Money(10), new Money(20), new Money(30));

        Money total = mapReduce(money, new MoneySum());
        Assertions.assertEquals(new Money(60), total);
    }

    @Test
    void mapReduceWithMonoid_highest() {
        List<Money> money = List.of(new Money(10), new Money(20), new Money(30));

        Money total = mapReduce(money, new HighestMoney());
        Assertions.assertEquals(new Money(30), total);
    }

    @Test
    void streamReduce_sum() {
        List<Money> money = List.of(new Money(10), new Money(20), new Money(30));

        Money total = money.stream().reduce(new Money(0), (money1, money2) -> new Money(money1.amount + money2.amount));
        Assertions.assertEquals(new Money(60), total);
    }

    @Test
    void streamReduce_highest() {
        List<Money> money = List.of(new Money(10), new Money(20), new Money(30));

        Money total = money.stream().reduce(new Money(0), (money1, money2) -> money1.amount > money2.amount ? money1 : money2);
        Assertions.assertEquals(new Money(30), total);
    }

    interface Monoid<TYPE> {
        TYPE zero();

        TYPE operation(TYPE first, TYPE second);
    }

    private <TYPE> TYPE mapReduce(List<TYPE> typeList, Monoid<TYPE> monoid) {
        return typeList.stream()
                .reduce(monoid.zero(), monoid::operation);
    }

    static class Money {
        private int amount;

        public Money(int amount) {
            this.amount = amount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Money money = (Money) o;
            return amount == money.amount;
        }

        @Override
        public int hashCode() {
            return Objects.hash(amount);
        }
    }

    static class MoneySum implements Monoid<Money> {

        @Override
        public Money zero() {
            return new Money(0);
        }

        @Override
        public Money operation(Money first, Money second) {
            return new Money(first.amount + second.amount);
        }
    }

    static class HighestMoney implements Monoid<Money> {

        @Override
        public Money zero() {
            return new Money(0);
        }

        @Override
        public Money operation(Money first, Money second) {
            return first.amount > second.amount ? first : second;
        }
    }
}
