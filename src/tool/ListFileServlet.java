package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @ClassName: ListFileServlet
* @Description: 列出Web系统中所有下载文件
* @author: 孤傲苍狼
* @date: 2015-1-4 下午9:54:40
*
*/ 
public class ListFileServlet extends HttpServlet {
	static ArrayList<String> prelist = new ArrayList<String>();

	public void ResultList(String fileName,double g) {
        try {

        	NumberFormat num = NumberFormat.getPercentInstance(); 
 	    	num.setMaximumIntegerDigits(2); 
 	    	num.setMaximumFractionDigits(2); 
			BufferedReader br = new BufferedReader(new FileReader(fileName));
        	String line;
        	String m="";
        	 String result="";
        	 String d; 
        	 int k=1;
        	while((line=br.readLine())!=null )
        		
            {    
        		Double c = Double.parseDouble(line.split("\\,")[3]);		
        	 	if(c>=g) {
        			
        			d=num.format(c);
            	
            	
//        		result=line.split("\\,")[0].split("\\-")[0]+"("+k+")"+","+line.split("\\,")[1]+","+d+","+(Integer.parseInt(a)+1)+"-"+(Integer.parseInt(a)+t+1);
              
                result=line.split("\\,")[0]+","+line.split("\\,")[1]+","+line.split("\\,")[2]+","+d;}
        	 	else{
        			
        			d=num.format(1-c);
        			result=line.split("\\,")[0]+","+line.split("\\,")[1]+","+line.split("\\,")[2]+","+d;}
              prelist.add(result);}
              
            
           br.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            }
	}
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取上传文件的目录
//        String uploadFilePath = this.getServletContext().getRealPath("/WEB-INF/upload");
 
    	String FilePath=(String) request.getAttribute("message1");
    	String Pep = (String) request.getParameter("Pep");
    	String SystemPath=FilePath+"program/"+Pep+"program/"+Pep+"_DataSet/";
//    	System.out.println(uploadFilePath);
		List<String> showlist = new ArrayList<String>();
		
