package com.blob.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.blob.model.Files;

public class FileDaoImplement implements FileDao {
	private SessionFactory factory;
	public List<Files> getFiles(){
		
		openFactory();
		
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		Query<Files> getFiles = session.createQuery("from Files", Files.class);		
		List<Files> allFiles= getFiles.getResultList();
		session.getTransaction().commit();
		
		closeFactory();
		
		return allFiles;
	}
	public void addFile(Files file) {
		openFactory();
		
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		session.save(file);
		session.getTransaction().commit();
		
		closeFactory();
		
	}
	public Files getFile(int id) {
		openFactory();
		
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		Files file=session.get(Files.class, id);
		
		closeFactory();
		return file;
	}
	public void deleteFile() {
		
		
		
	}
	
	public void openFactory() {
		factory = (SessionFactory) new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Files.class).buildSessionFactory();
	}
	public void closeFactory() {
		factory.close();
	}
	
	
	
}
