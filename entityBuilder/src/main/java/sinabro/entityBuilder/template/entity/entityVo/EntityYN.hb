package {{entityPackage}}.entity.entityVo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project : entityBuilder
 * @Date : 2021. 1. 7. 
 * @작성자 : sinabro
 * @변경이력 :
 * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
 */

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityYN {
}
