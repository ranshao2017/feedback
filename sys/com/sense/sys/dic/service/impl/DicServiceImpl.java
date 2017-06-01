package com.sense.sys.dic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.frame.base.BaseService;
import com.sense.frame.base.BusinessException;
import com.sense.frame.pub.model.PageInfo;
import com.sense.sys.dic.dao.DicDao;
import com.sense.sys.dic.service.DicService;
import com.sense.sys.entity.Dic;
import com.sense.sys.entity.DicDtl;

@Service
public class DicServiceImpl extends BaseService implements DicService {
	
	@Autowired
	private DicDao dicDao;
	
	/**
	 * 加载所有的字典
	 */
	public HashMap<String, List<HashMap<String, String>>> queryAllDicList() throws Exception{
		return dicDao.queryAllDicList();
	}
	
	/**
	 * 获取某个字典的json
	 */
	@Override
	public List<Map<String, Object>> queryDicDtlList(String dicCod) throws Exception{
		List<DicDtl> allDtl = dicDao.queryDicDtlByDicCod(dicCod);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if(CollectionUtils.isNotEmpty(allDtl)){
			for(DicDtl dtl : allDtl){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("value", dtl.getDicDtlCod());
				map.put("text", dtl.getDicDtlNam());
				resultList.add(map);
			}
		}
		return resultList;
	}

	/**
	 * 分页获取字典
	 */
	@Override
	public PageInfo queryDicWithPage(PageInfo pi ,String dicNamOrCod)throws Exception{
		return dicDao.queryDicByPage(pi, dicNamOrCod);
	}
	
	/**
	 * 分页获取字典明细，按照seqno排序
	 */
	@Override
	public PageInfo queryDicDtlWithPage(PageInfo pi, String dicCod) throws Exception{
		return dicDao.queryDicDtlByPage(pi, dicCod);
	}
	
	/**
	 * 新增字典：字典编码和名字不允许重复
	 */
	@Override
	public void addDic(Dic dic) throws Exception{
		String dicCod = dic.getDicCod();
		String dicNam = dic.getDicNam();
		Dic dbSysDic = commonDao.findEntityByID(Dic.class, dicCod);
		if(dbSysDic!=null){
			throw new BusinessException("字典编码已被占用，请重新输入！");
		}
		dbSysDic = dicDao.queryDicByNam(dicNam);
		if(dbSysDic!=null){
			throw new BusinessException("字典名称已被占用，请重新输入！");
		}
		dic.setIsBase("0");  //新增或者修改的字典只能是非基本项
		commonDao.saveEntity(dic);
	}
	
	/**
	 * 编辑字典
	 */
	@Override
	public void editDic(String oldDicCod,String oldDicNam ,Dic dic) throws Exception{
		Dic oldSysDic = commonDao.findEntityByID(Dic.class, oldDicCod);
		if(oldSysDic!=null&&"1".equals(oldSysDic.getIsBase())){
			throw new BusinessException("原字典为基本类型，不允许修改！");
		}
		String dicCod = dic.getDicCod();
		String dicNam = dic.getDicNam();
		Dic dbSysDic =  null;
		
		//编码修改了，即主键修改了,删掉原来的，再新增一个
		if(!oldDicCod.equals(dicCod)){
			dbSysDic = commonDao.findEntityByID(Dic.class, dicCod);
			if(dbSysDic!=null){
				throw new BusinessException("字典编码已被占用，请重新输入！");
			}
			
			dicDao.delDicAndDtl(oldDicCod);
			dbSysDic = dicDao.queryDicByNam(dicNam);
			if(dbSysDic!=null){
				throw new BusinessException("字典名称已被占用，请重新输入！");
			}
			dic.setIsBase("0");
			commonDao.saveEntity(dic);
		}else{
		//编码没变，即主键没变
			dbSysDic = dicDao.queryDicByNam(dicNam);
			if(dbSysDic!=null&&!oldDicCod.equals(dbSysDic.getDicCod())){
				throw new BusinessException("字典名称已被占用，请重新输入！");
			}
			oldSysDic.setDicNam(dicNam);
			commonDao.updateEntity(oldSysDic);
		}
	}
	
