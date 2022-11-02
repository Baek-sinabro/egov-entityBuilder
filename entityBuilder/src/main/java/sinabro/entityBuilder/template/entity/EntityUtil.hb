package {{entityPackage}}.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import {{entityPackage}}.entity.entityVo.EntityBase;
import {{entityPackage}}.entity.entityVo.EntityFK;
import {{entityPackage}}.entity.entityVo.EntityNOTNULL;
import {{entityPackage}}.entity.entityVo.EntityPK;

/**
 * @Project : entityBuilder
 * @Date : 2021. 1. 7. 
 * @작성자 : sinabro
 * @변경이력 :
 * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
 */

public class EntityUtil {
	
	public enum VALID { NULL , MAX_LENGTH , PK , FK_NULL}
	
	public class EntityValidException extends RuntimeException 
	{
		
		private static final long serialVersionUID = 1L;
		
		Map<String,String> map = new HashMap<>();
		
		public EntityValidException() {}
		public EntityValidException(String msg) { super(msg);}
		public EntityValidException add(Field field, VALID valid) {
			map.put(field.getName(), valid.toString());
			return this;
		}
		public Map<String,String> getValidError(){return map;}
	}
	
	public static <T extends EntityBase> List<Field> getDeclaredFieldsAll(Class<T> t , List<Field> listField )
	{
		listField.addAll(Arrays.asList(t.getDeclaredFields()));

		Class superClazz = t.getSuperclass();
		if(superClazz != null)
			getDeclaredFieldsAll(superClazz, listField);
		
		return listField;
	} 
	
	public static <T extends EntityBase> Map<String,Object> convertVoToMap(T t)
	{
		ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Map<String, Object> map = om.convertValue(t, new TypeReference<Map<String, Object>>() {});
        return map;
	}
	
	public static <T extends EntityBase> String getPKField(Class<T> t) 
	{
		List<Field> listField = new ArrayList<>();
		listField = getDeclaredFieldsAll(t , listField);
		for(Field field : listField)
		{
			EntityPK entityPK = field.getDeclaredAnnotation(EntityPK.class);
			EntityFK entityFK = field.getDeclaredAnnotation(EntityFK.class);
			if(entityPK != null && entityFK == null)
				return field.getName();
		}
		return "";
	}
	
	
	public static <T extends EntityBase> T convertMapToVoWithValid(Map<String,Object> map ,Class<T> t , String pkRendomValue) throws InstantiationException, IllegalAccessException
	{
		Annotation annotation;
		T obj = t.newInstance();
		List<Field> listField = new ArrayList<>();
		listField = getDeclaredFieldsAll(t , listField);
		
		for(Field field : listField)
		{
			EntityUtil.EntityValidException exception = EntityUtil.EntityValidException.class.newInstance();
			Object val = map.get(field.getName());
			EntityNOTNULL entityNOTNULL = field.getDeclaredAnnotation(EntityNOTNULL.class);
			EntityPK entityPK = field.getDeclaredAnnotation(EntityPK.class);
			EntityFK entityFK = field.getDeclaredAnnotation(EntityFK.class);
			Length length = field.getDeclaredAnnotation(Length.class);
			
			if(entityPK != null && entityFK == null && (val == null || "".equals(val.toString())) && length != null) {
				field.setAccessible(true);
				field.set(obj, pkRendomValue);
				map.put(field.toString(), pkRendomValue);
				continue;
			}
			
			if(entityNOTNULL != null && (val == null || "".equals(val.toString())))
			{
				if(entityFK != null)
					exception.add(field, VALID.FK_NULL);
				else
					exception.add(field, VALID.NULL);
			}
			if(length != null && val != null && length.max() < val.toString().length())
				exception.add(field, VALID.MAX_LENGTH);
			
			if(exception.getValidError().isEmpty() == false)
				throw exception;
			
			field.setAccessible(true);
			field.set(obj, val);
		}
		
		return obj;
	}
	
}


