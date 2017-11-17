import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArraysStreamLambdaTest {

    @Test
    public void testBasicArraysSteam() {
        Integer[] array = new Integer[] { 1, 2, 3, 4, 5 };

        double sum = Arrays.stream(array).mapToInt(value -> value).sum(); //typowy foreach dla tablic
        Assert.assertEquals(15, (int)sum);
    }

    @Test
    public void testCollector() {
        Integer[] array = new Integer[] { 1, 2, 3, 4, 5 };

        List<Integer> multipliedBySelf = Arrays.stream(array).map(i -> (i * i)).collect(Collectors.toList());

        System.out.println(Arrays.toString(multipliedBySelf.toArray(new Integer[multipliedBySelf.size()])));
    }

    private interface Functional<X, Y, Z> {
        double test(X x, Y y, Z z);
    }

    @Test
    public void testBasicFunction() {
        Function<Integer, Integer> powerFunction = integer -> integer*integer;
        Predicate<Integer> checkIf5 = x -> x == 5; //do sprawdzania wartosci

        Functional<Integer, Float, Double> functional = (x, y, z) -> x + y * z;


        Integer base = 5;

        Assert.assertEquals(15, (int)functional.test(base, 2f, base.doubleValue()));

        Assert.assertTrue(checkIf5.test(base));
        Assert.assertEquals(25, powerFunction.apply(base).intValue());
    }

    @Test
    public void testCollectorWithPredicate() {
        Integer[] array = new Integer[] {1, 2, 3, 4, 5 };

        List<Integer> advanced = Arrays.stream(array).filter(i-> i > 3).map(i->i*5).collect(Collectors.toList());

        System.out.println(Arrays.toString(advanced.toArray(new Integer[advanced.size()])));
    }

}