	/**
	 * 删除某个字典（非基础类型的字典才允许修改、删除）
	 */
	@Override
	public void delDic(String dicCod) throws Exception{
		if(StringUtils.isNotBlank(dicCod)){
			Dic dic = commonDao.findEntityByID(Dic.class, dicCod);
			if("1".equals(dic.getIsBase())){
				throw new BusinessException("基本类型的字典不允许删除或者修改！");
			}
			dicDao.delDicAndDtl(dicCod);
		}
		
	}
	
	/**
	 * 新增字典明细
	 */
	public void addDicDtl(DicDtl dicDtl) throws Exception{
		String dicCod = dicDtl.getDicCod();
		Dic dic = commonDao.findEntityByID(Dic.class, dicCod);
		if("1".equals(dic.getIsBase())){
			throw new BusinessException("基本类型的字典不允许变更明细！");
		}
		DicDtl dbDicDtl = dicDao.queryDicDtlByDtlCod(dicCod, dicDtl.getDicDtlCod());
		if(dbDicDtl!=null){
			throw new BusinessException("编码已被占用！");
		}
		dbDicDtl = dicDao.queryDicDtlByDtlNam(dicCod, dicDtl.getDicDtlNam());
		if(dbDicDtl!=null){
			throw new BusinessException("名字已被占用！");
		}
		if(dicDtl.getSeqNO()==null){
			List<DicDtl> dtls= dicDao.queryDicDtlByDicCod(dicDtl.getDicCod());
			if(dtls==null||dtls.size()==0){
				dicDtl.setSeqNO(1);
			}else{
				//取最后一个
				DicDtl lastDicDtl = dtls.get(dtls.size()-1);
				dicDtl.setSeqNO(lastDicDtl.getSeqNO()==null?(dtls.size()+1):(lastDicDtl.getSeqNO()+1));
			}
		}
		commonDao.saveEntity(dicDtl);
	}
	
	/**
	 * 修改字典明细：明细的编码不允许修改。。
	 */
	@Override
	public void editDicDtl(DicDtl dicDtl) throws Exception{
		String dicCod = dicDtl.getDicCod();
		Dic dic = commonDao.findEntityByID(Dic.class, dicCod);
		if("1".equals(dic.getIsBase())){
			throw new BusinessException("基本类型的字典不允许变更明细！");
		}
		DicDtl dbDicDtl = dicDao.queryDicDtlByDtlNam(dicCod, dicDtl.getDicDtlNam());
		if(dbDicDtl!=null&&!dicDtl.getDicDtlCod().equals(dbDicDtl.getDicDtlCod())){
			throw new BusinessException("名字已被占用！");
		}
		
		dbDicDtl = dicDao.queryDicDtlByDtlCod(dicCod, dicDtl.getDicDtlCod());
		if(dbDicDtl ==null){
			throw new BusinessException("字典明细已被删除，无法继续修改！");
		}
		
		dbDicDtl.setDicDtlNam(dicDtl.getDicDtlNam());
		dbDicDtl.setSeqNO(dicDtl.getSeqNO());
		commonDao.updateEntity(dbDicDtl);
	}
	
	/**
	 * 删除某个字典明细项（非基础类型的字典才允许修改、删除）
	 */
	@Override
	public void delDicDtl(String dicCod,String dicDtlCod) throws Exception{
		if(StringUtils.isNotBlank(dicCod)&&StringUtils.isNotBlank(dicDtlCod)){
			Dic dic = commonDao.findEntityByID(Dic.class, dicCod);
			if("1".equals(dic.getIsBase())){
				throw new BusinessException("基本类型的字典不允许删除或者修改！");
			}
			dicDao.delDicDtl(dicCod,dicDtlCod);
		}
	}
}