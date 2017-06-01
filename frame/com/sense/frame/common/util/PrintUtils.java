package com.sense.frame.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleExporterInputItem;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.apache.commons.io.IOUtils;

/**
 * 使用jasperReport做报表时的工具支持类. 有两个用途,生成jasperPrint对象,和设置导出时的session add by qinchao
 */
public class PrintUtils {
	
	/**
	 * 以附件下载或者以pdf文件的方式展示
	 * @attachmentFlag 是否以附件下载的方式出现
	 */
	public static OutputStream getOsFromRespone(HttpServletResponse response,String attachmentName,boolean attachmentFlag)throws Exception {
		response.reset();
		if(attachmentFlag){
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(attachmentName+"-"+DateUtil.dateTimeToDateTimeBox(new Date()), "UTF-8") + ".pdf");
		}else{
			response.setContentType("application/pdf");
		}
		OutputStream os = response.getOutputStream();
		return os;
	}
	
	/**
	 * 以附件下载或者以html流的方式展示
	 * @attachmentFlag 是否以附件下载的方式出现
	 */
	public static OutputStream getOsFromResponeHtml(HttpServletResponse response)throws Exception {
		response.reset();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		OutputStream os = response.getOutputStream();
		return os;
	}
	
	/**
	 * 获取模板的二进制内容
	 */
	public static byte[] getJapserModule(HttpServletRequest request, String jrModNam) throws Exception {
		ServletContext context = request.getSession().getServletContext();
		//无论tomcat还是weblogic都用以下方式获取
		InputStream japerModIS = context.getResourceAsStream("jr/" + jrModNam + ".jasper");
		if(null == japerModIS){
			japerModIS = IOUtils.toInputStream(context.getRealPath("/")+"/jr/" + jrModNam + ".jasper");
		}
		/*if ("WEBLOGIC".equals(deploy_server_type.toUpperCase())){
			
		}else{
			japerModIS = IOUtils.toInputStream(context.getRealPath("/")+"/jr/" + jrModNam + ".jasper");
		}*/
		return IOUtils.toByteArray(japerModIS);
	}

	// 多模板打印，套打
	public static void exportPdfStreamWithMultipleJp(List<JasperPrint> jprinterList, OutputStream os) throws Exception {
		JRPdfExporter ep = new JRPdfExporter();
		
		List<ExporterInputItem> itemList =new ArrayList<ExporterInputItem>();
		for(JasperPrint jp :jprinterList){
			itemList.add(new SimpleExporterInputItem(jp))  ;
		}
		ep.setExporterInput(new SimpleExporterInput(itemList));
		ep.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
		ep.exportReport();
	}

	/**
	 * 需要添加jackson-core-2.5.0.jar ，jackson-annotations-2.5.0.jar ，jackson-databind-2.5.0.jar
	 * 但是jackson-core-asl-1.9.3.jar还需要保留
	 * 因为 1.9 版本的包结构  org.codehaus.jackson
	 *     2.5版本的包结构  com.fastxml.jackson.core
	 * @param jprinter
	 * @param os
	 * @throws Exception
	 */
	public static void exportHtmlStreamWithSingleJp(JasperPrint jprinter, OutputStream os) throws Exception {
		HtmlExporter ep = new HtmlExporter();
		ep.setExporterInput(new SimpleExporterInput(jprinter));
		ep.setExporterOutput(new SimpleHtmlExporterOutput(os));
		ep.exportReport();
	}
	
	// 多模板打印，套打
	public static void exportHtmlStreamWithMultipleJp(List<JasperPrint> jprinterList, OutputStream os) throws Exception {
		HtmlExporter ep = new HtmlExporter();
		
		List<ExporterInputItem> itemList =new ArrayList<ExporterInputItem>();
		for(JasperPrint jp :jprinterList){
			itemList.add(new SimpleExporterInputItem(jp))  ;
		}
		ep.setExporterInput(new SimpleExporterInput(itemList));
		ep.setExporterOutput(new SimpleHtmlExporterOutput(os));
		ep.exportReport();
	}
	
	/**
	 * 存在一个bug，如果datalist的size为0时，不输出全部内容。
	 * 已修复：修改模板page的属性
	 */
	public static JasperPrint organJprinterWithMapList(byte[] jasperModuleCont,
			Map<String, Object> params, List<Map<String, ?>> datalist)
			throws Exception {
		JRDataSource dataSource = new JRMapCollectionDataSource(datalist);
		JasperPrint jp = null;

		JasperReport jasperReport = null;
		InputStream inStream = null;
		try {
			inStream = new ByteArrayInputStream(jasperModuleCont);
			jasperReport = (JasperReport) JRLoader.loadObject(inStream);
			jp = JasperFillManager.fillReport(jasperReport, params, dataSource);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException ex) {
					throw new Exception(ex);
				}
			}
		}
		return jp;
	}
	
	public static JasperPrint organJprinterWithBeanList(byte[] jasperModuleCont, Map<String, Object> params, List<?> datalist)
			throws Exception {
		JRDataSource dataSource = new JRBeanCollectionDataSource(datalist);
		JasperPrint jp = null;

		InputStream inStream = null;
		try {
			inStream = new ByteArrayInputStream(jasperModuleCont);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inStream);
			jp = JasperFillManager.fillReport(jasperReport, params, dataSource);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException ex) {
					throw new Exception(ex);
				}
			}
		}
		return jp;
	}
	
}