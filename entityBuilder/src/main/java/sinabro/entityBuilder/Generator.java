/**
 * 
 */
package sinabro.entityBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.jknack.handlebars.Template;

import sinabro.entityBuilder.Config.ETargetFileType;
import sinabro.entityBuilder.core.HandleBarsInnodyne;
import sinabro.entityBuilder.model.Column;
import sinabro.entityBuilder.model.Table;

/**
  * @FileName : Generator.java
  * @Project : entityBuilder
  * @Date : 2021. 1. 7. 
  * @작성자 : sinabro
  * @변경이력 :
  * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
  */

public class Generator {
	
	Map<String,String> commentMap = new HashMap<>();
	
	public void genMybatisMapperXml(Config config , boolean existFileDelete) throws Exception
	{
		String commonMapperTamplateContent = getLocalResource("sinabro/entityBuilder/template/entity/COMMON_mapper.xml");
		String mapperTemplateContent = getLocalResource("sinabro/entityBuilder/template/entity/mapperTemplate.xml");
		
		HandleBarsInnodyne hb = new HandleBarsInnodyne();
		Template commonMapperTamplate = hb.loadTemplate(commonMapperTamplateContent);
		hb.apply(commonMapperTamplate, config , Paths.get(config.getMapperTargetPath(), "entity" , "COMMON_mapper.xml").toString());
		
		config.setTamplateContent(mapperTemplateContent);
		config.setTargetFileType(ETargetFileType.TableName);
		config.setTargetFileSuffix("_mapper.xml");
		config.setTargetFilePrefix("");
		
		String targetPath = Paths.get(config.getMapperTargetPath(),"entity").toString();
		genSourceCode(config , targetPath,existFileDelete);
	}
	
	public void genEntityVO(Config config , boolean existFileDelete) throws Exception
	{
		String utilTemplate = getLocalResource("sinabro/entityBuilder/template/entity/EntityUtil.hb");
		String daoTemplate = getLocalResource("sinabro/entityBuilder/template/entity/entityDao/EntityDAO.hb");
		String serviceTemplate = getLocalResource("sinabro/entityBuilder/template/entity/entityService/EntityService.hb");
		String voNullTemplate = getLocalResource("sinabro/entityBuilder/template/entity/entityVo/EntityNOTNULL.hb");
		String voYNTemplate = getLocalResource("sinabro/entityBuilder/template/entity/entityVo/EntityYN.hb");
		String voBaseTemplate = getLocalResource("sinabro/entityBuilder/template/entity/entityVo/EntityBase.hb");
		String voFKTemplate = getLocalResource("sinabro/entityBuilder/template/entity/entityVo/EntityFK.hb");
		String voPKTemplate = getLocalResource("sinabro/entityBuilder/template/entity/entityVo/EntityPK.hb");
		String voRemarkTemplate = getLocalResource("sinabro/entityBuilder/template/entity/entityVo/EntityRemark.hb");
		String voTemplate = getLocalResource("sinabro/entityBuilder/template/entity/entityVo/voTemplate.hb");
		
		Template template = null;
		HandleBarsInnodyne hb = new HandleBarsInnodyne();
		
		template = hb.loadTemplate(utilTemplate);
		hb.apply(template, config , Paths.get(config.getEntityTargetPath(), "entity/EntityUtil.java").toString());
		
		template = hb.loadTemplate(daoTemplate);
		hb.apply(template, config , Paths.get(config.getEntityTargetPath(), "entity/entityDao/"+config.getEntityDAOName()+".java").toString());
		
		template = hb.loadTemplate(serviceTemplate);
		hb.apply(template, config , Paths.get(config.getEntityTargetPath(), "entity/entityService/"+config.getEntityServiceName()+".java").toString());
		
		template = hb.loadTemplate(voNullTemplate);
		hb.apply(template, config , Paths.get(config.getEntityTargetPath(), "entity/entityVo/EntityNOTNULL.java").toString());
		
		template = hb.loadTemplate(voYNTemplate);
		hb.apply(template, config , Paths.get(config.getEntityTargetPath(), "entity/entityVo/EntityYN.java").toString());
		
		template = hb.loadTemplate(voBaseTemplate);
		hb.apply(template, config , Paths.get(config.getEntityTargetPath(), "entity/entityVo/EntityBase.java").toString());
		
		template = hb.loadTemplate(voFKTemplate);
		hb.apply(template, config , Paths.get(config.getEntityTargetPath(), "entity/entityVo/EntityFK.java").toString());
		
		template = hb.loadTemplate(voPKTemplate);
		hb.apply(template, config , Paths.get(config.getEntityTargetPath(), "entity/entityVo/EntityPK.java").toString());
		
		template = hb.loadTemplate(voRemarkTemplate);
		hb.apply(template, config , Paths.get(config.getEntityTargetPath(), "entity/entityVo/EntityRemark.java").toString());
		
		config.setTamplateContent(voTemplate);
		config.setTargetFileType(ETargetFileType.TableClassName);
		config.setTargetFileSuffix(".java");
		config.setTargetFilePrefix("");
		
		String targetPath = Paths.get(config.getEntityTargetPath(),"entity/entityVo").toString();
		readComment(config);
		genSourceCode(config ,targetPath, existFileDelete);
	}
	
