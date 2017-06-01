package com.sense.frame.common.util;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.lang.StringUtils;

/**
 * 图片操作工具类：规划将通用的图片处理方法抽取到该类中
 */
public class PictureUtil {
	
	/**
	 * 按给定的长宽缩放图片
	 * @author zhangqh
	 * @param orginalImg 原图
	 * @param width 截取目标宽度
	 * @param height 截取目标高度
	 * @return 目标图片
	 */
	public static byte[] resizeImage(byte[] orginalImg, int width, int height) throws Exception{
		ByteArrayInputStream in = new ByteArrayInputStream(orginalImg);
		BufferedImage image = ImageIO.read(in);
		Builder<BufferedImage> builder = null;
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		if(imageWidth <= width && imageHeight <= height) {
			return orginalImg;
		}else{
			builder = Thumbnails.of(image).size(width, height);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(builder.asBufferedImage(), "jpg", out);
		return out.toByteArray();
	}
	
	/**
	 * 先按给定的长宽缩放图片，再以缩放后的图片中心为中心点截取给定的长宽
	 * @author zhangqh
	 * @param orginalImg 原图
	 * @param width 截取目标宽度
	 * @param height 截取目标高度
	 * @return 目标图片
	 */
	public static byte[] cutImage(byte[] orginalImg, int width, int height) throws Exception{
		ByteArrayInputStream in = new ByteArrayInputStream(orginalImg);
		BufferedImage image = ImageIO.read(in);
		Builder<BufferedImage> builder = null;
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		if(imageWidth <= width && imageHeight <= height) {
			return orginalImg;
		}else if(imageWidth <= width || imageHeight <= height){
			builder = Thumbnails.of(image).size(width, height);
		}else{
			float aimB = (float) width / height;
			float sourceB = (float) imageWidth / imageHeight;
			if(aimB > sourceB){
				image = Thumbnails.of(image).width(width).asBufferedImage(); 
			}else{
				image = Thumbnails.of(image).height(height).asBufferedImage(); 
			}
			builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, width, height).size(width, height);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(builder.asBufferedImage(), "jpg", out);
		return out.toByteArray();
	}
	
	/**
	 * 根据文件名判断是否是图片文件
	 * @author xfh
	 * @date 创建时间 2015-4-20
	 */
	public static boolean isPictureFormat(String fileName) {
		boolean rtn = false;
		if (StringUtils.isNotBlank(fileName)) {
			if (fileName.toUpperCase().indexOf("JPG") >= 0
					|| fileName.toUpperCase().indexOf("BMP") >= 0
					|| fileName.toUpperCase().indexOf("GIF") >= 0
					|| fileName.toUpperCase().indexOf("TIF") >= 0
					|| fileName.toUpperCase().indexOf("PNG") >= 0) {
				rtn = true;
			}
		}
		return rtn;
	}
	
	/**
	 * 判断文件是否为图片
	 * @author zhangqh
	 * @param is
	 * @return
	 */
	public static final boolean isImage(InputStream is) {
		boolean flag = false;
		try {
			BufferedImage bufreader = ImageIO.read(is);
			int width = bufreader.getWidth();
			int height = bufreader.getHeight();
			if (width == 0 || height == 0) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 图片旋转指定角度
	 * @author xfh
	 * @throws BusinessException
	 * @date 创建时间 2015-4-20
	 */
	public static byte[] rotatePic(byte[] picStream, String picformat,
			int rotate) throws Exception {
		picformat = picformat.replace(".", "").toUpperCase();
		if (rotate != 0 && isPictureFormat(picformat)) {
			try {
				// ------long start = System.currentTimeMillis();
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				BufferedImage src = toBufferedImage(picStream);
				// 经测试用Thumbnails的旋转方法的速度比较慢，所以用我们自己写的旋转方法
				// BufferedImage des
				// =Thumbnails.of(arc).scale(1.0).rotate(rotate).asBufferedImage();
				BufferedImage des = rotate(src, rotate);
				ImageIO.write(des, picformat, byteOut);
				picStream = byteOut.toByteArray();
				byteOut.close();
				// ------long end = System.currentTimeMillis();
			} catch (Exception ex) {
				throw new Exception("图片旋转时发生异常:" + ex.getMessage());
			}
		}
		return picStream;
	}

	/**
	 * 避免使用ImageIO.read()丟失圖片ICC信息，參照网上的帖子增加此方法，经验证可行
	 * http://www.oschina.net/question/1092_23668
	 * http://www.cnblogs.com/waya/archive/2008/11/04/1326402.html
	 * 
	 * @param oriImage
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage toBufferedImage(byte[] oriImage)
			throws Exception {
		Image image = Toolkit.getDefaultToolkit().createImage(oriImage);// 可讀取丟失ICC信息的圖片
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		image = new ImageIcon(image).getImage();
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			int transparency = Transparency.OPAQUE;
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null),
					image.getHeight(null), transparency);
		} catch (HeadlessException e) {
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			bimage = new BufferedImage(image.getWidth(null),
					image.getHeight(null), type);
		}

		int colorSpaceType = bimage.getColorModel().getColorSpace().getType();
		if (colorSpaceType == ColorSpace.TYPE_CMYK) {
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	/**
	 * 图像旋转
	 * 
	 * @param src
	 * @param angel
	 * @return
	 */
	private static BufferedImage rotate(Image src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		// calculate the new image size
		Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
				src_width, src_height)), angel);

		BufferedImage res = null;
		res = new BufferedImage(rect_des.width, rect_des.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = res.createGraphics();
		// transform
		g2.translate((rect_des.width - src_width) / 2,
				(rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

		g2.drawImage(src, null, null);
		return res;
	}

	private static Rectangle CalcRotatedSize(Rectangle src, int angel) {
		// if angel is greater than 90 degree, we need to do some conversion
		if (angel >= 90) {
			if (angel / 90 % 2 == 1) {
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}

		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);

		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
				- angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
				- angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;
		return new java.awt.Rectangle(new Dimension(des_width, des_height));
	}

	public static void main(String args[]) throws Exception {
		InputStream ins = new FileInputStream("d:\\20140721100000107091.jpg");
		byte[] inbyte = FileAndStreamUtil.InputStreamTOByte(ins);
		rotatePic(inbyte, ".jpg", 90);

	}

}