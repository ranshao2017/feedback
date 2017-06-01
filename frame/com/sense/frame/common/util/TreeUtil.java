package com.sense.frame.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.sense.frame.enumdic.EnumRootNodeID;
import com.sense.frame.pub.model.TreeModel;

/**
 * 构建树形结构： 树节点默认情况是如果有子阶段，state为close，叶子节点state为open 方法包括： 1.构造树
 * 2.构造带选择框的树（某些节点选择，某些不选中） 3.设置树的全部节点都打开 4.设置树展开到第几次的节点打开 5.为节点设置seqNO
 * 6.级联操作设置。
 */
public class TreeUtil{

	/**
	 * 返回json格式的树, 参数说明： 1.treeBeanList：需要构建成树的实体对象组成的list，该对象实现了treeBean接口
	 * 节点默认check=false 节点默认按照seqNO排序
	 */
	public static <T extends TreeBean> List<TreeModel> setTree(List<T> treeBeanList) throws Exception {
		return setTree(treeBeanList, "seqNO", false, null ,true);
	}

	/**
	 * 返回json格式的树, 参数说明： 1.treeBeanList：需要构建成树的实体对象组成的list，该对象实现了treeBean接口
	 * 2.orderColNam：TreeModel对象中用来排序的字段,
	 * 可以是TreeModel中的类变量，如id,text,seqNO,parentID，checked等
	 * 也可以是包含在attributes中的其它变量，如机构中orgCod。
	 * 3.isOrderColInAttrMap：用来排序的字段的位置，即是否在attributeMap中！ false代表不再map中
	 * 节点默认check=false
	 */
	public static <T extends TreeBean> List<TreeModel> setTree(
			List<T> treeBeanList, String orderColNam,
			boolean isOrderColInAttrMap) throws Exception {
		return setTree(treeBeanList, orderColNam, isOrderColInAttrMap, null,true);
	}

	/**
	 * 返回json格式的树， 设置树的哪些节点被选中，默认是都不选中 组织成有些节点被选中，有些没有被选中的格式
	 * 
	 * @param treeBeanList：整个树的List
	 * @param needChkList：需要被选中的节点的ID集合
	 */
	public static <T extends TreeBean> List<TreeModel> setTree(
			List<T> treeBeanList, List<String> needChkList) throws Exception {
		return setTree(treeBeanList, "seqNO", false, needChkList ,true);
	}
	
	/**
	 * 返回json格式的树， 设置树的哪些节点被选中，默认是都不选中 组织成有些节点被选中，有些没有被选中的格式
	 * 
	 * @param treeBeanList：整个树的List
	 * @param needChkList：需要被选中的节点的ID集合
	 */
	public static <T extends TreeBean> List<TreeModel> setTree(
			List<T> treeBeanList, List<String> needChkList,boolean frontPageCascadeFlag) throws Exception {
		return setTree(treeBeanList, "seqNO", false, needChkList ,frontPageCascadeFlag);
	}
	
	
	
