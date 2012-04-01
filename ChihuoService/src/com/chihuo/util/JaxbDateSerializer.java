package com.chihuo.util;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.chihuo.resource.MyConstants;

public class JaxbDateSerializer extends XmlAdapter<String, Date>{ 


    @Override 
    public String marshal(Date date) throws Exception { 
        return MyConstants.dateFormat.format(date); 
    } 

    @Override 
    public Date unmarshal(String date) throws Exception { 
        return MyConstants.dateFormat.parse(date); 
    } 
} 
