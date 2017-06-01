package com.sense.sys.pst.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.frame.base.BaseService;
import com.sense.frame.base.BusinessException;
import com.sense.frame.common.util.TreeUtil;
import com.sense.frame.enumdic.EnumRootNodeID;
import com.sense.frame.pub.model.PageInfo;
import com.sense.frame.pub.model.TreeModel;
import com.sense.sys.entity.Org;
import com.sense.sys.entity.ProcNode;
import com.sense.sys.entity.Pst;
import com.sense.sys.entity.PstProcNode;
import com.sense.sys.pst.dao.PstDao;
import com.sense.sys.pst.service.PstService;

@Service
public class PstServiceImpl extends BaseService implements PstService {
	
	@Autowired
	private PstDao pstDao;
	
	@Override
	public List<TreeModel> queryPstTreeWithRoot(String orgID) throws Exception {
		List<Pst> pstList = commonDao.findEntityList(Pst.class, "orgID", orgID);
		Pst virtueRootPst = new Pst();
		virtueRootPst.setPstID(EnumRootNodeID.ROOTNODE.getCode());
		virtueRootPst.setPstNam("顶级岗位");
		virtueRootPst.setSeqNO(1); // 这个一定要有，否则会报错
		virtueRootPst.setParentID(EnumRootNodeID.VIRTUEROOTNODE.getCode());
		pstList.add(virtueRootPst);
		return TreeUtil.setTree(pstList);
	}

	@Override
	public String addPst(Pst pst) throws Exception {
		boolean existPstCod = pstDao.queryPstExistCod(pst.getOrgID(), pst.getPstCod());
		if(existPstCod){
			throw new BusinessException("岗位编码已存在，请重新输入！");
		}
		boolean existPstNam = pstDao.queryPstExistNam(pst.getOrgID(), pst.getPstNam());
		if(existPstNam){
			throw new BusinessException("岗位名称已存在，请重新输入！");
		}
		if(pst.getSeqNO() == null){
			int seqNO = pstDao.queryPstSeqNO(pst.getOrgID());
			pst.setSeqNO(seqNO);
		}
		pst.setPstID(dBUtil.getCommonId());
		commonDao.saveEntity(pst);
		return pst.getPstID();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo queryPstWithPage(PageInfo pi, String orgID) throws Exception {
		pi = pstDao.queryPstWithPage(pi, orgID);
		List<Pst> list = (List<Pst>) pi.getRows();
		if(CollectionUtils.isNotEmpty(list)){
			for(Pst pst : list){
				if(!EnumRootNodeID.ROOTNODE.getCode().equals(pst.getParentID())){
					Pst parentPst = commonDao.findEntityByID(Pst.class, pst.getParentID());
					pst.setParentNam(parentPst.getPstNam());
				}
			}
		}
		return pi;
	}

	@Override
	public Pst queryPstByID(String pstID) throws Exception {
		Pst pst = commonDao.findEntityByID(Pst.class, pstID);
		Org org = commonDao.findEntityByID(Org.class, pst.getOrgID());
		pst.setOrgNam(org.getOrgNam());
		return pst;
	}

	@Override
	public String modifyPst(Pst pst) throws Exception {
		if(pst.getPstID().equals(pst.getParentID())){
			throw new BusinessException("岗位不能选择自身为上级岗位！");
		}
		if(pstDao.queryPstExistCod(pst.getOrgID(), pst.getPstCod(), pst.getPstID())){
			throw new BusinessException("岗位编码已存在，请重新输入！");
		}
		if(pstDao.queryPstExistNam(pst.getOrgID(), pst.getPstNam(), pst.getPstID())){
			throw new BusinessException("岗位名称已存在，请重新输入！");
		}
		commonDao.updateEntity(pst);
		return pst.getPstID();
	}

	@Override
	public void delPst(List<String> pstCheckList) throws Exception {
		pstDao.delPst(pstCheckList);
	}

	/**
	 * 查询具有的岗位
	 */
	@Override
	public PageInfo queryOwnedPstDgByUser(String usrID, String orgID) throws Exception {
		List<Pst> list = pstDao.queryPstOwne(usrID, orgID);
		PageInfo pi = new PageInfo();
		pi.setTotal("" + list.size());
		pi.setRows(list);
		return pi;
	}

	/**
	 * 查询可分配的岗位
	 */
	@Override
	public PageInfo queryNotOwnedPstDgByUser(String usrID, String orgID) throws Exception {
		List<Pst> list = pstDao.queryPstNotOwne(usrID, orgID);
		PageInfo pi = new PageInfo();
		pi.setTotal("" + list.size());
		pi.setRows(list);
		return pi;
	}

	@Override
	public List<TreeModel> queryPstWFTree(String pstID) throws Exception {
		List<ProcNode> nodeList = commonDao.findEntityList(ProcNode.class);
		List<String> nodeIDList = pstDao.queryProcNodeIDList(pstID);
		return TreeUtil.setTree(nodeList, nodeIDList);
	}

	@Override
	public void saveUsrProc(String pstID, String nodeIDs) throws Exception {
		pstDao.delPstProcNode(pstID);
		if(StringUtils.isNotBlank(nodeIDs)){
			String[] nodeIDArr = nodeIDs.split(",");
			for (int i = 0; i < nodeIDArr.length; i++) {
				PstProcNode rpn = new PstProcNode();
				rpn.setPk(dBUtil.getCommonId());
				rpn.setNodeID(nodeIDArr[i]);
				rpn.setPstID(pstID);
				commonDao.saveEntity(rpn);
			}
		}
	}
	
}