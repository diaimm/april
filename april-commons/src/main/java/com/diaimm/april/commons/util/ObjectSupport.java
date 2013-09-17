package com.diaimm.april.commons.util;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * equals 구현이나 hashCode 구현이 꼭!! 필요한 경우만 사용하도록 합니다.
 * </pre>
 * 
 * @author diaimm
 */
public abstract class ObjectSupport {
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	public boolean equals(Object o, String[] exceptFields) {
		return EqualsBuilder.reflectionEquals(this, o, exceptFields);
	}
	
	public boolean equals(Object o, String[] exceptFields, String[] exceptNullFields) {
		Class clazz = o.getClass();

        List<String> finalExceptFields = new ArrayList<String>();
        
        for (String fieldName : exceptNullFields) {
        	try {
        		Field field = clazz.getDeclaredField(fieldName);
        		field.setAccessible(true);
        		Object value = field.get(o);
        		if(value == null){
        			finalExceptFields.add(fieldName);
        		}
//        		if(value instanceof String){
//        			if(StringUtils.isEmpty((String) value)){
//        				finalExceptFields.add(fieldName);
//        			}
//        		}
			} catch (Exception e) {
				// do nothing
			}
		}
        
        for (String field : exceptFields) {
			finalExceptFields.add(field);
		}
                
		return EqualsBuilder.reflectionEquals(this, o, finalExceptFields.toArray(new String[finalExceptFields.size()]));
	}
	
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
