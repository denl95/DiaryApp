package denyslapin;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


public class ImagePanel extends JPanel implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File fileImage=null;
	private BufferedImage img;
	private int width;
	private int height;
	private JPopupMenu pm;
	ImagePanel(File f){
		try {
			pm =new JPopupMenu();
			JMenuItem deleteImage = new JMenuItem("Удалить");
			deleteImage.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							setVisible(false);
						}	
			});
			pm.add(deleteImage);
			addMouseListener(this);
			setFileImage(f);
			img=ImageIO.read(f);
			width=150;
			height=150;
			setPreferredSize(new Dimension(width,height));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			addMouseListener(this);
		
		//this.setLayout(new GridLayout());
		//Rectangle r = new Rectangle(0,0,500,500);
		//this.setBounds(r);
	}
	ImagePanel(BufferedImage b){
		pm =new JPopupMenu();
		JMenuItem deleteImage = new JMenuItem("Удалить");
		deleteImage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						setVisible(false);
					}	
		});
		pm.add(deleteImage);
		img= b;
		width=150;
		height=150;
		setPreferredSize(new Dimension(width,height));
		addMouseListener(this);
	}
	public BufferedImage getImage(){
		return img;
	}
	protected void paintComponent(Graphics gc){
		super.paintComponent(gc);
		gc.drawImage(img, 0, 0, 150, 150, null);
	}
	public File getFileImage() {
		return fileImage;
	}
	public void setFileImage(File fileImage) {
		this.fileImage = fileImage;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3){
			pm.show(e.getComponent(),e.getX(),e.getY());
		}
		
	}
	
}