	public void genEntityDBInfo(Config config , String templateFile , String targetFile) throws Exception
	{
		Connection connection = getConnection(config);
		DatabaseMetaData meta = connection.getMetaData();
		
		try {
			List<Table> viewList = new ArrayList<>(); 
			List<Table> list = getTableInfo(meta, config.getDbUser() , config.getTableNamePatten() , config);
			for(Table table : list) {
				table.getColumns().sort(new Comparator<Column>() {
					@Override
					public int compare(Column arg0, Column arg1) {
		              return arg0.getName().compareTo(arg1.getName());
			       }
				});
				
				if(config.getViewInfo().isView(table.getName()))
					viewList.add(table);
			}
			
			list.sort(new Comparator<Table>() {
				@Override
				public int compare(Table arg0, Table arg1) {
	              return arg0.getName().compareTo(arg1.getName());
		       }
			});	
			
			for(Table table : viewList) {
				list.remove(table);
			}
			
			Map<String,Object> dataMap = new HashMap<>();
			genDBFunctionProcedure(connection , config , dataMap);
			dataMap.put("tableList", list);
			
			HandleBarsInnodyne hb = new HandleBarsInnodyne();
			Template template = hb.loadTemplate(new File(templateFile));
			hb.apply(template, dataMap , targetFile);
		}
		finally {
			if(connection != null)	connection.close();
		}
	}
	
	public void genSourceCode(Config config,String targetPath) throws Exception
	{
		genSourceCode(config,targetPath,false);
	}
	
	protected void genSourceCode(Config config,String targetPath, boolean existFileDelete) throws Exception
	{
		HandleBarsInnodyne hb = new HandleBarsInnodyne();
		Template template = hb.loadTemplate(config.getTamplateContent());
		
		Connection connection = getConnection(config);
		DatabaseMetaData meta = connection.getMetaData();
		
		try {
			List<Table> list = getTableInfo(meta, config.getDbUser() , config.getTableNamePatten() , config);
			for(Table table : list)
			{
				String fileName = "";
				try
				{
					fileName = Paths.get(targetPath, getFileName(config,table)).toString();
					
					if(!existFileDelete)
					{
						File file = new File(fileName);
						if(file.exists())
							throw new Exception(String.format("존재하는 파일입니다. 삭제하고 진행하세요."));
					}
					
					table.setConfig(config);
					hb.apply(template, table , fileName);
					System.out.println(String.format("SUCCESS %s" , fileName));
				}
				catch(Exception ee)
				{
					System.out.println(String.format("================================================\nFAIL %s\n%s\n================================================\n" , ee.getMessage(), fileName));
				}
			}
		}
		finally {
			if(connection != null)	connection.close();
		}
	}
	
	protected Connection getConnection(Config config) throws ClassNotFoundException, SQLException
	{
		String url = config.getDbConUrl();
		Class.forName(config.getDbDriverName());
		Connection con = DriverManager.getConnection(url, config.getDbUser(), config.getDbPassword());
		return con;
	}
	
	protected void readComment(Config config) throws ClassNotFoundException, SQLException
	{
		Connection connection = getConnection(config);
		PreparedStatement preparedStatement = connection.prepareStatement(getQuery(config).comment(config.getDbUser()));
		ResultSet commentInfo = preparedStatement.executeQuery();
		try {
			while (commentInfo.next()) {
				String tableName = commentInfo.getString("TABLE_NAME");
				String columnName = commentInfo.getString("COLUMN_NAME");
				String comments = commentInfo.getString("COMMENTS");
				
				commentMap.put(String.format("%s.%s", tableName ,columnName), comments==null?"":comments);
			}
		}finally {
			if(commentInfo != null) commentInfo.close();
			if(connection != null) connection.close();
		}
		
	}
	
