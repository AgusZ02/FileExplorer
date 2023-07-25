package GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AvisoGUI extends JFrame{
	public AvisoGUI(String mensaje, String nombreVentana, Exception e, boolean confirmacion) {
		getContentPane().setLayout(null);
		this.setTitle(nombreVentana);
		JLabel lblAviso = new JLabel(mensaje);
		lblAviso.setVerticalAlignment(SwingConstants.TOP);
		lblAviso.setBounds(10, 10, 416, 61);
		getContentPane().add(lblAviso);
		
		setBounds(100, 100, 447, 201);
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.setSize(90, 41);
		btnAceptar.setLocation(175, 113);
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                dispose();
			}
		});
		getContentPane().add(btnAceptar);
		
		if (e!=null) {
			JTextArea textArea = new JTextArea();
			textArea.setBounds(10, 81, 416, 116);
			textArea.setText(e.getMessage());
			getContentPane().add(textArea);
			
		}
		
        if (confirmacion) {
            btnAceptar.setBounds(110, 232, 85, 21);
            JButton btnCancelar = new JButton("Cancelar");
		    btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                dispose();
			}
		    });
		    btnCancelar.setBounds(204, 232, 85, 21);
		    getContentPane().add(btnCancelar);
        } else{
            btnAceptar.setBounds(157, 130, 85, 21);
        }
        
		
	}
}
