package gui;

import entidades.HomeSolution;
import entidades.Tupla;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GestionProyectos extends  JPanel{
    private PanelManager panelManager;
    private JPanel gestionProyecto;
    private JLabel labelTitulo;
    private JLabel itemTareas;
    private JLabel itemProyecto;
    private JLabel labelProyecto;
    private JLabel labelTarea;
    private JLabel labelMensajeTarea;
    private JComboBox<String> tareas;
    private JButton asignarEmpleado;
    private JButton asignarEmpleadoEficiente;
    private JButton registrarRetrasoEnTarea;
    private JButton establecerComoFinalizada;
    private JButton agregarTarea;
    private JButton reasignarEmpleado;
    private JButton reasignarEmpleadoEficiente;
    private JButton proyectoFinalizado;
    private JButton costoActualProyecto;
    private JButton empleadosAsignados;
    private JButton datosProyecto;
    private JButton volverPrincipal;

    public GestionProyectos(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarFormulario();
    }
    public void armarFormulario()
    {   DefaultTableModel modelTabla=new DefaultTableModel();
        Integer numero=panelManager.consultarSeleccionado();
        Object[] listaTareas=panelManager.sistema().tareasDeUnProyecto(numero);
        armarPantalla(modelTabla);
        tareas.setModel(new DefaultComboBoxModel(listaTareas));
        tareas.setSelectedIndex(-1);
        volverPrincipal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(1);
            }
        });
        asignarEmpleado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (seleccionValida()) {
                    String titulo = tareas.getSelectedItem().toString();
                    try {
                        panelManager.sistema().asignarResponsableEnTarea(panelManager.consultarSeleccionado(), titulo);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "No hay empleados disponibles, el proyecto quedara pendiente");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"No hay tarea seleccionada");
                }
            }

        });
        asignarEmpleadoEficiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (seleccionValida()) {
                    String titulo = tareas.getSelectedItem().toString();
                    try {
                        panelManager.sistema().asignarResponsableMenosRetraso(panelManager.consultarSeleccionado(), titulo);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "No hay empleados disponibles, el proyecto quedara pendiente");
                    }
                }
                else{
                        JOptionPane.showMessageDialog(null,"No hay tarea seleccionada");
                }
            }
        });
        reasignarEmpleado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(seleccionValida()) {
                    String titulo = tareas.getSelectedItem().toString();
                    Object[] datos = panelManager.sistema().empleadosNoAsignados();
                    JComboBox empleados = new JComboBox();
                    empleados.setModel(new DefaultComboBoxModel(datos));
                    JPanel panel = new JPanel();
                    panel.add(new JLabel("Seleccione un empleado:"));
                    panel.add(empleados);
                    int resultado = JOptionPane.showOptionDialog(
                            null,
                            panel,
                            "Empleados no asignados",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            null
                    );
                    if (resultado == JOptionPane.OK_OPTION) {
                        try{
                        Integer legajo = Integer.parseInt(empleados.getSelectedItem().toString());
                        panelManager.sistema().reasignarEmpleadoEnProyecto(panelManager.consultarSeleccionado(), legajo, titulo);
                        }
                        catch (Exception exception ){
                            JOptionPane.showMessageDialog(null, "No hay empleado asignado anterioremente");
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"No hay tarea seleccionada");
                }
            }
        });
        reasignarEmpleadoEficiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(seleccionValida()) {
                    String titulo = tareas.getSelectedItem().toString();
                    try {
                        panelManager.sistema().reasignarEmpleadoConMenosRetraso(panelManager.consultarSeleccionado(), titulo);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "No hay empleados disponibles, el proyecto quedara pendiente");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"No hay tarea seleccionada");
                }
            }
        });
        establecerComoFinalizada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (seleccionValida()) {
                    String titulo = tareas.getSelectedItem().toString();
                    try {
                        panelManager.sistema().finalizarTarea(panelManager.consultarSeleccionado(), titulo);
                    }
                    catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(null,"La tarea ya estaba finalizada");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"No hay tarea seleccionada");
                }
            }
        });
        registrarRetrasoEnTarea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(seleccionValida()){
                    String titulo = tareas.getSelectedItem().toString();
                    String valor=JOptionPane.showInputDialog("Ingresar la cantidad de dias de retraso");
                    try {
                        double dias=Double.parseDouble(valor);
                        panelManager.sistema().registrarRetrasoEnTarea(panelManager.consultarSeleccionado(), titulo, dias);
                    }
                    catch (IllegalArgumentException | NullPointerException exception){
                        JOptionPane.showMessageDialog(null, "Los valores ingresados no son validos");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"No hay tarea seleccionada");
                }
            }
        });
        agregarTarea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FormularioTarea nuevaTarea=new FormularioTarea(null);
                nuevaTarea.setVisible(true);
                String titulo=nuevaTarea.getTitulo();
                String descripcion=nuevaTarea.getDescripcion();
                String dias=nuevaTarea.getDias();
                try{
                    panelManager.sistema().agregarTareaEnProyecto(panelManager.consultarSeleccionado(),
                            titulo,descripcion,Double.parseDouble(dias));
                    tareas.addItem(titulo);
                }
                catch(IllegalArgumentException | NullPointerException exception){
                    JOptionPane.showMessageDialog(null,"Los valores no son validos");
                }
            }
        });
        costoActualProyecto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double costo=panelManager.sistema().costoProyecto(panelManager.consultarSeleccionado());
                JOptionPane.showMessageDialog(null, "El costo actual del proyecto es: " + costo);
            }
        });
        proyectoFinalizado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FormularioProyectoFinalizado formularioProyectoFinalizado=new FormularioProyectoFinalizado(null);
                formularioProyectoFinalizado.setVisible(true);
                String fecha=formularioProyectoFinalizado.getFecha();
                try{
                    panelManager.sistema().finalizarProyecto(panelManager.consultarSeleccionado(), fecha);
                }
                catch(IllegalArgumentException exception){
                    JOptionPane.showMessageDialog(null,"Los valores no son validos");
                }
            }
        });
        empleadosAsignados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DefaultTableModel modelTabla = new DefaultTableModel();
                JTable tablaEmpleados = new JTable(modelTabla);
                JScrollPane scrollPane = new JScrollPane(tablaEmpleados);
                modelTabla.addColumn("Legajo");
                modelTabla.addColumn("Nombre");
                List<Tupla<Integer,String>> empleados=panelManager.sistema().empleadosAsignadosAProyecto(panelManager.consultarSeleccionado());
                for(Tupla<Integer,String> t:empleados) {
                    Object[] nuevaFila = {t.getValor1(), t.getValor2()};
                    modelTabla.addRow(nuevaFila);
                }
                JPanel panel = new JPanel();
                panel.add(new JLabel("Empleados Asignados"));
                panel.add(scrollPane);
                int resultado = JOptionPane.showOptionDialog(
                        null,
                        panel,
                        "Empleados Asignados",
                        JOptionPane.PLAIN_MESSAGE,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null
                );
            }
        });
        datosProyecto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Integer numero=panelManager.consultarSeleccionado();
                String infoProyecto=panelManager.sistema().consultarProyecto(numero);
                InformacionProyecto informacionProyecto=new InformacionProyecto(null,numero,infoProyecto);
                informacionProyecto.setVisible(true);
            }
        });
        setLayout(new BorderLayout());
        add(gestionProyecto,BorderLayout.CENTER);
    }
    private void armarPantalla(DefaultTableModel modelTabla)
    {   GridBagConstraints gbc = new GridBagConstraints();

        labelTitulo=new JLabel("Proyecto: " + panelManager.consultarSeleccionado() +
                      " " + panelManager.sistema().consultarDomicilioProyecto(panelManager.consultarSeleccionado()));
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        itemTareas=new JLabel("Gestion Tareas");
        itemTareas.setFont(new Font("Arial", Font.BOLD, 20));
        labelTarea =new JLabel("Tareas:");
        labelTarea.setFont(new Font("Arial", Font.BOLD, 18));
        tareas=new JComboBox<>();
        labelMensajeTarea=new JLabel("Seleccionar una tarea");
        labelMensajeTarea.setFont(new Font("Arial", Font.BOLD, 16));
        itemProyecto=new JLabel("Gestión Proyecto");
        itemProyecto.setFont(new Font("Arial", Font.BOLD, 20));

        asignarEmpleado=new JButton("Asignar empleado");
        asignarEmpleadoEficiente=new JButton("Asignar empleado con menos retrasos");
        registrarRetrasoEnTarea=new JButton("Regristrar retraso en tarea");
        establecerComoFinalizada=new JButton("Establecer tarea como finalizada");
        reasignarEmpleado=new JButton("Cambiar empleado reponsable");
        reasignarEmpleadoEficiente=new JButton("Cambiar empleado por el que tenga menos retrasos");
        agregarTarea=new JButton("Agregar una tarea");
        proyectoFinalizado=new JButton("Establecer como finalizado");
        costoActualProyecto=new JButton("Consultar costo actual proyecto");
        empleadosAsignados=new JButton("Empleados asignados");
        datosProyecto=new JButton("Ver imformación del proyecto");
        volverPrincipal=new JButton("Volver al menu");
        gestionProyecto=new JPanel();
        gestionProyecto.setLayout(new GridBagLayout());
//--------------------Diseño------------------------------------------
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 4, 10, 4);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gestionProyecto.add(labelTitulo, gbc);
        gbc.gridy = 1;
        gestionProyecto.add(itemTareas, gbc);
        gbc.gridy = 2;
        gestionProyecto.add(labelTarea, gbc);
        gbc.gridx = 1;
        gestionProyecto.add(tareas, gbc);
        gbc.gridx = 2;
        gestionProyecto.add(labelMensajeTarea, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gestionProyecto.add(asignarEmpleado, gbc);
        gbc.gridx = 2;
        gestionProyecto.add(asignarEmpleadoEficiente, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gestionProyecto.add(reasignarEmpleado, gbc);
        gbc.gridx = 2;
        gestionProyecto.add(reasignarEmpleadoEficiente, gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gestionProyecto.add(registrarRetrasoEnTarea, gbc);
        gbc.gridx = 2;
        gestionProyecto.add(establecerComoFinalizada, gbc);
        gbc.gridx = 3;
        gestionProyecto.add(agregarTarea, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gestionProyecto.add(itemProyecto, gbc);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gestionProyecto.add(costoActualProyecto, gbc);
        gbc.gridx = 2;
        gestionProyecto.add(proyectoFinalizado, gbc);
        gbc.gridx = 1;
        gbc.gridy = 8;
        gestionProyecto.add(empleadosAsignados, gbc);
        gbc.gridx=2;
        gestionProyecto.add(datosProyecto,gbc);
        gbc.gridx = 4;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.ipady=20;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gestionProyecto.add(volverPrincipal, gbc);
        deshabilitarBotones();
    }
    private boolean seleccionValida(){
        return tareas.getSelectedIndex()!=-1;
    }
    private void deshabilitarBotones(){
        if(panelManager.sistema().estaFinalizado(panelManager.consultarSeleccionado())){
            registrarRetrasoEnTarea.setEnabled(false);
            proyectoFinalizado.setEnabled(false);
            reasignarEmpleado.setEnabled(false);
            reasignarEmpleadoEficiente.setEnabled(false);
            establecerComoFinalizada.setEnabled(false);
        }

    }


}
