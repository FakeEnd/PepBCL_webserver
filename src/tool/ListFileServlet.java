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
* @Description: �г�Webϵͳ�����������ļ�
* @author: �°�����
* @date: 2015-1-4 ����9:54:40
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
        //��ȡ�ϴ��ļ���Ŀ¼
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
		 	
    	
//        //�洢Ҫ���ص��ļ���
//        Map<String,String> fileNameMap = new HashMap<String,String>();
//        //�ݹ����filepathĿ¼�µ������ļ���Ŀ¼�����ļ����ļ����洢��map������
//        listfile(new File(uploadFilePath),fileNameMap);//File�ȿ��Դ���һ���ļ�Ҳ���Դ���һ��Ŀ¼
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
//        ��Map���Ϸ��͵�listfile.jspҳ�������ʾ
//        request.setAttribute("fileNameMap", fileNameMap);
        
//        System.out.println(fileNameMap.values());
        request.getRequestDispatcher("/listfile.jsp").forward(request, response);
        prelist.clear();
    }
    
    /**
    * @Method: listfile
    * @Description: �ݹ����ָ��Ŀ¼�µ������ļ�
    * @Anthor:�°�����
    * @param file ������һ���ļ���Ҳ����һ���ļ�Ŀ¼
    * @param map �洢�ļ�����Map����
    */ 
    public void listfile(File file,Map<String,String> map){
        //���file����Ĳ���һ���ļ�������һ��Ŀ¼
        if(!file.isFile()){
            //�г���Ŀ¼�µ������ļ���Ŀ¼
            File files[] = file.listFiles();
            //����files[]����
            for(File f : files){
                //�ݹ�
                listfile(f,map);
            }
        }else{
            /**
             * �����ļ������ϴ�����ļ�����uuid_�ļ�������ʽȥ���������ģ�ȥ���ļ�����uuid_����
                file.getName().indexOf("_")�����ַ����е�һ�γ���"_"�ַ���λ�ã�����ļ��������ڣ�9349249849-88343-8344_��_��_��.avi
                ��ôfile.getName().substring(file.getName().indexOf("_")+1)����֮��Ϳ��Եõ���_��_��.avi����
             */
            String realName = file.getName().substring(file.getName().indexOf("_")+1);
            //file.getName()�õ������ļ���ԭʼ���ƣ����������Ψһ�ģ���˿�����Ϊkey��realName�Ǵ����������ƣ��п��ܻ��ظ�
            map.put(file.getName(), realName);
        }
        
        
    }
    
    
    public void listfilename(File file,List<String> showlist){
        //���file����Ĳ���һ���ļ�������һ��Ŀ¼
        if(!file.isFile()){
            //�г���Ŀ¼�µ������ļ���Ŀ¼
            File files[] = file.listFiles();
            //����files[]����
            for(File f : files){
                //�ݹ�
            	listfilename(f,showlist);
            }
        }else{
            /**
             * �����ļ������ϴ�����ļ�����uuid_�ļ�������ʽȥ���������ģ�ȥ���ļ�����uuid_����
                file.getName().indexOf("_")�����ַ����е�һ�γ���"_"�ַ���λ�ã�����ļ��������ڣ�9349249849-88343-8344_��_��_��.avi
                ��ôfile.getName().substring(file.getName().indexOf("_")+1)����֮��Ϳ��Եõ���_��_��.avi����
             */
            String realName = file.getName().substring(file.getName().indexOf("_")+1);
            //file.getName()�õ������ļ���ԭʼ���ƣ����������Ψһ�ģ���˿�����Ϊkey��realName�Ǵ����������ƣ��п��ܻ��ظ�
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
		File csv = new File(path);  // CSV�ļ�·��
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
	            while ((line = br.readLine()) != null)  //��ȡ�������ݸ�line����
	            {
	                everyLine = line;
//	                int s=(int)(Float.parseFloat(everyLine.split(",")[1]));
	                allString.add(everyLine);
	            }
//	            System.out.println("csv���������������"+allString.size());
	    } catch (IOException e)
	    {
	        e.printStackTrace();
	        
	    }finally{
	    	try {
				br.close();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
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