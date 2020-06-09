package eigene_Datenstrukturen.list;

import java.io.Serializable;

public class List<E> implements Serializable {
    private Node head;
    private Node tail;
    private int length = 0;

    public void add(E data) {
        if (head == null) {
            head = new Node(null, data);
            tail = head;
        } else {
            Node n = new Node(tail, data);
            tail.setNext(n);
            tail = n;
        }
        length++;
    }

    public int length() {
//        if(head == null)
//            return 0;
//
//        int counter = 1;
//        Node n = head;
//        while (n.getNext()!=null){
//            counter++;
//            n = n.getNext();
//        }
        return length;
    }

    public E get(int index) {
        if (index > length) {
            throw new IndexOutOfBoundsException();
        } else if (index >= length / 2) {
            int counter = length - 1;
            Node n = tail;
            while (counter != index) {
                counter--;
                n = n.getLast();
            }
            return (E) n.getData();
        } else {
            int counter = 0;
            Node n = head;
            while (counter != index) {
                counter++;
                n = n.getNext();
            }
            return (E) n.getData();
        }
    }

    public void remove(int index) {
        if (index > length) {
            throw new IndexOutOfBoundsException();
        } else if (index >= length / 2) {
            int counter = length - 1;
            Node n = tail;
            while (counter != index) {
                counter--;
                n = n.getLast();
            }

            n.getLast().setNext(n.getNext());
            if (n.getNext() != null)
                n.getNext().setLast(n.getLast());
            else
                tail = n.getLast();
            length--;
        } else {
            int counter = 0;
            Node n = head;
            while (counter != index) {
                counter++;
                n = n.getNext();
            }

            if (n.getLast() != null)
                n.getLast().setNext(n.getNext());
            else
                head = n.getNext();

            n.getNext().setLast(n.getLast());

            length--;
        }
    }
}
