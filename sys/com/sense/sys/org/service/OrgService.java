package com.sense.sys.org.service;

import java.util.List;

import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Org;

public interface OrgService {

	/**
	 * 获取机构树，不带根节点
	 */
	public List<TreeModel> getAllOrgTree() throws Exception;

	/**
	 * 获取机构树，带根节点
	 */
	public List<TreeModel> getOrgTreeWithRoot() throws Exception;
	
	/**
	 * 节点上移
	 */
	public void moveNodeUp(String moveID, String nearbyID) throws Exception ;
	
	/**
	 * 节点下移
	 */
	public void moveNodeDown(String moveID, String nearbyID) throws Exception ;
	
	/*
	 * 根据机构ID获取机构
	 */
	public Org queryOrgByID(String orgID) throws Exception ;

	/*
	 * 新增机构、下级机构
	 */
	public String addOrg(Org org) throws Exception;
	
	/*
	 * 删除机构
	 */
	public void delOrg(String delOrg) throws Exception;
	
	/*
	 * 修改机构
	 */
	public void modifyOrg(Org org) throws Exception;
	
}