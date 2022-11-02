package {{entityPackage}}.entity.entityVo;

import java.lang.reflect.Field;
import java.util.Map;
import javax.persistence.Column;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.champ.common.userException.ValidException;

/**
 * @Project : entityBuilder
 * @Date : 2021. 1. 7. 
 * @작성자 : sinabro
 * @변경이력 :
 * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
 */
public class EntityBase {
	
	int curPage = 1;
	int perPage = 1;
	int totalPages = 0;
	int totalCnt = 0;
	String orderBy = "";
	
	String pagingYN  = "N";
	String securityYN = "Y";
	
	String usrId;
	String roleName;
	String updatekey;
	String updatekey1;
	String updatekey2;
	String updatekey3;
	
	public EntityBase()
	{
		
	}
	
	public EntityBase(Map<String,Object> map) throws Exception
	{
		BeanUtils.populate(this, map);
	}
	
	public EntityBase(Map<String,Object> map,boolean bCheckValid) throws Exception
	{
		BeanUtils.populate(this, map);
		if(bCheckValid) Valid();
	}
	
	public void Valid() throws Exception {
		String tableName = this.getClass().getSimpleName().replaceAll("([A-Z])","_$1").toUpperCase()+":";
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field field : fields) {
			EntityNOTNULL entityNOTNULL = field.getAnnotation(EntityNOTNULL.class);
			EntityFK entityFK = field.getAnnotation(EntityFK.class);
			EntityPK entityPK = field.getAnnotation(EntityPK.class);
			EntityYN entityYN = field.getAnnotation(EntityYN.class);
			Length length = field.getAnnotation(Length.class);
			Column column = field.getAnnotation(Column.class);
			EntityRemark entityRemark = field.getAnnotation(EntityRemark.class);
			int max = length==null? 0 : length.max();
			
			if(entityNOTNULL != null) {
				Object obj = field.get(this);
				if(obj == null || "".equals(obj.toString())) {
					String message = String.format("'%s'은(는) 필수값 입니다.", entityRemark.remark()==null?column.name():entityRemark.remark());
					throw new ValidException(message, field , tableName+column.name() , entityRemark.remark());
				}
			}
			
			if(length != null ) {
				Object obj = field.get(this);
				if(obj != null) {
					String str = obj.toString();
					if(str != null && str.length() > length.max()) {
						String message = String.format("'%s'은(는) 문자열 길이는 최대 %d 입니다.", entityRemark.remark()==null?column.name():entityRemark.remark() , length.max());
						throw new ValidException(message, field , tableName+column.name() , entityRemark.remark());
					}
				}
			}
			
			if(entityYN != null ) {
				Object obj = field.get(this);
				if(obj != null) {
					String str = obj.toString();
					if(str == null || !("Y".equals(str) || "N".equals(str))) {
						String message = String.format("%s은(는) 'Y or N'값이어야 합니다.", entityRemark.remark()==null?column.name():entityRemark.remark());
						throw new ValidException(message, field , tableName+column.name() , entityRemark.remark());
					}
				}
			}
		}
	}
	
	public void ValidUpdate() throws Exception {
		String tableName = this.getClass().getSimpleName().replaceAll("([A-Z])","_$1").toUpperCase()+":";
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field field : fields) {
			EntityNOTNULL entityNOTNULL = field.getAnnotation(EntityNOTNULL.class);
			EntityFK entityFK = field.getAnnotation(EntityFK.class);
			EntityPK entityPK = field.getAnnotation(EntityPK.class);
			EntityYN entityYN = field.getAnnotation(EntityYN.class);
			Length length = field.getAnnotation(Length.class);
			Column column = field.getAnnotation(Column.class);
			EntityRemark entityRemark = field.getAnnotation(EntityRemark.class);
			int max = length==null? 0 : length.max();
			
			if(entityNOTNULL != null) {
				Object obj = field.get(this);
				if(obj != null && "".equals(obj.toString())) {
					String message = String.format("'%s'은(는) 필수값 입니다.", entityRemark.remark()==null?column.name():entityRemark.remark());
					throw new ValidException(message, field , tableName+column.name() , entityRemark.remark());
				}
			}
			
			if(length != null ) {
				Object obj = field.get(this);
				if(obj != null) {
					String str = obj.toString();
					if(str != null && str.length() > length.max()) {
						String message = String.format("'%s'은(는) 문자열 길이는 최대 %d 입니다.", entityRemark.remark()==null?column.name():entityRemark.remark() , length.max());
						throw new ValidException(message, field , tableName+column.name() , entityRemark.remark());
					}
				}
			}
			
			if(entityYN != null ) {
				Object obj = field.get(this);
				if(obj != null) {
					String str = obj.toString();
					if(str != null && !("Y".equals(str) || "N".equals(str))) {
						String message = String.format("%s은(는) 'Y or N'값이어야 합니다.", entityRemark.remark()==null?column.name():entityRemark.remark());
						throw new ValidException(message, field , tableName+column.name() , entityRemark.remark());
					}
				}
			}
		}
	}
	
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getPerPage() {
		return perPage;
	}
	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getPagingYN() {
		return pagingYN;
	}
	public void setPagingYN(String pagingYN) {
		this.pagingYN = pagingYN;
	}
	public String getSecurityYN() {
		return securityYN;
	}
	public void setSecurityYN(String securityYN) {
		this.securityYN = securityYN;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUpdatekey() {
		return updatekey;
	}
	public void setUpdatekey(String updatekey) {
		this.updatekey = updatekey;
	}
	public String getUpdatekey1() {
		return updatekey1;
	}
	public void setUpdatekey1(String updatekey1) {
		this.updatekey1 = updatekey1;
	}
	public String getUpdatekey2() {
		return updatekey2;
	}
	public void setUpdatekey2(String updatekey2) {
		this.updatekey2 = updatekey2;
	}
	public String getUpdatekey3() {
		return updatekey3;
	}
	public void setUpdatekey3(String updatekey3) {
		this.updatekey3 = updatekey3;
	}
	
	public Map<String,Object> toMap(){
		return new ObjectMapper().convertValue(this, Map.class);
	}
}

