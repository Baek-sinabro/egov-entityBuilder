/**
 * 
 */
package sinabro.entityBuilder.model;

import java.sql.Types;

import sinabro.entityBuilder.util.CommonUtil;

/**
  * @FileName : Column.java
  * @Project : entityBuilder
  * @Date : 2021. 1. 7. 
  * @작성자 : sinabro
  * @변경이력 :
  * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
  */

public class Column {

	String name;
	String dataType; //DB에 정의된 타입
	String remark;
	String javaType;
	int sqlType; //java.sql.types
	int maxLength;
	String allowNullYN = "Y";
	String primaryKeyYN = "N";
	String foreignKeyYN = "N";
	Table.ForeignKey foreignKey = null;
	String columnDef = "";
	
	public String getJavaType() throws Exception
	{
		switch(sqlType) 
		{
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.NVARCHAR:
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
			return "String";
		case Types.INTEGER:
		case Types.BIGINT:
		case Types.BIT:
			return "Integer";
		case Types.CLOB:
			return "byte[]";
		case Types.NCLOB:
			return "String";
		case Types.DECIMAL:
			if("NUMBER".equals(this.dataType))
				return "Integer";
			else
				return "Double";
		case Types.NUMERIC:
			if("NUMBER".equals(this.dataType))
				return "Integer";
			else
				return "Double";
			
		default:
			throw new Exception(String.format("미지원타입 : %d", sqlType));
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCamelName() {
		return CommonUtil.toCamelcase(this.name);
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getAllowNullYN() {
		return allowNullYN;
	}

	public void setAllowNullYN(String allowNullYN) {
		this.allowNullYN = allowNullYN;
	}

	public String getPrimaryKeyYN() {
		return primaryKeyYN;
	}

	public void setPrimaryKeyYN(String primaryKeyYN) {
		this.primaryKeyYN = primaryKeyYN;
	}

	public String getForeignKeyYN() {
		return foreignKeyYN;
	}

	public void setForeignKeyYN(String foreignKeyYN) {
		this.foreignKeyYN = foreignKeyYN;
	}

	public Table.ForeignKey getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(Table.ForeignKey foreignKey) {
		this.foreignKey = foreignKey;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getColumnDef() {
		return columnDef;
	}

	public void setColumnDef(String columnDef) {
		this.columnDef = columnDef;
	}
	
	
}
