package ru.prokdo.ksSolver.util.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public final class NumberGeneratorTest {

    @Test
    public void testGenerateIntWithinRange() {
        var from = 5;
        var until = 10;
        for (var i = 0; i < 100; i++) {
            int result = NumberGenerator.generateInt(from, until);
            assertTrue(result >= from && result <= until);
        }
    }

    @Test
    public void testGenerateIntReverseRange() {
        var from = 10;
        var until = 5;
        for (var i = 0; i < 100; i++) {
            var result = NumberGenerator.generateInt(from, until);
            assertTrue(result >= until && result <= from);
        }
    }

    @Test
    public void testGenerateIntEqualRange() {
        var from = 5;
        var until = 5;
        for (var i = 0; i < 100; i++) {
            var result = NumberGenerator.generateInt(from, until);
            assertEquals(from, result);
        }
    }

    @Test
    public void testGenerateIntUntil() {
        var until = 10;
        for (var i = 0; i < 100; i++) {
            var result = NumberGenerator.generateInt(until);
            assertTrue(result >= 0 && result <= until);
        }
    }

    @Test
    public void testGenerateIntNegativeUntil() {
        var until = -10;
        for (var i = 0; i < 100; i++) {
            var result = NumberGenerator.generateInt(until);
            assertTrue(result <= 0 && result >= until);
        }
    }

    @Test
    public void testGenerateDoubleWithinRange() {
        var from = 5;
        var until = 10;
        var precision = 2;
        for (var i = 0; i < 100; i++) {
            var result = NumberGenerator.generateDouble(from, until, precision);
            assertTrue(result > from && result < until);
        }
    }

    @Test
    public void testGenerateDoubleReverseRange() {
        var from = 10;
        var until = 5;
        var precision = 2;
        for (var i = 0; i < 100; i++) {
            var result = NumberGenerator.generateDouble(from, until, precision);
            assertTrue(result > until && result < from);
        }
    }

    @Test
    public void testGenerateDoubleEqualRange() {
        var from = 5;
        var until = 5;
        var precision = 2;
        for (var i = 0; i < 100; i++) {
            var result = NumberGenerator.generateDouble(from, until, precision);
            assertEquals(from, result);
        }
    }

    @Test
    public void testGenerateDoubleUntil() {
        var until = 10;
        var precision = 2;
        for (var i = 0; i < 100; i++) {
            var result = NumberGenerator.generateDouble(until, precision);
            assertTrue(result >= 0 && result < until + 1);
        }
    }

    @Test
    public void testGenerateDoubleNegativeUntil() {
        var until = -10;
        var precision = 2;
        for (var i = 0; i < 100; i++) {
            var result = NumberGenerator.generateDouble(until, precision);
            assertTrue(result <= 0 && result > until);
        }
    }

    @Test
    public void testGenerateDoubleInvalidPrecision() {
        var from = 5;
        var until = 10;
        assertThrows(IllegalArgumentException.class, () -> {
            NumberGenerator.generateDouble(from, until, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            NumberGenerator.generateDouble(from, until, -1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            NumberGenerator.generateDouble(10, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            NumberGenerator.generateDouble(10, -1);
        });
    }
}
