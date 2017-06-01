package com.sense.frame.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AliasToBeanResultTransformer extends org.hibernate.transform.AliasToBeanResultTransformer {
 	private static final long serialVersionUID = -8081082513705384784L;
 	
	@SuppressWarnings("rawtypes")
	private final Class resultClass;
	
	@SuppressWarnings("rawtypes")
	public AliasToBeanResultTransformer(Class resultClass) {
		super(resultClass);
		this.resultClass=resultClass;
	}
	@Override
	public Object transformTuple(Object[] tuple, String[] aliases){
		Field[] resultfield=resultClass.getDeclaredFields();
		List<String> propertys=new ArrayList<String>();
		for ( int i = 0; i < aliases.length; i++ ) {
			if ( aliases[ i ] != null ) {		
				String alias = aliases[ i ].toLowerCase();
				for(Field fl:resultfield){
					String propertyName=fl.getName();
					if(propertyName.toLowerCase().equals(alias)){
						propertys.add(propertyName);
					}
				}
			}
		}
		return super.transformTuple(tuple, propertys.toArray(aliases));
	}
}