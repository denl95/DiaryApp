package denyslapin;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class SignIn extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignIn frame = new SignIn();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SignIn() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize( 246, 230);
		setTitle("Вход");
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogin = new JLabel("Логин");
		lblLogin.setBounds(40, 44, 46, 14);
		contentPane.add(lblLogin);
		
		textField = new JTextField();
		textField.setBounds(40, 57, 156, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Пароль");
		lblPassword.setBounds(40, 89, 46, 14);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(40, 102, 156, 20);
		contentPane.add(passwordField);
		
		JButton signInButton = new JButton("Войти");
		signInButton.setBounds(40, 129, 156, 23);
		contentPane.add(signInButton);
		getRootPane().setDefaultButton(signInButton);
		signInButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DBEngine db = new DBEngine(textField.getText());
				if(db.hasUser(new String(passwordField.getPassword()))){
					ListNoteFrame listNote = new ListNoteFrame(db);
					dispose();
					listNote.setVisible(true);
				}
				else{
					JOptionPane.showMessageDialog(null, "Неверный логин или пароль");
				}
				
			}
			
		});
		
		
		JButton signUpButton = new JButton("Зарегистрироваться");
		signUpButton.setBounds(40, 153, 156, 23);
		contentPane.add(signUpButton);
		signUpButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SignUp su = new SignUp();
				dispose();
				su.setVisible(true);
			}
			
		});
	}
}
