
package controlador;

import java.util.ArrayList;
import java.util.Optional;
import javax.swing.table.DefaultTableModel;
import modelo.Producto;
import modelo.RepositorioProducto;
import org.springframework.beans.factory.annotation.Autowired;
import vista.ActualizarV;
import vista.InformeV;
import vista.Interfaz;
import vista.MuestraError;




public class ControladorProducto {
    @Autowired
    RepositorioProducto rp;
    Interfaz faz;
    ArrayList<Producto>listaProductos;
    

    public ControladorProducto(RepositorioProducto rp, Interfaz faz) {
        this.rp = rp;
        this.faz = faz;
    }
    
    
    
    
    public void setListaProductos(ArrayList<Producto>listaProductos){
        this.listaProductos = listaProductos;
    }
    
    public Producto agregar(Producto p){
        return rp.save(p);
    }
    
    public Producto actualizar(Producto p){
        return rp.save(p);
    }
    
    public boolean eliminar(Integer id){
        try{
            rp.deleteById(id);
            return true;
        }catch(Exception ex){
            return false;
        }
    }
    
    public ArrayList<Producto> obtenerProductos(){
        return (ArrayList<Producto>) rp.findAll();
    }
    
    public Optional<Producto> obtenerProducto(Integer id){
        return rp.findById(id);
    }
    
    public double InventarioTotal(){
        double suma = 0;
        for(Producto p : listaProductos){
            suma+=p.getPrecio()* p.getInventario();
        }
        return suma;
    }
    
    public String precioMenor(){
        String nombre = listaProductos.get(0).getNombre();
        double precio = listaProductos.get(0).getPrecio();
        for(Producto p: listaProductos){
            if (p.getPrecio() < precio){
                nombre = p.getNombre();
                precio = p.getPrecio();
            }
        }
        return nombre;
    }
    
    public String precioMayor(){
        String nombre = listaProductos.get(0).getNombre();
        double precio = listaProductos.get(0).getPrecio();
        for(Producto p: listaProductos){
            if (p.getPrecio() > precio){
                nombre = p.getNombre();
                precio = p.getPrecio();
            }
        }
        return nombre;
    }
    
    public double promedio(){
        double suma = 0;
        for(Producto p: listaProductos){
            suma+=p.getPrecio();
        }
        return suma/listaProductos.size();
    }
    
    public void eventoAgregar(){
        String nombre = faz.getCampoNombre();
        String precio = faz.getCampoPrecio();
        String inventario = faz.getCampoInventario();
        if(!nombre.equals("") && !precio.equals("") && !inventario.equals("")){
            Producto nuevo = new Producto(nombre, Double.parseDouble(precio),Integer.parseInt(inventario));
            listaProductos.add(nuevo);
            agregar(nuevo);
            DefaultTableModel modelo = (DefaultTableModel) faz.getInventario().getModel();
            modelo.insertRow(listaProductos.size()-1, new Object[]{nuevo.getNombre(), nuevo.getPrecio(), nuevo.getInventario()});  
        }else{
            MuestraError me = new MuestraError();
            me.setVisible(true);
        }         
    }
    
    public void eventoEliminar(){
        int filaEliminar = faz.getInventario().getSelectedRow();
        listaProductos.remove(filaEliminar);
        eliminar(listaProductos.remove(filaEliminar).getCodigo());
        DefaultTableModel modelo = (DefaultTableModel) faz.getInventario().getModel();
        modelo.removeRow(filaEliminar);
    }
    
    public void abrirVentanaAct(){
        ActualizarV a = new ActualizarV();
        a.setControlador(this);
        a.setVisible(true);
    }
    
    public void eventoActualizar(ActualizarV v){
        String nombre = v.getCampoNombreAct();
        String precio = v.getCampoPrecioAct();
        String inventario = v.getCampoInventarioAct();
        if(!nombre.equals("") && !precio.equals("") && !inventario.equals("")){
            int filaActualizar = faz.getInventario().getSelectedRow();
            DefaultTableModel modelo = (DefaultTableModel) faz.getInventario().getModel();
            listaProductos.get(filaActualizar).setInventario(Integer.parseInt(inventario));
            listaProductos.get(filaActualizar).setNombre(nombre);
            listaProductos.get(filaActualizar).setPrecio(Double.parseDouble(precio));
            actualizar(listaProductos.get(filaActualizar));
            modelo.setValueAt(nombre, filaActualizar, 0);
            modelo.setValueAt((Double.parseDouble(precio)), filaActualizar, 1);
            modelo.setValueAt((Integer.parseInt(inventario)), filaActualizar, 2);
        }else{
            MuestraError me = new MuestraError();
            me.setVisible(true);
        }
    }
    
    public void eventoInforme(){
        InformeV i = new InformeV(); 
        i.setVisible(true);
        i.setMayor( i.getMayor() +  precioMayor());
        i.setMenor( i.getMenor() +  precioMenor());
        i.setPromedio( i.getPromedio() + promedio());
        i.setValorInv( i.getValorInv() + InventarioTotal());
       
    }
    
    public void inicializaTabla(){
        DefaultTableModel modelo = (DefaultTableModel) faz.getInventario().getModel();
        int indi = 0;
        for(Producto p: listaProductos){
            modelo.insertRow(indi, new Object[]{p.getNombre(), p.getPrecio(), p.getInventario()});
            indi+=1;
        }
        
    }
}

