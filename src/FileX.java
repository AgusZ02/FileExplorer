

import java.awt.EventQueue;
import java.io.File;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FileX extends JFrame {

	private JPanel contentPane;
	private JTable table = new JTable();
	private DefaultTableModel modelo = new DefaultTableModel();
	private final String[] columnNames = {"Nombre", "Creacion", "Tipo", "Tamaño"};
	private JScrollPane scrollPane = new JScrollPane();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileX frame = new FileX();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public FileX() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		table.setBounds(30, 10, 396, 243);
		modelo.setDataVector(null, columnNames);
		modelo.setColumnCount(5);
		fillTable(new File("C:\\"));
		table.setModel(modelo);		
		//contentPane.add(table);

		//setContentPane(contentPane);
		scrollPane.setBounds(20, 20, this.getBounds().width-50, this.getBounds().height-96);
		scrollPane.setViewportView(table);
		table.setDefaultEditor(Object.class, null);
		contentPane.setLayout(null);
		contentPane.add(scrollPane);
		
		JButton btnAction = new JButton("Aceptar");
		btnAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				File f = null;
				if (modelo.getValueAt(index, 4) instanceof File) {
					f = (File) modelo.getValueAt(index, 4);
					if (f.isDirectory()) {
						
						modelo = new DefaultTableModel();
						modelo.setDataVector(null, columnNames);
						modelo.setColumnCount(5);
						fillTable(new File(f.getPath()));
						table.setModel(modelo);
						contentPane.repaint();
						table.repaint();
					}
				}
			}
		});
		btnAction.setBounds(20, 654, 85, 21);
		contentPane.add(btnAction);
	}

	public void fillTable(final File folder) {
			//Directorio superior
			Vector<Object> row = new Vector<>();
			try {
				row.add("///////DIRECTORIO ANTERIOR///////");
				row.add("--------------");
				row.add("--------------");
				row.add("--------------");
				row.add(new File(folder.getParent()));
				modelo.addRow(row);
				
			} catch (Exception e) {
				System.out.println("Directorio raiz");
			} finally{
			
				for (final File fileEntry : folder.listFiles()) {
				row = new Vector<Object>();
				
				//Casilla de nombre de fichero
				row.add(fileEntry.getName());
				//Casilla de fecha
				//TODO
				row.add("fecha");
				
				if (fileEntry.isDirectory()) {
					//Casilla de tipo
					row.add("Carpeta");
					//Casilla de tamaño (nulo)
					row.add("-------------");
				} else {
					
					
					//Casilla de tipo de archivo
					String[] arr = fileEntry.getName().split("\\.");
					if (arr.length == 2) {
						row.add(arr[1]);
					} else{
						row.add(arr[arr.length-1]);
					}
					//Casilla de tamaño. 
					//TODO: Unidad de tamaño correcta
					row.add(fileEntry.length() + " Bytes");
				}
				//Fichero
				row.add(fileEntry);
				
				modelo.addRow(row);
				
			}
			}
		}
}
