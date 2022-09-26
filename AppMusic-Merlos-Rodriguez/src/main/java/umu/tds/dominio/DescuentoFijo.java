package umu.tds.dominio;

public class DescuentoFijo implements Descuento {
	
	@Override
	public double calcDescuento(double precio) {
		return 0.9*precio;
	}
	
	public String getNombre() {
		return "Descuento Fijo";
	}
}
