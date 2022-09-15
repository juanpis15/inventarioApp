package modelo;



import controlador.ControladorProducto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import vista.Interfaz;

@SpringBootApplication
public class InventarioAppApplication {
        @Autowired
        RepositorioProducto rep;
        
	public static void main(String[] args) {
		//SpringApplication.run(InventarioAppApplication.class, args);
                SpringApplicationBuilder builder = new SpringApplicationBuilder(InventarioAppApplication.class);
                builder.headless(false);
                ConfigurableApplicationContext context = builder.run(args);
	}
        
        @Bean
        ApplicationRunner applicationRunner(){
            return args -> {
                final Log logger = LogFactory.getLog(getClass());
                Interfaz faz = new Interfaz();
                faz.setVisible(true);
                ControladorProducto controlador = new ControladorProducto(rep, faz);
                faz.setControlador(controlador);
                controlador.setListaProductos(controlador.obtenerProductos());
                controlador.inicializaTabla();
                
                System.out.println(controlador.obtenerProductos());
                
            };
        }

}
