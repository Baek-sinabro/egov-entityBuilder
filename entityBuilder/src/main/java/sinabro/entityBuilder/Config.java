/**
 * 
 */
package sinabro.entityBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import sinabro.entityBuilder.model.ConfigProperty;

/**
  * @FileName : Config.java
  * @Project : entityBuilder
  * @Date : 2021. 1. 7. 
  * @작성자 : sinabro
  * @변경이력 :
  * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
  */

public class Config {
	
	public static class ViewInfo {
		
		public void setViewPK(String viewName, String[] pkFieldNames) {
			List<String> pkList = new ArrayList<>();
			for(int i=0;i<pkFieldNames.length;i++) {
				pkList.add(pkFieldNames[i]);
			}
			viewMap.put(viewName, pkList);
		}
		
		public List<String> getListPk(String viewName){
			List<String> pkList = new ArrayList<>();
			if(viewMap.containsKey(viewName))
				pkList = (List<String>)viewMap.get(viewName);
			return pkList;
		}
		
		public boolean isView(String tableName) {
			return viewMap.containsKey(tableName);
				
		}
		
		Map<String,Object> viewMap = new HashMap<>();
	}
	
	enum ETargetFileType {TableName , TableClassName , TableCamelCase}
	
	ViewInfo viewInfo = new Config.ViewInfo();
	
	@ConfigProperty(value="db.driver" , desc="#database 연결 드라이버 클래스 이름")
	String dbDriverName;
	
	@ConfigProperty(value="db.connectionString" , desc="#database 연결문자열")
	String dbConUrl;
	
	@ConfigProperty(value="db.user" , desc="#database 사용자 아이디")
	String dbUser;
	
	@ConfigProperty(value="db.password" , desc="#database 비밀번호")
	String dbPassword;
	
	@ConfigProperty(value="db.type" , desc="#database 종류 (oracle,mssql)")
	String dbType = "oracle";
	
	@ConfigProperty(value="db.targetTableRegex" , desc="#엔티티 정보를 생성할 테이블명 (String.matched 정규식으로 표현)")
	String tableNamePatten;
	
	@ConfigProperty(value="entityVo.packageName" , desc="#생성할 vo packageName")
	String entityPackage;
	
	@ConfigProperty(value="entityVo.sqlSessionTemplateName" , desc="#mybatis sqlSessionTemplate 클래스명 지정하지 않으면 sqlSessionTemplate으로 생성됨.")
	String sqlSessionTemplateName="sqlSessionTemplate";
	
	@ConfigProperty(value="entityVo.serviceName" , desc="#엔티디에 접근하기 위한 @Service 클래스명 지정하지 않으면 EntityService로 생성됨.")
	String entityServiceName="EntityService";
	
	@ConfigProperty(value="entityVo.daoName" , desc="#엔티디에 접근하기 위한 @Component 클래스명 지정하지 않으면 EntityDAO로 생성됨.")
	String entityDAOName="EntityDAO";
	
	@ConfigProperty(value="entityVo.targetPath" , desc="#vo를 생성할 폴더경로")
	String entityTargetPath;
	
	@ConfigProperty(value="mapper.namespace" , desc="#mybatis mapper에 적용할 namespace명")
	String mapperNamespace;
	
	@ConfigProperty(value="mapper.targetPath" , desc="#mapper를 생성할 폴더경로")
	String mapperTargetPath;
	
	@ConfigProperty(value="system.tableName" , desc="#system(공통코드) 테이블명")
	String systemTable = "";
	
	@ConfigProperty(value="system.codeField" , desc="#system(공통코드) 필드명")
	String systemCodeField = "";
	
	@ConfigProperty(value="system.nameField" , desc="#system(공통코드) 필드이름")
	String systemNameField = "";
	
	@ConfigProperty(value="system.nameAliasSurffix" , desc="#system(공통코드) 필드 알리아스 접미사")
	String systemNameAliasSurffix = "NAME";
			
	
	String targetPath;
	
	String tamplateContent;
	
	ETargetFileType targetFileType;
	
	String targetFilePrefix = "";
	
	String targetFileSuffix = "";
	
