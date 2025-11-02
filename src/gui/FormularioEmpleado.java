package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioEmpleado extends JPanel {

    private PanelManager panelManager;
    private JPanel formularioEmpleado;
    private  JLabel tipoContratado;
    private JLabel tipoPermanente;
    private JLabel nombreContratado;
    private JLabel nombrePermanente;
    private JLabel valorHora;
    private JLabel valorDia;
    private JLabel categoria;
    private JTextField textNombreC;
    private JTextField textNombreP;
    private JTextField textValorHora;
    private JTextField textValorDia;
    private JComboBox<String> cCategoria;
    private JButton agregaContratado;
    private JButton agregaPermanente;
    private JButton volver;

    public FormularioEmpleado( PanelManager panelManager) {
        this.panelManager = panelManager;
        armarFormulario();
    }
    public void armarFormulario(){
        armarPantalla();
        volver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(1);
            }
        });
        agregaPermanente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panelManager.sistema().registrarEmpleado(textNombreP.getText(), Double.parseDouble(textValorDia.getText()),
                            cCategoria.getSelectedItem().toString());
                }
                catch(IllegalArgumentException exception){
                    JOptionPane.showMessageDialog(null, "Los valores ingresados no son validos");
                }
            }
        });
        agregaContratado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panelManager.sistema().registrarEmpleado(textNombreC.getText(), Double.parseDouble(textValorHora.getText()));
                }
                catch (IllegalArgumentException exception)
                {
                    JOptionPane.showMessageDialog(null, "Los valores ingresados no son validos");
                }
            }
        });
        setLayout(new BorderLayout());
        add(formularioEmpleado,BorderLayout.CENTER);
    }
    public void armarPantalla(){
        GridBagConstraints gbc=new GridBagConstraints();
        tipoContratado=new JLabel("Empleado contratado");
        tipoContratado.setFont(new Font("Arial", Font.BOLD, 20));
        tipoPermanente=new JLabel("Empleado de Planta Permanente");
        tipoPermanente.setFont(new Font("Arial", Font.BOLD, 20));
        nombreContratado =new JLabel("Nombre empleado");
        nombrePermanente=new JLabel("Nombre empleado");
        valorHora=new JLabel("Valor hora");
        valorDia=new JLabel("Valor dia");
        categoria=new JLabel("Categoria");
         textNombreC=new JTextField(30);
        textNombreP=new JTextField(30);
        textValorHora=new JTextField(10);
        textValorDia=new JTextField(10);
        String[] items={"INICIAL","TECNICO","EXPERTO"};
        cCategoria=new JComboBox(items);
        agregaContratado=new JButton("Agregar nuevo Contratado");
        agregaPermanente=new JButton("Agrgar nuevo Permanente");
        volver=new JButton("Volver al menu");
        formularioEmpleado=new JPanel();
        formularioEmpleado.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formularioEmpleado.add(tipoContratado, gbc);
        gbc.gridy = 1;
        formularioEmpleado.add(nombreContratado, gbc);
        gbc.gridx = 1;
        formularioEmpleado.add(valorHora, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formularioEmpleado.add(textNombreC, gbc);
        gbc.gridx = 1;
        formularioEmpleado.add(textValorHora, gbc);
        gbc.gridx = 3;
        gbc.gridy = 2;
        formularioEmpleado.add(agregaContratado, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formularioEmpleado.add(tipoPermanente, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formularioEmpleado.add(nombrePermanente, gbc);
        gbc.gridx = 1;
        formularioEmpleado.add(valorDia, gbc);
        gbc.gridx = 2;
        formularioEmpleado.add(categoria, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        formularioEmpleado.add(textNombreP, gbc);
        gbc.gridx = 1;
        formularioEmpleado.add(textValorDia, gbc);
        gbc.gridx = 2;
        formularioEmpleado.add(cCategoria, gbc);
        gbc.gridx = 3;
        formularioEmpleado.add(agregaPermanente, gbc);
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.ipady=20;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 6;
        formularioEmpleado.add(volver, gbc);
    }
}
