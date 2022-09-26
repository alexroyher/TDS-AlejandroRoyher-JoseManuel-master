package umu.tds.controlador;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import umu.tds.dominio.ListaCanciones;
import umu.tds.dominio.Usuario;

public class PDFGenerator {
	

	public boolean generatePDF(String path,Usuario usuarioActual) throws FileNotFoundException, DocumentException{
		FileOutputStream archivo;
		archivo = new FileOutputStream(path);
		Document documento = new Document();
		PdfWriter.getInstance(documento, archivo);
		documento.open();
		if (usuarioActual.getListasCanciones().isEmpty()) {
			documento.add(new Paragraph("No hay ninguna PlayList en la base de datos"));
			documento.close();
			return false;
		}
		for(ListaCanciones lista : usuarioActual.getListasCanciones()) {
			documento.add(new Paragraph("Canciones de la PlayList " + lista.getNombre()));
			documento.add(Chunk.NEWLINE);
			ArrayList<String> titulos = lista.getTitulos();
			ArrayList<String> interp = lista.getInterpretes();
			ArrayList<String> estilos = lista.getEstilos();
			for ( int i = 0; i < titulos.size();i++) {
				documento.add(new Paragraph("  -" + titulos.get(i) + " - " + interp.get(i) +  " - Estilo : " + estilos.get(i)));
				documento.add(Chunk.NEWLINE);
			}	
		}
		documento.close();
		return true;
	}
}
