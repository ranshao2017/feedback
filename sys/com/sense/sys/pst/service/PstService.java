package com.sense.sys.pst.service;

import java.util.List;

import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Pst;

public interface PstService {

	/**
	 * 获取指定部门的岗位树，不带根节点
	 */
	public List<TreeModel> queryPstTreeWithRoot(String orgID) throws Exception;

	public String addPst(Pst pst) throws Exception;

	/**
	 * 根据部门分页检索岗位
	 */
	public PageInfo queryPstWithPage(PageInfo pi, String orgID) throws Exception;

	public Pst queryPstByID(String pstID) throws Exception;

	public String modifyPst(Pst pst) throws Exception;

	/**
	 * 删除选定岗位
	 */
	public void delPst(List<String> pstCheckList) throws Exception;

	public PageInfo queryOwnedPstDgByUser(String usrID, String orgID) throws Exception;

	public PageInfo queryNotOwnedPstDgByUser(String usrID, String orgID) throws Exception;

	/**
	 * 根据岗位ID，获取该岗位的反馈流程节点权限
	 */
	public List<TreeModel> queryPstWFTree(String pstID) throws Exception;

	public void saveUsrProc(String pstID, String nodeIDs) throws Exception;

}