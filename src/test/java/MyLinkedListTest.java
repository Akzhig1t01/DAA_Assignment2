import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {
    private MyLinkedList list;

    @BeforeEach
    void setUp() {
        list = new MyLinkedList();
    }

    @Test
    void testHeadAndTailOperations() {
        list.add(10, null);
        list.add(20, null);

        assertEquals(2, list.size());
        assertEquals(10, list.get(0, null));
        assertEquals(20, list.get(1, null));
    }

    @Test
    void testInsertAtBoundaries() {
        list.add(0, 10, null); // Insert Head
        list.add(1, 30, null); // Insert Tail
        list.add(1, 20, null); // Insert Middle

        assertEquals(10, list.get(0, null));
        assertEquals(20, list.get(1, null));
        assertEquals(30, list.get(2, null));
    }

    @Test
    void testRemoveBoundaries() {
        list.add(10, null);
        list.add(20, null);
        list.add(30, null);

        assertEquals(10, list.remove(0, null)); // Remove Head
        assertEquals(30, list.remove(1, null)); // Remove Tail
        assertEquals(1, list.size());
        assertEquals(20, list.get(0, null));
    }

    @Test
    void testSingleElementBehavior() {
        list.add(100, null);
        assertEquals(100, list.get(0, null));
        assertEquals(100, list.remove(0, null));
        assertEquals(0, list.size());
    }

    @Test
    void testInvalidIndexExceptions() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0, null));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5, null));
    }
}