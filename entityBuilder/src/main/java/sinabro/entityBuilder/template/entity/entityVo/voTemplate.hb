package {{config.entityPackage}}.entity.entityVo;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Length;

/**
 * @Project : entityBuilder
 * @Date : 2021. 1. 7. 
 * @작성자 : sinabro
 * @변경이력 :
 * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
 */

@Table(name="{{name}}")
public class {{className}} extends EntityBase {
{{#columns}}{{#if (eq allowNullYN 'N')}}
	@EntityNOTNULL{{/if}}{{#if (neq (indexOf name '_YN') -1)}}
	@EntityYN{{/if}}{{#if (eq primaryKeyYN 'Y')}}
	@EntityPK{{/if}}{{#if (eq foreignKeyYN 'Y')}}
	@EntityFK(refTable="{{foreignKey.refTable}}" , refColumn="{{foreignKey.refColumn}}"){{/if}}{{#if (eq javaType 'String') }}
	@EntityRemark(remark = "{{remark}}")
	@Length(max = {{maxLength}}, message = "'{{remark}}'은(는) 최대 길이는 {{maxLength}}입니다.")
    @Column( name = "{{name}}")
	String {{camelCase name}};{{/if}}{{#if (neq javaType 'String') }}
	@Column(name = "{{name}}")
	{{javaType}} {{camelCase name}};{{/if}}
{{/columns}}

	public {{className}}(){}
	
	public {{className}}(Map<String,Object> map) throws Exception
	{
		super(map);
	}
	
	public {{className}}(Map<String,Object> map , boolean bCheckValid ) throws Exception
	{
		super(map,bCheckValid);
	}

{{#columns}}
	public void {{camelCase (concat 'set_' name)}}({{javaType}} {{camelCase name}})
	{
		this.{{camelCase name}} = {{camelCase name}};
	}
	
	public {{javaType}} {{camelCase (concat 'get_' name)}}()
	{
		return this.{{camelCase name}};
	}
{{/columns}}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		{{#columns}}sb.append("{{#if (neq @index 0)}},{{/if}} {{camelCase name}}=" + {{camelCase name}});
		{{/columns}}return sb.toString();
	}
}