	protected void genDBFunctionProcedure(Connection connection , Config config , Map<String,Object> dataMap) throws Exception {
		
		Map<String,String> replaceMap = new HashMap<>();
		replaceMap.put("&apos;", "'");
		replaceMap.put("&quot;", "\"");
		replaceMap.put("&#x3D;", "=");
		replaceMap.put("&gt;", ">");
		replaceMap.put("&lt;", "<");
		
		PreparedStatement preparedStatement = connection.prepareStatement(getQuery(config).source(config.getDbUser()));
		ResultSet objectInfo = preparedStatement.executeQuery();
		try {
			
			List<Map<String,String>> listFunction = new ArrayList<>();
			List<Map<String,String>> listProcedure = new ArrayList<>();
			
			while (objectInfo.next()) {
				String name = objectInfo.getString("NAME");
				String type = objectInfo.getString("TYPE");
				String val = objectInfo.getString("VAL");
				
				for(String key : replaceMap.keySet()) {
					String newString = replaceMap.get(key);
					val = val.replace(key, newString);
				}
				
				Map<String,String> map = new HashMap<>();
				map.put("name", name);
				map.put("val", val);
				
				if("FUNCTION".equals(type)) {
					listFunction.add(map);
				}
				if("PROCEDURE".equals(type)) {
					listProcedure.add(map);
				}
			}
			
			dataMap.put("listFunction", listFunction);
			dataMap.put("listProcedure", listProcedure);
			
		}finally {
			if(objectInfo != null) objectInfo.close();
		}
	}
	
	protected Query getQuery(Config config) {
		return new Query();
	}
	
	protected List<Table> getTableInfo(DatabaseMetaData meta , String tableOwner,String tableNamePatten , Config config) throws SQLException	
	{
		List<Table> list = new ArrayList<>();
		ResultSet tableInfo = meta.getTables(null, tableOwner , null, null);
		// Printing the column name and size
		try {
			while (tableInfo.next()) {
				Table table = new Table();
				String tableName = tableInfo.getString("Table_NAME");
				
				if(!"".equals(tableNamePatten) && !tableName.matches(tableNamePatten)) continue;
				
				List<Table.PrimaryKey> primaryKeyInfo = getPrimaryKeyInfo(meta,tableOwner,tableName);
				List<Table.ForeignKey> foreignKeyInfo = getForeignKeyInfo(meta,tableOwner,tableName);
				
				String type = tableInfo.getString("TABLE_TYPE");
				if("VIEW".equals(type)) {
					primaryKeyInfo = getViewPrimaryKeyInfo(config , tableOwner,tableName);
				}
				
				table.setName(tableName);
				table.setType(tableInfo.getString("TABLE_TYPE"));
				table.setRemark(tableInfo.getString("REMARKS"));
				table.setColumns(getColumnInfo(meta,tableOwner,tableName, primaryKeyInfo,foreignKeyInfo));
				table.setPrimaryKeys(primaryKeyInfo);
				table.setForeignKeys(foreignKeyInfo);
				
				list.add(table);
			}
		}
		finally {
			if(tableInfo != null) tableInfo.close();
		}
		return list;
	}
	
	protected List<Column> getColumnInfo(DatabaseMetaData meta , String tableOwner,String tableName
			, List<Table.PrimaryKey> primaryKeyInfo
			, List<Table.ForeignKey> foreignKeyInfo
			) throws SQLException
	{
		List<Column> list = new ArrayList<>();
		List<Column> listAllowNull = new ArrayList<>();
		List<Column> listNotAllowNull = new ArrayList<>();
		ResultSet rs = meta.getColumns(null, tableOwner, tableName, null);
		try {
			while (rs.next()) {
				
	//			ResultSetMetaData data = rs.getMetaData();
	//			for(int i=0;i<data.getColumnCount();i++)
	//			{
	//				String key = data.getColumnName(i+1);
	//				System.out.println(data.getColumnName(i+1));
	//				System.out.println(rs.getObject(key));
	//			}
	
				Column column = new Column();
				String columnName = rs.getString("COLUMN_NAME");
				String commentsKey = String.format("%s.%s", tableName ,columnName);
				
				column.setName(columnName);
				if(commentMap.containsKey(commentsKey))
					column.setRemark(commentMap.get(commentsKey));
				column.setDataType(rs.getString("TYPE_NAME"));
				column.setSqlType(rs.getInt("DATA_TYPE"));
				column.setMaxLength(rs.getInt("COLUMN_SIZE"));
				column.setColumnDef(rs.getString("COLUMN_DEF"));
				column.setAllowNullYN(rs.getInt("NULLABLE")==0 ? "N" : "Y");
				if("N".equals(column.getAllowNullYN()) && column.getColumnDef() != null) //notnull 인데 default가 정의되어 있으면 null가능
					column.setAllowNullYN("Y");
				for(Table.PrimaryKey PrimaryKey : primaryKeyInfo) {
					if(columnName.equals(PrimaryKey.getName()))
						column.setPrimaryKeyYN("Y");
				}
				for(Table.ForeignKey foreignKey : foreignKeyInfo) {
					if(columnName.equals(foreignKey.getName())) {
						column.setForeignKeyYN("Y");
						column.setForeignKey(foreignKey);
					}
				}
				if( "N".equals(column.getAllowNullYN())) listNotAllowNull.add(column);
				else listAllowNull.add(column);
			}
		}
		finally {
			if(rs != null) rs.close();
		}
		for(int i=0;i<listNotAllowNull.size();i++) list.add(listNotAllowNull.get(i));
		for(int i=0;i<listAllowNull.size();i++) list.add(listAllowNull.get(i));
		return list;
	}
	
