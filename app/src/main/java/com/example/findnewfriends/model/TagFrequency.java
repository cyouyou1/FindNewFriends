package com.example.findnewfriends.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class TagFrequency {

    public static List<Tag> createTags( Map<String, Integer> words_count_map) {

        List<Tag> tagList = new ArrayList<Tag>();
        TreeMap<String, Integer> sorted_words_count_map = sortByValue(words_count_map);
        Iterator<Map.Entry<String, Integer>> iterator = sorted_words_count_map.entrySet().iterator();
        for (int i = 0; i < 10; i++) {
            Map.Entry<String, Integer> result = iterator.next();
            tagList.add(new Tag(result.getKey(), result.getValue()));
        }
        return tagList;
    }

    public static TreeMap<String, Integer> sortByValue(Map<String, Integer> map) {
        ValueComparator vc = new ValueComparator(map);
        TreeMap<String, Integer> sortedMap = new TreeMap<>(vc);
        sortedMap.putAll(map);
        return sortedMap;
    }

}

class ValueComparator implements Comparator<String> {

    Map<String, Integer> map;

    public ValueComparator(Map<String, Integer> base) {
        this.map = base;
    }

    public int compare(String a, String b) {
        if (map.get(a) >= map.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}



