import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DynamicArrayTest {
    private DynamicArray array;

    @BeforeEach
    void setUp() {
        array = new DynamicArray();
    }

    @Test
    void testAddAndGet() {
        array.add(10, null);
        array.add(20, null);
        assertEquals(2, array.size());
        assertEquals(10, array.get(0, null));
        assertEquals(20, array.get(1, null));
    }

    @Test
    void testAutomaticResizing() {
        // Проверка расширения массива в 2 раза при достижении лимита
        for (int i = 0; i < 25; i++) {
            array.add(i, null);
        }
        assertEquals(25, array.size());
        assertEquals(24, array.get(24, null));
    }

    @Test
    void testAddAtIndex() {
        array.add(10, null);
        array.add(30, null);
        array.add(1, 20, null); // Вставка в середину

        assertEquals(3, array.size());
        assertEquals(10, array.get(0, null));
        assertEquals(20, array.get(1, null));
        assertEquals(30, array.get(2, null));
    }

    @Test
    void testRemove() {
        array.add(10, null);
        array.add(20, null);
        array.add(30, null);

        int removed = array.remove(1, null); // Удаляем из середины
        assertEquals(20, removed);
        assertEquals(2, array.size());
        assertEquals(30, array.get(1, null));
    }

    @Test
    void testContains() {
        array.add(5, null);
        array.add(15, null);

        assertTrue(array.contains(5, null));
        assertTrue(array.contains(15, null));
        assertFalse(array.contains(99, null));
    }

    @Test
    void testInvalidIndexExceptions() {
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(0, null));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0, null));
        assertThrows(IndexOutOfBoundsException.class, () -> array.add(-1, 5, null));
    }
}