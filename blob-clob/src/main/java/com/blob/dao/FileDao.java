package com.blob.dao;

import java.util.List;

import com.blob.model.Files;

public interface FileDao {
	
	public  List<Files> getFiles();
	public Files getFile(int id);
	public void addFile(Files file);
	public void deleteFile();

}
