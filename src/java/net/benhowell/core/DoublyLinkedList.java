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


class Node<T> {
  T element;
  Node<T> previous;
  Node<T> next;

  public Node(T element, Node<T> previous, Node<T> next) {
    this.previous = previous;
    this.next = next;
    this.element = element;
  }

  public T element() {
    return this.element;
  }

  public Node<T> previous() {
    return previous;
  }

  public Node<T> next() {
    return next;
  }

  public boolean hasPrevious(){
    return this.previous.element != null;
  }

  public boolean hasNext(){
    return this.next.element != null;
  }

  public void setElement(T element) {
    this.element = element;
  }

  protected void setPrevious(Node<T> previous) {
    this.previous = previous;
  }

  protected void setNext(Node<T> next) {
    this.next = next;
  }
}


public class DoublyLinkedList<T> {

  private Node<T> head;
  private Node<T> tail;
  private int size;

  public DoublyLinkedList(){
    head = new Node<>(null, null, null);
    tail = new Node<>(null, null, null);
    size = 0;

    head.setNext(tail);
    tail.setPrevious(head);
  }


  public void addFirst(T element){
    this.insertAfter(head, element);
  }

  public void add(T element){
    this.insertBefore(tail, element);
  }


  public void addAll(Collection<? extends T> c){
    c.stream().forEach(t -> add(t));
  }


  public Node<T> removeFirst() throws RuntimeException{
    if(this.isEmpty())
      throw new RuntimeException("Error: cannot call removeFirst() on empty list");
    Node<T> node = this.getNext(head);
    return this.remove(node);
  }


  public Node<T> removeLast() throws RuntimeException{
    if(this.isEmpty())
      throw new RuntimeException("Error: cannot call removeLast() on empty list");
    Node<T> node = this.getPrevious(tail);
    return this.remove(node);
  }


  public Node<T> getFirst(){
    if(this.isEmpty())
      throw new RuntimeException("Error: cannot call getFirst() on empty list");
    return this.getNext(head);
  }

  public Node<T> getLast(){
    if(this.isEmpty())
      throw new RuntimeException("Error: cannot call getLast() on empty list");
    return this.getPrevious(tail);
  }


  public boolean isEmpty(){
    return size == 0;
  }


  public String toString(){
    String s = "[";
    Node<T> node = head.next();
    while(node!= tail){
      s += node.element() + " ";
      node = node.next();
    }
    s += "]";
    return s;
  }


  private Node<T> getNext(Node<T> node) throws RuntimeException{
    if(node == tail)
      throw new RuntimeException("Error: cannot call next() on tail node");
    return node.next();
  }


  private Node<T> getPrevious(Node<T> node) throws RuntimeException{
    if(node == head)
      throw new RuntimeException("Error: cannot call previous() on head node");
    return node.previous();
  }


  private void insertBefore(Node<T> element, T value){
    Node<T> previousNode = this.getPrevious(element);
    Node<T> newNode = new Node<>(value, previousNode, element);
    element.setPrevious(newNode);
    previousNode.setNext(newNode);
    size++;
  }


  private void insertAfter(Node<T> element, T value){
    Node<T> nextNode = this.getNext(element);
    Node<T> newNode = new Node<>(value, element, nextNode);
    element.setNext(newNode);
    nextNode.setPrevious(newNode);
    size++;
  }


  private Node<T> remove(Node<T> node){
    Node<T> previous = this.getPrevious(node);
    Node<T> next = this.getNext(node);
    node.setPrevious(null);
    node.setNext(null);
    previous.setNext(next);
    next.setPrevious(previous);
    size--;
    return node;
  }
}
