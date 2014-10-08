package denyslapin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;


public class DBEngine {
	private String dbname = "DIARY";
	private Connection conn;
	private String login;
	
	public DBEngine (String user)
	{
	try{
		login=user;
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		conn = DriverManager.getConnection("jdbc:odbc:"+dbname,"","");
	}
	catch(SQLException e){
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	
	}
	public void updateNote(Note n, int id){
		try{
			String query = "Update Запись SET Дата_создания = ?,Название_записи = ?,Контент_записи = ?,Оценка_дня = ? WHERE Код_записи = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(n.date.getTime()));
			ps.setString(2, n.name);
			ps.setString(3, n.content);
			ps.setInt(4, n.rating);
			ps.setInt(5, id);
		    ps.executeUpdate();
		    ps.close();
		    
		    String queryFont = "Update Шрифт Set Цвет_шрифта=?, Размер_шрифта=?, Стиль_шрифта=?, Название_шрифта=? Where Код_записи=?";
		    PreparedStatement stf = conn.prepareStatement(queryFont);
		    stf.setInt(1, n.fontColor.getRGB());
		    stf.setInt(2, n.font.getSize());
		    stf.setInt(3, n.font.getStyle());
		    stf.setString(4, n.font.getFontName());
		    stf.setInt(5, id);
		    stf.executeUpdate();
		    stf.close();
		    
		    String deleteImg = "Delete from Изображения Where Код_записи=?";
		    PreparedStatement delps = conn.prepareStatement(deleteImg);
		    delps.setInt(1, id);
		    delps.executeUpdate();
		    delps.close();
		    
		    if(n.images!=null){
		    	String queryImage="Insert into Изображения(Изображение, Код_записи)" + "values(?, ?)";
		    	for(int i=0; i<n.images.size(); i++){
		    		PreparedStatement stimg = conn.prepareStatement(queryImage);
		    		BufferedImage f = n.images.get(i);
		    		ByteArrayOutputStream arr = new ByteArrayOutputStream();
		    		ImageIO.write(f, "jpg", arr);
		    		arr.flush();
		    		stimg.setBytes(1, arr.toByteArray());
		    		arr.close();
		    		stimg.setInt(2, id);
		    		stimg.executeUpdate();
		    		stimg.close();
		    }
		    }
			}
		    catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}
    public int saveNote(Note n){
    	int rows = 1;
    	try {
    		String query = "insert into Запись(Дата_создания,Название_записи,Контент_записи,Оценка_дня,Логин_пользователя) " +
    				"values(?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(n.date.getTime()));
			ps.setString(2, n.name);
			ps.setString(3, n.content);
			ps.setInt(4, n.rating);
			ps.setString(5, login);
		    rows=ps.executeUpdate();
		    ps.close();
		    
		    Statement st = conn.createStatement();
		    ResultSet rs = st.executeQuery("Select Max(Код_записи) From Запись");
		    int id = 0;
		    
		    while(rs.next()){
		    	id = rs.getInt(1);
		    }
		    
		    String queryFont = "Insert into Шрифт(Код_записи, Цвет_шрифта, Размер_шрифта, Стиль_шрифта, Название_шрифта)"+
		    		"values(?, ?, ?, ?, ?)";
		    PreparedStatement stf = conn.prepareStatement(queryFont);
		    stf.setInt(1, id);
		    stf.setInt(2, n.fontColor.getRGB());
		    stf.setInt(3, n.font.getSize());
		    stf.setInt(4, n.font.getStyle());
		    stf.setString(5, n.font.getFontName());
		    stf.executeUpdate();
		    stf.close();
		    if(n.images!=null){
		    	String queryImage="Insert into Изображения(Изображение, Код_записи)" + "values(?, ?)";
		    	for(int i=0; i<n.images.size(); i++){
		    		PreparedStatement stimg = conn.prepareStatement(queryImage);
		    		BufferedImage f = n.images.get(i);
		    		ByteArrayOutputStream arr = new ByteArrayOutputStream();
		    		ImageIO.write(f, "jpg", arr);
		    		arr.flush();
		    		stimg.setBytes(1, arr.toByteArray());
		    		arr.close();
		    		stimg.setInt(2, id);
		    		stimg.executeUpdate();
		    		stimg.close();
		    }
		    }
		    //stf.setInt(2, n.font.)
		   // ResultSet rs = ps.getGeneratedKeys();
		   // System.out.println(rs);
			//Statement st = conn.createStatement();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rows;
		
	}
    public ResultSet printNotes(){
    	PreparedStatement stmt;
    	ResultSet rs = null;
		try {
			String query = "select * from Запись where Логин_пользователя=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1,login);
			rs = stmt.executeQuery();      
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return rs;    
    }
    public ResultSet printNotes(String name, String date) {
    	PreparedStatement stmt;
    	ResultSet rs = null;
		try {
			String query = "SELECT Запись.*, Шрифт.Название_шрифта, Шрифт.Размер_шрифта, Шрифт.Стиль_шрифта, Шрифт.Цвет_шрифта, Изображения.Изображение, *"
					+ "FROM (Запись LEFT JOIN Изображения ON Запись.Код_записи = Изображения.Код_записи) "
					+ "INNER JOIN Шрифт ON Запись.Код_записи = Шрифт.Код_записи"
			 		+ " WHERE Логин_пользователя=? And Название_записи=? And Дата_создания=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, login);
			stmt.setString(2, name);
			SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
			Date temp = null;
			temp = (Date)f.parse(date);
			stmt.setDate(3, new java.sql.Date(temp.getTime()));
			rs = stmt.executeQuery();      
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
		e.printStackTrace();
		}
		 return rs;    
    }
    public void deleteNote(String name, String date){
    	PreparedStatement st ;
    	try{
    		String query = "Delete from Запись Where Название_записи=? And Дата_создания=? and Логин_пользователя=?";
    		st=conn.prepareStatement(query);
    		st.setString(1, name);
    		SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
			Date temp = null;
			temp = (Date)f.parse(date);
			st.setDate(2, new java.sql.Date(temp.getTime()));
    		st.setString(3, login);
    		st.executeUpdate();
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
    public void deleteNote(int id){
    	PreparedStatement st ;
    	try{
    		String query = "Delete from Запись Where Код_записи=? and Логин_пользователя=?";
    		st=conn.prepareStatement(query);
    		st.setInt(1, id);
    		st.setString(2, login);
    		st.executeUpdate();
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    }
    public ResultSet printNotes(Date start, Date end){
    	PreparedStatement st;
    	ResultSet rs = null;
    	try{
    		String query = "Select * from Запись Where (Дата_создания Between ? And ?) And Логин_пользователя=?";
    		st=conn.prepareStatement(query);
    		st.setDate(1, new java.sql.Date(start.getTime()));
    		st.setDate(2, new java.sql.Date(end.getTime()));
    		st.setString(3, login);
    		rs=st.executeQuery();
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return rs;
    }
    public ResultSet printNotes(String filter){
    	PreparedStatement st;
    	ResultSet rs = null;
    	try{
    		String query = "Select * from Запись Where Название_записи Like ? And Логин_пользователя=?";
    		st=conn.prepareStatement(query);
    		//st.setString(1, login);
    		st.setString(1, "%" + filter + "%");
    		st.setString(2, login);
    		rs=st.executeQuery();
    	}
    	catch (SQLException e) { 
			e.printStackTrace();
		}
    	return rs;
    }
    public ResultSet printNotes(String filter, Date start, Date end){
    	PreparedStatement st;
    	ResultSet rs = null;
    	try{
    		String query = "Select * from Запись Where Название_записи Like ? And Логин_пользователя=? And (Дата_создания Between ? And ?)";
    		st=conn.prepareStatement(query);
    		//st.setString(1, login);
    		st.setString(1, "%" + filter + "%");
    		st.setString(2, login);
    		st.setDate(3, new java.sql.Date(start.getTime()));
    		st.setDate(4, new java.sql.Date(end.getTime()));
    		rs=st.executeQuery();
    	}
    	catch (SQLException e) { 
			e.printStackTrace();
		}
    	return rs;
    }
    protected void finalize(){
    	try {
			conn.close();
		} 
    	catch (SQLException e) {
			e.printStackTrace();
		}
    }
  
    public void addUser(String pass){
    	PreparedStatement ps = null;
    	try{
    		String query = "Insert into Пользователи(Логин_пользователя, Пароль) values(?,?)";
    		ps = conn.prepareStatement(query);
    		ps.setString(1, login);
    		ps.setString(2, pass);
    		ps.executeUpdate();
    	}catch(Exception e){}
    }
    public boolean hasUser(){
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		String query = "Select * from Пользователи where Логин_пользователя=?";
    		ps = conn.prepareStatement(query);
    		ps.setString(1, login);
    		rs = ps.executeQuery();
    	}catch(Exception e){}
    	int count = 0;
    	try {
			while(rs.next()){
				count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count>0;
    }
    public boolean hasUser(String pass){
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		String query = "Select * from Пользователи where Логин_пользователя=? and Пароль=?";
    		ps = conn.prepareStatement(query);
    		ps.setString(1, login);
    		ps.setString(2, pass);
    		rs = ps.executeQuery();
    	}catch(Exception e){}
    	int count = 0;
    	try {
			while(rs.next()){
				count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count>0;
    }

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}