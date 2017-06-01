package com.sense.frame.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 类描述:生成验证码
 * @author qinchao 创建时间 2014-8-14
 */
public class ValidateCodeHelper {
	
	public static String VALIDATE_CODE = "validate_code";

	// 验证码图片的宽度。
	private int width = 200;
	// 验证码图片的高度。
	private int height = 50;
	// 验证码字符个数
	private int codeCount = 5;

	private int codeX = 0;
	// 字体高度
	private int fontHeight;
	private int codeY;

	// 字体颜色是否随机：默认黑色，
	private boolean randomFontColor = false;

	char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	// 构造函数：指定图片的宽度、高度和验证字符数量 ,字体颜色随机
	public ValidateCodeHelper(int imgWidth, int imgHeight, int imgCodeCount,
			boolean imgFontColorIsRandom) {
		this.width = imgWidth;
		this.height = imgHeight;
		this.codeCount = imgCodeCount;
		this.randomFontColor = imgFontColorIsRandom;

		codeX = width / (codeCount + 1);
		fontHeight = height - 2;
		codeY = height - 4;
	}

	// 构造函数：指定图片的宽度、高度和验证字符数量，字体颜色为黑色
	public ValidateCodeHelper(int imgWidth, int imgHeight, int imgCodeCount) {
		this(imgWidth, imgHeight, imgCodeCount, false);
	}

	// 构造函数：指定验证码数字个数
	public ValidateCodeHelper(int imgCodeCount) {
		this(200, 50, imgCodeCount);
	}

	// 构造函数:字体颜色是否随机
	public ValidateCodeHelper(boolean imgFontColorIsRandom) {
		this(200, 50, 5, imgFontColorIsRandom);
	}

	// 默认构造函数
	public ValidateCodeHelper() {
		this(false);
	}

	/**
	 * 产生随机验证码值
	 * 
	 * @author qinchao
	 * @date 创建时间 2014-9-25
	 */
	public String generateValidateCode() throws Exception {
		// 创建一个随机数生成器类
		Random random = new Random();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			sb.append(String.valueOf(codeSequence[random.nextInt(36)]));
		}
		return sb.toString();
	}

	/**
	 * 自动生成随机验证码，并保存在session中， 并在浏览器端生成随机验证码图片
	 * @sessionAttrName 需要在session中保存的属性名
	 * @author qinchao
	 * @date 创建时间 2014-9-25
	 */
	public void generateValidateCodeImg(HttpServletRequest request,
			HttpServletResponse response, String sessionAttrName)
			throws Exception {
		response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);

		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();

		// 创建一个随机数生成器类
		Random random = new Random();

		// 将图像填充为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		// 设置字体。
		g.setFont(font);

		// 画边框。
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);

		// 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
		g.setColor(Color.BLACK);
		for (int i = 0; i < 160; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			if (randomFontColor) {
				red = random.nextInt(255);
				green = random.nextInt(255);
				blue = random.nextInt(255);
			} else {
				red = 0;
				green = 0;
				blue = 0;
			}

			// 用随机产生的颜色将验证码绘制到图像中。
			g.setColor(new Color(red, green, blue));
			g.drawString(strRand, (i + 1) * codeX, codeY);

			// 将产生的四个随机数组合在一起。
			randomCode.append(strRand);
		}

		HttpSession session = request.getSession();
		session.setAttribute(sessionAttrName, randomCode.toString());

		ImageIO.write(buffImg, "jpeg", response.getOutputStream());// 将内存中的图片通过流动形式输出到客户端
	}

	/**
	 * 根据传入的字符串内容自动生成验证 并在浏览器端预览验证码图片
	 * @sessionAttrName 需要在session中保存的属性名
	 * @author qinchao
	 * @date 创建时间 2014-9-25
	 */
	public void viewValidateCodeImg(HttpServletRequest request,
			HttpServletResponse response, String codeValue,
			String sessionAttrName) throws Exception {
		response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		if (codeValue == null || codeValue.length() != codeCount) {
			throw new Exception("预览随机验证码出错，验证码内容与要求的字符数量不符合！");
		}
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();

		// 创建一个随机数生成器类
		Random random = new Random();

		// 将图像填充为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		// 设置字体。
		g.setFont(font);

		// 画边框。
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);

		// 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
		g.setColor(Color.BLACK);
		for (int i = 0; i < 160; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		int red = 0, green = 0, blue = 0;

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			String strRand = String.valueOf(codeValue.charAt(i));
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			if (randomFontColor) {
				red = random.nextInt(255);
				green = random.nextInt(255);
				blue = random.nextInt(255);
			} else {
				red = 0;
				green = 0;
				blue = 0;
			}

			// 用随机产生的颜色将验证码绘制到图像中。
			g.setColor(new Color(red, green, blue));
			g.drawString(strRand, (i + 1) * codeX, codeY);
		}

		HttpSession session = request.getSession();
		session.setAttribute(sessionAttrName, codeValue);

		ImageIO.write(buffImg, "jpeg", response.getOutputStream());// 将内存中的图片通过流动形式输出到客户端
	}

	public void viewValidateCodeImg(HttpServletRequest request, HttpServletResponse response, String validateCode) {
		
	}

}