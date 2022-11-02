package {{entityPackage}}.entity.entityService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import {{entityPackage}}.entity.EntityUtil;
import {{entityPackage}}.entity.entityDao.{{entityDAOName}};
import {{entityPackage}}.entity.entityVo.EntityBase;

/**
 * @Project : entityBuilder
 * @Date : 2021. 1. 7. 
 * @작성자 : sinabro
 * @변경이력 :
 * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
 */

@Service
public class {{entityServiceName}} {
	
	private static final Logger log = LoggerFactory.getLogger({{entityServiceName}}.class );
	
	@Autowired {{entityDAOName}} entityDAO;
	
	public String getYilsi()
	{
		return entityDAO.selectString("entity.yilsi" , new HashMap<>());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EntityBase> List<Map<String,Object>> selectList( T t) throws Exception
	{
		Map<String,Object> param = EntityUtil.convertVoToMap(t);
		
		return (List<Map<String,Object>>)entityDAO.selectList(getEntityID(t), param);
	}
	
	public List<Map<String,Object>> selectList( String tableName , Map<String,Object> param)
	{
		return entityDAO.selectList(getEntityID(tableName), param);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EntityBase> List<Map<String,Object>> selectListWithPage( T t) throws Exception
	{
		checkExsitField(t,"curPage");
		checkExsitField(t,"perPage");
		t.setPagingYN("Y");
		
		Map<String,Object> param = EntityUtil.convertVoToMap(t);
		
		return (List<Map<String,Object>>)entityDAO.selectList(getEntityID(t), param);
	}
	
	public List<Map<String,Object>> selectListWithPage( String tableName , Map<String,Object> param) throws NoSuchFieldException
	{
		if(param.get("curPage") == null || param.get("perPage") == null)
		{
			log.debug(String.format("페이징 쿼리에는 %s 필드가 정의되어야 합니다." , "curPage , perPage"));
			throw new NoSuchFieldException(String.format("페이징 쿼리에는 %s 필드가 정의되어야 합니다." , "curPage , perPage"));
		}
		param.put("pagingYN","Y");
		
		return entityDAO.selectList(getEntityID(tableName), param);
	}
	
	public <T extends EntityBase> Map<String,Object> selectOne( T t)
	{
		Map<String,Object> param = EntityUtil.convertVoToMap(t);
		return entityDAO.selectOne(getEntityID(t), param);
	}
	
	public Map<String,Object> selectOne( String tableName , Map<String,Object> param)
	{
		return entityDAO.selectOne(getEntityID(tableName), param);
	}
	
	public <T extends EntityBase> int insert(T t) throws Exception
	{
		t.Valid();
		Map<String,Object> param = EntityUtil.convertVoToMap(t);
		return entityDAO.insert(getEntityID(t), param);
	}
	
	@Transactional
	public <T extends EntityBase> int insert(List<T> listT)
	{
		for(T t : listT)
		{
			Map<String,Object> param = EntityUtil.convertVoToMap(t);
			entityDAO.insert(getEntityID(t), param);
		}
		return 1;
	}
	
	public int insert( String tableName , Map<String,Object> param)
	{
		return entityDAO.insert(getEntityID(tableName), param);
	}
	
	public <T extends EntityBase> int update(T t) throws Exception
	{
		t.ValidUpdate();
		Map<String,Object> param = EntityUtil.convertVoToMap(t);
		return entityDAO.update(getEntityID(t), param);
	}
	
	@Transactional
	public <T extends EntityBase> int update(List<T> listT)
	{
		for(T t : listT)
		{
			Map<String,Object> param = EntityUtil.convertVoToMap(t);
			entityDAO.update(getEntityID(t), param);
		}
		return 1;
	}
	
	public int update( String tableName , Map<String,Object> param)
	{
		return entityDAO.update(getEntityID(tableName), param);
	}
	
	public <T extends EntityBase> int merge(T t)
	{
		Map<String,Object> param = EntityUtil.convertVoToMap(t);
		return entityDAO.merge(getEntityID(t), param);
	}
	
	public int merge( String tableName , Map<String,Object> param)
	{
		return entityDAO.merge(getEntityID(tableName), param);
	}
	
	public <T extends EntityBase> int delete(T t)
	{
		Map<String,Object> param = EntityUtil.convertVoToMap(t);
		return entityDAO.deletePk(getEntityID(t), param);
	}
	
	public int delete( String tableName , Map<String,Object> param)
	{
		return entityDAO.delete(getEntityID(tableName), param);
	}
	
	private <T extends EntityBase> String getEntityID(T t)
	{
		String[] arr = t.getClass().toString().split("[.]");
		String className = arr[arr.length-1];
		String name = String.format("%s%s",className.substring(0, 1).toLowerCase() , className.substring(1));
		return String.format("educs.entity.%s" , name);
	}
	
	private <T extends EntityBase> String getEntityID(String tableName)
	{
		return String.format("educs.entity.%s" , tableName);
	}
	
	private <T extends EntityBase> void checkExsitField(T t , String fieldName) throws NoSuchFieldException
	{
		List<Field> listField = new ArrayList<>();
		listField = EntityUtil.getDeclaredFieldsAll(t.getClass() , listField);
		for(Field field : listField)
		{
			if(fieldName.equals(field.getName())) return;
		}
		log.debug(String.format("페이징 쿼리에는 %s 필드가 정의되어야 합니다." , "curPage , perPage"));
		throw new NoSuchFieldException(String.format("페이징 쿼리에는 %s 필드가 정의되어야 합니다." , fieldName));
	}

}
