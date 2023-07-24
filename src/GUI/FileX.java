package GUI;


import java.awt.Desktop;
import java.io.*;
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
import javax.swing.JTextField;

public class FileX extends JFrame {

	private static JPanel contentPane;
	private static JTable table = new JTable();
	private static DefaultTableModel modelo = new DefaultTableModel();
	private final static String[] columnNames = {"Nombre", "Creacion", "Tipo", "Tamaño", "Ubicación"};
	private JScrollPane scrollPane = new JScrollPane();
	private static JTextField tfLocation;
	private static File currentLocation;
	private JButton btnAction, btnNuevo;
	public FileX(){
		this("C://");
	}

	public FileX(String loc) {
		
		currentLocation = new File(loc);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		table.setBounds(30, 10, 396, 243);
		modelo.setDataVector(null, columnNames);
		modelo.setColumnCount(5);
		fillTable(currentLocation
);
		table.setModel(modelo);		
		//contentPane.add(table);

		//setContentPane(contentPane);
		scrollPane.setBounds(20, 59, 1230, 585);
		scrollPane.setViewportView(table);
		table.setDefaultEditor(Object.class, null);
		contentPane.setLayout(null);
		contentPane.add(scrollPane);
		
		btnAction = new JButton("Aceptar");
		btnAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				File f = null;
				if (modelo.getValueAt(index, 4) instanceof File) {
					f = (File) modelo.getValueAt(index, 4);
					accionEn(f);

				}
			}
		});
		btnAction.setBounds(20, 654, 85, 21);
		contentPane.add(btnAction);
		
		tfLocation = new JTextField();
		tfLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = tfLocation.getText();
				File f = new File(text);
				if (f.isDirectory() && f.exists()) {
					//TODO asistir al usuario en la busqueda
					accionEn(f);

				}
				else{
					//TODO ventana de error
					System.out.println("Directorio no encontrado");
				}
			}
		});
		tfLocation.setBounds(20, 10, 1230, 39);
		contentPane.add(tfLocation);
		tfLocation.setColumns(10);
		tfLocation.setText(currentLocation.getAbsolutePath());
		
		btnNuevo = new JButton("Nuevo");
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new CrearGUI(currentLocation);
				frame.setVisible(true);

			}
		});
		btnNuevo.setBounds(115, 654, 85, 21);
		contentPane.add(btnNuevo);
	}

	public static void fillTable(final File folder) {
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
				//TODO^^^^^^
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
	private static void accionEn(File f){
		if (f.isDirectory()) {
			modelo = new DefaultTableModel();
			modelo.setDataVector(null, columnNames);
			modelo.setColumnCount(5);
			fillTable(new File(f.getPath()));
			table.setModel(modelo);
			contentPane.repaint();
			table.repaint();
			tfLocation.setText(f.getPath());
			currentLocation = f;
		} else if (f.isFile() && Desktop.isDesktopSupported()) {
			Desktop d = Desktop.getDesktop();
			try {
				d.open(f);
			} catch (IOException e1) {
				// TODO ventana error
				e1.printStackTrace();
				System.out.println("No se pudo abrir el archivo");
			}
		}
		
	}


	public static void refresh(){
		accionEn(currentLocation);

	}
}
