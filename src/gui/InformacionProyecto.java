package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InformacionProyecto extends JDialog {
    private Integer numero;
    private String informacion;
    private JTextArea textArea;
    private JButton aceptar;

    public InformacionProyecto(Frame frame, Integer numero, String informacion) {
        super(frame);
        this.numero=numero;
        this.informacion = informacion;
        setSize(500, 300);
        setLocationRelativeTo(frame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        crearComponentes();
    }
    private void crearComponentes() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelTitulo = new JLabel("Proyecto número: " + numero);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panel.add(labelTitulo, gbc);

        // --- Área de texto con scroll ---
        textArea = new JTextArea(informacion);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(
                textArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        panel.add(scroll, gbc);

        // --- Botón Aceptar ---
        aceptar = new JButton("Aceptar");
        JPanel panelBotones = new JPanel();
        panelBotones.add(aceptar);

        gbc.gridy = 2;
        gbc.weighty = 0;
        panel.add(panelBotones, gbc);

        add(panel);

        aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
