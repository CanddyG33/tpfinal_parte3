package gui;

import entidades.HomeSolution;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class FormularioProyecto extends JPanel {
    private PanelManager panelManager;
    private JPanel formularioProyecto;
    private JLabel titulo;
    private JLabel tareas;
    private JLabel labelTituloTarea;
    private JLabel labelDescTarea;
    private JLabel labelDiasTarea;
    private JLabel clienteNombre;
    private JLabel clienteMail;
    private JLabel clienteTelefono;
    private JLabel fechaInicio;
    private JLabel fechaFin;
    private JLabel domicilio;
    private JTextField textTituloTarea;
    private JTextField textDescTarea;
    private JTextField textDiasTarea;
    private JTextField textClienteNombre;
    private JTextField textClienteMail;
    private JTextField textClienteTelefono;
    private JTextField textDomicilio;
    private JFormattedTextField textFechaInicio;
    private JTextField textFechaFin;
    private JTable tableTareas;
    private JButton agregar;
    private JButton crearProyecto;
    private JButton volverPrincipal;


    public FormularioProyecto(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarFormulario();
    }
    public void armarFormulario()
    {   DefaultTableModel modelTabla=new DefaultTableModel();
        armarPantalla(modelTabla);
        volverPrincipal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(1);
            }
        });
        agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numFila = modelTabla.getRowCount() + 1;
                Object[] nuevaFila = {
                        textTituloTarea.getText(),
                        textDescTarea.getText(),
                        textDiasTarea.getText()
                };
                modelTabla.addRow(nuevaFila);
                textTituloTarea.setText("");
                textDescTarea.setText("");
                textDiasTarea.setText("");
            }

        });

        crearProyecto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String[] titulos=new String[modelTabla.getRowCount()];
               String[] desc=new String[modelTabla.getRowCount()];;
               double[] dias=new double[modelTabla.getRowCount()];;
               String[] cliente={textClienteNombre.getText(),
                                 textClienteMail.getText(),
                                 textClienteTelefono.getText()};
               armarArrays(titulos, desc,dias, modelTabla);
               try {
                   panelManager.sistema().registrarProyecto(titulos, desc, dias, textDomicilio.getText(),
                           cliente, textFechaInicio.getText(),
                           textFechaFin.getText());
                   panelManager.mostrar(5);
               }
               catch (IllegalArgumentException exception){
                   JOptionPane.showMessageDialog(null, "Los valores ingresados no son validos");
               }
            }
        });
        setLayout(new BorderLayout());
        add(formularioProyecto,BorderLayout.CENTER);
    }
    private void armarArrays(String[] titulos, String[] desc,double[] dias, DefaultTableModel tabla){
        for(int i=0;i<tabla.getRowCount();i++){
            titulos[i]=tabla.getValueAt(i,0).toString().trim();
            desc[i]=tabla.getValueAt(i,1).toString();
            dias[i]=Double.parseDouble(tabla.getValueAt(i,2).toString());
        }
    }

    private void armarPantalla(DefaultTableModel modelTabla)
    {   GridBagConstraints gbc = new GridBagConstraints();

        modelTabla.addColumn("Titulo");
        modelTabla.addColumn("Descripción");
        modelTabla.addColumn("Dias");
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        titulo=new JLabel("Nuevo Proyecto");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        tareas =new JLabel("Tareas");
        tareas.setFont(new Font("Arial", Font.BOLD, 16));
        labelTituloTarea=new JLabel("Titulo");
        labelDescTarea=new JLabel("Descripción");
        labelDiasTarea=new JLabel("Dias");
        textTituloTarea=new JTextField(30);
        textDescTarea=new JTextField(50);
        textDiasTarea=new JTextField(10);

        clienteNombre=new JLabel("Nombre cliente");
        clienteMail=new JLabel("eMail cliente");
        clienteTelefono=new JLabel("Telefono cliente");
        domicilio=new JLabel("Domicilio");
        fechaInicio=new JLabel("Fecha de inicio");
        fechaFin=new JLabel("Fecha finalización");

        textClienteNombre=new JTextField(30);
        textClienteMail=new JTextField(50);
        textClienteTelefono=new JTextField(10);
        textDomicilio=new JTextField(40);
        textFechaInicio=new JFormattedTextField(formatter);
        textFechaFin=new JFormattedTextField(formatter);
        tableTareas=new JTable(modelTabla);
        JScrollPane scrollPane = new JScrollPane(tableTareas);
        agregar=new JButton("Agregar");
        crearProyecto=new JButton("Crear Proyecto");
        volverPrincipal=new JButton("Volver al menu");
        formularioProyecto=new JPanel();
        formularioProyecto.setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        formularioProyecto.add(titulo, gbc);
        gbc.gridy = 1;
        formularioProyecto.add(tareas, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formularioProyecto.add(labelTituloTarea, gbc);
        gbc.gridx = 1;
        formularioProyecto.add(labelDescTarea, gbc);
        gbc.gridx = 2;
        formularioProyecto.add(labelDiasTarea, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        formularioProyecto.add(textTituloTarea, gbc);
        gbc.gridx = 1;
        formularioProyecto.add(textDescTarea, gbc);
        gbc.gridx = 2;
        formularioProyecto.add(textDiasTarea, gbc);
        gbc.gridx = 3;
        formularioProyecto.add(agregar, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        formularioProyecto.add(scrollPane, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        formularioProyecto.add(clienteNombre, gbc);
        gbc.gridx = 1;
        formularioProyecto.add(clienteMail, gbc);
        gbc.gridx = 2;
        formularioProyecto.add(clienteTelefono, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        formularioProyecto.add(textClienteNombre, gbc);
        gbc.gridx = 1;
        formularioProyecto.add(textClienteMail, gbc);
        gbc.gridx = 2;
        formularioProyecto.add(textClienteTelefono, gbc);
        gbc.gridx = 1;
        formularioProyecto.add(textClienteMail, gbc);
        gbc.gridx = 2;
        formularioProyecto.add(textClienteTelefono, gbc);
        gbc.gridx = 2;
        gbc.gridy = 7;
        formularioProyecto.add(fechaInicio, gbc);
        gbc.gridx = 3;
        formularioProyecto.add(fechaFin, gbc);
        gbc.gridx = 0;
        gbc.gridy = 8;
        formularioProyecto.add(domicilio,gbc);
        gbc.gridx = 1;
        formularioProyecto.add(textDomicilio,gbc);
        gbc.gridx = 2;
        formularioProyecto.add(textFechaInicio, gbc);
        gbc.gridx = 3;
        formularioProyecto.add(textFechaFin, gbc);
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.ipady=20;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formularioProyecto.add(crearProyecto, gbc);
        gbc.gridx = 4;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.ipady=20;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formularioProyecto.add(volverPrincipal, gbc);
    }
}
