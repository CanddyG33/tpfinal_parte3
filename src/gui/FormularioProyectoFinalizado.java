package gui;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class FormularioProyectoFinalizado extends JDialog{
    private JFormattedTextField fechaFinalizacion;
    private JButton btnAceptar;

    public FormularioProyectoFinalizado(Frame owner) {
        super(owner, "Finalizar Proyecto", true);
        setSize(500, 300);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        crearComponentes();
    }

    private void crearComponentes() {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelFecha = new JLabel("Fecha de finalización");
        fechaFinalizacion = new JFormattedTextField(formatter);
        fechaFinalizacion.setColumns(5);
        btnAceptar = new JButton("Aceptar");


        // --- Diseño ---
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(labelFecha, gbc);
        gbc.gridx = 1;
        panel.add(fechaFinalizacion, gbc);

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

    public String getFecha() {
        return fechaFinalizacion.getText().trim();
    }
}
