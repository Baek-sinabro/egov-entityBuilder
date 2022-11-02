/**
 * 
 */
package sinabro.entityBuilder;

/**
  * @FileName : QueryMSSQL.java
  * @Project : entityBuilder
  * @Date : 2021. 3. 22. 
  * @작성자 : sinabro
  * @변경이력 :
  * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
  */

public class QueryMSSQL extends Query{
	
	public String comment(String owner) {

		StringBuffer sb =  new StringBuffer();
//		sb.append("\n SELECT TABLE_NAME,");
//		sb.append("\n     COLUMN_NAME,");
//		sb.append("\n     COMMENTS");
//		sb.append("\n FROM");
//		sb.append("\n     ALL_COL_COMMENTS");
//		sb.append("\n WHERE");
//		sb.append("\n     OWNER = '"+owner+"'");
//		sb.append("\n ORDER BY");
//		sb.append("\n     TABLE_NAME,COLUMN_NAME");
		return sb.toString();				
								
	}

}
