package itma_datatable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.input.ReversedLinesFileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



@WebServlet("/DatatableControlServlet")
public class DatatableControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private String filename = "/home/sets/nafeesa/messagesOriginal";
	
    public DatatableControlServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		int start = 0;
		int end = 10;
		int total = 0, display_total = 0;
	
		String sStart = request.getParameter("iDisplayStart");
		String sEnd = request.getParameter("iDisplayLength");
		
		String sSearch = request.getParameter("sSearch");
		
		
		if (sStart != null) 
		{
			start = Integer.parseInt(sStart);
			
			if (start < 0) 
			{
				start = 0;
			}
			
		}
			
		if (sEnd != null) 
		{
			end = Integer.parseInt(sEnd);
			
			if (end < 10 || end > 100) 
			{
				end = 10;
			}
		}
			
		
		
		List<ServiceLogObj> serivelogList = new ArrayList<ServiceLogObj>();
		
		//FILE TOTAL LINE COUNT
		total = GetTotalRecordCount();
		
		if( sSearch != "" )
		{
			display_total = GetTotalSearchRecordCount(sSearch);
		}
		else
		{
			display_total = total;
		}
		

		if( sSearch != "" )
		{
			start = display_total - start;
			
			if(start < end )
			{
				end = 1;
			}
			else
			{
				end = start - ( end - 1 );
			}
			
			if( start == 0 )
			{
				start = 1;
				
				serivelogList = GetSearchRecords_SED(start,end,sSearch);

			}
			else
			{
				serivelogList = GetSearchRecords_SED(end,start,sSearch);

			}
			
		}
		else 
		{
			if( start == 0 )
			{
				serivelogList = GetFirstRecords(start,end);
			}
			else
			{
				start = total - start;
				
				if(start < end )
				{
					end = 1;
				}
				else
				{
					end = start - ( end - 1 );
				}
				
				//serivelogList = GetNextRecords(start,end); //BAD PERFORMANCE
				serivelogList = GetNextRecords_SED(start,end); //GOOD PERFORMANCE
				
			}
		
		
		}
	
		ServiceLogJsonObject servicelogJsonObject = new ServiceLogJsonObject();
		servicelogJsonObject.setiTotalDisplayRecords(display_total);
		servicelogJsonObject.setiTotalRecords(total);
		servicelogJsonObject.setsEcho(request.getParameter("sEcho"));
	
		
		servicelogJsonObject.setAaData(serivelogList);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json2 = gson.toJson(servicelogJsonObject);
		response.setHeader("Cache-Control", "no-store");

		out.print(json2);
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	//================================================================================================================//
	
	public int GetTotalRecordCount()
	{
		int total = -1;
		
		try
		{
			
			Path path = Paths.get(filename);
			long linecount = Files.lines(path).count();
			total = (int)linecount;
			
		}
		catch(Exception e)
		{
			
		}
		
		return total;
		
	}
	
	public int GetTotalSearchRecordCount(String search_str)
	{
		
		int total = -1;
		
		List<String> linecount =  new ArrayList<String>();
		
		try
		{
			
			linecount = RunScript("/home/itma/script/Itma_config.sh service_log_search_count \"grep -i "+search_str+" "+filename+" | wc -l\"");

			total = Integer.parseInt(linecount.get(0));
			
		}
		catch(Exception e)
		{
			
		}
		
		return total;
		
	}
	
	public List<ServiceLogObj> GetFirstRecords(int start,int end)
	{
		List<ServiceLogObj> obj = new ArrayList<ServiceLogObj>();
		
		String line;
		
		try
		{
			
			ReversedLinesFileReader r_reader = new ReversedLinesFileReader(new File(filename));
			
			for( int i = start; i < end; i++)
			{
				if( (line = r_reader.readLine()) != null )
				{
					ServiceLogObj obj2 = new ServiceLogObj();
					obj2.setMessages("\""+line+"\"");
					obj.add(obj2);
				}
			}
			
		}
		catch(Exception e)
		{
			
		}
		
		return obj;
		
	}
	
	public List<ServiceLogObj> GetNextRecords(int start,int end)
	{
		List<ServiceLogObj> obj = new ArrayList<ServiceLogObj>();
		
		String line;
		
		try
		{
			for(int i = end; i < start; i++)
			{
				try(Stream<String> lines = Files.lines(Paths.get(filename))){
					
					line = lines.skip(i).findFirst().get();
					
					ServiceLogObj obj2 = new ServiceLogObj();
					obj2.setMessages("\""+line+"\"");
					obj.add(obj2);
				}
			}
			
			Collections.reverse(obj);
			
		}
		catch(Exception e)
		{
			
		}
		
		return obj;
	}
	
	public List<ServiceLogObj> GetNextRecords_SED(int start,int end)
	{
		List<ServiceLogObj> obj = new ArrayList<ServiceLogObj>();
		
		List<String> line = new ArrayList<String>();
		
		try
		{
			
			/*for(int i = start ; i > end; i--)
			{*/

				
				line = RunScript("/home/itma/script/Itma_config.sh service_logfile \"sed -n "+end+","+start+"p "+filename+"\"");

				for(String l:line)
				{
					ServiceLogObj obj2 = new ServiceLogObj();
					obj2.setMessages("\""+l+"\"");
					obj.add(obj2);
				}
				
			//}
		
			Collections.reverse(obj);
		}
		catch(Exception e)
		{
			
		}
		
		return obj;
	}
	
	public List<ServiceLogObj> GetSearchRecords_SED(int start,int end,String search_str)
	{
		List<ServiceLogObj> obj = new ArrayList<ServiceLogObj>();
		
		List<String> line = new ArrayList<String>();
		
		try
		{
			
			/*for(int i = start ; i > end; i--)
			{*/

				
				line = RunScript("/home/itma/script/Itma_config.sh service_log_search \"grep -i "+search_str+" "+filename+" | sed -n "+start+","+end+"p\"");

				for(String l:line)
				{
					ServiceLogObj obj2 = new ServiceLogObj();
					obj2.setMessages("\""+l+"\"");
					obj.add(obj2);
				}
				
			//}
		
			Collections.reverse(obj);
		}
		catch(Exception e)
		{
			
		}
		
		return obj;
	}

	
	public static List<String> RunScript(String paramval)
	{
		List<String> lines = new ArrayList<String>();
		String str = null;
		
		try
		{
			Runtime rt=Runtime.getRuntime();
			
			String[] cmd= {"/usr/bin/bash","-c",""+paramval};
			
			System.out.println("SCRIPT COMMAND : "+ paramval);
			
			Process pro=rt.exec(cmd);
			
			BufferedReader br=new BufferedReader(new InputStreamReader(pro.getInputStream()));
			
			while( (str=br.readLine()) != null )
			{
				lines.add(str);
			}
			
			
			if(str==null)
			{
				BufferedReader brfErr=new BufferedReader(new InputStreamReader(pro.getErrorStream()));
				str=brfErr.readLine();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return lines;
	}
	
	
}
