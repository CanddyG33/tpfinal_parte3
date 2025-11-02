package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioTarea extends JDialog {

        private JTextField textTitulo;
        private JTextField textDescripcion;
        private JTextField textDias;
        private JButton btnAceptar;

        public FormularioTarea(Frame owner) {
            super(owner, "Nueva Tarea", true);
            setSize(500, 300);
            setLocationRelativeTo(owner);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            crearComponentes();
        }

        private void crearComponentes() {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 2, 2, 2);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel labelTitulo = new JLabel("Título:");
            textTitulo = new JTextField(30);

            JLabel labelDescripcion = new JLabel("Descripción:");
            textDescripcion = new JTextField( 30);

            JLabel labelDias = new JLabel("Cantidad de días:");
            textDias = new JTextField();
            textDias.setColumns(5);
            btnAceptar = new JButton("Aceptar");

            // --- Diseño ---
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(labelTitulo, gbc);
            gbc.gridx = 1;
            panel.add(textTitulo, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(labelDescripcion, gbc);
            gbc.gridx = 1;
            panel.add(textDescripcion,gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(labelDias, gbc);
            gbc.gridx = 1;
            panel.add(textDias, gbc);

            JPanel panelBotones = new JPanel();
            panelBotones.add(btnAceptar);

            gbc.gridx = 0; gbc.gridy = 3;
            gbc.gridwidth = 2;
            panel.add(panelBotones, gbc);

            add(panel);

            btnAceptar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }

        public String getTitulo() {
            return textTitulo.getText().trim();
        }

        public String getDescripcion() {
            return textDescripcion.getText().trim();
        }

        public String getDias() {
            return textDias.getText().trim();
        }

}
