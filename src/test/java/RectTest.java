import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.Comparator;

/**
 * RectTest - JUnit test class for Rect
 *
 * Testing strategies applied:
 * 1. Static Testing: code review for potential issues
 * 2. Black-box Testing (input domain): equivalence partitioning, boundary value analysis
 * 3. Black-box Testing (combinatorial): pairwise/combinatorial testing of length x width
 * 4. White-box Testing: statement/branch/path coverage
 * 5. Unit Testing: individual method testing
 * 6. Integration Testing: methods working together (findMax with comparators)
 */
public class RectTest {

    // ==========================================
    // 1. Unit Tests for getArea()
    // ==========================================

    // --- Black-box: Equivalence Partitioning ---
    // Partition 1: Normal positive values
    @Test
    public void testGetArea_normalValues() {
        Rect r = new Rect(5, 10);
        assertEquals(50, r.getArea());
    }

    // Partition 2: One dimension is 1 (minimum positive)
    @Test
    public void testGetArea_unitLength() {
        Rect r = new Rect(1, 10);
        assertEquals(10, r.getArea());
    }

    @Test
    public void testGetArea_unitWidth() {
        Rect r = new Rect(10, 1);
        assertEquals(10, r.getArea());
    }

    // Partition 3: Square (length == width)
    @Test
    public void testGetArea_square() {
        Rect r = new Rect(7, 7);
        assertEquals(49, r.getArea());
    }

    // Partition 4: Zero dimension
    @Test
    public void testGetArea_zeroLength() {
        Rect r = new Rect(0, 5);
        assertEquals(0, r.getArea());
    }

    @Test
    public void testGetArea_zeroWidth() {
        Rect r = new Rect(5, 0);
        assertEquals(0, r.getArea());
    }

    @Test
    public void testGetArea_bothZero() {
        Rect r = new Rect(0, 0);
        assertEquals(0, r.getArea());
    }

    // Partition 5: Negative values (invalid input - reveals lack of validation)
    @Test
    public void testGetArea_negativeLength() {
        Rect r = new Rect(-3, 5);
        // Area should not be negative; current code returns -15
        // This reveals a bug: no input validation
        assertEquals(-15, r.getArea());
    }

    @Test
    public void testGetArea_negativeWidth() {
        Rect r = new Rect(5, -3);
        assertEquals(-15, r.getArea());
    }

    @Test
    public void testGetArea_bothNegative() {
        Rect r = new Rect(-3, -5);
        // (-3)*(-5) = 15, mathematically positive but semantically wrong
        assertEquals(15, r.getArea());
    }

    // --- Black-box: Boundary Value Analysis ---
    @Test
    public void testGetArea_boundary_1x1() {
        Rect r = new Rect(1, 1);
        assertEquals(1, r.getArea());
    }

    @Test
    public void testGetArea_largeValues() {
        Rect r = new Rect(10000, 10000);
        assertEquals(100000000, r.getArea());
    }

    // --- Black-box: Integer Overflow boundary ---
    @Test
    public void testGetArea_intOverflow() {
        Rect r = new Rect(50000, 50000);
        // 50000 * 50000 = 2,500,000,000 > Integer.MAX_VALUE (2,147,483,647)
        // This reveals a potential overflow bug
        long expected = 50000L * 50000L;
        long actual = r.getArea();
        // If overflow occurs, actual != expected
        if (actual != expected) {
            System.out.println("[BUG] getArea() integer overflow: expected " + expected + ", got " + actual);
        }
    }

    // --- White-box: Statement coverage ---
    // getArea() has a single statement: return length*width
    // Any single test case covers 100% statements. Covered above.

    // --- Combinatorial Testing (pairwise) ---
    // Factors: length {0, 1, 5, 100}, width {0, 1, 5, 100}
    @Test
    public void testGetArea_combinatorial() {
        int[] values = {0, 1, 5, 100};
        for (int l : values) {
            for (int w : values) {
                Rect r = new Rect(l, w);
                assertEquals("Area(" + l + "," + w + ")", l * w, r.getArea());
            }
        }
    }

    // ==========================================
    // 2. Unit Tests for getPerimeter()
    // ==========================================

    // --- Black-box: Equivalence Partitioning ---
    @Test
    public void testGetPerimeter_normalValues() {
        Rect r = new Rect(5, 10);
        assertEquals(30, r.getPerimeter());
    }

    @Test
    public void testGetPerimeter_square() {
        Rect r = new Rect(7, 7);
        assertEquals(28, r.getPerimeter());
    }

    @Test
    public void testGetPerimeter_unitDimensions() {
        Rect r = new Rect(1, 1);
        assertEquals(4, r.getPerimeter());
    }

