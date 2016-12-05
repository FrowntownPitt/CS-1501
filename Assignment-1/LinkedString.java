//import java.util.Iterable;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class LinkedString<T> implements Iterable<T>{

	Node<T> head = new Node<>();
	int count = 0;

	class Node<T> {
		Object data;
		Node next;

		public Node(){

		}
	}

	public void addItem(T item){
		if(head.data == null){
			head = new Node<T>();
			head.data = item;
			count++;
		} else {
			Node current = head;
			while(current.next != null){
				current = current.next;
			}
			current.next = new Node<T>();
			current.next.data = item;
			count++;
		}
	}

	public int size(){
		return count;
	}

	public Iterator<T> iterator(){
		return new LinkedStringIterator<T>();
	}

	private class LinkedStringIterator<T> implements Iterator<T> {

		Node next;

		LinkedStringIterator(){
			next = head;
		}
		
		public boolean hasNext(){
			return !(next == null);
		}

		public T next(){
			T ret = (T)next.data;
			next = next.next;
			return ret;
		}

		public void remove(){

		}
	}
}