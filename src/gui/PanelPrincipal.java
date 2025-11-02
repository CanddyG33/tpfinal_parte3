package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelPrincipal extends JPanel {

    private PanelManager panel;
    private JPanel panelPrincipal;
    private JButton jButtonNuevoEmpleado;
    private JButton jButtonGestionEmpleados;
    private JButton jButtonNuevoProyecto;
    private JButton jButtonListaProyectos;


    public PanelPrincipal(PanelManager panel)
    {
        this.panel=panel;
        armarFormulario();
    }
    public void armarFormulario()
    {

        panelPrincipal=new JPanel();
        panelPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
        jButtonNuevoEmpleado=new JButton("Nuevo Empleado");
        jButtonGestionEmpleados=new JButton("Gestión Empleados");
        jButtonNuevoProyecto=new JButton("Nuevo Proyecto");
        jButtonListaProyectos=new JButton("Lista Proyectos");
        panelPrincipal.add(jButtonNuevoEmpleado);
        panelPrincipal.add(jButtonGestionEmpleados);
        panelPrincipal.add(jButtonNuevoProyecto);
        panelPrincipal.add(jButtonListaProyectos);

        jButtonNuevoEmpleado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.mostrar(2);
            }
        });
        jButtonGestionEmpleados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.mostrar(3);
            }
        });
        jButtonNuevoProyecto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.mostrar(4);
            }
        });
        jButtonListaProyectos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.mostrar(5);
            }
        });
        setLayout(new BorderLayout());
        add(panelPrincipal,BorderLayout.CENTER);
    }
}
