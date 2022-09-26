package umu.tds.controlador;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import umu.tds.dao.DAOException;
import umu.tds.dao.UsuarioDAO;
import umu.tds.dominio.Cancion;
import umu.tds.dominio.Usuario;

public class MusicPlayer {
	private MediaPlayer mediaPlayer;
	private Path mp3;
	private boolean isplaying;
	private String cancionRep;


	public MusicPlayer() {
        this.isplaying = false;
    }
	
	public void playCancion(Cancion c, Usuario usuarioActual,UsuarioDAO adaptadorUsuario) throws DAOException {
		if(this.isplaying) {
			if(this.cancionRep.equals(c.getTitulo())) {
				this.mediaPlayer.play();
				return;
			}
			else {
				this.stopCancion();
			}
		}
		try {
			com.sun.javafx.application.PlatformImpl.startup(()->{});
		} catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception: " + ex.getMessage());
		 }
		// reproducir una canción
		String url = c.getRutaFichero();
		URL uri = null;
		mp3 = null;
		try {
			uri = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		System.setProperty("java.io.tmpdir", ".");
		try {
			mp3 = Files.createTempFile("now-playing", ".mp3");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(mp3.getFileName());
		try (InputStream stream = uri.openStream()) {
			Files.copy(stream, mp3, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("finished-copy: " + mp3.getFileName());
		Media media = new Media(mp3.toFile().toURI().toString());
		this.mediaPlayer = new MediaPlayer(media);
		this.mediaPlayer.play();
		this.isplaying = true;
		this.cancionRep = c.getTitulo();
		usuarioActual.addRecientes(c);
		adaptadorUsuario.update(usuarioActual);
		
	}
	
	public void stopCancion() {
		 if (this.isplaying && mediaPlayer != null) {
             this.mediaPlayer.stop();
             this.mp3.toFile().delete();
             this.isplaying = false;
         }
	}
	
	public void pauseCancion() {
		if(this.isplaying && this.mediaPlayer!=null) {
			this.mediaPlayer.pause();
		}
	}
	
	
}