			ResultList(SystemPath+"/Result.txt",0.5);
			request.setAttribute("showlist", prelist);       
//			RequestDispatcher rd = request.getRequestDispatcher("/result2.jsp");
//			rd.forward(request, response);
//			prelist.clear();
		 	
    	
//        //存储要下载的文件名
//        Map<String,String> fileNameMap = new HashMap<String,String>();
//        //递归遍历filepath目录下的所有文件和目录，将文件的文件名存储到map集合中
//        listfile(new File(uploadFilePath),fileNameMap);//File既可以代表一个文件也可以代表一个目录
//        List<String> namelist=new ArrayList<String>();
//        
//        listfilename(new File(uploadFilePath),namelist);
//        List<String> showlist=new ArrayList<String>();
//        String fasta="";
//        List<String> predict=new ArrayList<String>();
//        for(String tempvalue:namelist){
//        	if(tempvalue.contains(".fasta.")){
//        		predict=getcsv(uploadFilePath+ System.getProperty("file.separator")+tempvalue);
//        	}else{
//        		fasta=getfasta(uploadFilePath+ System.getProperty("file.separator")+tempvalue);
//        	}
//        }
////        System.out.println("fasta"+fasta);
//        List<String> sublist=new ArrayList<String>();
//        sublist = subSeq(fasta);
//        
//        for(String value:sublist){
//        	System.out.println("sublist"+value);
//        }
//        predict.remove(0);
//		for (int i = 0; i < predict.size(); i++) {
//					
//					String out[] = predict.get(i).split(",");
//					String sub = sublist.get(i * 2);
//					String subseq = sublist.get(i * 2 + 1);
//						int predict_label=(int)(Float.parseFloat(out[0]));
//						showlist.add(sub + "," + subseq + ","+predict_label+"," + out[1]);//
//					System.out.println("showlist:"+sub + "," + subseq + ","+predict_label+"," + out[1]);
//					
//				
//		
//				}
//
//        request.setAttribute("showlist", showlist);
//        将Map集合发送到listfile.jsp页面进行显示
//        request.setAttribute("fileNameMap", fileNameMap);
        
//        System.out.println(fileNameMap.values());
        request.getRequestDispatcher("/listfile.jsp").forward(request, response);
        prelist.clear();
    }
    
    /**
    * @Method: listfile
    * @Description: 递归遍历指定目录下的所有文件
    * @Anthor:孤傲苍狼
    * @param file 即代表一个文件，也代表一个文件目录
    * @param map 存储文件名的Map集合
    */ 
    public void listfile(File file,Map<String,String> map){
        //如果file代表的不是一个文件，而是一个目录
        if(!file.isFile()){
            //列出该目录下的所有文件和目录
            File files[] = file.listFiles();
            //遍历files[]数组
            for(File f : files){
                //递归
                listfile(f,map);
            }
        }else{
            /**
             * 处理文件名，上传后的文件是以uuid_文件名的形式去重新命名的，去除文件名的uuid_部分
                file.getName().indexOf("_")检索字符串中第一次出现"_"字符的位置，如果文件名类似于：9349249849-88343-8344_阿_凡_达.avi
                那么file.getName().substring(file.getName().indexOf("_")+1)处理之后就可以得到阿_凡_达.avi部分
             */
            String realName = file.getName().substring(file.getName().indexOf("_")+1);
            //file.getName()得到的是文件的原始名称，这个名称是唯一的，因此可以作为key，realName是处理过后的名称，有可能会重复
            map.put(file.getName(), realName);
        }
        
        
    }
    
    
    public void listfilename(File file,List<String> showlist){
        //如果file代表的不是一个文件，而是一个目录
        if(!file.isFile()){
            //列出该目录下的所有文件和目录
            File files[] = file.listFiles();
            //遍历files[]数组
            for(File f : files){
                //递归
            	listfilename(f,showlist);
            }
        }else{
            /**
             * 处理文件名，上传后的文件是以uuid_文件名的形式去重新命名的，去除文件名的uuid_部分
                file.getName().indexOf("_")检索字符串中第一次出现"_"字符的位置，如果文件名类似于：9349249849-88343-8344_阿_凡_达.avi
                那么file.getName().substring(file.getName().indexOf("_")+1)处理之后就可以得到阿_凡_达.avi部分
             */
            String realName = file.getName().substring(file.getName().indexOf("_")+1);
            //file.getName()得到的是文件的原始名称，这个名称是唯一的，因此可以作为key，realName是处理过后的名称，有可能会重复
            if(realName.contains(".fasta") && realName.contains(".fasta.")==false){
            	 showlist.add(file.getName());
            }else if( realName.contains(".fasta.predict.csv")  ){
            	showlist.add(file.getName());
            }
           
        }
        
        
    }
    
    public String getfasta(String path) throws IOException{
    	BufferedReader input = new BufferedReader(new  FileReader(path));  
    	String message = "";  
    	String line = null;  
    	while((line = input.readLine()) != null) {  
    	    message += line+"\r\n";  
    	}  
    	System.out.println(message);  
    	return message;
    }
    
	public List<String> subSeq(String text) {
		List<String> list = new ArrayList<String>();
		String[] lines = text.split("\r\n");

		StringBuffer sequence = new StringBuffer();
		String lasttitle = "", title = "";
		for (int index = 0; index < lines.length; index++) {
			if (lines[index].length() == 0) {

				continue;
			}
			if (lines[index].charAt(0) == '>') {

				title = lines[index].substring(1);
				if (sequence.length() != 0) {
					list.add(lasttitle);
					list.add(sequence.toString());

				}

				lasttitle = title;
				sequence.setLength(0);
				continue;
			}
			sequence.append(lines[index]);
		}
		if (sequence.length() != 0) {

			list.add(lasttitle);
			list.add(sequence.toString());
		}
		return list;

	}
	
	
	
	public List<String> getcsv(String path){
		File csv = new File(path);  // CSV文件路径
		List<String> allString= new ArrayList<>();;
	    BufferedReader br = null;
	    try
	    {
	        br = new BufferedReader(new FileReader(csv));
	    } catch (FileNotFoundException e)
	    {
	        e.printStackTrace();
	    }finally{
	    	
	    }
	    String line = "";
	    String everyLine = "";
	    try {
	            while ((line = br.readLine()) != null)  //读取到的内容给line变量
	            {
	                everyLine = line;
//	                int s=(int)(Float.parseFloat(everyLine.split(",")[1]));
	                allString.add(everyLine);
	            }
//	            System.out.println("csv表格中所有行数："+allString.size());
	    } catch (IOException e)
	    {
	        e.printStackTrace();
	        
	    }finally{
	    	try {
				br.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	    }
		return allString;
	}
    
    
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}