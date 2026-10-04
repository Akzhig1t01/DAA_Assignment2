public class MyLinkedList {
    private static class Node {
        int value;
        Node prev;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void add(int x, Metrics metrics) {
        Node newNode = new Node(x);
        if (size == 0) {
            head = tail = newNode;
            if (metrics != null) {
                metrics.addMove();
                metrics.addMove();
            }
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            if (metrics != null) {
                metrics.addMove();
                metrics.addMove();
                metrics.addMove();
            }
        }
        size++;
    }

    private Node getNode(int index, Metrics metrics) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        Node curr;
        if (index < size / 2) {
            curr = head;
            if (metrics != null) metrics.addStep();
            for (int i = 0; i < index; i++) {
                curr = curr.next;
                if (metrics != null) metrics.addStep();
            }
        } else {
            curr = tail;
            if (metrics != null) metrics.addStep();
            for (int i = size - 1; i > index; i--) {
                curr = curr.prev;
                if (metrics != null) metrics.addStep();
            }
        }
        return curr;
    }

    public void add(int index, int x, Metrics metrics) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        if (index == size) {
            add(x, metrics);
            return;
        }
        Node newNode = new Node(x);
        if (index == 0) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
            if (metrics != null) {
                metrics.addMove();
                metrics.addMove();
                metrics.addMove();
            }
        } else {
            Node target = getNode(index, metrics);
            Node prevNode = target.prev;
            prevNode.next = newNode;
            newNode.prev = prevNode;
            newNode.next = target;
            target.prev = newNode;
            if (metrics != null) {
                metrics.addMove();
                metrics.addMove();
                metrics.addMove();
                metrics.addMove();
            }
        }
        size++;
    }

    public int remove(int index, Metrics metrics) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        Node target = getNode(index, metrics);
        int val = target.value;

        if (size == 1) {
            head = tail = null;
            if (metrics != null) {
                metrics.addMove();
                metrics.addMove();
            }
        } else if (target == head) {
            head = head.next;
            head.prev = null;
            if (metrics != null) {
                metrics.addMove();
                metrics.addMove();
            }
        } else if (target == tail) {
            tail = tail.prev;
            tail.next = null;
            if (metrics != null) {
                metrics.addMove();
                metrics.addMove();
            }
        } else {
            target.prev.next = target.next;
            target.next.prev = target.prev;
            if (metrics != null) {
                metrics.addMove();
                metrics.addMove();
            }
        }
        size--;
        return val;
    }

    public int get(int index, Metrics metrics) {
        return getNode(index, metrics).value;
    }

    public boolean contains(int x, Metrics metrics) {
        Node curr = head;
        while (curr != null) {
            if (metrics != null) {
                metrics.addStep();
                metrics.addComparison();
            }
            if (curr.value == x) return true;
            curr = curr.next;
        }
        return false;
    }
}