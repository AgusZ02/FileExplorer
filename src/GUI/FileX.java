package GUI;


import java.awt.Desktop;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FileX extends JFrame {

	private static JPanel contentPane;
	private static JTable table = new JTable();
	private static JTable tableFavs = new JTable();
	private static DefaultTableModel modelo, modeloFavs, tmpModel;
	
	private final static String[] columnNamesMain = {"Nombre", "Última modificación", "Tipo", "Tamaño"};
	private JScrollPane scrollPane = new JScrollPane();
	private static JTextField tfLocation;
	private static File currentLocation;
	private JButton btnAction, btnNuevo, btnBorrar, btnFavorito;
	private JFrame vAvisos;
	private final File[] favoritos = {new File("C:\\Users\\agus\\Desktop"), new File("C:\\Users\\agus\\Documents"), new File("C:\\Users\\agus\\Downloads"), new File("C:\\Users\\agus\\Pictures"), new File("C:\\")};
	private JScrollPane scrollPane_1 = new JScrollPane();
	private Properties propFile = new Properties();
	private JPanel panel1;

	public FileX(){
		this("C:\\Users\\agus\\Desktop");
	}

	public FileX(String loc) {
		tmpModel = null;
		try {
			propFile.load(new FileInputStream(new File("configuracion.properties")));
			tmpModel = new DefaultTableModel();
			tmpModel.setColumnCount(5);
			
			for (Object o : propFile.keySet()) {
				if (o instanceof String) {
					String s = (String) o;
					File f = new File(propFile.getProperty(s));
					Vector<Object> row = new Vector<Object>();
					row.add(f.getName());
					row.add("");
					row.add("");
					row.add("");
					row.add(f);
					tmpModel.addRow(row);
				}
			}
		} catch (FileNotFoundException e) {
			try {
				new File("configuracion.properties").createNewFile();
				int i = 0;
				for (File favs : favoritos) {
					propFile.setProperty(String.valueOf(i), favs.getPath());
					i++;
				}
                
                propFile.store(new FileWriter(new File("configuracion.properties")), "Rutas");
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}


		currentLocation = new File(loc);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

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
					vAvisos = new AvisoGUI("Error, no se halla el directorio.", "Error al buscar el directorio", new Exception(""), false);
                    vAvisos.setVisible(true);
					System.out.println("Directorio no encontrado");
				}
			}
		});
		tfLocation.setBounds(20, 20, 1230, 29);
		contentPane.add(tfLocation);
		tfLocation.setColumns(10);
		tfLocation.setText(currentLocation.getAbsolutePath());

		//Tabla de vinculos favoritos
		

		modeloFavs = new DefaultTableModel();
		modeloFavs.setDataVector(null, columnNamesMain);
		modeloFavs.setColumnCount(5);
		if (tmpModel!=null) {
			modeloFavs = tmpModel;
		} else{
			
		//Guardar los vinculos favoritos por defecto (Array de rutas)
		for (File f : favoritos) {
			Vector<Object> row = new Vector<>();
			row.add(f.getName());
			row.add(f.getName());
			row.add(f.getName());
			row.add(f.getName());
			row.add(f);
			modeloFavs.addRow(row);
		}
	}
		tableFavs.addMouseListener(new MouseAdapter() { //Si se pulsa sobre la tabla de favoritos, la seleccion de la tabla principal se quita
			@Override
			public void mousePressed(MouseEvent e) {
				if (table.getSelectedRow()!=-1) {
					table.getSelectionModel().clearSelection();
				}
			}
		});
		tableFavs.setModel(modeloFavs);
		
		tableFavs.getColumnModel().removeColumn(tableFavs.getColumnModel().getColumn(4));
		tableFavs.getColumnModel().removeColumn(tableFavs.getColumnModel().getColumn(3));
		tableFavs.getColumnModel().removeColumn(tableFavs.getColumnModel().getColumn(2));
		tableFavs.getColumnModel().removeColumn(tableFavs.getColumnModel().getColumn(1));
		scrollPane_1.setBounds(20, 59, 276, 585);
		scrollPane_1.setViewportView(tableFavs);
		contentPane.add(scrollPane_1);
		table.addMouseListener(new MouseAdapter() { //Si se pulsa sobre la tabla principal, la seleccion que hubiera en la tabla de favoritos se va
			@Override
			public void mouseClicked(MouseEvent e) {
				if (tableFavs.getSelectedRow()!=-1) {
					tableFavs.getSelectionModel().clearSelection();
				}
			}
		});

		table.setBounds(30, 10, 396, 243);
		accionEn(currentLocation);
		scrollPane.setBounds(306, 59, 944, 585);
		scrollPane.setViewportView(table);
		table.setDefaultEditor(Object.class, null);
		contentPane.setLayout(null);
		contentPane.add(scrollPane);
		
		btnAction = new JButton("Aceptar");
		btnAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = null;
				if (tableFavs.getSelectionModel().isSelectionEmpty()) {
					selectedFile = (File) modelo.getValueAt(table.getSelectedRow(), 4);	
				} else{
					selectedFile = (File) modeloFavs.getValueAt(tableFavs.getSelectedRow(), 4);		
				}
				accionEn(selectedFile);
			}
		});
		btnAction.setBounds(20, 654, 85, 21);
		contentPane.add(btnAction);
		
		
		btnNuevo = new JButton("Nuevo");
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new CrearGUI(currentLocation);
				frame.setVisible(true);

			}
		});
		btnNuevo.setBounds(115, 654, 85, 21);
		contentPane.add(btnNuevo);
		
		btnBorrar = new JButton("Eliminar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = (File) modelo.getValueAt(table.getSelectedRow(), 4);					
				// En caso de ser directorio, borra todos los datos.
				
				
				if (selectedFile.isDirectory() && selectedFile.list().length > 0) {
					
					vAvisos = new AvisoGUI("Este directorio contiene: " + selectedFile.list().length + "items. Estás seguro?", "Confirmar eliminación de directorio", new Exception(""), true);
                    vAvisos.setVisible(true);
					//TODO, borrar sólo si se elige "Aceptar" en la ventana de avisos.
					eliminarFicheros(selectedFile);
					
				}
				
				try {
					selectedFile.delete();
					accionEn(currentLocation);
				} catch (Exception e3) {
					vAvisos = new AvisoGUI("Error, no se ha podido eliminar.", "Error al eliminar", e3,  false);
                    vAvisos.setVisible(true);
					System.out.println("No se ha podido eliminar");
				}
			}
		});
		btnBorrar.setBounds(211, 654, 85, 21);
		contentPane.add(btnBorrar);
		
		btnFavorito = new JButton("Añadir a favorito");
		btnFavorito.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!table.getSelectionModel().isSelectionEmpty()){
					File f = (File) modelo.getValueAt(table.getSelectedRow(), 4);
					Vector<Object> row = new Vector<Object>();
					row.add(f.getName());
					row.add("");
					row.add("");
					row.add("");
					row.add(f);
					modeloFavs.addRow(row);
					propFile.setProperty(String.valueOf(propFile.keySet().size()), f.getPath());

				}
				tableFavs.setModel(modeloFavs);
				try {
					propFile.store(new FileWriter(new File("configuracion.properties")), "Rutas");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnFavorito.setBounds(306, 654, 85, 21);
		contentPane.add(btnFavorito);
		
		
	}

	/**
	 * Método para listar todos los items del directorio folder.
	 * Crea un vector con la siguiente info de cada item:
	 * Nombre, última modificación, tipo de dato, tamaño en bytes
	 * Y se añade a la tabla.
	 * @param folder el directorio del que se quieren listar los items.
	 */
	private static void fillTable(File folder, DefaultTableModel modelo) {
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
				row.add(new SimpleDateFormat("MM-dd-yyyy").format(new Date(fileEntry.lastModified())));
				if (fileEntry.isDirectory()) {
					//Casilla de tipo
					row.add("Carpeta");
					//Casilla de tamaño (nulo)
					try{
						row.add(fileEntry.listFiles().length + " items");
					} catch(NullPointerException e){
						//continue; //<-- Esta línea para no mostrar directorios inaccesibles
						row.add(0 + "/Inaccesible"); //<-- Esta línea para mostrar directorios inaccesibles
					}

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
	
	/**
	 * Este método es llamado cada vez que se efectúe una acción en un archivo o directorio desde la aplicación.
	 * Si es carpeta, limpia la GUI y se listan los items del directorio nuevo
	 * Si es archivo, se abre este mismo.
	 * @param f La ruta al archivo/directorio que se quiere ejercer la acción.
	 */
	private static void accionEn(File f){
		if (f.isDirectory()) {
			modelo = new DefaultTableModel();
			modelo.setDataVector(null, columnNamesMain);
			modelo.setColumnCount(5);
			fillTable(new File(f.getPath()), modelo);
			table.setModel(modelo);
			table.getColumnModel().removeColumn(table.getColumnModel().getColumn(4));
			contentPane.repaint();
			table.repaint();
			tfLocation.setText(f.getPath());
			currentLocation = f;
		} else if (f.isFile() && Desktop.isDesktopSupported()) {
			Desktop d = Desktop.getDesktop();
			try {
				d.open(f);
			} catch (IOException e1) {
				JFrame vAvisos = new AvisoGUI("Error, no se puede abrir el archivo.", "Error al abrir el archivo.", e1, false);
                vAvisos.setVisible(true);
				e1.printStackTrace();
				System.out.println("No se pudo abrir el archivo");
			}
		}
		
	}

	public static void refresh(){
		accionEn(currentLocation);
	}

	/**
	 * Algoritmo recursivo para eliminar todos los datos de las carpetas dentro de una carpeta.
	 */
	private void eliminarFicheros(File current){
		
		for (File f : current.listFiles()) {
			if (f.isFile()) {
				f.delete();
			} else if (f.isDirectory()) {
				if (f.list().length == 0) {
					f.delete();
				} else {
					eliminarFicheros(f);
					
				}
			}
		}
		current.delete();
	}


}
