package com.monk.util.http.updownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class DownloadPhotoServlet
 */
@WebServlet("/DownloadPhotoServlet")
public class DownloadPhotoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadPhotoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		ServletOutputStream outFile = response.getOutputStream();
		FileInputStream inputFile = null;
		try {
			String filename = request.getParameter("filename");
			if(filename == null){
				outFile.print("params error");
				return;
			}
			
			/**
			 * TODO 自己传文件路径进来
			 */
			String path = "";
			
			File file = new File(path + filename);
			if(file == null || !file.exists()){
				outFile.print("file not exists");
				return;
			}
			byte[] buffer = new byte[1024];
			response.setHeader("Content-disposition",
					"attachment;filename=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"));
			response.setContentLength((int) file.length());
			inputFile = new FileInputStream(file);
			int readBytes = -1;
			while ((readBytes = inputFile.read(buffer, 0, 1024)) != -1) {
				outFile.write(buffer, 0, readBytes);
				outFile.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outFile != null) {
				outFile.close();
			}
			if (inputFile != null) {
				inputFile.close();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
