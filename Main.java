import java.util.ArrayList;
import javax.swing.*;

public class Main
{
    //Creamos los Arraylist de los productos
    ArrayList<ProductoElectronico> productosElectronicos;
    ArrayList<ProductoAlimenticio> productosAlimenticios;
    ArrayList<Producto> registro;

    //Constructor
    public Main(ArrayList<ProductoElectronico> productosElectronicos, ArrayList<ProductoAlimenticio> productosAlimenticios, ArrayList<Producto> registro)
    {
        this.productosElectronicos=productosElectronicos;
        this.productosAlimenticios=productosAlimenticios;
        this.registro=registro;
    }
    //Imprimimos ambos arreglos de productos:
    public void verProductos()
    {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Productos Electrónicos:\n");
        for (ProductoElectronico productoElectronico : productosElectronicos)
        {
            mensaje.append(productoElectronico.verInfo()).append("\n");
        }
        mensaje.append("\nProductos Alimenticios:\n");
        for (ProductoAlimenticio productoAlimenticio : productosAlimenticios)
        {
            mensaje.append(productoAlimenticio.verInfo()).append("\n");
        }
        JOptionPane.showMessageDialog(null, mensaje.toString(), "Lista de Productos", JOptionPane.INFORMATION_MESSAGE);
    }
    //Vender productos al usuario
    public void venderProductos(int tipo, String nombre, int cantidad)
    {
        //Vendemos el producto en base al tipo
        if(tipo==1)
        {
            for (ProductoElectronico productoElectronico : productosElectronicos)
            {
                if (productoElectronico.nombre.equals(nombre))
                {
                    anadirRegistro(nombre, cantidad);
                    productoElectronico.venderProducto(cantidad);
                }
            }
        }
        else if (tipo==2)
        {
            for (ProductoAlimenticio productoAlimenticio : productosAlimenticios)
            {
                if (productoAlimenticio.nombre.equals(nombre))
                {
                    anadirRegistro(nombre, cantidad);
                    productoAlimenticio.venderProducto(cantidad);
                }
            }
        }
    }
    //Añadimos productos en base a lo escogido
    public void anadirProductos(int tipo) {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del nuevo producto:");
        String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad:");
        int cantidad = Integer.parseInt(cantidadStr);

        boolean encontrado = false;

        // Verifica si el producto ya existe en productos alimenticios
        for (ProductoAlimenticio productoAlimenticio : productosAlimenticios) {
            if (productoAlimenticio.nombre.equals(nombre)) {
                productoAlimenticio.cantidad += cantidad;
                encontrado = true;
                break;
            }
        }

        // Verifica si el producto ya existe en productos electrónicos
        for (ProductoElectronico productoElectronico : productosElectronicos) {
            if (productoElectronico.nombre.equals(nombre)) {
                productoElectronico.cantidad += cantidad;
                encontrado = true;
                break;
            }
        }

        // Si no lo encuentra, añade un nuevo producto
        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "Parece que tenemos un producto nuevo, se añadirá a la lista.");
            if (tipo == 1) {  // Electrónico
                ProductoElectronico producto = new ProductoElectronico(nombre, cantidad);
                productosElectronicos.add(producto);
            } else if (tipo == 2) {  // Alimenticio
                ProductoAlimenticio producto = new ProductoAlimenticio(nombre, cantidad);
                productosAlimenticios.add(producto);
            }
            JOptionPane.showMessageDialog(null, "Producto añadido exitosamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Producto actualizado exitosamente.");
        }
    }
    //Añade elementos a nuestro registro
    public void anadirRegistro(String nombre, int cantidad)
    {
        Producto producto = new Producto(nombre, cantidad);
        registro.add(producto);
    }
    //Nos muestra los distintos registros
    public void imprimirRegistro()
    {
        StringBuilder mensaje = new StringBuilder();
        for (Producto producto : registro)
        {
            mensaje.append("Nombre: ").append(producto.nombre).append("\n");
            mensaje.append("Cantidad vendida: ").append(producto.cantidad).append("\n");
            mensaje.append("Precio total: ").append(producto.cantidad * producto.precioBase).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, mensaje.toString(), "Registro de Ventas", JOptionPane.INFORMATION_MESSAGE);
    }
    //Función para darle una interfaz al usuario
    public void iniciarInterfaz() {
        int opcion;
        String[] opciones = {"Ver productos", "Comprar producto", "Imprimir registro de ventas", "Añadir nuevo producto", "Salir"};

        do {
            opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción:", "Menú",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

            switch (opcion) {
                case 0:
                    verProductos();
                    break;
                case 1:
                    String tipoStr = JOptionPane.showInputDialog("Ingrese el tipo de producto (1: Electrónico, 2: Alimenticio):");
                    int tipo = Integer.parseInt(tipoStr);
                    String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto a comprar:");
                    String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a comprar:");
                    int cantidad = Integer.parseInt(cantidadStr);
                    venderProductos(tipo, nombre, cantidad);
                    break;
                case 2:
                    imprimirRegistro();
                    break;
                case 3:
                    String tipoNuevoStr = JOptionPane.showInputDialog("Ingrese el tipo de producto (1: Electrónico, 2: Alimenticio):");
                    int tipoNuevo = Integer.parseInt(tipoNuevoStr);
                    anadirProductos(tipoNuevo);
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "Saliendo...");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida.");
            }
        } while (opcion != 4);
    }
    //Main
    public static void main(String[] args)
    {
        ArrayList<ProductoElectronico> productosElectronicos = new ArrayList<ProductoElectronico>();
        ArrayList<ProductoAlimenticio> productosAlimenticios = new ArrayList<ProductoAlimenticio>();
        ArrayList<Producto> registro = new ArrayList<Producto>();

        Main main = new Main(productosElectronicos, productosAlimenticios, registro);
        main.iniciarInterfaz();
    }
}