	/**
	 * 返回json格式的树 参数说明： 1.treeBeanList：需要构建成树的实体对象组成的list，该对象实现了treeBean接口
	 * 2.orderColNam：TreeModel对象中用来排序的字段,
	 * 可以是TreeModel中的类变量，如id,text,seqNO,parentID，checked等
	 * 也可以是包含在attributes中的其它变量，如机构中orgCod。
	 * 3.isOrderColInAttrMap：用来排序的字段的位置，即是否在attributeMap中！ false代表不再map中
	 * 4.needChkList：设置树的哪些节点被选中，默认是都不选中 组织成有些节点被选中，有些没有被选中的格式
	 * 5.sortParentID  : 根节点ID
	 */
	public static <T extends TreeBean> List<TreeModel> setTree(List<T> treeBeanList, String orderColNam,
			boolean isOrderColInAttrMap, List<String> needChkList,boolean frontPageCascadeFlag ) throws Exception {
		boolean specialPid = true ;//pid既不是eamsroot，也不是virtualroot
		HashMap<String,String> sepceicalTreeIDAndPidList= new HashMap<String,String>();
		String sortParentID = EnumRootNodeID.ROOTNODE.getCode();
		if (orderColNam == null || orderColNam.equals("")) {
			throw new Exception("排序列不能为空");
		}
		
		Map<String,TreeModel> allNodeMap = new HashMap<String,TreeModel>();
		List<TreeModel> allNodeList = new ArrayList<TreeModel>();
		List<TreeModel> firstNodeList = new ArrayList<TreeModel>();

		boolean VirtueRoot = false;
		TreeModel VirtueRootTreeModel = null;
		
		if (treeBeanList != null && treeBeanList.size() > 0) {
			TreeModel treeModel = null;
			for (int i = 0; i < treeBeanList.size(); i++) {
				String pid = ((TreeBean) treeBeanList.get(i)).obtainTreeParentID();
				treeModel = new TreeModel((TreeBean) treeBeanList.get(i));// 构造treeModel
				if (isOrderColInAttrMap) {
					treeModel.setOrderColCont(treeModel.getAttributes()
						.get(orderColNam));
				}

				if (pid == null
						|| pid.equals("")
						|| pid.trim()
							.equals(EnumRootNodeID.ROOTNODE.getCode())) {
					// 第一级节点为实际数据库的数据
					treeModel.setParentID(EnumRootNodeID.ROOTNODE.getCode());
					firstNodeList.add(treeModel);
					specialPid= false;
				} else if (pid.trim()
					.equals(EnumRootNodeID.VIRTUEROOTNODE.getCode())) {
					// 第一级节点为虚拟出的根节点
					VirtueRoot = true;
					VirtueRootTreeModel = treeModel;
					specialPid= false;
				}
				allNodeList.add(treeModel);
				allNodeMap.put(treeModel.getId(), treeModel);
			}

			// 如果是有传入虚拟根节点，则第一级节点为虚拟根
			if (VirtueRoot) {
				firstNodeList = new ArrayList<TreeModel>();
				firstNodeList.add(VirtueRootTreeModel);
				sortParentID = EnumRootNodeID.VIRTUEROOTNODE.getCode();
			}
			
			// 如果是特殊根节点
			if (specialPid) {
				HashMap<String,TreeBean> sepceicalTreeList= new HashMap<String,TreeBean>();
				
				for (TreeBean specialTreeModel : treeBeanList) {
					sepceicalTreeList.put(specialTreeModel.obtainTreeId(), specialTreeModel);
					sepceicalTreeIDAndPidList.put(specialTreeModel.obtainTreeId(), specialTreeModel.obtainTreeParentID());
				}
				firstNodeList = new ArrayList<TreeModel>();
				for(String id:getFirstNodeIDList(treeBeanList)){
					TreeModel specialTreeModel = allNodeMap.get(id);
					specialTreeModel.setParentID(EnumRootNodeID.ROOTNODE.getCode());
					firstNodeList.add(specialTreeModel);
				}
			}

			// 1.设置排序字段
			if (isOrderColInAttrMap) {
				orderColNam = "orderColCont";
			}

			// 2. 整理allMenuMap,进行排序组成<父ID，list<子菜单>>
			Map<String, List<TreeModel>> arrangeNodeMap = new HashMap<String, List<TreeModel>>();
			sortMenuList(sortParentID, allNodeList, arrangeNodeMap, orderColNam);

			// 3.为一级菜单设置child
			SortUtil.sortList(firstNodeList, orderColNam, 1);// 对一组list进行排序
			sortFirstMenuList(firstNodeList, arrangeNodeMap, needChkList,frontPageCascadeFlag);
		}
		
		// 如果是特殊根节点
		if (specialPid) {
			for(TreeModel tmp: firstNodeList){
				tmp.setParentID(sepceicalTreeIDAndPidList.get(tmp.getId()));//把parentid重设回来
			}
		}
		return firstNodeList;
	}

