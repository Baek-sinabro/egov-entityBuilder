작성자 : sinabro

연락처 : sinabro.sig61@gmail.com

목적 : 전자정부프레임웍에서 mybatis 연동으로 사용시 insert , update , delete , select 쿼리를 매번
작성하는 번거로움이 있어 자동으로 mybatis용 mapper를 생성해 주고 Table에 대응하는 vo를 자동으로 생성해 준다.


주요기능 : 
1. Table의 메타 데이타 기반으로 vo와 mapper를 생성해 준다.
2. 기본 insert , update , delete , select 지원
3. select 시 페이징 쿼리 지원
4. insert , update시 not null 필드에 값이 없으면 Exception으로 메시지 처리해 준다.
5. insert , update시 필드값 최대길이 점증해서 Exception으로 메시지 처리해 준다.

사용방법 : java -jar entityBuilder [config.property 파일경로]
*config.property 파일경로가 없으면 현재 폴더에서 config.property를 찾는다.
*config.property가 없으면 config.property파일을 생성해준다. 설정값 편집해서 실행하면 된다.
*config.property에 대한 설명은 config.property파일에 명시되어 있다.

spring적용 예시:

@Controler
public class MyCtrl() {

	@Autowired EntityService entityService;
	
	@ResponseBody
	@RequestMapping("insertTest")
	public String test(@RequestBody Map<String,Object> paramMap) {
	
		try{
			TbUser tbUser = new TbUser(paramMap);
			entityService.insert(tbUser);
		} catch(EntityValidException e) {
			return e.getMessage();
		}
		return "성공";
	}
}  

