package com.ivt.mis.common.swing;
import java.util.ArrayList;
import java.util.Vector;

public class DefaultCompletionFilter implements CompletionFilter {

    private Vector vector;

    public DefaultCompletionFilter() {
        vector = new Vector();
    }

    public DefaultCompletionFilter(Vector v) {
        vector = v;
    }
    public ArrayList filter(String text) {
        ArrayList list=new ArrayList();
        String txt=text.trim();
        int length=txt.length();
        for(int i=0;i<vector.size();i++){
            Object o=vector.get(i);
            String str=o.toString();
            if(length==0||str.contains(txt.toUpperCase()))   //输入内容与记录前端比较
                list.add(o);
        }
        return list;
    }
}
