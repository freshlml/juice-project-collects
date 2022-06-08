package com.juice.normal.test.spring.core.utils;

public class UtilsTest {

    public static void main(String argv[]) {



    }
    private static String prefix = "${";
    private static String suffix = "}";
    /*private int findRelativeSuffix(String value, int prefixStart) {
        int findPos = prefixStart + prefix.length();
        int predit = 1;
        while(predit != 0 && findPos<value.length()) {
            int nextPrefixPos = value.indexOf(prefix, findPos);
            int nextSuffixPos = value.indexOf(suffix, findPos);
            if(nextSuffixPos == -1) {
                break;
            } else if(nextPrefixPos == -1){
                predit--;
                findPos = nextSuffixPos + suffix.length();
            } else if(nextPrefixPos > nextSuffixPos) {
                predit--;
                findPos = nextSuffixPos + suffix.length();
            } else {
                predit++;
                findPos = nextPrefixPos + prefix.length();
            }
        }
        if(predit != 0) return -1;
        return findPos- suffix.length();
    }*/

}
