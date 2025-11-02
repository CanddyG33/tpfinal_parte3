package entidades;

import java.util.List;

/**
 * Interfaz que define las operaciones principales del sistema HomeSolution.
 * Permite gestionar empleados, proyectos y tareas, así como realizar consultas,
 * asignaciones y operaciones administrativas.
 */

/**
 * Modificación 20-10
 *
 * Metodos modificados
 * finalizarProyecto, se agrega un parametro y Excepcion
 * registrarRetrasoEnTarea se agrega el throws en el metodo
 * algunas Excepciones deben generarse tambien cuando el proyecto esta finalizado
 * Metodo Agregado
 *
 * tareasDeUnProyecto
 * consultarProyecto
 */

/**
 * Modificación 25-10
 * Se agregaron o cambiaron parametros
 * costoProyecto
 * tieneRetrasos
 */
public interface IHomeSolution {

    // ============================================================
    // REGISTRO DE EMPLEADOS
    // ============================================================

    /**
     * Registra un empleado con un nombre y un valor base por hora.     *
     * @param nombre Nombre del empleado.
     * @param valor Valor de trabajo del empleado.
     * @throws IllegalArgumentException Si el nombre es nulo o vacío, o el valor es negativo.
     */
    public void registrarEmpleado(String nombre, double valor) throws IllegalArgumentException;

    /**
     * Registra un empleado con nombre, valor y categoría.     *
     * @param nombre Nombre del empleado.
     * @param valor Valor de trabajo del empleado.
     * @param categoria Categoría del empleado (por ejemplo, "Junior", "Senior").
     * @throws IllegalArgumentException Si alguno de los parámetros es inválido.
     */
    public void registrarEmpleado(String nombre, double valor, String categoria) throws IllegalArgumentException;

    // ============================================================
    // REGISTRO Y GESTIÓN DE PROYECTOS
    // ============================================================

    /**
     * Registra un nuevo proyecto en el sistema.
     *
     * @param titulos Títulos de las tareas del proyecto.
     * @param descripcion Descripciones de cada tarea.
     * @param dias Días estimados de duración de cada tarea.
     * @param domicilio Domicilio donde se desarrollará el proyecto.
     * @param cliente Datos del cliente (nombre, mail, teléfono).
     * @param inicio Fecha de inicio del proyecto (formato YYYY-MM-DD).
     * @param fin Fecha de finalización estimada (formato YYYY-MM-DD).
     * @throws IllegalArgumentException Si los datos son inconsistentes o faltan.
     */
    public void registrarProyecto(String[] titulos, String[] descripcion, double[] dias,
                                  String domicilio, String[] cliente, String inicio, String fin)
            throws IllegalArgumentException;

    // ============================================================
    // ASIGNACIÓN Y GESTIÓN DE TAREAS
    // ============================================================

    /**
     * Asigna un empleado responsable a una tarea específica dentro de un proyecto.     *
     * @param numero Número o código del proyecto.
     * @param titulo Título de la tarea a asignar.
     * @throws Exception si intenta asignar a una tarea ya asignada o el proyecto esta finalizado
     */
    public void asignarResponsableEnTarea(Integer numero, String titulo) throws Exception;

    /**
     * Asigna a la tarea el empleado con menos retrasos acumulados.
     *
     * @param numero Número o código del proyecto.
     * @param titulo Título de la tarea.
     *@throws Exception si no hay empleados disponibles o la tarea ya fue asignada o el proyecto esta finalizado
     */
    public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception;

