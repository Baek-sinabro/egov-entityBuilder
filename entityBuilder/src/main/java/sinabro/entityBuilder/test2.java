/**
 * 
 */
package sinabro.entityBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
  * @FileName : test2.java
  * @Project : entityBuilder
  * @Date : 2021. 6. 18. 
  * @작성자 : sinabro
  * @변경이력 :
  * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
  */

public class test2 {
	
	public static void main(String[] args) throws Exception {
		
		int cnt = 0;

		String ss = "197311301812346";
		System.out.println(ss.substring(2));
		
	}
	
	public static void set(String...strs) {
		
		String msg = "인증번호[%s]가[]%s";
		
		System.out.println(String.format(msg, strs));
	}

}
