package com.sense.sys.dic.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.entity.Dic;
import com.sense.sys.entity.DicDtl;

public interface DicService {
	
	/**
	 * 加载所有的字典
	 */
	public HashMap<String, List<HashMap<String, String>>> queryAllDicList() throws Exception;
	
	/**
	 * 获取某个字典的json
	 */
	public List<Map<String, Object>> queryDicDtlList(String dicCod) throws Exception;

	/**
	 * 分页获取字典
	 */
	public PageInfo queryDicWithPage(PageInfo pi, String dicNamOrCod) throws Exception;
	
	/**
	 * 分页获取字典明细，按照seqno排序
	 */
	public PageInfo queryDicDtlWithPage(PageInfo pi, String dicCod) throws Exception;
	
	/**
	 * 新增字典
	 */
	public void addDic(Dic dic) throws Exception;
	
	/**
	 * 编辑字典
	 */
	public void editDic(String oldDicCod,String oldDicNam ,Dic dic) throws Exception;
	
	/**
	 * 删除某个字典（非基础类型的字典才允许修改、删除）
	 */
	public void delDic(String dicCod) throws Exception;
	
	/**
	 * 新增字典明细
	 */
	public void addDicDtl(DicDtl dicDtl) throws Exception;
	
	/**
	 * 修改字典明细
	 */
	public void editDicDtl(DicDtl dicDtl) throws Exception;
	
	/**
	 * 删除某个字典明细项（非基础类型的字典才允许修改、删除）
	 */
	public void delDicDtl(String dicCod,String dicDtlCod) throws Exception;
}