package com.sense.sys.entity;

import java.io.Serializable;
/**
 * 字典明细联合主键
 */
public class DicDtlPk implements Serializable {
	private static final long serialVersionUID = 1L;
	private String dicCod;
	private String dicDtlCod;
	public String getDicCod() {
		return dicCod;
	}
	public void setDicCod(String dicCod) {
		this.dicCod = dicCod;
	}
	public String getDicDtlCod() {
		return dicDtlCod;
	}
	public void setDicDtlCod(String dicDtlCod) {
		this.dicDtlCod = dicDtlCod;
	}
	
    @Override  
    public boolean equals(Object o) {  
        if(o instanceof DicDtlPk){  
        	DicDtlPk key = (DicDtlPk)o ;  
            if(this.dicCod.equals(key.getDicCod()) && this.dicDtlCod.equals(key.getDicDtlCod())){  
                return true ;  
            }  
        }  
        return false ;  
    }  
      
    @Override  
    public int hashCode() {  
        return this.dicCod.hashCode() + this.dicDtlCod.hashCode();  
    } 
    
}
