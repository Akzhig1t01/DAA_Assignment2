public class MinHeap {
    private int[] heap;
    private int size;

    public MinHeap(int capacity) {
        this.heap = new int[capacity];
        this.size = 0;
    }

    public MinHeap() {
        this(10);
    }

    public int size() { return size; }

    private void ensureCapacity(Metrics metrics) {
        if (size == heap.length) {
            int[] newHeap = new int[heap.length * 2];
            for (int i = 0; i < size; i++) {
                if (metrics != null) metrics.addStep();
                newHeap[i] = heap[i];
                if (metrics != null) metrics.addMove();
            }
            heap = newHeap;
        }
    }

    public void insert(int x, Metrics metrics) {
        ensureCapacity(metrics);
        heap[size] = x;
        if (metrics != null) metrics.addMove();
        bubbleUp(size, metrics);
        size++;
    }

    public int peekMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        return heap[0];
    }

    public int extractMin(Metrics metrics) {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        int min = heap[0];
        if (metrics != null) metrics.addStep();
        heap[0] = heap[size - 1];
        if (metrics != null) metrics.addMove();
        size--;
        if (size > 0) {
            bubbleDown(0, metrics);
        }
        return min;
    }

    private void bubbleUp(int index, Metrics metrics) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (metrics != null) {
                metrics.addStep();
                metrics.addStep();
                metrics.addComparison();
            }
            if (heap[index] < heap[parent]) {
                swap(index, parent, metrics);
                index = parent;
            } else {
                break;
            }
        }
    }

    private void bubbleDown(int index, Metrics metrics) {
        while (index * 2 + 1 < size) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = left;

            if (metrics != null) { metrics.addStep(); metrics.addStep(); metrics.addComparison(); }
            if (right < size) {
                if (metrics != null) metrics.addComparison();
                if (heap[right] < heap[left]) {
                    smallest = right;
                }
            }

            if (metrics != null) { metrics.addStep(); metrics.addStep(); metrics.addComparison(); }
            if (heap[smallest] < heap[index]) {
                swap(index, smallest, metrics);
                index = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j, Metrics metrics) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        if (metrics != null) {
            metrics.addMove();
            metrics.addMove();
            metrics.addMove();
        }
    }
}