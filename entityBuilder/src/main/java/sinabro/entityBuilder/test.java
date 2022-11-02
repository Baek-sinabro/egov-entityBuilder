/**
 * 
 */
package sinabro.entityBuilder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

import sinabro.entityBuilder.model.Table;
import sinabro.entityBuilder.util.CommonUtil;

/**
 * @FileName : test.java
 * @Project : entityBuilder
 * @Date : 2021. 1. 7.
 * @작성자 : sinabro
 * @변경이력 :
 * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
 */

public class test {

	public static void main(String[] args) throws Exception {

		String aaa = CommonUtil.toCamelcase("TbWhoiwonsa");

		System.out.println(aaa);
		
		
		
		Config config = new Config();
		config.setEntityPackage("educs.entity");
		
		Table ta = new Table();
		ta.setConfig(config);

		Class.forName("oracle.jdbc.driver.OracleDriver");
		// Getting the connection
		String url = "jdbc:oracle:thin:@192.168.0.122:1521/orcl";
		Connection con = DriverManager.getConnection(url, "EDUCSPOSCO", "EDUCS_POSCO");
		System.out.println("Connection established......");
		// Retrieving the meta data object
		DatabaseMetaData meta = con.getMetaData();
		// Retrieving the columns in the database
		ResultSet tableInfo = meta.getTables(null, null, "TB_%", null);
		// Printing the column name and size
		while (tableInfo.next()) {
			
			ta.setName(tableInfo.getString("Table_NAME"));
			ta.setType(tableInfo.getString("TABLE_TYPE"));
			ta.setRemark(tableInfo.getString("REMARKS"));
			
			System.out.println(ta.getName());
		}
		System.out.println("===== 끝 ======" );
		CommonUtil.toCamelcase("Test_aaa");
		/*
		
		Map<String, Map<String, Map<String, Object>>> tables = new HashMap<>();
		ResultSet rs = meta.getColumns(null, null, "TB_WHOIWONSA", null);
		while (rs.next()) {
		
			Column column = new Column();
			
			column.setName(rs.getString("COLUMN_NAME"));
			column.setRemark(rs.getString("REMARKS"));
			column.setDataType(rs.getString("TYPE_NAME"));
			column.setSqlType(rs.getInt("DATA_TYPE"));
			column.setMaxLength(rs.getInt("COLUMN_SIZE"));
			column.setAllowNull(rs.getInt("NULLABLE")==0 ? false : true);
			
			ta.addColumn(column);
		}
		

		rs = meta.getPrimaryKeys(null, null, "TB_WHOIWONSA");
		while (rs.next()) {
			String colName = rs.getString("COLUMN_NAME");
			int keySeq = rs.getInt("KEY_SEQ");
			
			String pkName = rs.getString("PK_NAME");
			
			ta.addPrimaryKey(new Table.PriamryKey(rs.getString("COLUMN_NAME"), rs.getInt("KEY_SEQ")));
			
			
		}
		
		 System.out.println("");
		 System.out.println("");
		ResultSet foreignKeys = meta.getImportedKeys(null, null, "TB_WHOIWONSA");
		while(foreignKeys.next()){
		    String pkTableName = foreignKeys.getString("PKTABLE_NAME");
		    String fkTableName = foreignKeys.getString("FKTABLE_NAME");
		    String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
		    String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
		    
		    Table.ForeignKey fk = new Table.ForeignKey(fkColumnName, pkTableName, pkColumnName);
		    ta.addForeignKey(fk);
		    

		}
		
		
		HadleBarsInnodyne hb = new HadleBarsInnodyne();
		hb.apply("D:/java/innodyneProject_egov/educs_egov/target/classes/entityBuilder/template/mapper.xml", ta
				, "D:/java/innodyneProject_egov/educs_egov/src/main/resources/egovframework/sqlmap/mappers/entity/WHOIWONSA_mapper.xml");
		*/
		
	}
	
	public static <T> void sss(T t)
	{
		String s = t.getClass().getTypeName();
		System.out.println(s);
		
		s = t.toString();
		System.out.println(s);
		
		String[] arr = t.toString().split("[.]");
		String aaaa = arr[arr.length-1];
		String name = String.format("%s%s",aaaa.substring(0, 1).toLowerCase() , aaaa.substring(1));
		System.out.println(name);
		
        
	}

}
