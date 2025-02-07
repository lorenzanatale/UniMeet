package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestingClass {
    @Test
    public void testSomma() {  // Ho corretto il nome del metodo
        String mario = "mario";
        assertEquals("pallemario", "palle" + mario); // Corretto l'assert
    }
}

