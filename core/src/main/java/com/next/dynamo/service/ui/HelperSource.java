package com.next.dynamo.service.ui;

import java.io.IOException;

public class HelperSource {

    public String trimString(String content, Integer trimLength) throws IOException {
        try{
            if (trimLength == null) {
                trimLength = 150;
            }
            if(content == null){
            	return "";
            }
            if (content.length() > trimLength) {
                return content.substring(0, trimLength);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return content;
    }

}
