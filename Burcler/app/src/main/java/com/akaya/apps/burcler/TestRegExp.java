package com.akaya.apps.burcler;

import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by agshin on 8/6/15.
 */
public class TestRegExp {

    String test = "am dddddddddd ksjfj ma amma fjwr am rgergkejv ma  maok3rgerg  am fewrkwrt ma";
    String patternStartWidth = "\\b(<td class=)";
    String patternEndsWidth = "\\S*(</td>)(\\b)";
//    String patternStartWidth = "\\b(am)";
//    String patternEndsWidth = "\\S*(ma)(\\b)";
    String patternIncludeIn = "(\\w)(http://qadinlar.biz/templates/xxx/images/burc)(\\w)";
    String patter4 = "(status:)(\\d+)";
    String pattern1 = patternStartWidth+"|"+patternEndsWidth;//+patternIncludeIn+"&"
    String line = "vəz dvəf-və dvəvər status sdsdvə avə";
    Pattern r4 = Pattern.compile(pattern1,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
//    Pattern r1 = Pattern.compile(patternStartWidth,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
//    Pattern r2 = Pattern.compile(patternEndsWidth,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
//    Pattern r3 = Pattern.compile(patternIncludeIn,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
//    Pattern r5 = Pattern.compile(patter4);  // Now create matcher object. Matcher m = r3.matcher(line);  System.out.println("-----------------");



    public ArrayList<String> test(String hs){
        ArrayList<String> ret = new ArrayList<String>();
        Matcher m  = r4.matcher(hs);
        while(m.find()) {
            ret.add(m.group());
        }
        return ret;
    }
}