	/**
	 * 返回树的打开层次 num表示要打开的层次 1表示第一级节点打开
	 */
	public static List<TreeModel> setTreeOpenLevel(
			List<TreeModel> treeModelList, int num) {
		if (num > 0) {
			num--;
			for (TreeModel treeModel : treeModelList) {
				List<TreeModel> childModelList = treeModel.getChildren();
				if (childModelList != null && childModelList.size() > 0) {
					treeModel.setState("open");
					setTreeOpenLevel(childModelList, num);
				}
			}
		}
		return treeModelList;
	}

	/**
	 * 把树的所有节点设置为打开
	 * 
	 * @param treeModelList
	 * @return
	 */
	public static List<TreeModel> setAllNodesOpen(List<TreeModel> treeModelList) {

		if(treeModelList!=null){
			for (TreeModel treeModel : treeModelList) {
				List<TreeModel> childModelList = treeModel.getChildren();
				if (childModelList != null && childModelList.size() > 0) {
					treeModel.setState("open");
					setAllNodesOpen(childModelList);
				}
			}
		}
		
		return treeModelList;
	}

	/**
	 * 整理allMenuMap,进行排序组成<父ID，list<子菜单>>
	 * 
	 * @param parent
	 * @param allNodeList
	 * @param allNodeMap
	 */
	private static void sortMenuList(String parent,
			List<TreeModel> allNodeList,
			Map<String, List<TreeModel>> allNodeMap, String orderColNam) throws Exception {
		// 验证parent是否为"",
		if (parent == null || parent.equals("")) {
			throw new Exception("父节点ID为空");
		}

		// 验证allNodeList
		List<TreeModel> sunMenuList = new ArrayList<TreeModel>();

		// 获取子菜单
		for (TreeModel menuModel : allNodeList) {
			if (menuModel.getParentID().equals(parent))
				sunMenuList.add(menuModel);
		}

		if (sunMenuList != null && sunMenuList.size() > 0) {
			// 排序子菜单
			sunMenuList = SortUtil.sortList(sunMenuList, orderColNam, 1);// 对一组list进行排序
			allNodeMap.put(parent, sunMenuList);
			// 循环递归子菜单
			for (TreeModel menuModel : sunMenuList) {
				String tempPare = menuModel.getId();
				sortMenuList(tempPare, allNodeList, allNodeMap, orderColNam);
			}
		}
	}

