/**
 * 
 */
package sinabro.entityBuilder;

/**
  * @FileName : Query.java
  * @Project : entityBuilder
  * @Date : 2021. 3. 22. 
  * @작성자 : sinabro
  * @변경이력 :
  * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
  */

public class Query {
	
	public String comment(String owner) {

		StringBuffer sb =  new StringBuffer();
		sb.append("\n SELECT TABLE_NAME,");
		sb.append("\n     COLUMN_NAME,");
		sb.append("\n     REPLACE(COMMENTS,'YN','여부') COMMENTS");
		sb.append("\n FROM");
		sb.append("\n     ALL_COL_COMMENTS");
		sb.append("\n WHERE");
		sb.append("\n     OWNER = '"+owner+"'");
		sb.append("\n ORDER BY");
		sb.append("\n     TABLE_NAME,COLUMN_NAME");
		return sb.toString();				
								
	}
	
	public String source(String owner) {
		
		StringBuffer sb =  new StringBuffer();
		sb.append("\n SELECT  NAME ");
		sb.append("\n     , TYPE ");
		sb.append("\n     , SUBSTR(XMLAGG( XMLELEMENT(X,'', TEXT) ORDER BY LINE).EXTRACT('//text()').GETCLOBVAL(), 1)  VAL ");
		sb.append("\n FROM ALL_SOURCE");
		sb.append("\n WHERE 1=1 ");
		sb.append("\n     AND TYPE IN ('FUNCTION','PROCEDURE') ");
		//sb.append("\n     AND name = 'FN_ENCRYPT_PASS' ");
		sb.append("\n 	  AND OWNER = '"+owner+"'");
		
		sb.append("\n GROUP BY NAME , TYPE");
		sb.append("\n ORDER BY TYPE");
		return sb.toString();
	}
	

}
