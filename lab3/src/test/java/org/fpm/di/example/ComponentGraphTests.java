package org.fpm.di.example;

import org.fpm.di.ComponentGraph;
import org.fpm.di.ComponentGraphVertex;
import org.fpm.di.ComponentImplementation;
import static org.junit.Assert.*;
import org.junit.Test;

public class ComponentGraphTests {
    @SuppressWarnings({"ConstantValue", "SimplifiableAssertion", "EqualsWithItself"})
    @Test
    public void equalsTest() {
        var vertices1 = new ComponentGraphVertex<?>[] {
            new ComponentGraphVertex<>(Object.class, new ComponentImplementation<>(Object.class)),
            new ComponentGraphVertex<>(Object.class, new ComponentImplementation<>(Integer.class)),
        };

        var vertices2 = new ComponentGraphVertex<?>[] {
            new ComponentGraphVertex<>(Object.class, new ComponentImplementation<>(Object.class)),
            new ComponentGraphVertex<>(Object.class, new ComponentImplementation<>(Double.class)),
        };

        var graph1 = new ComponentGraph(vertices1);
        var graphCopy1 = new ComponentGraph(vertices1);
        var graph2 = new ComponentGraph(vertices2);

        assertFalse(graph1.equals(null));
        assertTrue(graph1.equals(graph1));
        assertTrue(graph1.equals(graphCopy1));
        assertFalse(graph1.equals(graph2));
    }

    @Test
    public void hashCodeTest() {
        var vertices1 = new ComponentGraphVertex<?>[] {
            new ComponentGraphVertex<>(Object.class, new ComponentImplementation<>(Object.class)),
            new ComponentGraphVertex<>(Object.class, new ComponentImplementation<>(Integer.class)),
        };

        var graph1 = new ComponentGraph(vertices1);
        var graph2 = new ComponentGraph(vertices1);

        int hash1 = graph1.hashCode();
        int hash2 = graph2.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    public void toStringTest() {
        var vertices1 = new ComponentGraphVertex<?>[] {
            new ComponentGraphVertex<>(Object.class, new ComponentImplementation<>(Double.class)),
            new ComponentGraphVertex<>(
                Integer.class,
                new ComponentImplementation<>(Integer.class, 1)
            ),
        };

        var graph1 = new ComponentGraph(vertices1);
        var expectedResult = "ComponentGraph [{Object -> Double}, {Integer -> Integer (instance=1)}]";
        String actualResult = graph1.toString();

        assertEquals(expectedResult, actualResult);
    }
}
