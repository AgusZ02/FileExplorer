package gui;


import java.awt.Desktop;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FileX extends JFrame {

	private static JPanel contentPane;
	private final static JTable table = new JTable();
	private final static JTable tableFavs = new JTable(){
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private static DefaultTableModel modelo, modeloFavs;
	private final static String[] columnNamesMain = {"Nombre", "Última modificación", "Tipo", "Tamaño"};
	private static JTextField tfLocation;
	private static File currentLocation;
	private final JButton btnAction, btnNuevo, btnBorrar, btnFavorito;

	private final static String userHome = System.getProperty("user.home");
	private final static File[] favoritos = {new File(userHome + "\\Desktop"), new File(userHome+"\\Documents"), new File(userHome+"\\Downloads"), new File(userHome+"\\Pictures")};
	private final JScrollPane scrollPane_1 = new JScrollPane();
	private final Properties propFile = new Properties();

	private String[] row = new String[5];

	public FileX(){
		this(favoritos[0]);
	}

	public FileX(File currentLocation) {

		System.out.println(userHome);
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
					JOptionPane.showMessageDialog(null,"Error, no se encuentra el directorio.");
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
		modeloFavs.setColumnCount(5);
		try {
			propFile.load(new FileInputStream(new File("pinned_paths.properties")));
			System.out.println("Leyendo vinculos...");
			for (Object o : propFile.keySet()) {
				if (o instanceof String s) {
					File f = new File(propFile.getProperty(s));
					row[0] = f.getName();
					row[4] = f.getPath();
					modeloFavs.addRow(row);
					System.out.println("Cargado el vinculo a " + f.getName());
				}
			}
			System.out.println("Cargados todos los vinculos del archivo en el menú");
		} catch (FileNotFoundException e) {
			System.out.println("Archivo de vinculos no encontrado, creando...");
			
			try {
				new File("pinned_paths.properties").createNewFile();
				int i = 0;
				for (File favs : favoritos) {
					propFile.setProperty(String.valueOf(i), favs.getPath());
					i++;
				}
                
                propFile.store(new FileWriter(new File("pinned_paths.properties")), "Rutas");
				System.out.println("Archivo pinned_paths.properties creado.");
				//Guardar los vinculos favoritos por defecto (Array de rutas)
				for (File f : favoritos) {
					row[0] = f.getName();
					row[4] = f.getPath();
					modeloFavs.addRow(row);
				}
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2){
					System.out.println("bien");
					File selectedFile = new File((String) modelo.getValueAt(table.getSelectedRow(), 4));
					accionEn(selectedFile);
				}
			}

		});
		tableFavs.addMouseListener(new MouseAdapter() { //Si se pulsa sobre la tabla de favoritos, la seleccion de la tabla principal se quita
			@Override
			public void mousePressed(MouseEvent e) {
				if (table.getSelectedRow()!=-1) {
					table.getSelectionModel().clearSelection();
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Click1");
				if (e.getClickCount()==2){
					System.out.println("bien");
					File selectedFile = new File((String) modeloFavs.getValueAt(tableFavs.getSelectedRow(), 4));
					accionEn(selectedFile);
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
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(306, 59, 944, 585);
		scrollPane.setViewportView(table);
		table.setDefaultEditor(Object.class, null);

		contentPane.setLayout(null);
		contentPane.add(scrollPane);
		
		btnAction = new JButton("Aceptar");
		btnAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = null;
				try{if (tableFavs.getSelectionModel().isSelectionEmpty()) {
					selectedFile = new File((String) modelo.getValueAt(table.getSelectedRow(), 4));
				} else{
					selectedFile = new File((String) modeloFavs.getValueAt(tableFavs.getSelectedRow(), 4));
				}
					accionEn(selectedFile);
				} catch(NullPointerException e1){
					JOptionPane.showMessageDialog(null, "Error, no se puede retroceder más");
				}

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
				int[] indices;
				if (tableFavs.isColumnSelected(0)) {
					indices = tableFavs.getSelectionModel().getSelectedIndices();
					for (int i : indices){
						String path =(String) modeloFavs.getValueAt(i,4);
						modeloFavs.removeRow(i);
						for (Object o : propFile.keySet()){
							if (o instanceof String s && propFile.getProperty(s).equals(path)){
								propFile.remove(s);
							}
						}
					}
					try{
						propFile.store(new FileWriter(new File("pinned_paths.properties")), "Rutas");
						System.out.println("Cambios guardados?");
					}catch(IOException e2){
						JOptionPane.showMessageDialog(null, "No se ha podido eliminar.");
					}


				} else {
					indices = table.getSelectionModel().getSelectedIndices();
					for (int i = 0; i < indices.length; i++) {
						File selectedFile = new File((String) modelo.getValueAt(indices[i], 4));
						// En caso de ser directorio, borra todos los datos.
						if (selectedFile.isDirectory() && selectedFile.list().length > 0) {
							JOptionPane.showMessageDialog(null, "Este directorio contiene: " + selectedFile.list().length + "items. Estás seguro?");
							//TODO, borrar sólo si se elige "Aceptar" en la ventana de avisos.
							eliminarFicheros(selectedFile);
						}
						try {
							selectedFile.delete();
						} catch (Exception e3) {
							JOptionPane.showMessageDialog(null, "No se ha podido eliminar.");
							System.out.println("No se ha podido eliminar");
						}
					}
					refresh();
				}
			}
		});
		btnBorrar.setBounds(211, 654, 85, 21);
		contentPane.add(btnBorrar);
		
		btnFavorito = new JButton("Añadir a favorito");
		btnFavorito.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int keySetSize = propFile.keySet().size();
				if(!table.getSelectionModel().isSelectionEmpty()){
					File f = new File((String) modelo.getValueAt(table.getSelectedRow(), 4));
					row[0] = f.getName();
					row[4] = f.getPath();
					modeloFavs.addRow(row);
					propFile.setProperty(String.valueOf(keySetSize), f.getPath());

				}
				System.out.println("Se ha guardado " + propFile.getProperty(String.valueOf(keySetSize-1)) + "En favoritos.");
				try {
					propFile.store(new FileWriter(new File("pinned_paths.properties")), "Rutas");
				} catch (IOException e1) {
					System.out.println("No se ha podido guardar la ruta en favoritos");
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
			//Vector<Object> row = new Vector<>();
			String[] row = new String[5];
			try {
				row[0] = "///////DIRECTORIO ANTERIOR///////";
				row[1] ="--------------";
				row[2] = "--------------";
				row[3] = "--------------";
				row[4] = folder.getParent();
				modelo.addRow(row);
				
			} catch (Exception e) {
				System.out.println("Directorio raiz");
			} finally{
			
				for (final File fileEntry : folder.listFiles()) {

				
				//Casilla de nombre de fichero
				row[0] = fileEntry.getName();
				
				
				//Casilla de fecha
				row[1] = new SimpleDateFormat("MM-dd-yyyy").format(new Date(fileEntry.lastModified()));

				if (fileEntry.isDirectory()) {
					//Casilla de tipo
					row[2] = "Carpeta";
					//Casilla de tamaño (nulo)
					try{
						row[3] = fileEntry.listFiles().length + " items";
					} catch(NullPointerException e){
						//continue; //<-- Esta línea para no mostrar directorios inaccesibles
						row[3] = 0 + "/Inaccesible"; //<-- Esta línea para mostrar directorios inaccesibles
					}

				} else {
					//Casilla de tipo de archivo
					String[] arr = fileEntry.getName().split("\\.");
					if (arr.length == 2) {
						row[2] = arr[1];
					} else{
						row[2] = arr[arr.length-1];
					}
					//Casilla de tamaño. 
					//TODO: Unidad de tamaño correcta
					row[3] = fileEntry.length() + " Bytes";
				}
				//Fichero
				row[4] =fileEntry.getPath();
				
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
			tfLocation.setText(f.getPath());
			currentLocation = f;
		} else if (f.isFile() && Desktop.isDesktopSupported()) {
			Desktop d = Desktop.getDesktop();
			try {
				d.open(f);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null,"Error, no se puede abrir el archivo");
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