    /**
     * Registra un retraso en una tarea de un proyecto.
     * Un retraso modifica la fecha real de finalización.     *
     * @param numero Número o código del proyecto.
     * @param titulo Título de la tarea.
     * @param cantidadDias Días de retraso acumulados.
     * @throws IllegalArgumentException Si los valores son incorrectos.
     */
    public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias) throws IllegalArgumentException;

    /**
     * Agrega una nueva tarea a un proyecto existente, tener en cuenta que se modifica
     * la fecha de finalización tanto la real como la prevista.
     * @param numero Número o código del proyecto.
     * @param titulo Título de la nueva tarea.
     * @param descripcion Descripción de la tarea.
     * @param dias Días estimados de duración.
     * @throws IllegalArgumentException Si los valores son incorrectos o el proyecto ya esta finalizado.
     */
    public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias) throws  IllegalArgumentException;

    /**
     * Marca una tarea como finalizada.     *
     * @param numero Número o código del proyecto.
     * @param titulo Título de la tarea a finalizar.
     * @throws Exception Si la tarea ya estaba finalizada.
     */
    public void finalizarTarea(Integer numero, String titulo)
            throws Exception;

    /**
     * Marca un proyecto completo como finalizado.
     * @param numero Número o código del proyecto.
     * @param fin Fecha de inicio de finalización (formato YYYY-MM-DD).
     * @throws IllegalArgumentException si la fecha es incorrecta( anterior a la fecha de inicio)
     */
    public void finalizarProyecto(Integer numero, String fin) throws IllegalArgumentException;


    // ============================================================
    // REASIGNACIÓN DE EMPLEADOS
    // ============================================================

    /**
     * Reasigna un empleado a una tarea determinada dentro de un proyecto.
     * Libera al empleado anterior.
     * @param numero Número o código del proyecto.
     * @param legajo Legajo del empleado a reasignar.
     * @param titulo Título de la tarea.
     * @throw  Exception si no hay empleados disponibles o si no tiene asignado un empleado previamente
     */
    public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo) throws Exception;

    /**
     * Reasigna al empleado con menos retrasos acumulados a una tarea.
     * Libera al empleado anterior.
     * @param numero Número o código del proyecto.
     * @param titulo Título de la tarea.
     * @throw  Exception si no hay empleados disponibles o si no tiene asignado un empleado previamente
     */
    public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo)throws Exception;

    // ============================================================
    // CONSULTAS Y REPORTES
    // ============================================================

    /**
     *  Calcula el costo total del proyecto (activo, pendiente o finalizado).
     * @param numero Numero o código del proyecto
     * @return Costo total acumulado.
     */
    public double costoProyecto(Integer numero) ;

    /**
     * Devuelve una lista de proyectos finalizados (número y domicilio).
     * @return Lista de tuplas (número, domicilio).
     */
    public List<Tupla<Integer, String>> proyectosFinalizados();

    /**
     * Devuelve una lista de proyectos pendientes (aún no iniciados o incompletos).
     * @return Lista de tuplas (número, domicilio).
     */
    public List<Tupla<Integer, String>> proyectosPendientes();

    /**
     * Devuelve una lista de proyectos actualmente activos.
     * @return Lista de tuplas (número, domicilio).
     */
    public List<Tupla<Integer, String>> proyectosActivos();

    /**
     * Devuelve los empleados que no están asignados a ningún proyecto.
     * @return Arreglo de empleados no asignados (solo numero de legajo)
     */
    public Object[] empleadosNoAsignados();

    /**
     * Indica si un proyecto ya fue finalizado.
     * @param numero Número o código del proyecto.
     * @return true si está finalizado, false en caso contrario.
     */
    public boolean estaFinalizado(Integer numero);

    /**
     * Consulta la cantidad total de retrasos acumulados por un empleado.
     * @param legajo Legajo del empleado.
     * @return Cantidad de retrasos.
     */
    public int consultarCantidadRetrasosEmpleado(Integer legajo) ;

    /**
     * Devuelve los empleados asignados a un proyecto determinado.
     * @param numero Número o código del proyecto.
     * @return Lista de tuplas (legajo, nombre del empleado).
     */
    public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero);

    // ============================================================
    // NUEVOS REQUERIMIENTOS
    // ============================================================

    /**
     * Devuelve las tareas no asignadas de un proyecto.
     * @param numero Número o código del proyecto.
     * @return Arreglo de tareas sin asignar.
     */
    public Object[] tareasProyectoNoAsignadas(Integer numero) ;
    /**
     * Devuelve las tareas no asignadas de un proyecto.
     * @param numero Número o código del proyecto.
     * @return Arreglo de tareas de un proyecto.
     */
    public Object[] tareasDeUnProyecto(Integer numero);


    /**
     * Consulta el domicilio del proyecto.
     * @param numero Número o código del proyecto.
     * @return Dirección donde se realiza el proyecto.
     */
    public String consultarDomicilioProyecto(Integer numero) ;

    /**
     * Indica si un empleado tiene retrasos en tareas asignadas.
     * @param legajo Legajo del empleado.
     * @return true si tiene retrasos, false en caso contrario.
     */
    public boolean tieneRestrasos(Integer legajo) ;

    /**
     * Devuelve la lista de todos los empleados registrados.
     * @return Lista de tuplas (legajo, nombre del empleado).
     */
    public List<Tupla<Integer, String>> empleados();
    /**
     * Devuelve la informacion generada en el toString.
     * @numero numero de proyecto.
     */
    public String consultarProyecto(Integer numero);
}