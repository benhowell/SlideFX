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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Ben Howell [ben@benhowell.net] on 19-Aug-2014.
 */
public class Trial {

  public static ArrayList<Map<String,String>> loadExampleRun(Config config, String item){

    ArrayList<Map<String,String>> es = new ArrayList<>();
    List s = Configuration.getConfigList(config, item);
    for(Object c: s){
      es.add(create((Config)c));
    }

    return es;
  }

  public static HashMap<String,String> create(Config config){
    HashMap<String,String> m = new HashMap<>();
    m.put("id", config.getString("id"));
    m.put("type", config.getString("type"));
    m.put("imagePath", Trial.class.getResource("/img/").toString());
    m.put("imageName", config.getString("image"));
    m.put("text", config.getString("text"));
    m.put("name", config.getString("name"));

    try {
      m.put("example", config.getString("example"));
    }
    catch (ConfigException.Missing e) {
      //we don't care if there is no example for a trial item
    }
    return m;
  }

  public static List<List<Map<String, String>>> getTrialBlocks(List<Config> config){

    List<List<Map<String, String>>> l = new ArrayList<>();
    for(Config c: config){
      l.add(createRandomTrialBlock(c));
    }
    return l;
  }

  public static ArrayList<Map<String, String>> createRandomTrialBlock(Config config){
    String c = config.getString("category");
    List<String> ns = new ArrayList<>();
    ns.addAll(Util.shuffle(Configuration.getStringList(config, "names")));

    List<Map<String,String>> tsh = Util.shuffle(Configuration.getConfigList(config, "trials"));

    ArrayList<Map<String,String>> ts = new ArrayList<Map<String,String>>();
    for(Object tshc: tsh){
      ts.add(create((Config)tshc));
    }

    for(int i=0;i<ts.size();i++){
      ts.get(i).put("name", ns.get(i));
      ts.get(i).put("category", c);
    }

    return ts;
  }

  public static ArrayList<HashMap<String,String>> createRandomTrialRun(Config config, String item){
    Stream<List<Map<String, String>>> blockStream = getTrialBlocks(Configuration.getConfigList(config, item)).stream();
    ArrayList<HashMap<String,String>> list = new ArrayList<>();
    Stream<Map<String,String>> s = blockStream.flatMap((blocks) -> blocks.stream());
    s.forEach(Util.shuffle(list)::add);

    if(Configuration.getConfigBoolean(config, "experiment.randomiseCategoryPresentation"))
      return Util.shuffle(list);
    return list;
  }
}
