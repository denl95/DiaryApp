package denyslapin;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SignUp extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passField;
	private JPasswordField passwordField;

	
	public SignUp() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				SignIn si = new SignIn();
				dispose();
				si.setVisible(true);
			}
		});
		setSize( 246, 230);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Регистрация");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		textField = new JTextField();
		textField.setBounds(41, 53, 156, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblLogin = new JLabel("Логин");
		lblLogin.setBounds(41, 40, 46, 14);
		contentPane.add(lblLogin);
		
		JLabel lblPassword = new JLabel("Пароль");
		lblPassword.setBounds(41, 82, 46, 14);
		contentPane.add(lblPassword);
		
		passField = new JPasswordField();
		passField.setBounds(41, 96, 156, 20);
		contentPane.add(passField);
		passField.setColumns(10);
		
		JLabel label = new JLabel("Повторите пароль");
		label.setBounds(41, 125, 156, 14);
		contentPane.add(label);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(41, 139, 156, 20);
		contentPane.add(passwordField);
		
		JButton signUpButton = new JButton("Зарегистрироваться");
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String login = textField.getText();
				String pass1 = new String(passField.getPassword());
				String pass2 = new String (passwordField.getPassword());
				DBEngine db = new DBEngine(login);
				if(!db.hasUser()){
					if(pass1.compareTo(pass2)==0){
						db.addUser(pass1);
						SignIn si = new SignIn();
						dispose();
						si.setVisible(true);
					}
					else{
						JOptionPane.showMessageDialog(null, "Пароли не совпадают", "Ошибка", JOptionPane.ERROR_MESSAGE);
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "Пользователь с таким логином уже существует", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		signUpButton.setBounds(41, 170, 156, 23);
		contentPane.add(signUpButton);
	}
}