	protected List<Table.PrimaryKey> getPrimaryKeyInfo(DatabaseMetaData meta , String tableOwner,String tableName) throws SQLException
	{
		List<Table.PrimaryKey> list = new ArrayList<>();
		ResultSet rs = meta.getPrimaryKeys(null, tableOwner, tableName);
		try {
			while (rs.next()) {
				String colName = rs.getString("COLUMN_NAME");
				int keySeq = rs.getInt("KEY_SEQ");
				
				list.add(new Table.PrimaryKey(colName, keySeq));
			}
		}
		finally {
			if(rs != null) rs.close();
		}
		return list;
	}
	
	protected List<Table.PrimaryKey> getViewPrimaryKeyInfo(Config config , String tableOwner,String viewName) throws SQLException
	{
		List<Table.PrimaryKey> list = new ArrayList<>();
		List<String> pkList = config.getViewInfo().getListPk(viewName);

		for(int i=0;i<pkList.size();i++) {
			
			list.add(new Table.PrimaryKey(pkList.get(i), i+1));
		}

		return list;
	}
	
	protected List<Table.ForeignKey> getForeignKeyInfo(DatabaseMetaData meta , String tableOwner,String tableName) throws SQLException
	{
		List<Table.ForeignKey> list = new ArrayList<>();
		ResultSet foreignKeys = meta.getImportedKeys(null, tableOwner, tableName);
		try{
			while(foreignKeys.next()){
			    String pkTableName = foreignKeys.getString("PKTABLE_NAME");
			    String fkTableName = foreignKeys.getString("FKTABLE_NAME");
			    String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
			    String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
			    
			    list.add( new Table.ForeignKey(fkColumnName, pkTableName, pkColumnName));
	
			}
		}finally {
			if(foreignKeys != null) foreignKeys.close();
		}	

		return list;
	}
	
	protected String getFileName(Config config , Table table ) throws Exception
	{
		String name;
		switch(config.getTargetFileType())
		{
		case TableName:
			name = table.getName();
			break;
		case TableClassName:
			name = table.getClassName();
			break;
		case TableCamelCase:
			name = table.getCamelName();
			break;
		default:
			throw new Exception("");
		}
		
		return String.format("%s%s%s", config.getTargetFilePrefix(), name , config.getTargetFileSuffix());
	}
	
	public static String getProjectPathRoot() throws Exception 
	 {
		 String projectPath = getProjectPath();
		 projectPath = projectPath.replaceAll("target/classes/", "");
	     return projectPath;
	 }
	 
	 public static String getProjectPath() throws Exception 
	 {
		 URL url = ApplicationMain.class.getProtectionDomain().getCodeSource().getLocation();
	     String filePath = URLDecoder.decode(url.getPath(), "utf-8");
	     filePath = filePath.substring(1, filePath.length());
	     return filePath;
	 }
	 
	 public String getLocalResource(String path) throws Exception {
		 
		 String str = "";
		 StringBuffer sb = new StringBuffer();
		 InputStream ioStream = ApplicationMain.class
			        .getClassLoader()
			        .getResourceAsStream(path);
		 if(ioStream == null) throw new FileNotFoundException();
		 
		 InputStreamReader isr = new InputStreamReader(ioStream, "UTF-8");
		 BufferedReader br = new BufferedReader(isr );
		 while((str=br.readLine()) != null)
		 {
			 sb.append(str+"\n");
		 }
		 return sb.toString();
	}
	

}
