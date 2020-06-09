package eigene_Datenstrukturen.list;

import java.io.Serializable;

public class Node implements Serializable {
    private Node last;
    private Node next;
    private Object data;

    public Node(Node last, Object data) {
        this.last = last;
        this.data = data;
    }

    public Node getLast() {
        return last;
    }

    public void setLast(Node last) {
        this.last = last;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
