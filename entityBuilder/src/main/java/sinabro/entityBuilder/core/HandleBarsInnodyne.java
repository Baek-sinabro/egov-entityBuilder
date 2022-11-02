/**
 * 
 */
package sinabro.entityBuilder.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;

import sinabro.entityBuilder.util.CommonUtil;

/**
  * @FileName : HandleBarsInnodyne.java
  * @Project : entityBuilder
  * @Date : 2022. 8. 9. 
  * @작성자 : sinabro
  * @변경이력 :
  * @프로그램 설명 : https://github.com/Baek-sinabro/egov-entityBuilder.git
  */

public class HandleBarsInnodyne {
	
	Handlebars _hb = new Handlebars();
	
	public HandleBarsInnodyne() 
	{
		regHelper();
	}
	
	public boolean apply(String templateFilePath , Object Data , String targetFilePath)  throws Exception
	{
		try
		{
			String temlplateString = readFile(templateFilePath);
			apply(temlplateString, Data , new File(targetFilePath));
		}
		catch(Exception e)
		{
			
		}
		return true;		
	}
	
	public boolean apply(File templateFile , Object Data , File targetFile)  throws Exception
	{
		try
		{
			String temlplateString = readFile(templateFile);
			apply(temlplateString, Data , targetFile);
		}
		catch(Exception e)
		{
			
		}
		return true;		
	}
	
	public boolean apply(String templateString , Object Data , File targetFile)  throws Exception
	{
		try
		{
			String content = apply(templateString,Data);
			writeFile(targetFile , content);
		}
		catch(Exception e)
		{
			throw e;
		}
		return true;
	}
	
	
	
	public String apply(String temlplateString , Object Data)  throws Exception
	{
		String genStr = "";
		try
		{
			Template template = loadTemplate(temlplateString);
			genStr = apply(template, Data);
		}
		catch(Exception e)
		{
			throw e;
		}
		return removeEmptyLine(genStr);
	}
	
	public boolean apply(Template template , Object Data , String targetFileName) throws Exception
	{
		try
		{
			return apply(template,Data,new File(targetFileName));
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public boolean apply(Template template , Object Data , File targetFile) throws Exception 
	{
		String genStr = "";
		try
		{
			genStr = apply(template,Data);
			writeFile(targetFile,genStr);			
		}
		catch(Exception e)
		{
			throw e;
		}
		return true;
	}
	
	public String apply(Template template , Object Data) throws Exception
	{
		String genStr = "";
		try
		{
			genStr = template.apply(Data);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return removeEmptyLine(genStr);
	}
	
	public Template loadTemplate(File file) throws IOException
	{
		return loadTemplate(readFile(file));
	}
	
	public Template loadTemplate(String templateString) throws IOException
	{
		return _hb.compileInline(templateString);
	}
	
	public <T> void registerHelper(String conText , Helper<T> helper)
	{
		_hb.registerHelper(conText, helper);
	}
	
	private String removeEmptyLine(String str) {
		
		if(str.indexOf("package")==0) return str;// java파일은 제외
		
		String[] other = new String[] {"<sql","</sql","<select","</select","<update","</update","<insert","</insert","<delete","</delete"};
		StringBuffer sb = new StringBuffer();
		String[] arr = str.split("[\n]");
		for(int i=0;i<arr.length;i++) {
			String buf = arr[i].trim();
			if(!"".equals(buf)) {
				sb.append(arr[i]);
				sb.append("\n");
				for(int j=0;j<other.length;j++) {
					if(buf.indexOf(other[j])==0)
						sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
	
	private String readFile(String filePath) throws IOException
	{
		File file = new File(filePath);
		return readFile(file);
	}
	
	private String readFile(File file) throws IOException
	{
		String buf;
		StringBuffer sb = new StringBuffer();
		BufferedReader bReader = new BufferedReader(new FileReader(file));
		while((buf=bReader.readLine()) != null)
		{
			sb.append(buf);
			sb.append("\n");
		}
		if(bReader != null) bReader.close();
		
		return sb.toString();
	}
	
	public void writeFile(String filePath , String content) throws IOException
	{
		File file = new File(filePath);
		writeFile(file,content);
	}
	
	private void writeFile(File file , String content) throws IOException
	{
		file.getParentFile().mkdirs();

		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		bWriter.write(content);
		bWriter.close();
	}
	
	private boolean isExist(String filePath)
	{
		File file = new File(filePath);
		return file.exists();
	}
	
	protected void regHelper()
	{
		_hb.registerHelper("eq", new Helper<Object>(){
			public Object apply(Object context, Options options) throws IOException {
				// TODO Auto-generated method stub
				Object obj = options.param(0);
				boolean ret = (obj != null && obj.equals(context));
				return ret;
			}
			
		});
		_hb.registerHelper("neq", new Helper<Object>(){
			public Object apply(Object context, Options options) throws IOException {
				// TODO Auto-generated method stub
				Object obj = options.param(0);
				boolean ret = !(obj.equals(context));
				return ret;
			}
			
		});
		_hb.registerHelper("and", new Helper<Object>(){
			public Object apply(Object context, Options options) throws IOException {
				// TODO Auto-generated method stub
				Object obj = options.param(0);
				boolean ret = (boolean)context&&(boolean)obj;
				return ret;
			}
			
		});
		_hb.registerHelper("or", new Helper<Object>(){
			public Object apply(Object context, Options options) throws IOException {
				// TODO Auto-generated method stub
				Object obj = options.param(0);
				boolean ret = (boolean)context||(boolean)obj;
				return ret;
			}
		});
		
		_hb.registerHelper("camelCase", new Helper<String>(){
			public String apply(String context, Options options) throws IOException {
				return CommonUtil.toCamelcase(context);
			}
		});
		
		_hb.registerHelper("concat", new Helper<String>(){
			public String apply(String context, Options options) throws IOException {
				StringBuffer sb = new StringBuffer();
				int size = options.params.length;
				for(int i=0;i<size;i++)
				{
					if(i == 0) sb.append(context);
					sb.append(options.param(i).toString());
				}
				return sb.toString();
			}
		});
		
		_hb.registerHelper("in", new Helper<Object>(){
			public Object apply(Object context, Options options) throws IOException {
				String source = context.toString();
				String[] arr = options.param(0).toString().split(",");
				for(int i=0;i<arr.length;i++)
				{
					if(source.equals(arr[i]))
						return true;
				}
				return false;
			}
		});
		
		_hb.registerHelper("indexOf", new Helper<Object>(){
			public Object apply(Object context, Options options) throws IOException {
				String source = context.toString();
				String option = options.param(0).toString();
				return source.indexOf(option);
			}
		});
	}
	

}
