package GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AvisoGUI extends JFrame{
	public AvisoGUI(String mensaje, String nombreVentana, boolean confirmacion) {
		getContentPane().setLayout(null);
		this.setTitle(nombreVentana);
		JLabel lblAviso = new JLabel(mensaje);
		lblAviso.setVerticalAlignment(SwingConstants.TOP);
		lblAviso.setBounds(10, 10, 416, 175);
		getContentPane().add(lblAviso);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                dispose();
			}
		});
		getContentPane().add(btnAceptar);
		
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
            btnAceptar.setBounds(157, 232, 85, 21);
        }
        
		
	}
}
