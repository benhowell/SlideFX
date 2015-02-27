/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Ben Howell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.benhowell.core;

import java.util.Collection;

/**
 * Created by Ben Howell [ben@benhowell.net] on 27-Feb-2015.
 */


class DLLNode<T> {
  DLLNode<T> previous;
  DLLNode<T> next;
  T element;

  public DLLNode(T element, DLLNode<T> previous, DLLNode<T> next) {
    this.previous = previous;
    this.next = next;
    this.element = element;
  }

  public T getElement() {
    return this.element;
  }

  public void setElement(T element) {
    this.element = element;
  }

  public DLLNode<T> getPrevious() {
    return previous;
  }

  public void setPrevious(DLLNode<T> previous) {
    this.previous = previous;
  }

  public DLLNode<T> getNext() {
    return next;
  }

  public void setNext(DLLNode<T> next) {
    this.next = next;
  }


}

public class DoublyLinkedList<T> {

    private DLLNode<T> head;
    private DLLNode<T> tail;
    private int size;

  public DoublyLinkedList(){
    head = new DLLNode<>(null, null, null);
    tail = new DLLNode<>(null, null, null);
    size = 0;

    head.setNext(tail);
    tail.setPrevious(head);
  }


  public void addFirst(T element){
    this.insertAfter(head, element);
  }


  public void addLast(T element){
    this.insertBefore(tail, element);
  }


  public void addAll(Collection<? extends T> c){
    c.stream().forEach(t -> addLast(t));
  }


  public boolean hasPrevious(DLLNode<T> node){
    if(node.getPrevious() == head)
      return false;
    return true;
  }


  public boolean hasNext(DLLNode<T> node){
    if(node.getNext() == tail)
      return false;
    return true;
  }


  public DLLNode<T> removeFirst() throws RuntimeException{
    if(this.isEmpty())
      throw new RuntimeException("Error: cannot call removeFirst() on empty list");
    DLLNode<T> node = this.getNext(head);
    return this.remove(node);
  }


  public DLLNode<T> removeLast() throws RuntimeException{
    if(this.isEmpty())
      throw new RuntimeException("Error: cannot call removeLast() on empty list");
    DLLNode<T> node = this.getPrevious(tail);
    return this.remove(node);
  }


  public DLLNode<T> getFirst(){
    if(this.isEmpty())
      throw new RuntimeException("Error: cannot call getFirst() on empty list");
    return this.getNext(head);
  }

  public DLLNode<T> getLast(){
    if(this.isEmpty())
      throw new RuntimeException("Error: cannot call getLast() on empty list");
    return this.getPrevious(tail);
  }


  public boolean isEmpty(){
    return size == 0;
  }

  public String toString(){
    String s = "[";
    DLLNode<T> node = head.getNext();
    while(node!= tail){
      s += node + " ";
      node = node.getNext();
    }
    s += "]";
    return s;
  }


  public DLLNode<T> getNext(DLLNode<T> node) throws RuntimeException{
    if(node == tail)
      throw new RuntimeException("Error: cannot call getNext() on tail node");
    return node.getNext();
  }


  public DLLNode<T> getPrevious(DLLNode<T> node) throws RuntimeException{
    if(node == head)
      throw new RuntimeException("Error: cannot call getPrevious() on head node");
    return node.getPrevious();
  }


  private void insertBefore(DLLNode<T> element, T value){
    DLLNode<T> previousNode = this.getPrevious(element);
    DLLNode<T> newNode = new DLLNode<>(value, previousNode, element);
    element.setPrevious(newNode);
    previousNode.setNext(newNode);
    size++;
  }


  private void insertAfter(DLLNode<T> element, T value){
    DLLNode<T> nextNode = this.getNext(element);
    DLLNode<T> newNode = new DLLNode<>(value, element, nextNode);
    element.setNext(newNode);
    nextNode.setPrevious(newNode);
    size++;
  }


  private DLLNode<T> remove(DLLNode<T> node){
    DLLNode<T> previous = this.getPrevious(node);
    DLLNode<T> next = this.getNext(node);
    node.setPrevious(null);
    node.setNext(null);
    previous.setNext(next);
    next.setPrevious(previous);
    size--;
    return node;
  }
}
