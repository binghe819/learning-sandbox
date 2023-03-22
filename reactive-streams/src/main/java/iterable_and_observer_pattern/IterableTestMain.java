package iterable_and_observer_pattern;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Iterable은
 */
public class IterableTestMain {

    public static void main(String[] args) {
//        Iterable<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
//
//        for (Iterator<Integer> it = list.iterator(); it.hasNext(); ) {
//            Integer data = it.next();
//            System.out.println(data);
//        }

        Iterable<Integer> iter = () ->
            new Iterator<>() {
                final static int MAX = 5;
                int i = 0;

                @Override
                public boolean hasNext() {
                    return i < MAX;
                }

                @Override
                public Integer next() {
                    return ++i;
                }
        };


        for (Iterator<Integer> iterator = iter.iterator(); iterator.hasNext(); ) {
            System.out.println(iterator.next());
        }
    }
}
