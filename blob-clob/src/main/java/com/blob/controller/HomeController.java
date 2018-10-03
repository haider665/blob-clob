package com.blob.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.ModelAndView;

import com.blob.dao.FileDaoImplement;
import com.blob.model.FileUpload;
import com.blob.model.Files;
import com.blob.model.Test;
import org.springframework.core.io.ByteArrayResource;
@Controller
public class HomeController {

	private static final MediaType IMAGE_JPEG = null;
	private static final MediaType TEXT_PLAIN = null;
	private static final MediaType Application_PDF = null;

	@RequestMapping("/")
	public String home(Model model) {

		FileDaoImplement fdi = new FileDaoImplement();
		List<Files> allFiles = fdi.getFiles();
		for (Files f : allFiles) {
			System.out.println(f);
		}
		String s="123 sfdjk .jpg";
		String arr[]=s.split(Pattern.quote("."));
		for(int i=0;i<arr.length;i++) {
			System.out.println(arr[i]);
		}
		model.addAttribute("allFiles", allFiles);
		return "home";
	}

	@RequestMapping(value = "savefile", method = RequestMethod.POST)
	public ModelAndView saveimage(@RequestParam CommonsMultipartFile[] file, HttpSession session) throws Exception {
		for (CommonsMultipartFile files : file) {
			ServletContext context = session.getServletContext();
			String path = context.getRealPath("/upload/");
			String filename = files.getOriginalFilename();

			System.out.println(path + " " + filename);

			byte[] bytes = files.getBytes();
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(path, files.getOriginalFilename())));
			stream.write(bytes);
			stream.flush();
			stream.close();
		}
		return new ModelAndView("result");
	}

	@RequestMapping("/test")
	public @ResponseBody HttpEntity<byte[]> downloadB(@RequestParam(value = "file") CommonsMultipartFile file,
			@RequestParam(value = "fileName") String fileName) throws IOException {
		// File file = getFile();
//	    byte[] document = FileCopyUtils.copyToByteArray(file);
		byte[] document = file.getBytes();
		System.out.println(fileName + "   ----  " + file.getOriginalFilename());
		Files newFile = new Files();
		newFile.setName(file.getOriginalFilename());
		newFile.setPhoto(document);
		FileDaoImplement fdi = new FileDaoImplement();
		fdi.addFile(newFile);
//	    Files f=fdi.getFile(3);
//	    byte [] document=f.getPhoto();

		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("image", "jpeg"));
		header.set("Content-Disposition", "inline; filename=" + file.getName());
		header.setContentLength(document.length);
		return new HttpEntity<byte[]>(document, header);
	}

	@RequestMapping(value="/view" , produces="application/vnd.ms-excel")
	public @ResponseBody HttpEntity<byte[]> view(@RequestParam(value = "fileId") int id) throws IOException {
		
		MediaType m=null;
		System.out.println("--------------------------------");
		System.out.println(id);
		FileDaoImplement fdi = new FileDaoImplement();
		Files f = fdi.getFile(id);
		String arr[]= f.getName().split(Pattern.quote(".") );
		
		if(arr[arr.length-1].equals("jpg") || arr[arr.length-1].equals("png") ) {
			m=MediaType.IMAGE_JPEG;
			System.out.println("jpg    png");
		}
		if(arr[arr.length-1].equals("txt") || arr[arr.length-1].equals("docx") ) {
//			m=MediaType.TEXT_PLAIN;
			m=new MediaType("application","vnd.openxmlformats-officedocument.wordprocessingml.document");
			System.out.println("txt    docx");
		}
		if(arr[arr.length-1].equals("pdf") ) {
			m=MediaType.APPLICATION_PDF;
			System.out.println("     pdf  ");
		}
		byte[] document = f.getPhoto();
				// 
		HttpHeaders header = new HttpHeaders();
		//header.setContentType(m);
		header.set("Content-Disposition", "inline; filename=");
		header.setContentLength(document.length);
		return new HttpEntity<byte[]>(document, header);
	}

	@RequestMapping("/download")
	public ResponseEntity<ByteArrayResource> downloadFile2(@RequestParam(value="fileId") int id) throws IOException {
		
		MediaType m=null;
		
		FileDaoImplement fdi = new FileDaoImplement();
		Files file = fdi.getFile(id);
		System.out.println(file.getName());
		String arr[]= file.getName().split(Pattern.quote(".") );
//		for(int i=0; i<arr.length;i++) {
//			System.out.println(arr[i]);
//		}
		if(arr[arr.length-1].equals("jpg") || arr[arr.length-1].equals("png") ) {
			m=IMAGE_JPEG;
			System.out.println("jpg    png");
		}
		if(arr[arr.length-1].equals("txt") || arr[arr.length-1].equals("docx") ) {
			m=TEXT_PLAIN;
			System.out.println("txt    docx");
		}
		if(arr[arr.length-1].equals("pdf") ) {
			m=Application_PDF;
			System.out.println("     pdf  ");
		}
		
		byte[] data = file.getPhoto();
		ByteArrayResource resource = new ByteArrayResource(data);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName().toString())
				.contentType(m).contentLength(data.length).body(resource);
	}

//	@RequestMapping("/download")
//	public @ResponseBody Resource downloadC(HttpSession session,HttpServletResponse response, @RequestParam(value="fileId") int id) throws FileNotFoundException {
//	    FileDaoImplement fdi = new FileDaoImplement();
//		Files file = fdi.getFile(id);
//		ServletContext context = session.getServletContext();
//		String path = context.getRealPath("/upload/");
//		
//		//InputStream in = new FileInputStream(file);
//	   // response.setContentType(APPLICATION_PDF);
//	   // response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
//	   // response.setHeader("Content-Length", String.valueOf(file.length()));
//	    return new FileSystemResource();
//	}
	
	

}
