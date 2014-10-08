package denyslapin;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.JLabel;


public class DateFilter extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JXDatePicker dp1;
	private Date date1;
	private JXDatePicker dp2;
	private Date date2;
	
	
	
	public DateFilter(JFrame frame, String str) {
		super(frame,str);
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		setLocationRelativeTo(frame);
		setSize(360, 108);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		dp1 = new JXDatePicker();
		dp2 = new JXDatePicker();
		{
			JLabel label = new JLabel("\u041D\u0435 \u0440\u0430\u043D\u0435\u0435");
			contentPanel.add(label);
		}
		contentPanel.add(dp1);
		{
			JLabel label = new JLabel("\u041D\u0435 \u043F\u043E\u0437\u0434\u043D\u0435\u0435");
			contentPanel.add(label);
		}
		contentPanel.add(dp2);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(dp1.getDate()!=null && dp2.getDate()!=null){
							date1 = dp1.getDate();
							date2 = dp2.getDate();
							if(date1.compareTo(date2)<=0){
								dispose();
							}
							else{JOptionPane.showMessageDialog(null, "Вторая дата должна быть после первой");}
						}
					}
					
				});
			}
			{
				JButton cancelButton = new JButton("Отмена");
				cancelButton.setActionCommand("Отмена");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
					
				});
			}
		}
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}
	
}
