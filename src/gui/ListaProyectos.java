package gui;

import entidades.Estado;
import entidades.HomeSolution;
import entidades.Proyecto;
import entidades.Tupla;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class ListaProyectos extends JPanel{
    private PanelManager panelManager;
    private JPanel listaProyectos;
    private JLabel estado;
    private JRadioButton pendiente;
    private JRadioButton activo;
    private JRadioButton finalizado;
    private ButtonGroup grupoOpciones;
    private JTable proyectos;
    private JButton mostrar;
    private JButton gestion;


    public ListaProyectos(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarFormulario();
    }
    public void armarFormulario(){
        DefaultTableModel modelTabla=new DefaultTableModel();
        armarPantalla(modelTabla);
        mostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(pendiente.isSelected())
                    cargarTabla(Estado.pendiente,modelTabla);
                if(activo.isSelected())
                    cargarTabla(Estado.activo,modelTabla);
                if(finalizado.isSelected())
                     cargarTabla(Estado.finalizado,modelTabla);
                if (modelTabla.getRowCount()==0)
                    JOptionPane.showMessageDialog(null, "No hay proyectos");
            }
        });
        gestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada=proyectos.getSelectedRow();
                if (filaSeleccionada!=-1) {
                    int fila = proyectos.getSelectedRow();
                    panelManager.seleccionar(Integer.parseInt(proyectos.getValueAt(fila, 0).toString()));
                    panelManager.mostrar(6);
                }
                else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un proyecto");
            }
        });
        setLayout(new BorderLayout());
        add(listaProyectos,BorderLayout.CENTER);
    }
    private void armarPantalla(DefaultTableModel modelTabla){
        GridBagConstraints gbc = new GridBagConstraints();

        modelTabla.addColumn("Numero Proyecto");
        modelTabla.addColumn("Datos");

        estado=new JLabel("Seleccionar estado del proyecto");
        pendiente=new JRadioButton(Estado.pendiente);
        activo=new JRadioButton(Estado.activo);
        finalizado=new JRadioButton(Estado.finalizado);
        grupoOpciones=new ButtonGroup();
        grupoOpciones.add(pendiente);
        grupoOpciones.add(activo);
        grupoOpciones.add(finalizado);
        pendiente.setSelected(true);
        proyectos=new JTable(modelTabla);
        JScrollPane scrollPane = new JScrollPane(proyectos);
        mostrar=new JButton("Mostrar proyectos seleccionados");
        gestion=new JButton("Ver seleccionado");
        listaProyectos=new JPanel();
        listaProyectos.setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        listaProyectos.add(estado, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        listaProyectos.add(pendiente, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        listaProyectos.add(activo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        listaProyectos.add(finalizado, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        listaProyectos.add(mostrar, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        listaProyectos.add(scrollPane, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.ipady=20;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        listaProyectos.add(gestion,gbc);

    }
    private void cargarTabla(String opcion,DefaultTableModel tableModel){
        List <Tupla<Integer,String>> proyectos=new ArrayList<>();

        switch(opcion)
        {
            case Estado.pendiente:
                proyectos = panelManager.sistema().proyectosPendientes();
                break;
            case Estado.activo:
                proyectos = panelManager.sistema().proyectosActivos();
            break;
            case Estado.finalizado:
                proyectos = panelManager.sistema().proyectosFinalizados();
                break;
        }
        tableModel.setRowCount(0);

        for(Tupla<Integer,String> t:proyectos) {
            Object[] nuevaFila = {t.getValor1(), t.getValor2()};
            tableModel.addRow(nuevaFila);
        }
    }

}
