package umu.tds.dominio;

public class DescuentoJovenes implements Descuento{
	
	@Override
	public double calcDescuento(double precio) {
		return 0.70*precio;
	}
	
	public String getNombre() {
		return "Descuento Joven";
	}
}
