package com.heal.dominic.ResourceManager;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class SoundController {

	public static void playPopSound() {
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(SoundController.class.getResource("/sounds/pop.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			JOptionPane.showMessageDialog(null, "Error Loading Sound"
					, "pop.wav caused an Unsupported Audio File Exception",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error Loading Sound"
					, "pop.wav caused an IO Exception",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			JOptionPane.showMessageDialog(null, "Error Loading Sound"
					, "pop.wav caused an Line Unavailable Exception",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null, "Error Loading Sound"
					, "pop.wav caused an Unexpected Error",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	public static void playTickSound() {
		try {
			AudioInputStream audioInputStream;
			audioInputStream = AudioSystem
					.getAudioInputStream(SoundController.class.getResource("/sounds/blip1.wav"));
			final Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e){
			System.out.println(e);
		}
	}

	public static void playLoginSound() {
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(SoundController.class
					.getResource("/sounds/pad_confirm.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}

	public static void playDeleteSound() {
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem
					.getAudioInputStream(SoundController.class.getResource("/sounds/Delete.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		}
		
	}
	
	

}