	/**
	 * 为一级菜单设置child
	 * frontPageCascadeFlag 前台页面的级联是否关闭或者启用，涉及到树的展示。add by qc
	 * @param firstMenuList
	 * @param allMenuMap
	 */
	private static void sortFirstMenuList(List<TreeModel> firstNodeList,
			Map<String, List<TreeModel>> allNodeMap, List<String> needChkList,boolean frontPageCascadeFlag) {

		for (int i = 0; i < firstNodeList.size(); i++) {

			List<TreeModel> childList = allNodeMap.get(firstNodeList.get(i).getId());
			
			//如果前台关闭了级联，则所有node都要正常设置checked状态 ；
			//如果开启了级联，则只需要未级node设置check状态
			if(!frontPageCascadeFlag){
				if (needChkList != null
						&& needChkList.contains(firstNodeList.get(i).getId())) {
					firstNodeList.get(i).setChecked(true);
				}
			}

			if (childList != null && childList.size() > 0) {

				// 有child，则把父级的状态设置为closed
				firstNodeList.get(i).setState("closed");

				// 设置children
				firstNodeList.get(i).setChildren(childList);
				//
				sortFirstMenuList(firstNodeList.get(i).getChildren(), allNodeMap, needChkList,frontPageCascadeFlag);
			} else {

				if(frontPageCascadeFlag){
					// 没有child,设置叶子节点的状态是选中的
					if (needChkList != null
							&& needChkList.contains(firstNodeList.get(i).getId())) {
						firstNodeList.get(i).setChecked(true);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * 方法简介:设置treeModel的节点选择状态
	 * 
	 * @tree 已经组织好的tree
	 * @return 关键字   说明
	 * @throws 异常说明   发生条件
	 * @author qinchao
	 * @date 创建时间 2014-4-21
	 */
	public static  void setTreeModelCheckedNode(List<TreeModel> tree, List<String> needCheckedIds){
		if(CollectionUtils.isEmpty(tree) || CollectionUtils.isEmpty(needCheckedIds))
			return;
		for(TreeModel treeModel : tree){
			List<TreeModel> childTree = treeModel.getChildren();
			if(CollectionUtils.isNotEmpty(childTree)){
				setTreeModelCheckedNode(childTree, needCheckedIds);
			}else{
				if(needCheckedIds.contains(treeModel.getId())){
					treeModel.setChecked(true);
				}
			}
		}
	}

	/**
	 * 设置树级联cascade 如果子节点所有的节点都是checked状态，那么父节点才能是checked， 否则父节点的状态必须是unchecked
	 * 
	 * @author qinchao
	 * @date 创建时间 2013-9-2
	 */
	/*private static boolean setCascade(List<TreeModel> treeList) {
		boolean allChildrenNodeSelected = true; // 所有子节点都被选中了（包括直接和非直接子节点）
		for (TreeModel tree : treeList) {

			if (tree.isChecked()) {
				// 只有树的节点被选中时，才去判断递归，构造根本不用递归，效率提高了很多

				List<TreeModel> childTreeList = tree.getChildren();

				if (childTreeList != null && childTreeList.size() > 0) {
					allChildrenNodeSelected = setCascade(childTreeList);

					for (TreeModel childTree : childTreeList) {
						allChildrenNodeSelected = allChildrenNodeSelected
								&& childTree.isChecked();
					}
				} else {
					allChildrenNodeSelected = tree.isChecked();
				}

				if (!allChildrenNodeSelected) {
					tree.setChecked(false);
				}
			}

		}

		return allChildrenNodeSelected;
	}*/

	/*
	 * public static void main(String[] s) throws Exception{ List ls = new
	 * ArrayList(); ls.add("1"); }
	 */
	/**
	 * 合并两个树： 如果没有子tree，则过滤掉父节点
	 * @param parentTreeList机构树构成的list，用来做非叶子节点，
	 *            leafTreeList：用户或者岗位树构成的list，用来作为叶子节点
	 * @author rbn
	 * @date 创建时间 2013-9-4
	 */
	public static List<TreeModel> mergeTree(List<TreeModel> parentTreeList, List<TreeModel> leafTreeList) throws Exception {
		if(CollectionUtils.isEmpty(parentTreeList)){
			return new ArrayList<TreeModel>();
		}
		if(CollectionUtils.isEmpty(leafTreeList)){
			return parentTreeList;
		}
		
		Iterator<TreeModel> iterator = parentTreeList.iterator();
		while (iterator.hasNext()) {
			TreeModel treeModel = iterator.next();

			// 保持有child的节点被关闭，没有child的节点被过滤掉了，add by qc
			treeModel.setState("closed");

			List<TreeModel> childrenModels = treeModel.getChildren();
			List<TreeModel> treeModelist = findTreeModel(leafTreeList, treeModel.getId());
			if (treeModelist.size() > 0) {
				treeModel.setChildren(treeModelist);
				if (childrenModels != null && childrenModels.size() > 0) {
					treeModel.addTreeModelList((mergeTree(childrenModels, leafTreeList)));
					if (treeModel.getChildren().size() == 0
							|| treeModel.getChildren() == null) {
						iterator.remove();
					}
				} else {
					String orgID = treeModel.getId();
					List<TreeModel> list = findTreeModel(leafTreeList, orgID);
					if (list.size() > 0) {
						treeModel.setChildren(list);
					} else {
						iterator.remove();
					}
				}
			} else {
				if (childrenModels != null && childrenModels.size() > 0) {
					treeModel.setChildren(mergeTree(childrenModels, leafTreeList));
					if (treeModel.getChildren().size() == 0
							|| treeModel.getChildren() == null) {
						iterator.remove();
					}
				} else {
					String orgID = treeModel.getId();
					List<TreeModel> list = findTreeModel(leafTreeList, orgID);
					if (list.size() > 0) {
						treeModel.setChildren(list);
					} else {
						iterator.remove();
					}
				}
			}
		}
		return parentTreeList;
	}
	
	/**
	 * 合并两个树：父节点返回全部
	 * @author rbn
	 * @date 创建时间 2013-9-4
	 */
	/*public static List<TreeModel> mergeTreeWithAllParent(List<TreeModel> parentTreeList,
			List<TreeModel> leafTreeList) throws Exception {
		if(leafTreeList==null||leafTreeList.size()<=0){
			return parentTreeList ;
		}else if(parentTreeList==null||parentTreeList.size()<=0){
			return leafTreeList ;
		}
		Iterator<TreeModel> iterator = parentTreeList.iterator();
		while (iterator.hasNext()) {
			TreeModel treeModel = iterator.next();
			// 保持有child的节点被关闭，没有child的节点被过滤掉了，add by qc
			treeModel.setState("closed");
			List<TreeModel> childrenModels = treeModel.getChildren();
			if (childrenModels == null||childrenModels.size() <= 0) {
				List<TreeModel> treeModelist = findTreeModel(leafTreeList, treeModel.getId());
				if (treeModelist.size() > 0) {
					treeModel.addTreeModelList(treeModelist);
				}
			}
		}
		return parentTreeList;
	}
*/

	
	/**
	 * 方法简介:获取可以作为一级节点的node的IDlist
	 * <p>
	 * 方法详述
	 * </p>
	 * 某个list可能一级节点的父节点并不是eamsroot，
	 * 
	 * @author qinchao
	 * @date 创建时间 2014-4-2
	 */
	public static  <T extends TreeBean>  List<String> getFirstNodeIDList(List<T> treeBeanList) throws Exception {
		// 1.先把每个节点的id放到set中
		HashSet<String> nodeIDSet = new HashSet<String>();
		List<String> rtnFistNodeIDList = new ArrayList<String>();
		
		for (TreeBean treeModel : treeBeanList) {
			String nodeID = treeModel.obtainTreeId();// ID一定不是空的
			if (StringUtils.isNotEmpty(nodeID)) {
				nodeIDSet.add(nodeID);
			}
		}

		String nodePID = "";
		for (TreeBean treeModel : treeBeanList) {
			nodePID = treeModel.obtainTreeParentID();
			if (StringUtils.isEmpty(nodePID)|| nodePID.equals(EnumRootNodeID.ROOTNODE.getCode())|| nodePID.equals(EnumRootNodeID.VIRTUEROOTNODE.getCode())) {
				
				rtnFistNodeIDList.add(treeModel.obtainTreeId());

			} else if(!nodeIDSet.contains(nodePID)){
				
				rtnFistNodeIDList.add(treeModel.obtainTreeId());
			}
		}

		return rtnFistNodeIDList ;
	}
    /**
     * 
     * 方法简介.
     * 查找treeModels是否存在findID，如果存在返回一个包含findID的TreeModel集合
     * <p>方法详述</p>
     *
     * @param 关键字   说明
     * @return 关键字   说明
     * @throws 异常说明   发生条件
     * @author rbn
     * @date 创建时间 2013-9-4
     * @since V1.0
     */
	public static List<TreeModel> findTreeModel(List<TreeModel> treeModels,String findID)throws Exception{
		List<TreeModel> treeModeList = new ArrayList<TreeModel>();
		for(TreeModel treeModel:treeModels){
			if(treeModel.getAttributes().containsValue(findID)){
				treeModeList.add(treeModel);
			}
		}		
		return treeModeList; 
	}
}
