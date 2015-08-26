package com.akaya.apps.burcler;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A complete Java program that demonstrates how to
 * extract a tag from a line of HTML using the Pattern
 * and Matcher classes.
 */
public class PatternMatcherGroupHtml
{
    public PatternMatcherGroupHtml(){

    }
    public ArrayList<String> parse(String html)
    {
        String[] ms = html.split("Zebra-Red");
        String ss = ms[1];
//
//        String test = "<td class=\"fc\" style=\"text-align: left;\"><img border=\"0\" title=\"\" src=\"http://qadinlar.biz/templates/xxx/images/burc-buga.png\" alt=\"\" style=\"border-radius: 7px; float: left; margin: 0px;\">&nbsp; <b><span class=\"masha_index masha_index15\" rel=\"15\"></span>Buğa </b>- Yeni perspektivlər və cazibədar imkanların yarandığı gündür. Vədlər verən və nələrdənsə bəhs edən insanlara asanlıqla inanmayın.<br><span class=\"masha_index masha_index16\" rel=\"16\"></span>\n" +
//                "\t\t\t\tİndi arxasınca getməyə hazır olduğunuz, inandığınız insandan əslində mümkün qədər uzaq durmalısınız.<br><span class=\"masha_index masha_index17\" rel=\"17\"></span>\n" +
//                "\t\t\t\tMaliyyənizlə risk etməyin.<br><span class=\"masha_index masha_index18\" rel=\"18\"></span>\n" +
//                "\t\t\t\tSəfərə yollanmaq fikrinə düşsəniz, ailə üzvlərinizi də unutmayın. Tənha iş və təkbaşına fəaliyyət üçün uğursuz gündür.<br><span class=\"masha_index masha_index19\" rel=\"19\"></span>\n" +
//                "\t\t\t\tAxşam saatlarında şəxsi münasibətlərlə bağlı əhəmiyyətli qərar qəbul etməyin.<br>\n" +
//                "\t\t\t\t</td>";
        ArrayList<String> ret = new ArrayList<String>();
        // the pattern we want to search for
        Pattern p = Pattern.compile("<td([^<]*)</td>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(ss);

        // if we find a match, get the group
        if (m.find())
        {
            ret.add(m.group());
        }

        return ret;
    }
}