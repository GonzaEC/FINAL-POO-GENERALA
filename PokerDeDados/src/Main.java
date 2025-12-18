import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.vista.IVista;
import com.edu.unlu.generala.vista.InterfazConsola;
import com.edu.unlu.generala.vista.InterfazGrafica;

public class Main {
    public static void main(String[] args) {
        ControladorGenerala controlador = new ControladorGenerala();
        IVista grafica = new InterfazGrafica( controlador);
        controlador.setVista(grafica);
        grafica.iniciar();



    }
}