package tool;

import java.io.PrintStream;
import org.apache.tomcat.util.http.fileupload.ProgressListener;

class ProcessServlet$1
  implements ProgressListener
{
  ProcessServlet$1(ProcessServlet paramProcessServlet) {}
  
  public void update(long pBytesRead, long pContentLength, int arg2)
  {
    System.out.println("�ļ���СΪ��" + pContentLength + ",��ǰ�Ѵ���" + pBytesRead);
  }
}
