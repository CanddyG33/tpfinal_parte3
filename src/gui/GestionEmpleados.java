package gui;

import entidades.Estado;
import entidades.Tupla;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GestionEmpleados extends JPanel{
    private PanelManager panelManager;
    private JPanel gestionEmpleados;
    private JLabel labelEmpleados;
    private JTable tablaEmpleados;
    private JScrollPane scrollPane;
    private JButton verRetrasos;
    private JButton volverPrincipal;

    public GestionEmpleados(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarFormulario();
    }

    public void armarFormulario() {
        DefaultTableModel modelTabla = new DefaultTableModel();
        armarPantalla(modelTabla);
        cargarTabla(modelTabla);
        volverPrincipal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(1);
            }
        });

        verRetrasos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int filaSeleccionada = tablaEmpleados.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        int fila = tablaEmpleados.getSelectedRow();
                        int retrasos=panelManager.sistema().consultarCantidadRetrasosEmpleado(Integer.parseInt(tablaEmpleados.getValueAt(fila, 0).toString()));
                        String mensaje=retrasos==0?"No tiene retrasos":"Tiene :"+retrasos +" retrasos";
                        JOptionPane.showMessageDialog(null, mensaje);
                    }
                }
            });


        setLayout(new BorderLayout());
        add(gestionEmpleados, BorderLayout.CENTER);
    }

    private void armarPantalla(DefaultTableModel modelTabla) {
        GridBagConstraints gbc = new GridBagConstraints();

        modelTabla.addColumn("Legajo");
        modelTabla.addColumn("Nombre");
        labelEmpleados=new JLabel("Empleados ");
        labelEmpleados.setFont(new Font("Arial", Font.BOLD, 18));
        tablaEmpleados=new JTable(modelTabla);
        scrollPane=new JScrollPane(tablaEmpleados);
        verRetrasos=new JButton("Ver retrasos del empleado seleccionado");
        volverPrincipal = new JButton("Volver al menu");
        gestionEmpleados = new JPanel();
        gestionEmpleados.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gestionEmpleados.add(labelEmpleados,gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gestionEmpleados.add(scrollPane,gbc);
        gbc.gridx=1;
        gbc.gridy=2;
        gestionEmpleados.add(verRetrasos,gbc);
        gbc.gridx = 2;
        gbc.gridy=3;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.ipady=20;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gestionEmpleados.add(volverPrincipal, gbc);
    }
    private void cargarTabla(DefaultTableModel tableModel){
        List<Tupla<Integer,String>> empleados;
        empleados = panelManager.sistema().empleados();
        tableModel.setRowCount(0);
        if(empleados!=null)
            for(Tupla<Integer,String> t:empleados) {
                Object[] nuevaFila = {t.getValor1(), t.getValor2()};
                tableModel.addRow(nuevaFila);
            }
    }
}