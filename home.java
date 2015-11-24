package bd_conexao;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Box;
import javax.swing.JList;
import javax.swing.JTextArea;

public class home {
	private JFrame frame;
	private JButton buttonNewUser;
	private JTextField textField;
	private JTextArea Livros;
	private JTextArea Filmes;
	private JButton btnEstouComSorte;
	int i;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					home window = new home();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public home() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(300, 600));
		frame.setMaximumSize(new Dimension(50, 100));
		frame.setBounds(100, 100, 450, 300);

		textField = new JTextField();
		textField.setColumns(10);

		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Livros = new JTextArea();
		
		Filmes = new JTextArea();
		
		JButton btnRecomendar = new JButton("Recomendar");
		btnRecomendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String id = textField.getText();
				try {
					Conexao.init(id);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				while (Conexao.done == 0);
				Livros.setText(Conexao.livro);
				Filmes.setText(Conexao.filme);
				
				i = 0;
				
			}
		});
		
		
		btnEstouComSorte = new JButton("Estou com sorte");
		btnEstouComSorte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (i % 2 == 0){
					try{
						java.awt.Desktop.getDesktop().browse(java.net.URI.create(Conexao.urlFilme));
					} catch (Exception e){
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				
				} else {
					try{
						java.awt.Desktop.getDesktop().browse(java.net.URI.create(Conexao.urlLivro));
					} catch (Exception e){
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				}
				i++;

				
			}
		});
		
		
		
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(Filmes, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(Livros, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(btnRecomendar))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(147)
							.addComponent(btnEstouComSorte)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addComponent(btnRecomendar)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(Livros, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
						.addComponent(Filmes, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnEstouComSorte)
					.addContainerGap(13, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
