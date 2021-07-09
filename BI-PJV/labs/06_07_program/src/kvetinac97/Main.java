package kvetinac97;

import alsa.entity.Product;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static alsa.SampleData.*;

public class Main {

    public static void main ( String[] args ) {

        /*
         * Chyby:
         *  - Error: neošetřitelné, chyba systému (OutOfMemory) -> ukončit program
         *  - RuntimeException: chyba programátora (IndexOutOfBounds)
         *  - Exception: klasická chyba, cílené (IOException...)
         */

        InputStream inputStream; // byte stream
        Reader reader; // character stream

        FileInputStream fileInputStream; // rozšiřují binární proudy, práce se soubory
        FileReader fileReader; // rozšiřují znakové proudy, práce se soubory

        // -- LAMBDY

        Function<Integer, Integer> fun = a -> a + 5;
        Function<Integer, Integer> doubleFun = fun.compose(fun);

        // Input, Output
        System.out.println(doubleFun.apply(10));

        // Vícenásobná funkce
        Function<Integer, Function<Double, String>> sumStr = a -> b -> Double.toString(a + 2 * b);
        System.out.println(sumStr.apply(3).apply(1.5));

        // Stream
        List<Product> products = new ArrayList<>(Arrays.asList(lenovoE500, hpBusinnesPlus, samsungMediaPlus));

        // forEach
        products.stream().forEach(product -> {System.out.println(product);});
        products.forEach(System.out::println);

        // filter
        products.stream().filter(product -> product.price() > 5000).forEach(System.out::println);

        // map
        products.stream().map(product -> product.increaseCount()).forEach(System.out::println);
        products.stream().map(Product::increaseCount).forEach(System.out::println);

        // convert to Map<String, Product>
        products.stream().collect(Collectors.toMap(Product::name, /*product -> product*/Function.identity())).forEach((name, product) -> System.out.println("Name: " + name + ", prod: " + product));

        // sorting
        products.stream().sorted(Comparator.comparing(Product::name)).forEach(System.out::println);

        // reduce - sečtení cen např.
        System.out.println(products.stream().map(Product::price).reduce(0.0, Double::sum));

        // StringJoiner - jednoduché převedení do [ xxx, yyy ]
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        products.stream().map(Product::name).forEach(joiner::add);
        System.out.println(joiner.toString());

    }

}