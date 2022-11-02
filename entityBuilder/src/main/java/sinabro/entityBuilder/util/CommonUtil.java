/**
 * 
 */
package sinabro.entityBuilder.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import sinabro.entityBuilder.ApplicationMain;

/**
  * @FileName : CommonUtil.java
  * @Project : entityBuilder
  * @Date : 2022. 7. 28. 
  * @작성자 : sinabro
  * @변경이력 :
  * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
  */

public class CommonUtil {
	
	public static String toCamelcase(String org) {
		if (org.indexOf('_') < 0 && Character.isLowerCase(org.charAt(0))) {
			return org;
		}
		StringBuilder result = new StringBuilder();
		boolean nextUpper = false;
		int len = org.length();

		for (int i = 0; i < len; i++) {
			char currentChar = org.charAt(i);
			if (currentChar == '_') {
				nextUpper = true;
			} else {
				if (nextUpper) {
					result.append(Character.toUpperCase(currentChar));
					nextUpper = false;
				} else {
					result.append(Character.toLowerCase(currentChar));
				}
			}
		}
		return result.toString();
	}

}
