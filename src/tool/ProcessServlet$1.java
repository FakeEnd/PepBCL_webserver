package tool;

import java.io.PrintStream;
import org.apache.tomcat.util.http.fileupload.ProgressListener;

class ProcessServlet$1
  implements ProgressListener
{
  ProcessServlet$1(ProcessServlet paramProcessServlet) {}
  
  public void update(long pBytesRead, long pContentLength, int arg2)
  {
    System.out.println("文件大小为：" + pContentLength + ",当前已处理" + pBytesRead);
  }
}
