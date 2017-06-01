package com.sense.frame.common.util;

/**
 * 分页实现类
 */
public class SplitPage {
  private static int DEFAULT_PAGESIZE = 10; //默认页面大小
  private static int DEFAULT_PAGE = 1; //默认第一页
  private int currentpage = 1;
  private int rowcount = 0;
  private int pagecount = 0;
  private int pagesize = DEFAULT_PAGESIZE;
  private int startrow = 0;
  /**
 * @return the pagesize
 */
public int getPagesize() {
	return pagesize;
}

/**
 * @param pagesize the pagesize to set
 */
public void setPagesize(int pagesize) {
	this.pagesize = pagesize;
}

private Object queryClass = null;
  private Object queryClass2 = null;   //2007-12-20 By ChengWei
  public Object getQueryClass2() {
	  return queryClass2;
  }

  public void setQueryClass2(Object queryClass2) {
	  this.queryClass2 = queryClass2;
  }

  public SplitPage() {
  }
  
  public SplitPage(int count,int pagesize){
	  this.pagesize = pagesize;
	  rowcount = count;
	  // 计算页数
      pagecount = ( (rowcount % this.pagesize) != 0) ?
        (rowcount / this.pagesize + 1) : (rowcount / this.pagesize);
  }

  public SplitPage(int count) {
    rowcount = count;
    // 计算页数
    pagecount = ( (rowcount % this.pagesize) != 0) ?
        (rowcount / this.pagesize + 1) : (rowcount / this.pagesize);
  }
  
  /**
   *计算页数
   * @param count 条数
   */
  public void reInit(int count){
	    rowcount = count;
	    // 计算页数
	    pagecount = ( (rowcount % this.pagesize) != 0) ?
	        (rowcount / this.pagesize + 1) : (rowcount / this.pagesize);	  
  }

  public Object getQueryClass() {
    return this.queryClass;
  }

  public void setQueryClass(Object queryClass) {
    this.queryClass = queryClass;
  }

  /**
   * 获得开始的行数
   * @return int
   */
  public int getStartRow() {
    if (currentpage > pagecount)
      currentpage = pagecount;
    if (currentpage < 1)
      currentpage = 1;
    startrow = (currentpage - 1) * pagesize ;
    return startrow;
  }

  public int getCurrentPage() {
    return this.currentpage;
  }

  public void setCurrentPage(String currentpage) {
    if (currentpage.trim().length() == 0) {
      this.currentpage = DEFAULT_PAGE;
    }
    else {
      try {
        this.currentpage = Integer.parseInt(currentpage);
      }
      catch (Exception ex) {
        this.currentpage = DEFAULT_PAGE;
      }
    }
    if (this.currentpage < 1) {
      this.currentpage = DEFAULT_PAGE;
    }
    if (this.currentpage > pagecount) {
      this.currentpage = pagecount;
    }

  }
  
  public void updatePageCount(){
	  pagecount = ( (rowcount % this.pagesize) != 0) ?
		        (rowcount / this.pagesize + 1) : (rowcount / this.pagesize);
  }

  public int getPageSize() {
    return this.pagesize;
  }

  public void setPageSize(String pagesize) {
    if (pagesize.trim().length() == 0) {
      this.pagesize = DEFAULT_PAGESIZE;
    }
    else {
      try {
        this.pagesize = Integer.parseInt(pagesize);
      }
      catch (Exception ex) {
        this.pagesize = DEFAULT_PAGESIZE;
      }
    }
    if (this.pagesize < 1) {
      this.pagesize = DEFAULT_PAGESIZE;
    }
  }

  public int getRowCount() {
    return this.rowcount;
  }

  public int getPageCount() {
    return this.pagecount;
  }

