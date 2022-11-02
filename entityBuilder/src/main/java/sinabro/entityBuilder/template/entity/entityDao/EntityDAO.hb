package {{entityPackage}}.entity.entityDao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @Project : entityBuilder
 * @Date : 2021. 1. 7. 
 * @작성자 : sinabro
 * @변경이력 :
 * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
 */

@Component
public class {{entityDAOName}} {
	
	@Autowired
	@Qualifier("{{sqlSessionTemplateName}}") 
	SqlSessionTemplate sqlSessionTemplate;
	
	public String selectString(String queryId , Map<String,Object> param){
		return sqlSessionTemplate.selectOne(queryId , param );
	}

	public <T> int update(String table , Map<String,Object> param) {
		String queryId = String.format("%s.updateWithPK", table);
		return sqlSessionTemplate.update(queryId , param );
	}
	
	public <T> int merge(String table , Map<String,Object> param) {
		String queryId = String.format("%s.merge", table);
		return sqlSessionTemplate.update(queryId , param );
	}
	
	public <T> int delete(String table , Map<String,Object> param) {
		String queryId = String.format("%s.deleteWithPK", table);
		return sqlSessionTemplate.update(queryId , param );
	}
	
	public <T> int deletePk(String table , Map<String,Object> param) {
		String queryId = String.format("%s.delete", table);
		return sqlSessionTemplate.update(queryId , param );
	}
	
	public <T> int insert(String table , Map<String,Object> param) {
		String queryId = String.format("%s.insert", table);
		return sqlSessionTemplate.update(queryId , param );
	}
	
	public <T> T selectOne(String table , Map<String,Object> param) {
		String queryId = String.format("%s.selectId", table);
		return sqlSessionTemplate.selectOne(queryId , param);
	}
	
	public List<Map<String,Object>> selectList(String table , Map<String,Object> param) {
		String queryId = String.format("%s.selectList", table);
		convertOrderBySnakeCase(param);
		return sqlSessionTemplate.selectList(queryId , param);
	}
	
	private void convertOrderBySnakeCase(Map<String,Object> param) {
		if( param != null && param.containsKey("orderBy"))
		{
			String val = param.get("orderBy").toString();
			val = val.replaceAll("([A-Z])","_$1").toUpperCase(); // caseCase 를 snakeCase로변경
			param.put("orderBy",val);
		}
	}
}