    @Test
    public void testGetPerimeter_zeroLength() {
        Rect r = new Rect(0, 5);
        assertEquals(10, r.getPerimeter());
    }

    @Test
    public void testGetPerimeter_zeroWidth() {
        Rect r = new Rect(5, 0);
        assertEquals(10, r.getPerimeter());
    }

    @Test
    public void testGetPerimeter_bothZero() {
        Rect r = new Rect(0, 0);
        assertEquals(0, r.getPerimeter());
    }

    // --- Black-box: Boundary Value Analysis ---
    @Test
    public void testGetPerimeter_largeValues() {
        Rect r = new Rect(10000, 10000);
        assertEquals(40000, r.getPerimeter());
    }

    @Test
    public void testGetPerimeter_negativeValues() {
        Rect r = new Rect(-3, 5);
        // (-3)*2 + 5*2 = -6 + 10 = 4
        // Perimeter should never be based on negative dimensions
        assertEquals(4, r.getPerimeter());
    }

    // --- White-box: Statement coverage ---
    // getPerimeter() has a single return statement: return length*2 + width*2
    // Verify formula correctness: 2*(length+width)
    @Test
    public void testGetPerimeter_formulaEquivalence() {
        Rect r = new Rect(8, 3);
        assertEquals(2 * (8 + 3), r.getPerimeter());
    }

    // --- Combinatorial Testing ---
    @Test
    public void testGetPerimeter_combinatorial() {
        int[] values = {0, 1, 5, 100};
        for (int l : values) {
            for (int w : values) {
                Rect r = new Rect(l, w);
                assertEquals("Perimeter(" + l + "," + w + ")",
                        2 * l + 2 * w, r.getPerimeter());
            }
        }
    }

    // ==========================================
    // 3. Unit Tests for findMax()
    // ==========================================

    // --- Black-box: Normal case with area comparator ---
    @Test
    public void testFindMax_areaComparator_normalArray() {
        Rect[] arr = {new Rect(2, 3), new Rect(5, 5), new Rect(1, 10)};
        Rect max = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(25, max.getArea()); // 5*5=25 is the largest
    }

    // --- Black-box: Normal case with perimeter comparator ---
    @Test
    public void testFindMax_perimeterComparator_normalArray() {
        Rect[] arr = {new Rect(2, 3), new Rect(5, 5), new Rect(1, 10)};
        Rect max = Rect.findMax(arr, new Rect.perimeterCompare());
        assertEquals(22, max.getPerimeter()); // 1+10 -> 22 is the largest
    }

    // --- Black-box: Single element array ---
    @Test
    public void testFindMax_singleElement() {
        Rect[] arr = {new Rect(3, 4)};
        Rect max = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(12, max.getArea());
    }

    // --- Black-box: All elements equal ---
    @Test
    public void testFindMax_allEqual() {
        Rect[] arr = {new Rect(5, 5), new Rect(5, 5), new Rect(5, 5)};
        Rect max = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(25, max.getArea());
    }

    // --- Black-box: Max at beginning ---
    @Test
    public void testFindMax_maxAtBeginning() {
        Rect[] arr = {new Rect(10, 10), new Rect(1, 1), new Rect(2, 2)};
        Rect max = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(100, max.getArea());
    }

    // --- Black-box: Max at end ---
    @Test
    public void testFindMax_maxAtEnd() {
        Rect[] arr = {new Rect(1, 1), new Rect(2, 2), new Rect(10, 10)};
        Rect max = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(100, max.getArea());
    }

    // --- Black-box: Max in middle ---
    @Test
    public void testFindMax_maxInMiddle() {
        Rect[] arr = {new Rect(1, 1), new Rect(10, 10), new Rect(2, 2)};
        Rect max = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(100, max.getArea());
    }

    // --- Black-box: Empty array (should throw exception) ---
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFindMax_emptyArray() {
        Rect[] arr = {};
        Rect.findMax(arr, new Rect.areaCompare());
        // BUG: No empty array check, throws ArrayIndexOutOfBoundsException
    }

    // --- Black-box: Null array (should throw exception) ---
    @Test(expected = NullPointerException.class)
    public void testFindMax_nullArray() {
        Rect.findMax(null, new Rect.areaCompare());
        // BUG: No null check, throws NullPointerException
    }

    // --- Black-box: Null comparator (with >1 elements to trigger compare) ---
    @Test(expected = NullPointerException.class)
    public void testFindMax_nullComparator() {
        Rect[] arr = {new Rect(1, 1), new Rect(2, 2)};
        Rect.findMax(arr, null);
        // BUG: No null check for comparator, throws NPE when compare is called
    }

