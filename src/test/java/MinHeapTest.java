import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {
    private MinHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MinHeap();
    }

    @Test
    void testInsertAndPeek() {
        heap.insert(15, null);
        heap.insert(5, null);
        heap.insert(20, null);

        assertEquals(5, heap.peekMin());
    }

    @Test
    void testSortedExtractMin() {
        // Требование: n вызовов extractMin должны возвращать отсортированный массив
        int[] input = {40, 10, 30, 5, 12, 1};
        for (int x : input) {
            heap.insert(x, null);
        }

        int[] expected = {1, 5, 10, 12, 30, 40};
        for (int exp : expected) {
            assertEquals(exp, heap.extractMin(null));
        }
        assertEquals(0, heap.size());
    }

    @Test
    void testDuplicatesInHeap() {
        heap.insert(10, null);
        heap.insert(10, null);
        heap.insert(5, null);

        assertEquals(5, heap.extractMin(null));
        assertEquals(10, heap.extractMin(null));
        assertEquals(10, heap.extractMin(null));
    }

    @Test
    void testEmptyHeapExceptions() {
        assertThrows(IllegalStateException.class, () -> heap.peekMin());
        assertThrows(IllegalStateException.class, () -> heap.extractMin(null));
    }
}