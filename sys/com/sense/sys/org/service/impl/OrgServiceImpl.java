package com.sense.sys.org.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.frame.base.BaseService;
import com.sense.frame.base.BusinessException;
import com.sense.frame.common.util.TreeUtil;
import com.sense.frame.enumdic.EnumRootNodeID;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Org;
import com.sense.sys.entity.Usr;
import com.sense.sys.org.dao.OrgDao;
import com.sense.sys.org.service.OrgService;

@Service
public class OrgServiceImpl extends BaseService implements OrgService {
	
	@Autowired
	private OrgDao orgDao;
	/**
	 * 获取机构树，不带根节点
	 */
	@Override
	public List<TreeModel> getAllOrgTree() throws Exception {
		return TreeUtil.setTree(commonDao.findEntityList(Org.class));
	}
	
	/**
	 * 获取机构树，带根节点
	 */
	@Override
	public List<TreeModel> getOrgTreeWithRoot() throws Exception {
		List<Org> orgList = commonDao.findEntityList(Org.class);
		Org virtueRootOrg = new Org();
		virtueRootOrg.setOrgID(EnumRootNodeID.ROOTNODE.getCode());
		virtueRootOrg.setOrgNam("顶级机构");
		virtueRootOrg.setSeqNO(1); // 这个一定要有，否则会报错
		virtueRootOrg.setParentID(EnumRootNodeID.VIRTUEROOTNODE.getCode());
		orgList.add(virtueRootOrg);
		return TreeUtil.setTreeOpenLevel(TreeUtil.setTree(orgList), 1);
	}

	// ----------------------------节点移动：上下
	@Override
	public void moveNodeUp(String moveID, String nearbyID) throws Exception {
		moveNodeSeqno(moveID, nearbyID, true);
	}

	@Override
	public void moveNodeDown(String moveID, String nearbyID) throws Exception {
		moveNodeSeqno(moveID, nearbyID, false);
	}

	/**
	 * 上移或者下移某个节点, 先检查移动节点的seqno是否相等，不等则交换即可;相等的情况下，把一个节点加1
	 * 
	 * @param upflag向上移动或者向下移动
	 */
	private void moveNodeSeqno(String moveID, String nearbyID, boolean upflag)
			throws Exception {
		Org moveOrg = commonDao.findEntityByID(Org.class, moveID);
		if (moveOrg == null) {
			throw new BusinessException("调整级别失败,需要调整实体对象已被删除");
		}
		Integer moveSeqno = moveOrg.getSeqNO() == null ? 1 : moveOrg.getSeqNO();

		Org nearbyOrg = commonDao.findEntityByID(Org.class, nearbyID);
		if (nearbyOrg == null) {
			throw new BusinessException("调整级别失败,需要调整的临近实体对象已被删除");
		}
		Integer nearbySeqno = nearbyOrg.getSeqNO() == null ? 1 : nearbyOrg
				.getSeqNO();
		if (moveSeqno == nearbySeqno) {
			if (upflag) {
				nearbySeqno++;
			} else {
				moveSeqno++;
			}
		} else {
			Integer switchSeqno = moveSeqno;
			moveSeqno = nearbySeqno;
			nearbySeqno = switchSeqno;
		}
		moveOrg.setSeqNO(moveSeqno);
		nearbyOrg.setSeqNO(nearbySeqno);
		commonDao.updateEntity(moveOrg);
		commonDao.updateEntity(nearbyOrg);
	}
	
	/*
	 * 根据机构ID获取机构信息
	 */
	@Override
	public Org queryOrgByID(String orgID) throws Exception {
		
		return commonDao.findEntityByID(Org.class, orgID);
	}
	
	
	/*
	 * 新增机构、下级机构
	 */
	@Override
	public String addOrg(Org org) throws Exception {
		String orgCod = org.getOrgCod();
		String orgNam = org.getOrgNam();
		String parentID = org.getParentID();
		Integer seqno = org.getSeqNO();
		
		//验证机构编码是否存在
		if(commonDao.findEntityList(Org.class, "orgCod", orgCod).size()!=0){
			throw new BusinessException("机构编码已存在，请重新输入！");
		}
		//验证机构名称是否存在
		if(commonDao.findEntityList(Org.class, "orgNam", orgNam).size()!=0){
			throw new BusinessException("机构名称已存在，请重新输入！");
		}
		if(seqno==null){
			seqno = commonDao.findEntityList(Org.class, "parentID", parentID).size() + 1;
		}
		org.setOrgID(dBUtil.getCommonId());
		org.setSeqNO(seqno);
		commonDao.saveEntity(org);
		
		return org.getOrgID();
	}
	
	/*
	 * 删除机构
	 */
	@Override
	public void delOrg(String delOrg) throws Exception {
		
		//判断此机构下是否有用户
		if(commonDao.findEntityList(Usr.class, "orgID", delOrg).size()!=0){
			throw new BusinessException("此机构下存在用户，不能删除！");
		}
		//判断此机构下是否有下级机构
		if(commonDao.findEntityList(Org.class,"parentID",delOrg).size()!=0){
			throw new BusinessException("此机构下存在下级机构，不能删除！");
		}
		commonDao.delEntityById(Org.class, delOrg);
	}
	
	/*
	 * 修改机构
	 */
	@Override
	public void modifyOrg(Org org) throws Exception {
		String orgID,orgCod,orgNam;
		orgID = org.getOrgID();
		orgCod = org.getOrgCod();
		orgNam = org.getOrgNam();
		
		//验证机构编码是否重复
		if(orgDao.queryOrgByCod(orgCod, orgID).size()!=0){
			throw new BusinessException("机构编码重复，请重新输入！");
		}
		//验证机构名称是否重复
		if(orgDao.queryOrgByNam(orgNam, orgID).size()!=0){
			throw new BusinessException("机构名称重复，请重新输入！");
		}
		//机构的父级不能是自身
		if(org.getOrgID().equals(org.getParentID())){
			throw new BusinessException("机构的父级不能是自身！");
		}
		commonDao.updateEntity(org);	
	}
	
}