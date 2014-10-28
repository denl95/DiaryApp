package denyslapin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JCheckBox;


public class ListNoteFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTable table;
	private JMenu menu;
	private JPanel panel_1;
	private JButton showNoteButton;
	private JButton nFilterButton;
	private JButton dFilterButton;
	private DateFilter datefilter;
	private NameFilter namefilter;
	private EditingFrame editframe;
	private DBEngine db;
	private JButton deleteButton;
	private DefaultTableModel model;
	private Vector<Integer> id;
	private JCheckBox chckbxFname;
	private JCheckBox chckbxFdate;
	/* 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListNoteFrame frame = new ListNoteFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	/**
	 * Create the frame.
	 * @param db2 
	 */
	public ListNoteFrame(DBEngine db2) {
		db = db2;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				SignIn si = new SignIn();
				dispose();
				si.setVisible(true);
			}
		});
		setSize( 400, 600);
		setLocationRelativeTo(null);
		Dimension d = new Dimension(400, 200);
		this.setMinimumSize(d);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		datefilter = new DateFilter(this, "Фильтр по дате");
		namefilter = new NameFilter(this, "Фильтр по названию");
		menu = new JMenu("Действия");
		/*menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				editframe = new EditingFrame();
				editframe.setDb(db);
				editframe.setVisible(true);
			}
		});*/
		menuBar.add(menu);
		
		JMenuItem createNote = new JMenuItem("Cоздать новую запись");
		createNote.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				editframe = new EditingFrame();
				editframe.setDb(db);
				editframe.setVisible(true);
				
			}
			
		});
		menu.add(createNote);
		
		JMenuItem changeUser = new JMenuItem("Cменить пользователя");
		changeUser.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				SignIn si = new SignIn();
				si.setVisible(true);
				
			}
			
		});
		menu.add(changeUser);
		
		JMenuItem exit = new JMenuItem("Выйти");
		exit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
				
			}
			
		});
		menu.add(exit);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		final String filter[] = new String[1];
		nFilterButton = new JButton("Фильтр по названию");
		nFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				namefilter.setVisible(true);
				 filter[0] = namefilter.getName();
				if(filter[0]!=null){
					chckbxFname.setSelected(true);
				}
			}
			
		});
		
		
		final Date[] dates = new Date[2];
		dFilterButton = new JButton("Фильтр по дате");
		dFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				datefilter.setVisible(true);
				dates[0] = datefilter.getDate1();
				dates[1] = datefilter.getDate2();
				if(dates[0]!=null && dates[1]!=null){
					chckbxFdate.setSelected(true);
				}
				
			}
		});
		chckbxFname = new JCheckBox();
		panel.add(chckbxFname);
		panel.add(nFilterButton);
		chckbxFdate = new JCheckBox();
		panel.add(chckbxFdate);
		panel.add(dFilterButton);
		ActionListener checkFilter = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if((dates[0]!=null && dates[1]!=null)||(filter[0]!=null)){
					id = new Vector<Integer>();
					Vector<Vector<String>> data = new Vector<Vector<String>>();
		
					if(chckbxFname.isSelected()&&!chckbxFdate.isSelected()){
					showNotes(db.printNotes(filter[0]), data, id);
					}
					else if(!chckbxFname.isSelected()&&chckbxFdate.isSelected()){
						showNotes(db.printNotes(dates[0], dates[1]), data, id);
					}
					else if (chckbxFname.isSelected()&&chckbxFdate.isSelected()){
						if(dates[0]!=null && dates[1]!=null && !filter[0].isEmpty())
						showNotes(db.printNotes(filter[0], dates[0], dates[1]), data, id);
					}
					else if(!chckbxFname.isSelected()&&!chckbxFdate.isSelected()){
						showNotes(db.printNotes(), data, id);
					}
				}
			}
		};
		chckbxFname.addActionListener(checkFilter);
		chckbxFdate.addActionListener(checkFilter);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		
		final Vector<Vector<String>> data = new Vector<Vector<String>>();
		id = new Vector<Integer>();
		showNotes(db.printNotes(), data, id);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		table.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getClickCount()==2){
					int t = table.getSelectedRow();
					if(t!=-1){
						Vector<String> temp = data.elementAt(t);
						String name = temp.elementAt(0);
						String date = temp.elementAt(1);
						ResultSet rs = db.printNotes(name, date);
						Note n = null;
						int id = 0;
						try {
							
							if(rs.next()){
								
							id=rs.getInt(1);
							Date d = rs.getDate("Дата_создания");
							String name1 = rs.getString("Название_записи");
							String content = rs.getString("Контент_записи");
							int rate = rs.getInt("Оценка_дня");
							String nameFont = rs.getString(7);
							int styleFont = rs.getInt(8);
							int sizeFont = rs.getInt(9);
							int colorFont = rs.getInt(10);
							Font font = new Font(nameFont, sizeFont, styleFont);
							Color c = new Color(colorFont);
							ArrayList<BufferedImage> arr = null;
							byte[] imageInByte = rs.getBytes(11);
							
							if(imageInByte!=null){
								
								arr = new ArrayList<BufferedImage>();
								InputStream in = new ByteArrayInputStream(imageInByte);
								BufferedImage bImageFromConvert = ImageIO.read(in);
								arr.add(bImageFromConvert);
								while(rs.next()){
									
									imageInByte=rs.getBytes(11);
									in = new ByteArrayInputStream(imageInByte);
									bImageFromConvert = ImageIO.read(in);
									arr.add(bImageFromConvert);
								
								}
								
							}
							n = new Note(name1, content, arr, font, rate, d, c);
							
							}
						} catch (SQLException | IOException e1) {
							e1.printStackTrace();
						}
						
					
						editframe = new EditingFrame(n, id);
						editframe.setDb(db);
						dispose();
						editframe.setVisible(true);
					}
				}
			}
		});
		
		panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(panel_1, BorderLayout.SOUTH);
		showNoteButton = new JButton("Просмотреть запись");
		panel_1.add(showNoteButton);
		showNoteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int t = table.getSelectedRow();
				if(t!=-1){
					Vector<String> temp = data.elementAt(t);
					String name = temp.elementAt(0);
					String date = temp.elementAt(1);
					ResultSet rs = db.printNotes(name, date);
					Note n = null;
					int id=0;
					try {
						
						if(rs.next()){
							
						id=rs.getInt(1);
						Date d = rs.getDate("Дата_создания");
						String name1 = rs.getString("Название_записи");
						String content = rs.getString("Контент_записи");
						int rate = rs.getInt("Оценка_дня");
						String nameFont = rs.getString(7);
						int styleFont = rs.getInt(8);
						int sizeFont = rs.getInt(9);
						int colorFont = rs.getInt(10);
						Font font = new Font(nameFont, sizeFont, styleFont);
						Color c = new Color(colorFont);
						ArrayList<BufferedImage> arr = null;
						byte[] imageInByte = rs.getBytes(11);
						
						if(imageInByte!=null){
							
							arr = new ArrayList<BufferedImage>();
							InputStream in = new ByteArrayInputStream(imageInByte);
							BufferedImage bImageFromConvert = ImageIO.read(in);
							arr.add(bImageFromConvert);
							while(rs.next()){
								
								imageInByte=rs.getBytes(11);
								in = new ByteArrayInputStream(imageInByte);
								bImageFromConvert = ImageIO.read(in);
								arr.add(bImageFromConvert);
							
							}
							
						}
						n = new Note(name1, content, arr, font, rate, d, c);
						
						}
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
					editframe = new EditingFrame(n, id);
					editframe.setDb(db);
					dispose();
					editframe.setVisible(true);
				}
			}
		});
		
		deleteButton = new JButton("Удалить запись");
		panel_1.add(deleteButton);
		deleteButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int t = table.getSelectedRow();
				if(t!=-1){
					int idtemp = id.elementAt(t);
					id.remove(t);
					db.deleteNote(idtemp);
					model.removeRow(t);
				}
			}
		});
	}
	public DBEngine getDb() {
		return db;
	}
	public void setDb(DBEngine db) {
		this.db = db;
	}
	public void showNotes(ResultSet result,Vector<Vector<String>> data, Vector<Integer> id){
		
		Vector<String> colsnames = new Vector<String>();
		colsnames.add("Название");
		colsnames.add("Дата");
		try{
		while (result.next()) {
			 Vector<String> row = new Vector<String>();
       	     String name = result.getString("Название_записи");
	         java.sql.Date date = result.getDate("Дата_создания");
	         row.add(name);
	         SimpleDateFormat f= new SimpleDateFormat("dd.MM.yyyy");
	         row.add(f.format(date));
	         data.add(row);
	         int temp = result.getInt("Код_записи");
	         id.add(temp);
	      }
		
		}
		catch(SQLException e1){e1.printStackTrace();};
		model = new DefaultTableModel(data, colsnames){
			@Override
			    public boolean isCellEditable(int row, int column) {
			       return false;
			    }
		};
		table.setModel(model);
	}
	
}