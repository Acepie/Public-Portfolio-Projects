// Assignment 9
// Matuszak Caitlin
// cmatusza
// Radwan Ameen

import java.util.*;

// Represents a list of type T
interface IList<T> extends Iterable<T> {

    // Compute the length of this list
    int length();

    // return this list as a cons if possible
    Cons<T> asCons();

    // Returns the first object in this list that passes the pred if one exists
    T find(IPred<T> pred);

    // Removes the given item from the list if possible
    IList<T> remove(T item);

}

// Represents an empty list of type T
class Empty<T> implements IList<T> {

    // Compute the length of this empty list
    public int length() {
        return 0;
    }

    // Generate an iterator for this empty list
    public Iterator<T> iterator() {
        return new ListIterator<T>(this);
    }

    // Return this empty list as a cons if possible
    public Cons<T> asCons() {
        throw new ClassCastException("empty lists can't be cons");
    }

    // Find the first item in this empty list that satisfies the given pred
    public T find(IPred<T> pred) {
        throw new RuntimeException("Object not in list");
    }

    // Remove the given item from this empty list if possible
    public IList<T> remove(T item) {
        return this;
    }
}

// Represents a non-empty list of type T
class Cons<T> implements IList<T> {
    T first;
    IList<T> rest;

    Cons(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    // Compute the length of this list
    public int length() {
        return 1 + this.rest.length();
    }

    // Generate an iterator for this list
    public Iterator<T> iterator() {
        return new ListIterator<T>(this);
    }

    // Return this list as a cons if possible
    public Cons<T> asCons() {
        return this;
    }

    // Find the first item in this list that satisfies the given pred if
    // possible
    public T find(IPred<T> pred) {
        if (pred.apply(this.first)) {
            return this.first;
        }
        else {
            return this.rest.find(pred);
        }
    }

    // Remove the given item from this list if possible
    public IList<T> remove(T item) {
        if (this.first.equals(item)) {
            return this.rest;
        }
        else {
            return new Cons<T>(this.first, this.rest.remove(item));
        }
    }
}

// Represents an iterator for ILists
class ListIterator<T> implements Iterator<T> {
    IList<T> list;

    ListIterator(IList<T> list) {
        this.list = list;
    }

    // Determines if the list has a next item
    public boolean hasNext() {
        return this.list.length() != 0;
    }

    // Returns the next item in this iteration of the list
    // EFFECT: advance the iterator to the next value
    public T next() {
        T value = this.list.asCons().first;
        this.list = this.list.asCons().rest;
        return value;
    }

    // Remove the item just returned by next - unsupported in this case
    public void remove() {
        throw new UnsupportedOperationException("Method not supported");
    }
}

// Represents a predicate for type T
interface IPred<T> {

    // Applies this predicate to the given t
    boolean apply(T t);
}
