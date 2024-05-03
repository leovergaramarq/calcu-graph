package data_structures;

public class MyStack<T> {

    class Node<T> {

        public T elem;

        public Node(T element) {
            this.elem = element;
        }
    }

    private final Node<T> elems[];
    private int peek;
    private final int size;

    public MyStack(int size) {
        if (size < 0) {
            size = 1;
        }
        this.size = size;
        this.peek = -1;
        this.elems = new Node[size];
    }

    public void push(T element) {
        if (peek < size - 1) {
            elems[++peek] = new Node(element);
        }
    }

    public T pop() {
        if (peek > -1) {
            peek--;
            return elems[peek + 1].elem;
        }
        return null;
    }

    public T peek() {
        if (peek > -1) {
            return elems[peek].elem;
        }
        return null;
    }

    public boolean isFull() {
        return peek == size - 1;
    }

    public void print() {
        if (peek < 0) {
            return;
        }
        int i = 0;
        while (i <= peek) {
            System.out.print(elems[i].elem + "\t");
            i++;
        }
        System.out.println("");
    }

    public void printFullVector() {
        for (int i = 0; i < size; i++) {
            if (elems[i] == null) {
                System.out.print(-1 + "\t");
            } else {
                System.out.print(elems[i].elem + "\t");
            }
        }
        System.out.println("");
    }

    @Override
    public String toString() {
        if (peek < 0) {
            return "";
        }
        String toString = "";
        int i = 0;
        while (i <= peek) {
            toString += elems[i].elem;
            if (i < peek) {
                toString += " ";
            }
            i++;
        }
        return toString;
    }
}
