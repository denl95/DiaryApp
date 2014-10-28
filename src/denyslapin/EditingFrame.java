package denyslapin;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXDatePicker;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class EditingFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JMenuBar menuBar;
	private JTextArea textArea;
	private JSlider slider;
	private ListNoteFrame listframe;
	private DBEngine db;
	private JComboBox colors;
	private JComboBox name;
	private JSpinner size;
	private JComboBox style;
	private JXDatePicker dp;
	private JPanel imgpanel;
	private JScrollPane scrollimg;
	private boolean isChangedText = false;
	private boolean isUpdate = false;
	private int id;
	public EditingFrame() {
		
		final EditingFrame main = this;
		setSize(640, 467);
		setLocationRelativeTo(null);
		//setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				boolean isExit = false;
				onClose(isExit);
			}
		});
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenu actions = new JMenu("Действия");
		menuBar.add(actions);
		
		JMenuItem saveMenu = new JMenuItem("Сохранить");
		saveMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String sField = textField.getText();
				String sArea = textArea.getText();
				if((sField.isEmpty())||(sArea.isEmpty())||(dp.getDate()==null)){
					JOptionPane.showMessageDialog(main, "Заполните необходимые поля", "Cообщение", JOptionPane.ERROR_MESSAGE);
				}
				else{
					saveNote();
					JOptionPane.showMessageDialog(main, "Cохранение успешно завершено", "Cообщение", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		actions.add(saveMenu);
		
		
		JMenuItem setImage = new JMenuItem("Прикрепить картинку");
		actions.add(setImage);
		setImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG, BMP and PNG images", "jpg","bmp", "png");
				chooser.setFileFilter(filter);
				int value = chooser.showOpenDialog(main);
				if(value==JFileChooser.APPROVE_OPTION){
					File f= chooser.getSelectedFile();
					if(imgpanel==null){
						imgpanel = new JPanel();
						imgpanel.setLayout(new WrapLayout());
						scrollimg = new JScrollPane(imgpanel);
						scrollimg.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						scrollimg.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						main.getContentPane().add(scrollimg, BorderLayout.SOUTH);
						//scrollimg.setLayout(new WrapLayout());
						//scrollimg.setVerticalScrollBarPolicy(scrollimg.);
						imgpanel.setSize(new Dimension(600,150));
					}
					ImagePanel img = new ImagePanel(f);
					imgpanel.add(img);
					img.revalidate();
					//imgpanel.revalidate();
				}
			}
		});
		JMenuItem showList = new JMenuItem("Просмотреть все записи");
		showList.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isExit = false;
				onClose(isExit);
			}
			
		});
		actions.add(showList);
		
		JMenuItem exit = new JMenuItem("Выйти");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				boolean isExit = true;
				onClose(isExit);
			}
		});
		actions.add(exit);
		
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		JPanel mainpanel = new JPanel();
		mainpanel.setBorder(null);
		
		this.getContentPane().add(mainpanel, BorderLayout.CENTER);
		mainpanel.setLayout(null);
		JPanel textfields = new JPanel();
		textfields.setBounds(5, 0, 464, 385);
		//textfields.setBorder(new LineBorder(new Color(0, 0, 0)));
		mainpanel.add(textfields);
		textfields.setLayout(null);

		textField = new JTextField();
		KeyAdapter text = new KeyAdapter(){
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				setChangedText(true);
			}
		};
		textField.addKeyListener(text);
		textField.setBounds(0, 0, 464, 20);
		textField.setBorder(new LineBorder(new Color(0, 0, 0)));
		textfields.add(textField);
		textField.setColumns(10);
		//textField.setFont(new Font("TimesRoman", Font.BOLD,20));
		
		textArea = new JTextArea();
		textArea.addKeyListener(text);
		textArea.setBounds(0, 22, 464, 363);
		textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		textfields.add(textArea);
		JPanel calendar = new JPanel();
		calendar.setBounds(467, 0, 156, 385);
		calendar.setAlignmentX(Component.RIGHT_ALIGNMENT);
		calendar.setBorder(null);
		mainpanel.add(calendar);
		calendar.setLayout(null);
		
		JLabel label = new JLabel("Оценка дня");
		label.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label.setBounds(20, 43, 119, 32);
		calendar.add(label);
		
		slider = new JSlider();
		slider.setBackground(Color.MAGENTA);
		slider.setValue(5);
		slider.setMaximum(5);
		slider.setMinimum(1);
		slider.setBounds(10, 86, 143, 24);
		slider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
			 int value = slider.getValue();
			 switch (value){
			 	case 1:
			 		slider.setBackground(Color.BLUE);
			 		break;
			 	case 2:
			 		slider.setBackground(Color.CYAN);
			 		break;
			 	case 3:
			 		slider.setBackground(Color.RED);
			 		break;
			 	case 4:
			 		slider.setBackground(Color.ORANGE);
			 		break;
			 	case 5:
			 		slider.setBackground(Color.MAGENTA);
			 		break;
			 	default:
			 		
			 }
				
			}
			
		});
		
		dp = new JXDatePicker();
		dp.setBounds(10,0,146,32);
		dp.setDate(new Date());
		calendar.add(dp);
		calendar.add(slider);
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		this.getContentPane().add(panel, BorderLayout.NORTH);
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		name = new JComboBox(fonts);
		panel.add(name);
		
		size = new JSpinner();
		size.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				textField.setFont(new Font((String)name.getSelectedItem(),Font.PLAIN,(int)size.getValue()));
				textArea.setFont(new Font((String)name.getSelectedItem(),Font.PLAIN,(int)size.getValue()));
			}
		});
		name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField.setFont(new Font((String)name.getSelectedItem(),Font.PLAIN,(int)size.getValue()));
				textArea.setFont(new Font((String)name.getSelectedItem(),Font.PLAIN,(int)size.getValue()));
			}
		});
		size.setModel(new SpinnerNumberModel(12, 8, 20, 1));
		panel.add(size);
		String items[] = {"Черный","Красный", "Зеленый"};
		colors = new JComboBox(items);
		colors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = (String)colors.getSelectedItem();
				switch (item)
				{
				case "Черный":
					textField.setForeground(Color.BLACK);
					textArea.setForeground(Color.BLACK);
					break;
				case "Красный":
					textField.setForeground(Color.RED);
					textArea.setForeground(Color.RED);
					break;
				case "Зеленый":
					textField.setForeground(Color.GREEN);
					textArea.setForeground(Color.GREEN);
					break;
					default:
				}
			}
		});
		panel.add(colors);
		String styles[] = {"Обычный","Жирный","Курсив","Жирный с курсивом"};
		
		style = new JComboBox(styles);
		style.setFont(new Font("Tahoma", Font.BOLD, 12));
		style.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch ((String)style.getSelectedItem()){
				case "Обычный":
					textField.setFont(new Font((String)name.getSelectedItem(),Font.PLAIN,(int)size.getValue()));
					textArea.setFont(new Font((String)name.getSelectedItem(),Font.PLAIN,(int)size.getValue()));
					break;
				case "Жирный":
					textField.setFont(new Font((String)name.getSelectedItem(),Font.BOLD,(int)size.getValue()));
					textArea.setFont(new Font((String)name.getSelectedItem(),Font.BOLD,(int)size.getValue()));
					break;
				case "Курсив":
					textField.setFont(new Font((String)name.getSelectedItem(),Font.ITALIC,(int)size.getValue()));
					textArea.setFont(new Font((String)name.getSelectedItem(),Font.ITALIC,(int)size.getValue()));
					break;
				case "Жирный с курсивом":
					textField.setFont(new Font((String)name.getSelectedItem(), Font.BOLD | Font.ITALIC,(int)size.getValue()));
					textArea.setFont(new Font((String)name.getSelectedItem(), Font.BOLD | Font.ITALIC,(int)size.getValue()));
					break;
				default:
				}
			}
		});
		panel.add(style);
		//imgpanel = new ImagePanel();
		//this.getContentPane().add(imgpanel,BorderLayout.SOUTH);
		//imgpanel.setPreferredSize(new Dimension(100,100));
		//imgpanel.revalidate();
	}
	public EditingFrame(Note n, int id){
		
		this();
		setId(id);
		isUpdate = true;
		textField.setText(n.name);
		textArea.setText(n.content);
		slider.setValue(n.rating);
		if(n.font!=null){
			
			textField.setFont(n.font);
			textArea.setFont(n.font);
			textField.setForeground(n.fontColor);
			textArea.setForeground(n.fontColor);
			
		}
		dp.setDate(n.date);
		if(n.images!=null){
			
			imgpanel = new JPanel();
			imgpanel.setLayout(new WrapLayout());
			scrollimg = new JScrollPane(imgpanel);
			scrollimg.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollimg.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			getContentPane().add(scrollimg, BorderLayout.SOUTH);
			for(int i=0; i < n.images.size(); i++){
				
				ImagePanel img = new ImagePanel(n.images.get(i));
				imgpanel.add(img);
				img.revalidate();
				
			}
			imgpanel.setSize(new Dimension(600,150));
		}
	}
	public boolean getisChangedText() {
		return isChangedText;
	}
	public void setChangedText(boolean isChangedText) {
		this.isChangedText = isChangedText;
	}
	public void saveNote(){
		ArrayList<BufferedImage> t=null;
		if(imgpanel!=null){
			t = new ArrayList<BufferedImage>();
			for(int i=0; i < imgpanel.getComponentCount(); i++){
				ImagePanel c = (ImagePanel)imgpanel.getComponent(i);
				if(c.isVisible()){
					t.add(c.getImage());
				}
			}
		}
		Note n = new Note(textField.getText(),textArea.getText(), t, textField.getFont(),
				slider.getValue(), dp.getDate(), textField.getForeground());
		
		if(isUpdate){
			db.updateNote(n, id);
		}
		else {db.saveNote(n);}
			setChangedText(false);
	}
	public void onClose(boolean isExit){
		EditingFrame main = this;
		if(isChangedText){
			int value = JOptionPane.showConfirmDialog(main, "Cохранить изменения?");
			if(value==JOptionPane.YES_OPTION){
				String sField = textField.getText();
				String sArea = textArea.getText();
				if((sField.isEmpty())||(sArea.isEmpty())||(dp.getDate()==null)){
					JOptionPane.showMessageDialog(main, "Заполните необходимые поля", "Cообщение", JOptionPane.ERROR_MESSAGE);
				}
				else{
					saveNote();
					if(!isExit){
						dispose();
						listframe = new ListNoteFrame(db);
						listframe.setVisible(true);
					}
					else{System.exit(0);}
				}
			}
			else if(value==JOptionPane.NO_OPTION){
				if(!isExit){
					dispose();
					listframe = new ListNoteFrame(db);
					listframe.setVisible(true);
					}
					else{System.exit(0);}
			}
		}
		else{
			if(!isExit){
				dispose();
				listframe = new ListNoteFrame(db);
				listframe.setVisible(true);
				}
				else{System.exit(0);}
		}
	}
	public DBEngine getDb() {
		return db;
	}
	public void setDb(DBEngine db) {
		this.db = db;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}

