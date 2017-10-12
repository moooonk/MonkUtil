package com.monk.util.http.updownload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UploadPhoto
 */
@MultipartConfig(location="e:\\")
@WebServlet("/UploadPhoto")
public class UploadPhoto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String fileNameEx="filename=\".+\"";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadPhoto() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String userId = request.getParameter("userId");
        String extra = request.getParameter("extra");
		try (PrintWriter out = response.getWriter()) {
			for(Part p : request.getParts()){
	            String fname = getFileName(p);
	            if(fname == null){
	                continue;
	            }
	            File file = new File(fname);
	            String path = System.getProperty("uploadPath");
	            if(path != null){
	            	writeTo(file.getName(), p, path);
	            }else{
	            	p.write(file.getName());
	            }
	            out.write("上传成功："+ file.getName() + userId + extra);
			}
			out.close();
		}catch (Exception e) {
            e.printStackTrace();
        }finally{
        	
        }
	}
	
	private String getFileName(Part part){
	    String cotentDesc = part.getHeader("content-disposition");
	    String fileName = null;
	    Pattern pattern = Pattern.compile(fileNameEx);
	    Matcher matcher = pattern.matcher(cotentDesc);
	    if(matcher.find()){
	        fileName = matcher.group();
	        fileName = fileName.substring(10, fileName.length() - 1);
	    }
	    return fileName;
	}

	private void writeTo(String fileName, Part part, String path) throws IOException, FileNotFoundException {
        InputStream in = part.getInputStream();
        OutputStream out = new FileOutputStream(path + fileName);
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.close();
    }
	
}
