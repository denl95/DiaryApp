package denyslapin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.ArrayList;


public class Note {
	public String name;
	public String content;
	public ArrayList<BufferedImage> images;
	public Font font;
	public int rating;
	public Color fontColor;
	public Date date;
	public Note(String name,String content,ArrayList<BufferedImage> images,Font font,int rating,Date date,Color c){
		this.name = name;
		this.content = content;
		this.images = images;
		this.font = font;
		this.rating = rating;
		this.date = date;
		this.fontColor = c;
	}
	public void print(){
		System.out.println(name+content+rating);
	}
}
