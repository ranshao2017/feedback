package com.sense.sys.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sense.frame.common.util.TreeBean;
import com.sense.frame.enumdic.EnumRootNodeID;

@Entity
@Table(name = "SYS_PRONODE")
@SuppressWarnings("serial")
public class ProcNode implements TreeBean, Serializable {
	@Id
	@Column(name = "NODEID")
	private String nodeID;// 流程节点ID

	@Column(name = "NODENAM")
	private String nodeNam;// 节点名称

	@Column(name = "SEQNO")
	private Integer seqNO;// 顺序号
	
	@Column(name = "PARENTID")
	private String parentID;//父节点
	
	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public String getNodeNam() {
		return nodeNam;
	}

	public void setNodeNam(String nodeNam) {
		this.nodeNam = nodeNam;
	}

	public Integer getSeqNO() {
		return seqNO;
	}

	public void setSeqNO(Integer seqNO) {
		this.seqNO = seqNO;
	}

	/*******************************************
	 * Tree
	 ******************************************/

	@Override
	public String obtainTreeId() {
		return this.nodeID;
	}

	@Override
	public String obtainTreeText() {
		return this.nodeNam;
	}

	@Override
	public String obtainTreeParentID() {
		return parentID;
	}

	@Override
	public String obtainTreeState() {
		return null;
	}

	@Override
	public int obtainTreeSeqNO() {
		return seqNO==null?0:seqNO;
	}

	@Override
	public String obtainIconCls() {
		if(EnumRootNodeID.ROOTNODE.getCode().equals(parentID)){
			return null;
		}
		return "icon-page-red";
	}

	@Override
	public Map<String, String> obtainTreeAttributes() throws Exception {
		// 把其他属性放到map中
		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("nodeID", nodeID);
		attrMap.put("nodeNam", nodeNam);
		attrMap.put("seqNO", "" + seqNO);
		attrMap.put("parentID", parentID);
		return attrMap;
	}
}