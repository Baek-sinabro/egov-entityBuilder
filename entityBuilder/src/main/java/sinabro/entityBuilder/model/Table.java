/**
 * 
 */
package sinabro.entityBuilder.model;

import java.util.ArrayList;
import java.util.List;

import sinabro.entityBuilder.Config;
import sinabro.entityBuilder.util.CommonUtil;



/**
  * @FileName : table.java
  * @Project : entityBuilder
  * @Date : 2021. 1. 7. 
  * @작성자 : sinabro
  * @변경이력 :
  * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
  */

public class Table {
	
	public static class PrimaryKey
	{
		String name;
		int keySeq;
		
		public PrimaryKey(String name , int keySeq)
		{
			this.name = name;
			this.keySeq = keySeq;
		}
		
		public String getName() {return name;}
		public int getKeySeq() {return keySeq;}
		
		public String toString()
		{
			return String.format("(name=%s , seq=%d", this.name , this.keySeq );
		}
	}
	
	public static class ForeignKey
	{
		String name;
		String refTable;
		String refColumn;
		
		public ForeignKey(String name , String refTable , String refColumn)
		{
			this.name = name;
			this.refTable = refTable;
			this.refColumn = refColumn;
		}
		
		public String getName() {return name;}
		public String getRefTable() {return refTable;}
		public String getRefColumn() {return refColumn;}
		
		public String toString()
		{
			return String.format("(name=%s , ref=%s:%s ", this.name , this.refTable , this.refColumn );
		}
	}
	
	
	String name;
	String calmelName;
	String className;
	String remark;
	String type;
	List<PrimaryKey> primaryKeys = new ArrayList<>();
    List<ForeignKey> foreignKeys = new ArrayList<>();
    List<Column> columns = new ArrayList<>();
    List<Column> notPKcolumns = new ArrayList<>();
    Config config;
    
    public String getEntityPackage()
    {
    	return config.getEntityPackage();
    }
    
    public String getMapperNamespace()
    {
    	return config.getMapperNamespace();
    }
    
    
	
	public void addPrimaryKey(PrimaryKey primaryKey)
	{
		primaryKeys.add(primaryKey);
	}
	
	public void addForeignKey(ForeignKey foreignKey)
	{
		foreignKeys.add(foreignKey);
	}
	
	public void addColumn(Column column)
	{
		columns.add(column);
	}

	public String getCamelName() {
		return CommonUtil.toCamelcase(name);
	}
	
	public String getClassName() {
		String camel = getCamelName();
		return String.format("%s%s", camel.substring(0,1).toUpperCase() , camel.substring(1));
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<PrimaryKey> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(List<PrimaryKey> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public List<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}

	public void setForeignKeys(List<ForeignKey> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
		for(Column column : columns) {
			if("N".equals(column.primaryKeyYN)) notPKcolumns.add(column);
		}
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public List<Column> getNotPKcolumns() {
		return notPKcolumns;
	}

	public void setNotPKcolumns(List<Column> notPKcolumns) {
		this.notPKcolumns = notPKcolumns;
	}

	
	

}
