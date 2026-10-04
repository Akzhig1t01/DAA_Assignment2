public class DynamicArray {
    private int[] data;
    private int size;

    public DynamicArray() {
        this.data = new int[10];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    private void ensureCapacity(Metrics metrics) {
        if (size == data.length) {
            int[] newData = new int[data.length * 2];
            for (int i = 0; i < size; i++) {
                if (metrics != null) metrics.addStep();
                newData[i] = data[i];
                if (metrics != null) metrics.addMove();
            }
            data = newData;
        }
    }

    public void add(int x, Metrics metrics) {
        ensureCapacity(metrics);
        data[size++] = x;
        if (metrics != null) metrics.addMove();
    }

    public void add(int index, int x, Metrics metrics) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        ensureCapacity(metrics);
        for (int i = size; i > index; i--) {
            if (metrics != null) metrics.addStep();
            data[i] = data[i - 1];
            if (metrics != null) metrics.addMove();
        }
        data[index] = x;
        if (metrics != null) metrics.addMove();
        size++;
    }

    public int remove(int index, Metrics metrics) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        int removedValue = data[index];
        if (metrics != null) metrics.addStep();
        for (int i = index; i < size - 1; i++) {
            if (metrics != null) metrics.addStep();
            data[i] = data[i + 1];
            if (metrics != null) metrics.addMove();
        }
        size--;
        return removedValue;
    }

    public int get(int index, Metrics metrics) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        if (metrics != null) metrics.addStep();
        return data[index];
    }

    public boolean contains(int x, Metrics metrics) {
        for (int i = 0; i < size; i++) {
            if (metrics != null) {
                metrics.addStep();
                metrics.addComparison();
            }
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }
}