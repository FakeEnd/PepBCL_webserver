package tool;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.ProgressListener;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

/**
 * Servlet implementation class ProcessServlet
 */
@WebServlet("/ProcessServlet")
public class ProcessServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static ArrayList<String> prelist = new ArrayList<String>();

    private static void printMessage(final InputStream input) {
        new Thread(new Runnable() {
            public void run() {
                Reader reader = new InputStreamReader(input);
                BufferedReader bf = new BufferedReader(reader);
                String line = null;
                try {
                    while ((line = bf.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String realpath = this.getServletContext().getRealPath("/");

        String data = "";
        String input_path = "";
        String input_filename = "";
        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
        //上传时生成的临时文件保存目录
        String tempPath = this.getServletContext().getRealPath("/WEB-INF/temp");
        File tmpFile = new File(tempPath);
        if (!tmpFile.exists()) {
            //创建临时目录
            tmpFile.mkdir();
        }

        //消息提示
        String message = "";
        try {
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置工厂的缓冲区的大小，当上传的文件大小超过缓冲区的大小时，就会生成一个临时文件存放到指定的临时目录当中。
            factory.setSizeThreshold(1024 * 100);//设置缓冲区的大小为100KB，如果不指定，那么缓冲区的大小默认是10KB
            //设置上传时生成的临时文件的保存目录
            factory.setRepository(tmpFile);
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //监听文件上传进度
            upload.setProgressListener(new ProgressListener() {
                public void update(long pBytesRead, long pContentLength, int arg2) {
                    System.out.println("文件大小为：" + pContentLength + ",当前已处理：" + pBytesRead);
                    /**
                     * 文件大小为：14608,当前已处理：4096
                     文件大小为：14608,当前已处理：7367
                     文件大小为：14608,当前已处理：11419
                     文件大小为：14608,当前已处理：14608
                     */
                }
            });
            //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");
            //3、判断提交上来的数据是否是上传表单的数据
            if (!ServletFileUpload.isMultipartContent(request)) {
                //按照传统方式获取数据
                return;
            }

            //设置上传单个文件的大小的最大值，目前是设置为1024*1024字节，也就是1MB
            upload.setFileSizeMax(1024 * 1024);
            //设置上传文件总量的最大值，最大值=同时上传的多个文件的大小的最大值的和，目前设置为10MB
            upload.setSizeMax(1024 * 1024 * 10);
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(new ServletRequestContext(request));
            for (FileItem item : list) {
                System.out.println(item);
                //如果fileitem中封装的是普通输入项的数据
                if (item.isFormField()) {
                    String name = item.getFieldName();
//                    System.out.print(name);
                    //解决普通输入项的数据的中文乱码问题
                    String value = item.getString("UTF-8");
                    //value = new String(value.getBytes("iso8859-1"),"UTF-8");
                    if (name.trim().equals("data")) {
                        data = value;
                    }
//                    System.out.println(name + "=" + value);
                } else {//如果fileitem中封装的是上传文件
                    //得到上传的文件名称，
                    String filename = item.getName();
                    if (filename == null || filename.trim().equals("")) {
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    filename = filename.substring(filename.lastIndexOf(System.getProperty("file.separator")) + 1);
                    //得到上传文件的扩展名
                    String fileExtName = filename.substring(filename.lastIndexOf(".") + 1);
                    //如果需要限制上传的文件类型，那么可以通过文件的扩展名来判断上传的文件类型是否合法
                    System.out.println("上传的文件的扩展名是：" + fileExtName);
                    //获取item中的上传文件的输入流
                    InputStream in = item.getInputStream();
                    //得到文件保存的名称
                    String saveFilename = makeFileName(filename);
                    //得到文件的保存目录
                    String realSavePath = makePath(saveFilename, savePath);
                    input_path = realSavePath;
                    input_filename = saveFilename;
                    //创建一个文件输出流
                    FileOutputStream out = new FileOutputStream(realSavePath + System.getProperty("file.separator") + saveFilename);
                    System.out.println("realSavePath：" + realSavePath);
                    System.out.println("saveFilename：" + saveFilename);
                    //创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    //判断输入流中的数据是否已经读完的标识
                    int len = 0;
                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    while ((len = in.read(buffer)) > 0) {
                        //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                        out.write(buffer, 0, len);
                    }
                    //关闭输入流
                    in.close();
                    //关闭输出流
                    out.close();
                    //删除处理文件上传时生成的临时文件
                    //item.delete();
                    message = "文件上传成功！";
                    System.out.println(message);
//                    System.out.println(realSavePath + System.getProperty("file.separator") + saveFilename);
//                    System.out.println(message);
                    request.setAttribute("message", realSavePath);

                }
            }
        } catch (FileSizeLimitExceededException e) {
            e.printStackTrace();
            System.out.println("single file is too large");
            request.setAttribute("message", "单个文件超出最大值！！！");
            request.getRequestDispatcher("/message.jsp").forward(request, response);
            return;
        } catch (SizeLimitExceededException e) {
            e.printStackTrace();
            System.out.println("the number of files are exceed");
            request.setAttribute("message", "上传文件的总的大小超出限制的最大值！！！");
            request.getRequestDispatcher("/message.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            System.out.println("load file fail");
            message = "文件上传失败！";
            e.printStackTrace();
        }

//		System.out.println("data second"+data);
        if (!data.equals("") && data != null) {
            System.out.println("data is not empty and is not null");
            //得到文件保存的名称
            System.out.println("test1");
            String saveFilename = makeFileName("input.fasta");
            //得到文件的保存目录
            String realSavePath = makePath(saveFilename, savePath);
            input_path = realSavePath;
            input_filename = saveFilename;
            //创建一个文件输出流
            BufferedWriter bw = new BufferedWriter(new FileWriter(realSavePath + System.getProperty("file.separator") + saveFilename));
            bw.write(data);
            bw.flush();
            bw.close();
//    		request.setAttribute("message", realSavePath);
//    		request.setAttribute("message1", realpath);
//    		Pep = "Nm7";
//    		System.out.println(Pep);
        }
        //input_path = "C:\\Users\\chenhuangrong\\Desktop\\files";
        try {
            predict(input_path, input_filename, realpath);
            System.out.println("test");
            System.out.println(input_path);
            System.out.println(input_filename);
            System.out.println(realpath);

//            find_sub(input_path, input_filename, realpath);
//            fetch_features1(input_path, input_filename, realpath);
//			fetch_features2(input_path,input_filename,realpath);
            //fetch_features3(Pep,input_path,input_filename,realpath);
//			fetch_features3(input_path,input_filename,realpath);
//			fetch_features4(input_path,input_filename,realpath);
//            fetch_features5(input_path, input_filename, realpath);
//			fetch_features6(input_path,input_filename,realpath);
//			fetch_features7(input_path,input_filename,realpath);
//            fetch_features8(input_path, input_filename, realpath);
//            generating_xls_results(input_path, input_filename, realpath);
//            Get_Result_csv(input_path, input_filename);
//			Pep = "Nm7";
//			 request.setAttribute("Pep", Pep);
//			 System.out.println("后面的PEP是："+Pep);

//            System.out.print(input_path + System.getProperty("file.separator") + "/Result.txt");
//        ResultList("E:\\PepBCL\\web\\WEB-INF\\result.tsv");
            ResultList(input_path + System.getProperty("file.separator") + "result.tsv");
//            System.out.print(input_path + System.getProperty("file.separator") + "/Result.txt");
            System.out.print(prelist);
            request.setAttribute("prelist", prelist);
            RequestDispatcher rd = request.getRequestDispatcher("/result.jsp");
            rd.forward(request, response);
            prelist.clear();

        } catch (InterruptedException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        //String SystemPath=realpath+"program/"+Pep+"_DataSet/";
//    	System.out.println(uploadFilePath);

    }

    public void predict(String input_path, String input_filename, String realpath) throws IOException, InterruptedException {
        String file_path = input_path + System.getProperty("file.separator") + input_filename;
        String tsv_path = input_path + System.getProperty("file.separator") + "input.tsv";
        ArrayList<String> list_name = new ArrayList();
        ArrayList<String> list_sequence = new ArrayList();
        ArrayList<String> list_index = new ArrayList();
        ArrayList list_sample = new ArrayList();

        try {
            RandomAccessFile br1 = new RandomAccessFile(new File(file_path), "r");
            RandomAccessFile br2 = new RandomAccessFile(new File(file_path), "r");
            RandomAccessFile br3 = new RandomAccessFile(new File(file_path), "r");
            FileOutputStream wr = new FileOutputStream(new File(tsv_path));
            String str = null;

            while ((str = br1.readLine()) != null) {
                if (!str.equals("") && str.startsWith(">")) {
                    list_name.add(str.split(">")[1]);
                }
            }

            System.out.println("list_name:" + list_name);

            while ((str = br2.readLine()) != null) {
                if (!str.equals("") && !str.startsWith(">")) {
                    list_sequence.add(str);
                }
            }

            System.out.println("list_sequence:" + list_sequence);

            while ((str = br3.readLine()) != null) {
                if (!str.equals("") && str.startsWith(">")) {
                    list_index.add(str);
                }
            }

            System.out.println("list_index:" + list_index);

            int i;
            for (i = 0; i < list_name.size(); ++i) {
                list_sample.add((String) list_sequence.get(i));
            }

            System.out.println("list_sample:" + list_sample);
            wr.write("sequence\n".getBytes());

            for (i = 0; i < list_sample.size(); ++i) {
                wr.write(((String) list_sample.get(i)).getBytes());
                wr.write("\r\n".getBytes());
            }

            br1.close();
            br2.close();
            br3.close();
            wr.close();
        } catch (FileNotFoundException var16) {
            var16.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String python_path = "/root/anaconda3/envs/hwj_lib/bin/python";
        String model_path = realpath + "program/prot_bert/train/Evaluate.py";
        String result_path = input_path + System.getProperty("file.separator");
        System.out.println("py path:" + python_path);
        System.out.println("model_path:" + model_path);
        System.out.println("input_path:" + input_path);
        System.out.println("tsv_path:" + tsv_path);
        System.out.println("realpath:" + realpath);

        String[] args = new String[]{python_path, model_path, tsv_path, result_path};
//        String[] args = new String[]{python_path, model_path, "-input_path",tsv_path, "-output_path",result_path};
        System.out.println(args);
        Process proc = Runtime.getRuntime().exec(args);
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        System.out.println("line:" + line);
//        printMessage(proc.getInputStream());
//        printMessage(proc.getErrorStream());
        while ((line = in.readLine()) != null) {
            System.out.println("line:" + line);
        }

        in.close();
        proc.waitFor();
        System.out.println("proc.exitValue():" + proc.exitValue());

    }


    public void ResultList(String fileName) {
        try {

            NumberFormat num = NumberFormat.getPercentInstance();
            num.setMaximumIntegerDigits(2);
            num.setMaximumFractionDigits(2);
            //NumberFormat nf = NumberFormat.getNumberInstance();
            //nf.setMinimumFractionDigits(4);
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            //String m="";
            String result = "";
            // String d;
            //int k=1;
            line = br.readLine();
            while ((line = br.readLine()) != null) {


                result = line.split("\t")[0] + "," + line.split("\t")[1] + "," + line.split("\t")[2] + "," + line.split("\t")[3];
                prelist.add(result);
            }

            br.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void find_sub(String path, String filename, String realpath) {
        String file_path = path + System.getProperty("file.separator") + filename;
        String output_file_name = filename.split("_")[1].split("_")[0] + "_subs.txt";
        String output_file_path = path + System.getProperty("file.separator") + output_file_name;
        String output_index = filename.split("_")[1].split("_")[0] + "_index.csv";
        String output_index_path = path + System.getProperty("file.separator") + output_index;
        String output_position = filename.split("_")[1].split("_")[0] + "_position.csv";
        String output_position_path = path + System.getProperty("file.separator") + output_position;
        //		System.out.println("final:"+path + System.getProperty("file.separator") + filename);
        List<String> params = new ArrayList<String>();
        //python版本
//                    params.add("/anaconda3/envs/python27/bin/python2.7");
        params.add("D:\\anaconda3\\envs\\pytorch\\python.exe");
        params.add(realpath + "program/subs.py");
        params.add(file_path);
        params.add(output_file_path);
        params.add(output_index_path);
        params.add(output_position_path);

        //  params.add(Pep);
        //        System.out.println(params);
        Process process = null;
        try {
            process = new ProcessBuilder(params).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        process.destroy();
    }

    //AthMethPre
    private void fetch_features1(String path, String filename, String realpath) throws IOException, InterruptedException {

        String file_path = path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0] + "_subs.txt";
        String output_file_name = filename.split("_")[1].split("_")[0] + "_AthMethPre.csv";
        String output_file_path = path + System.getProperty("file.separator") + output_file_name;
        //		System.out.println("final:"+path + System.getProperty("file.separator") + filename);
        List<String> params = new ArrayList<String>();
        //		System.out.println("feature representation file:"+realpath+"/program/2RFH-FeatureRepresentation.py");
        //python版本
        params.add("D:\\anaconda3\\envs\\pytorch\\python.exe");
//            params.add("D:\\anaconda3\\envs\\pytorch\\python.exe");
        params.add(realpath + "program/AthMethPre_500.py");
        params.add(file_path);
        params.add(output_file_path);
        //  params.add(Pep);
        //        System.out.println(params);
        Process process = new ProcessBuilder(params).start();
        process.waitFor();
        process.destroy();
    }

    //PCP
    private void fetch_features5(String path, String filename, String realpath) throws IOException, InterruptedException {

        //String filename_without_EXT = filename.substring(0,filename.lastIndexOf("."));
        //	System.out.println("filename_without_EXT:"+filename_without_EXT);
        String file_path = path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0] + "_subs.txt";
        String output_file_name = filename.split("_")[1].split("_")[0] + "_PCP.csv";
        String output_file_path = path + System.getProperty("file.separator") + output_file_name;
        //	System.out.println("final:"+path + System.getProperty("file.separator") + filename);
        List<String> params = new ArrayList<String>();
        //	System.out.println("feature representation file:"+realpath+"/program/2RFH-FeatureRepresentation.py");
        //	String a="java -jar "+realpath+"program/Result.jar "+file_path+" "+Pep;
        //python版本
//        params.add("/anaconda3/envs/python27/bin/python2.7");
        params.add("D:\\anaconda3\\envs\\pytorch\\python.exe");
        // params.add("-jar");
        params.add(realpath + "program/PCP.py");
        params.add(file_path);
        params.add(output_file_path);
        params.add("RNA");
        params.add("0");
        params.add(realpath + "program/physical_chemical_properties_RNA.txt");

        //    System.out.println(params);
        Process process = new ProcessBuilder(params).start();
        process.waitFor();
        process.destroy();
    }

    //RFH
    private void fetch_features8(String path, String filename, String realpath) throws IOException, InterruptedException {

        String file_path = path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0] + "_subs.txt";
        String output_file_name = filename.split("_")[1].split("_")[0] + "_RFH.csv";
        String output_file_path = path + System.getProperty("file.separator") + output_file_name;
        //	System.out.println("final:"+path + System.getProperty("file.separator") + filename);
        List<String> params = new ArrayList<String>();
        //	System.out.println("feature representation file:"+realpath+"/program/2RFH-FeatureRepresentation.py");
        //	String a="java -jar "+realpath+"program/Result.jar "+file_path+" "+Pep;
        //python版本
//        params.add("/anaconda3/envs/python27/bin/python2.7");
        params.add("D:\\anaconda3\\envs\\pytorch\\python.exe");
        // params.add("-jar");
        params.add(realpath + "program/RFH.py");
        params.add(file_path);
        params.add(output_file_path);
        params.add("RNA");
        params.add("0");
        //params.add(Pep);

        //System.out.println(params);
        Process process = new ProcessBuilder(params).start();
        process.waitFor();
        process.destroy();
    }

    private void generating_xls_results(String path, String filename, String realpath) throws IOException, InterruptedException {

        //System.out.println("Location_name:"+location_name);
        //	String file_path1=path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0]+"_2RFH.csv";
        String file_path2 = path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0] + "_AthMethPre.csv";
        //	String file_path3=path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0]+"_KNN.csv";
        //	String file_path4=path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0]+"_MMI.csv";
        String file_path5 = path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0] + "_PCP.csv";
        //	String file_path6=path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0]+"_PseDNC.csv";
        //	String file_path7=path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0]+"_PseEIIP.csv";
        String file_path8 = path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0] + "_RFH.csv";
        String output_file_name = filename.split("_")[1].split("_")[0] + "_3features_predict.csv";
        String output_file_path = path + System.getProperty("file.separator") + output_file_name;

        List<String> params1 = new ArrayList<String>();
        //python版本
//        params1.add("/anaconda3/envs/python27/bin/python2.7");
        params1.add("python");
        params1.add(realpath + "program/predict_prob_model.py");
        //    params1.add(file_path1);
        //    params1.add(realpath+"program/"+location_name+System.getProperty("file.separator")+"2RFH_dimensions.csv");
        //    params1.add(realpath+"program/"+location_name+System.getProperty("file.separator")+"SVMcrossvalidation_2RFH.model");

        params1.add(file_path2);
        params1.add(realpath + "program/" + System.getProperty("file.separator") + "AthMethPre_dimensions.csv");
        params1.add(realpath + "program/" + System.getProperty("file.separator") + "AthMethPre.model");

        //    params1.add(file_path3);
        //    params1.add(realpath+"program/"+location_name+System.getProperty("file.separator")+"KNN_dimensions.csv");
        //    params1.add(realpath+"program/"+location_name+System.getProperty("file.separator")+"SVMcrossvalidation_KNN.model");

        //    params1.add(file_path4);
        //    params1.add(realpath+"program/"+location_name+System.getProperty("file.separator")+"MMI_dimensions.csv");
        //    params1.add(realpath+"program/"+location_name+System.getProperty("file.separator")+"SVMcrossvalidation_MMI.model");

        params1.add(file_path5);
        params1.add(realpath + "program/" + System.getProperty("file.separator") + "PCP_dimensions.csv");
        params1.add(realpath + "program/" + System.getProperty("file.separator") + "PCP.model");

        //    params1.add(file_path6);
        //    params1.add(realpath+"program/"+location_name+System.getProperty("file.separator")+"PseDNC_dimensions.csv");
        //    params1.add(realpath+"program/"+location_name+System.getProperty("file.separator")+"SVMcrossvalidation_PseDNC.model");
        //
        //    params1.add(file_path7);
        //    params1.add(realpath+"program/"+location_name+System.getProperty("file.separator")+"PseEIIP_dimensions.csv");
        //    params1.add(realpath+"program/"+location_name+System.getProperty("file.separator")+"SVMcrossvalidation_PseEIIP.model");

        params1.add(file_path8);
        params1.add(realpath + "program/" + System.getProperty("file.separator") + "RFH_dimensions.csv");
        params1.add(realpath + "program/" + System.getProperty("file.separator") + "RFH.model");

        params1.add(realpath + "program/" + System.getProperty("file.separator") + "iter_model/");
        //System.out.println("best_features:"+realpath+"program/"+location_name+System.getProperty("file.separator")+"best_f-score_features.csv");


        params1.add(output_file_path);
        //params1.add(best_feature_subset_path);
        //params1.add(log_path);
        System.out.println(params1);
        Process process1 = new ProcessBuilder(params1).start();
        process1.waitFor();
        BufferedReader br = new BufferedReader(new InputStreamReader(process1.getInputStream()));
        String line;
        StringBuffer s = new StringBuffer();
        while ((line = br.readLine()) != null) {
            s.append(line);
        }
        process1.destroy();

    }


    private void Get_Result_csv(String path, String filename) throws IOException, InterruptedException {

        //String filename_without_EXT = filename.substring(0,filename.lastIndexOf("."));
        //	System.out.println("filename_without_EXT:"+filename_without_EXT);
        try {
//            String file_path1=path + System.getProperty("file.separator") + filename;
            String file_path2 = path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0] + "_3features_predict.csv";
            String file_path3 = path + System.getProperty("file.separator") + "Result.txt";
            String file_path4 = path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0] + "_index.csv";
            String file_path5 = path + System.getProperty("file.separator") + filename.split("_")[1].split("_")[0] + "_position.csv";

            //	System.out.println("file_path1:"+file_path1);
            ArrayList<String> list1 = new ArrayList<String>();
            ArrayList<String> list2 = new ArrayList<String>();
            ArrayList<String> list3 = new ArrayList<String>();
            ArrayList<String> list4 = new ArrayList<String>();
            ArrayList<String> list5 = new ArrayList<String>();

//            RandomAccessFile br1=new RandomAccessFile(new File(file_path1), "r");
            RandomAccessFile br2 = new RandomAccessFile(new File(file_path2), "r");
            RandomAccessFile br3 = new RandomAccessFile(new File(file_path4), "r");
            RandomAccessFile br4 = new RandomAccessFile(new File(file_path5), "r");
            FileOutputStream wr1 = new FileOutputStream(new File(file_path3));

            String str = null;


            //seq No.
            while ((str = br3.readLine()) != null) {

                list1.add(str.split(",")[0]);

            }
            //		System.out.println("label:"+list3);
            //seq pos
            while ((str = br4.readLine()) != null) {

                list2.add(str.split(",")[0]);

            }
            //		System.out.println("label:"+list3);

            //label
            br2.seek(0);

            while ((str = br2.readLine()) != null) {
                if (str.startsWith("label")) {
                    continue;
                } else {
                    list3.add(str.split(",")[0]);
                }
            }
            //		System.out.println("label:"+list3);

            //scores
            br2.seek(0);
            while ((str = br2.readLine()) != null) {
                if (str.startsWith("label")) {
                    continue;
                } else {
                    list4.add(str.split(",")[1]);
                }
            }

            //	System.out.println("scores:"+list4);

            for (int i = 0; i < list1.size(); i++) {

                list5.add(list1.get(i) + "," + list2.get(i) + "," + list3.get(i) + "," + list4.get(i));

            }
            //    System.out.println("res:"+list5);
            for (int i = 0; i < list5.size(); i++) {
                wr1.write((list5.get(i)).getBytes());

                wr1.write(("\r\n").getBytes());

            }
            br2.close();
            br3.close();
            br4.close();
            wr1.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String makeFileName(String filename) {  //2.jpg
        //为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
        return UUID.randomUUID().toString() + "_" + filename;
    }

    private String makePath(String filename, String savePath) {
        //得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
        int hashcode = filename.hashCode();
        int dir1 = hashcode & 0xf;  //0--15
        int dir2 = (hashcode & 0xf0) >> 4;  //0-15
        //构造新的保存目录
        String dir = savePath + System.getProperty("file.separator") + dir1 + System.getProperty("file.separator") + dir2;  //upload\2\3  upload\3\5
        //File既可以代表文件也可以代表目录
        File file = new File(dir);
        //如果目录不存在
        if (!file.exists()) {
            //创建目录
            file.mkdirs();
        }
        return dir;
    }

}
