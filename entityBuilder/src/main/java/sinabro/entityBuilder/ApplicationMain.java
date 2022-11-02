/**
 * 
 */
package sinabro.entityBuilder;

import java.io.File;
import java.nio.file.Paths;

/**
  * @FileName : ApplicationMain.java
  * @Project : entityBuilder
  * @Date : 2021. 1. 7. 
  * @작성자 : sinabro
  * @변경이력 :
  * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
  */

public class ApplicationMain {
	
	
	 public static void main(String[] args) throws Exception {
		 
		 String path = "";
		 String thisPath = System.getProperty("user.dir");
		 
		 Config config = new Config();
		 Generator gen = new Generator();
		 
		 if(args.length > 0 && args[0].matches(".*config.property$")) {
			 path = args[0];
		 } else {
			 path = Paths.get(thisPath , "config.property").toString();
			 if(!new File(path).exists()) {
				 config.saveConfig(new File(path));
				 System.out.println(String.format("%s 파일을 생성했습니다. 수정후 다시 시도하세요.", path));
				 return;
			 }
		 }
		 
		 config.loadConfig(new File(path));
		 gen = new Generator();
		 gen.genMybatisMapperXml(config, true);
		 gen.genEntityVO(config, true);
		 

		 System.out.println("완료");
		 

	 }

}