  /**
   * 获得分页栏 ,参数actionName 是事件，事件和参数用?来连接如果记录数为0，<br>
   * 导航HTML语句返回空字符串
   * @param actionName 事件名称
   * @return String
   */
  public String getNavBar(String actionName) {
   
    if (rowcount == 0) {
      return "";
    }
    StringBuffer html = new StringBuffer();
    if (pagecount >= 1) {
      html.append("共" + this.getRowCount() + "条&nbsp;&nbsp;&nbsp;&nbsp;");
      html.append("第" + currentpage + "页/共" + pagecount + "页");
      if (pagecount == 1) {
        return html.toString();
      }
      else {
        // 生成“首页”和“上一页”导航链接
        if (this.currentpage >= 1) {
          //首页时，“首页”和“上一页”无链接
          if (currentpage == 1) {
            html.append("&nbsp;&nbsp;");
            html.append("首页");
            html.append("&nbsp;&nbsp;");
            html.append("上一页");
            html.append("&nbsp;&nbsp;");
          }
          else {
            html.append("&nbsp;&nbsp;<a href=\"");
            html.append("#");
            html.append("\" onclick='javascript:query(" + (1) +")'>");
            html.append("首页");
            html.append("</a>&nbsp;&nbsp;");

            html.append("<a id=pgup href=\"");
            html.append("#");
            html.append("\" onclick='javascript:query(" + (currentpage - 1) +")'>");
            html.append("上一页");
            html.append("</a>&nbsp;&nbsp;");
          }
        }

        // 生成“下一页”和“末页”导航链接
        if (this.currentpage <= pagecount) {
          //末页时，“下一页”和“末页”无链接
          if (currentpage == pagecount) {
            html.append("&nbsp;&nbsp;");
            html.append("下一页");
            html.append("&nbsp;&nbsp;");
            html.append("末页");
          }
          else {
            html.append("&nbsp;&nbsp;");
            html.append("<a id=pgdw href=\"");
            html.append("#");
            html.append("\" onclick='javascript:query(" + (currentpage + 1) +")'>");
            html.append("下一页");
            html.append("</a>&nbsp;&nbsp;");

            html.append("<a href=\"");
            html.append("#");
            html.append("\" onclick='javascript:query(" + (pagecount) +")'>");
            html.append("末页");
            html.append("</a>&nbsp;");
          }
        }

        //生成转向页面导航链接
        html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        html.append("第");
        html.append("\n");
        
        html.append("<input type='text' name='gotopagenum' id='gotopagenum' size='2' value='" + currentpage + "'>");
        
        html.append("页");       
        html.append("\n");
        html.append("<input type='button' value='跳转' onClick=\"MM_jumpMenu();\">");
        html.append("<script language=javascript>\n");
        
        html.append(" function MM_jumpMenu(){ //v3.0\n");
        html.append("	if(checkInputNumber(document.getElementById('gotopagenum').value)) {\n");
        html.append(" 		query(document.getElementById('gotopagenum').value);\n");
        html.append("	} else {\n");
        html.append("		alert('请输入一个有效的页数！');\n");
        html.append("	}\n");
        html.append("}\n");
        html.append(" function checkInputNumber(str) {\n");
        html.append("	var comp = '0123456789';\n");
        html.append("	var flag = true;\n");
        html.append("	for(var i=0; i<str.length; i++) {\n");
        html.append("		if(comp.indexOf(str.substr(i,1)) == -1) {\n");
        html.append("			flag = false;\n");
        html.append("			break;\n");
        html.append("		}\n");
        html.append("	}\n");
        html.append("	if(flag) {\n");
        html.append("		if(parseInt(str) <= 0 || parseInt(str) > " + pagecount + ") {\n");
        html.append("			flag = false;\n");
        html.append("		}\n");
        html.append("	}\n");
        html.append("	return flag;\n");
        html.append("}\n");
        
        
        html.append("</script>\n");
      }
    }
    return html.toString();
  }

public int getRowcount() {
	return rowcount;
}

public void setRowcount(int rowcount) {
	this.rowcount = rowcount;
}
}