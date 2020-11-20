package com.security.demo.predicate;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;



public class PredicateTest {


    @Test
    public void whenFilterList_thenSuccess(){
        List<String> names = Arrays.asList("Adam", "Alexander", "John", "Tom");
//        List<String> result = names.stream()
//                .filter(name -> name.startsWith("A") && name.length()>5 )
//                .collect(Collectors.toList());
//        Predicate<String> predicate1 = str -> str.startsWith("A");
//        Predicate<String> predicate2 =  str -> str.length() < 5;
        Predicate<String> predicate1 = str->str.startsWith("A");
        Predicate<String> predicate2 = str->str.length()<5;

        /**
         * predicate2.negate()) 取反
         */

        List<String> result = names.stream()
                .filter(predicate1.and(predicate2.negate()))
                .collect(Collectors.toList());
        System.out.println(result);
//        assertEquals(2, result.size());
//        assertThat(result,);
    }
}
