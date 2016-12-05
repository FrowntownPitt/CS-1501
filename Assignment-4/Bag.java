import java.util.Iterator;
import java.util.NoSuchElementException;

public class Bag<T> implements Iterable<T>{
    private Node<T> first;
    private int n;

    private String identifier;

    private static class Node<T>{
        private T item;
        private Node<T> next;
        //private Node<T> previous;

        public T getItem(){
            return item;
        }
    }

    public Bag(){
        first = null;
        n = 0;
    }

    public Bag(String name){
        identifier = name;
        first = null;
        n = 0;
    }

    public boolean isEmpty(){
        return first == null;
    }

    public int size(){
        return n;
    }

    public void add(T item){
        Node<T> oldFirst = first;
        first = new Node<T>();
        first.item = item;
        first.next = oldFirst;
        
        //oldFirst.previous = first;

        n++;
    }

    public boolean remove(T item){
        if(first == item){
            first = first.next;
            n--;
            return true;
        }
        Node<T> prev = first;
        Node<T> current = prev.next;

        while(current != null){
            if(item == current){
                prev.next = current.next;
                n--;
                return true;
            }
            prev = prev.next;
            current = current.next;
        }
        return false;
    }

    public Iterator<T> iterator(){
        return new ListIterator<T>(first);
    }

    @SuppressWarnings("unchecked")
    public String toString(){
        String out = "Airport: " + identifier + "\n";
        Iterator itr = iterator();
        //System.out.println("Made iterator.");
        while(itr.hasNext()){
            String s = ((T)itr.next()).toString();
            out = out + "\t" + s + "\n";
            //System.out.println(s);
        }
        return out;
    }

    private class ListIterator<T> implements Iterator<T>{
        private Node<T> current;
        private Node<T> previous;

        boolean canRemove = false;

        public ListIterator(Node<T> first){
            current = first;
            previous = first;
        }

        public boolean hasNext(){
            return current != null;
        }

        public void remove(){
            if(canRemove){
                if(previous != null && previous == current){
                        //first = first.next;
                    //Cannot happen, next() must be invoked first
                } else if(previous != null && previous.next == current){
                    first = first.next;
                    previous = previous.next;
                } else {
                    previous.next = current;
                }
            }
            canRemove = false;
        }

        public T next(){
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            if(previous != current && previous.next != current){
                previous = previous.next;
            } else {
                
            }

            T item = current.item;
            current = current.next;
            canRemove = true;
            return item;
        }
    }
    
}
