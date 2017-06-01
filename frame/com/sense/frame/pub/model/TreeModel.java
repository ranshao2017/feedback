package com.sense.frame.pub.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sense.frame.common.util.TreeBean;

/**
 * 统一的树模型
 */
public class TreeModel {

	//树ID
    private String id;
	
	//树名称
	private String text;
	
	// 父级ID
	private String parentID;
	
	//打开或者关闭
	private String state;
		
	//顺序号
	private int seqNO;
	
	//是否被选中
	private boolean checked;

	//图标
	private String iconCls;
	
	//排序字段内容：作用是用来存储排序内容的 add by qinchao
	private String orderColCont;
	
	//其他属性
	private Map<String, String> attributes =new HashMap<String, String>();

	//子节点
    private List<TreeModel> children = new ArrayList<TreeModel>();
	
	/**
	 * 构造函数
	 * @return
	 */
	public TreeModel(TreeBean treeBean) throws Exception{
		this.id = treeBean.obtainTreeId();
		this.text = treeBean.obtainTreeText();
		this.parentID = treeBean.obtainTreeParentID();
		this.state = treeBean.obtainTreeState();
		this.seqNO = treeBean.obtainTreeSeqNO();
		this.attributes = treeBean.obtainTreeAttributes();
		this.checked = false;
		this.iconCls = treeBean.obtainIconCls();		
	}
	
	/**
	 * 空构造函数
	 * @return
	 */
	public TreeModel() throws Exception{
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getSeqNO() {
		return seqNO;
	}

	public void setSeqNO(int seqNO) {
		this.seqNO = seqNO;
	}

	public List<TreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<TreeModel> children) {
		this.children = children;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
 
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}	
	
	public void addTreeModelList(List<TreeModel> treeModels){
		this.children.addAll(treeModels);
	}
	
    public String getOrderColCont() {
		return orderColCont;
	}

	public void setOrderColCont(String orderColCont) {
		this.orderColCont = orderColCont;
	}
}