	public void loadConfig(File file) throws Exception {
		
		Properties properties = readProperty(file);
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field field : fields) {
			ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
			if(annotation != null) {
				String key = annotation.value();
				String value = properties.getProperty(key);
				field.setAccessible(true);
				field.set(this, value);
			}
		}
	}
	
	public void saveConfig(File file) throws Exception {
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
		writer.write("# entityBuilder config 파일 (자동생성)\n\n");
		
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field field : fields) {
			field.setAccessible(true);
			ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
			if(annotation != null) {
				String key = annotation.value();
				String desc = annotation.desc();if(desc==null)desc="";
				String value = (String)field.get(this);if(value==null)value="";
				
				writer.write(String.format("%s\n" , desc));
				writer.write(String.format("%s = %s\n\n", key , value));
			}
		}
		writer.close();
	}
	
	protected Properties readProperty(File file) throws Exception {
		
		Properties properties = new Properties();
		properties.load(new FileReader(file));
		
		return properties;
	}
	
	
	public String getDbDriverName() {
		return dbDriverName;
	}
	public void setDbDriverName(String dbDriverName) {
		this.dbDriverName = dbDriverName;
	}
	public String getDbConUrl() {
		return dbConUrl;
	}
	public void setDbConUrl(String dbConUrl) {
		this.dbConUrl = dbConUrl;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public String getEntityPackage() {
		return entityPackage;
	}
	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}
	public String getMapperNamespace() {
		return mapperNamespace;
	}
	public void setMapperNamespace(String mapperNamespace) {
		this.mapperNamespace = mapperNamespace;
	}
	public String getTableNamePatten() {
		return tableNamePatten;
	}
	public void setTableNamePatten(String tableNamePatten) {
		this.tableNamePatten = tableNamePatten;
	}
	
	public ETargetFileType getTargetFileType() {
		return targetFileType;
	}
	public void setTargetFileType(ETargetFileType targetFileType) {
		this.targetFileType = targetFileType;
	}
	public String getTargetFilePrefix() {
		return targetFilePrefix;
	}
	public void setTargetFilePrefix(String targetFilePrefix) {
		this.targetFilePrefix = targetFilePrefix;
	}
	public String getTargetFileSuffix() {
		return targetFileSuffix;
	}
	public void setTargetFileSuffix(String targetFileSuffix) {
		this.targetFileSuffix = targetFileSuffix;
	}
	
	public ViewInfo getViewInfo() {
		return viewInfo;
	}
	public String getSqlSessionTemplateName() {
		return sqlSessionTemplateName;
	}
	public void setSqlSessionTemplateName(String sqlSessionTemplateName) {
		this.sqlSessionTemplateName = sqlSessionTemplateName;
	}
	public String getEntityServiceName() {
		return entityServiceName;
	}
	public void setEntityServiceName(String entityServiceName) {
		this.entityServiceName = entityServiceName;
	}
	public String getEntityDAOName() {
		return entityDAOName;
	}
	public void setEntityDAOName(String entityDAOName) {
		this.entityDAOName = entityDAOName;
	}

	public String getEntityTargetPath() {
		return entityTargetPath;
	}

	public void setEntityTargetPath(String entityTargetPath) {
		this.entityTargetPath = entityTargetPath;
	}

	public String getMapperTargetPath() {
		return mapperTargetPath;
	}

	public void setMapperTargetPath(String mapperTargetPath) {
		this.mapperTargetPath = mapperTargetPath;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getTamplateContent() {
		return tamplateContent;
	}

	public void setTamplateContent(String tamplateContent) {
		this.tamplateContent = tamplateContent;
	}

	public String getSystemTable() {
		return systemTable;
	}

	public void setSystemTable(String systemTable) {
		this.systemTable = systemTable;
	}

	public String getSystemCodeField() {
		return systemCodeField;
	}

	public void setSystemCodeField(String systemCodeField) {
		this.systemCodeField = systemCodeField;
	}

	public String getSystemNameField() {
		return systemNameField;
	}

	public void setSystemNameField(String systemNameField) {
		this.systemNameField = systemNameField;
	}

	public String getSystemNameAliasSurffix() {
		return systemNameAliasSurffix;
	}

	public void setSystemNameAliasSurffix(String systemNameAliasSurffix) {
		this.systemNameAliasSurffix = systemNameAliasSurffix;
	}
	
}
