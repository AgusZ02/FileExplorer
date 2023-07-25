package GUI;

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class CrearGUI extends JFrame{
	private JTextField tfNombre;
	private JButton btnCrear;
    private JRadioButton rdbtnCarpeta, rdbtnArchivo;
    private JLabel lblNombre, lblExtension;
    private JComboBox<String> comboBox;
    
    private final String[] extensiones = {".txt"};
    private boolean folder = true;
    private JFrame vAvisos;
    public CrearGUI(File currentLocation) {
		getContentPane().setLayout(null);
		
		rdbtnCarpeta = new JRadioButton("Carpeta");
		rdbtnCarpeta.setSelected(true);
        rdbtnCarpeta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBox.setEnabled(false);
                folder = true;
			}
		});
		rdbtnCarpeta.setBounds(106, 63, 103, 21);
		getContentPane().add(rdbtnCarpeta);
		
		rdbtnArchivo = new JRadioButton("Archivo");
		rdbtnArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBox.setEnabled(true);
                folder = false;

			}
		});
		rdbtnArchivo.setBounds(216, 63, 103, 21);
		getContentPane().add(rdbtnArchivo);
		
		btnCrear = new JButton("Aceptar");
		btnCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!tfNombre.getText().isBlank()) {
                    String folderName = tfNombre.getText();
                    File newFile = null;
                    if (folder) {
                       newFile = new File(currentLocation.getPath()+"//"+folderName);
                        try {
                            newFile.mkdir();
                            FileX.refresh();
                            dispose();
                        } catch (Exception ex) {
                            vAvisos = new AvisoGUI("Error, no se ha podido crear el directorio.", "Error al crear el directorio", false);
                            vAvisos.setVisible(true);
                        } 
                    } else {
                        newFile = new File(currentLocation + "//" + folderName + (String)comboBox.getSelectedItem());
                        try {
                            newFile.createNewFile();
                            FileX.refresh();
                            dispose();
                        } catch (IOException e1) {
                            vAvisos = new AvisoGUI("Error, no se ha podido crear el archivo " + newFile.getName(), "Error al crear el archivo", false);
                            vAvisos.setVisible(true);
                            System.out.println("No se ha podido crear el archivo " + newFile);
                            e1.printStackTrace();
                        }
                    }
                    
                } else{
                    vAvisos = new AvisoGUI("Error al crear, caja de texto vacía.", "Texto vacío", false);
                    vAvisos.setVisible(true);
                }
                
			}
		});
		btnCrear.setBounds(164, 137, 85, 21);
		getContentPane().add(btnCrear);
		
		tfNombre = new JTextField();
		tfNombre.setBounds(106, 38, 320, 19);
		getContentPane().add(tfNombre);
		tfNombre.setColumns(10);
		
		lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(10, 41, 85, 13);
		getContentPane().add(lblNombre);
		
		comboBox = new JComboBox<String>(extensiones);
        comboBox.setSelectedIndex(0);
		comboBox.setBounds(106, 90, 320, 21);
		comboBox.setEnabled(false);
		getContentPane().add(comboBox);
		

        lblExtension = new JLabel("Extensión:");
		lblExtension.setBounds(10, 94, 85, 13);
		getContentPane().add(lblExtension);
	}
}