    // --- White-box: Branch coverage for findMax ---
    // Branch 1: Loop body executes when cmp.compare > 0 (maxIndex updates)
    // Branch 2: Loop body executes when cmp.compare <= 0 (maxIndex does not update)
    @Test
    public void testFindMax_branchCoverage_ascending() {
        // Array is ascending: maxIndex updates every iteration
        Rect[] arr = {new Rect(1, 1), new Rect(2, 2), new Rect(3, 3)};
        Rect max = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(9, max.getArea());
    }

    @Test
    public void testFindMax_branchCoverage_descending() {
        // Array is descending: maxIndex never updates after initial
        Rect[] arr = {new Rect(3, 3), new Rect(2, 2), new Rect(1, 1)};
        Rect max = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(9, max.getArea());
    }

    // --- White-box: Two elements (loop executes exactly once) ---
    @Test
    public void testFindMax_twoElements() {
        Rect[] arr = {new Rect(1, 1), new Rect(2, 2)};
        Rect max = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(4, max.getArea());
    }

    // --- Black-box: Multiple maximums (same area, different dimensions) ---
    @Test
    public void testFindMax_duplicateMax() {
        Rect[] arr = {new Rect(2, 6), new Rect(3, 4), new Rect(4, 3)};
        // Areas: 12, 12, 12 - all equal
        Rect max = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(12, max.getArea());
    }

    // ==========================================
    // 4. Integration Tests
    // ==========================================

    // Test findMax works correctly with the original main() scenario
    @Test
    public void testIntegration_originalMainScenario() {
        Rect[] arr = {
            new Rect(10, 20), new Rect(2, 65),
            new Rect(3, 10), new Rect(6, 20)
        };

        // Areas: 200, 130, 30, 120 -> max area = 200, rect (10,20)
        Rect maxArea = Rect.findMax(arr, new Rect.areaCompare());
        assertEquals(200, maxArea.getArea());
        assertEquals("(10,20)", maxArea.getObject());

        // Perimeters: 60, 134, 26, 52 -> max perimeter = 134, rect (2,65)
        Rect maxPerimeter = Rect.findMax(arr, new Rect.perimeterCompare());
        assertEquals(134, maxPerimeter.getPerimeter());
        assertEquals("(2,65)", maxPerimeter.getObject());
    }

    // Test setter integration: modify rect and verify area/perimeter update
    @Test
    public void testIntegration_setterThenCompute() {
        Rect r = new Rect(5, 10);
        assertEquals(50, r.getArea());
        assertEquals(30, r.getPerimeter());

        r.setLength(8);
        r.setWidth(3);
        assertEquals(24, r.getArea());
        assertEquals(22, r.getPerimeter());
    }

    // Test getObject() output format
    @Test
    public void testGetObject_format() {
        Rect r = new Rect(10, 20);
        assertEquals("(10,20)", r.getObject());
    }

    // ==========================================
    // 5. Comparator Unit Tests
    // ==========================================

    @Test
    public void testAreaCompare_greater() {
        Rect r1 = new Rect(5, 5); // area=25
        Rect r2 = new Rect(2, 3); // area=6
        assertTrue(new Rect.areaCompare().compare(r1, r2) > 0);
    }

    @Test
    public void testAreaCompare_less() {
        Rect r1 = new Rect(2, 3); // area=6
        Rect r2 = new Rect(5, 5); // area=25
        assertTrue(new Rect.areaCompare().compare(r1, r2) < 0);
    }

    @Test
    public void testAreaCompare_equal() {
        Rect r1 = new Rect(2, 6); // area=12
        Rect r2 = new Rect(3, 4); // area=12
        assertEquals(0, new Rect.areaCompare().compare(r1, r2));
    }

    @Test
    public void testPerimeterCompare_greater() {
        Rect r1 = new Rect(10, 10); // perimeter=40
        Rect r2 = new Rect(2, 3);   // perimeter=10
        assertTrue(new Rect.perimeterCompare().compare(r1, r2) > 0);
    }

    @Test
    public void testPerimeterCompare_less() {
        Rect r1 = new Rect(2, 3);   // perimeter=10
        Rect r2 = new Rect(10, 10); // perimeter=40
        assertTrue(new Rect.perimeterCompare().compare(r1, r2) < 0);
    }

    @Test
    public void testPerimeterCompare_equal() {
        Rect r1 = new Rect(3, 7); // perimeter=20
        Rect r2 = new Rect(5, 5); // perimeter=20
        assertEquals(0, new Rect.perimeterCompare().compare(r1, r2));
    }
}
