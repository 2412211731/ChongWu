package com.chongwu.utils.common;
/**
 * <p>
 * Title: PinyinConvertor.java
 * </p>
 * <p>
 * Description: 汉字拼音首字符提取
 * </p>
 * <p>
 * Copyright:Copyright(c)2012
 * </p>
 * <p>
 * Company: CDSF
 * </p>
 * <p>
 * CreateTime:2012-8-22 下午09:10:20
 * </p>
 * 
 * @author XYW
 * @version 1.0
 * @bugs 不支持多音字处理
 */
public class PinyinConvertor {

	// 简体中文的编码范围从B0A1（45217）一直到F7FE（63486）
	private static int BEGIN = 45217;
	private static int END = 63486;

	// 按照声 母表示，这个表是在GB2312中的出现的第一个汉字，也就是说“啊”是代表首字母a的第一个汉字。
	// i, u, v都不做声母, 自定规则跟随前面的字母

	private static char[] chartable = { '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈',
			'哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌',
			'塌', '挖', '昔', '压', '匝' };

	// 二十六个字母区间对应二十七个端点
	// GB2312码汉字区间十进制表示
	private static int[] table = new int[27];

	// 对应首字母区间表
	private static char[] initialtable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'T', 'T', 'W', 'X', 'Y', 'Z' };

	// 初始化
	static {
		for (int i = 0; i < 26; i++) {
			// 得到GB2312码的首字母区间端点表，十进制
			table[i] = gbValue(chartable[i]);
		}
		table[26] = END;// 区间表结尾
	}

	// ------------------------public方法区------------------------

	/**
	 * 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串 最重要的一个方法，思路如下：一个个字符读入、判断、输出
	 * 
	 * @param sourceStr
	 * @param length
	 *            返回长度：为0时只返回第一个汉字首字母，为1时返回每个汉字的首字母。
	 * @return
	 */
	public static String cn2py(String sourceStr, int length) {
		if(sourceStr == null || sourceStr.equals("")){
			return "#";
		}
		
		String result = "";
		int StrLength = sourceStr.length();
		try {
			for (int i = 0; i < StrLength; i++) {
				result += char2InitialSpecial(sourceStr.charAt(i));
			}
		} catch (Exception e) {
			result = "#";
		}

		if (length == 0) {
			return result.length() > 1 ? result.substring(0, 1) : result;
		} else {
			return result;
		}
	}

	// ------------------------private方法区------------------------

	/**
	 * 输入字符,得到他的声母,英文字母返回对应的大写字母,其他非简体汉字返回 '#'
	 */
	private static char char2InitialSpecial(char ch) {

		// 对英文字母的处理：小写字母转换为大写，大写的直接返回
		if (ch >= 'a' && ch <= 'z')
			return (char) (ch - 'a' + 'A');
		if (ch >= 'A' && ch <= 'Z')
			return ch;
		// 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内，
		// 若不是，则直接返回。
		// 若是，则在码表内的进行判断。
		int gb = gbValue(ch);// 汉字转换首字母
		// 在码表区间之前，直接返回
		if ((gb < BEGIN) || (gb > END)) {
			return '#';
		}

		int i;
		for (i = 0; i < 26; i++) {// 判断匹配码表区间，匹配到就break,判断区间形如“[,)”
			if ((gb >= table[i]) && (gb < table[i + 1]))
				break;
		}

		if (gb == END) {// 补上GB2312区间最右端
			i = 25;
		}

		return initialtable[i]; // 在码表区间中，返回首字母
	}

	/**
	 * 
	 * 取出汉字的编码，将一个汉字（GB2312）转换为十进制表示
	 */
	private static int gbValue(char ch) {
		String str = new String();
		str += ch;
		try {
			byte[] bytes = str.getBytes("GB2312");
			if (bytes.length < 2)
				return 0;
			return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
		} catch (Exception e) {
			return 0;
		}
	}
}
