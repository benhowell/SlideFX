/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ben Howell
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

import java.util.*;

/**
 * Created by Ben Howell [ben@benhowell.net] on 19-Sep-2014.
 */
public class Util {

  public static List<String> listFromResource(String resource){
    String text = new Scanner(Util.class.getResourceAsStream(resource)).useDelimiter("\\A").next();
    return Arrays.asList(text.split(System.getProperty("line.separator")));
  }

  public static List shuffle(List l){
    long seed = System.nanoTime();
    Collections.shuffle(l, new Random(seed));
    return l;
  }

  public static ArrayList shuffle(ArrayList l){
    long seed = System.nanoTime();
    Collections.shuffle(l, new Random(seed));
    return l;
  }

  public static <T> List<List<T>> zip(List... lists) {
    List<List<T>> zipped = new ArrayList<List<T>>();
    for (List<T> list : lists) {
      for (int i = 0, listSize = list.size(); i < listSize; i++) {
        List<T> list2;
        if (i >= zipped.size())
          zipped.add(list2 = new ArrayList<T>());
        else
          list2 = zipped.get(i);
        list2.add(list.get(i));
      }
    }
    return zipped;
  }
}
