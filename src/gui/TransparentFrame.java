/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 *
 * @author moh
 */
public class TransparentFrame extends JFrame {
 	private static final long serialVersionUID = -6124015747153365483L;

	public TransparentFrame() {

		// Make a PicturePanel and add it to the main JFrame area
		final PicturePanel p = new PicturePanel();
		add(p, BorderLayout.CENTER);

		// Now, add all three slides and set them up with the appropriate
		// values and call methods.
		// ------- alpha slider -------
		JSlider opacity = new JSlider(0,100);
		opacity.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider) e.getSource();
				p.setOpacity(s.getValue()/100f);
				p.repaint();
			}
		});
		
		// ------- red slider -------
		JSlider red = new JSlider(0,100);
		red.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider) e.getSource();
				p.setRed(s.getValue()/100);
				p.repaint();
			}
			});
		
		// ------- green slider -------
		JSlider green = new JSlider(0,100);
		green.addChangeListener(new ChangeListener() {

			
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider) e.getSource();
				p.setGreen(s.getValue()/100f);
				p.repaint();			}
		});
		
		// ------- blue slider -------
		JSlider blue = new JSlider(0,100);
		blue.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider) e.getSource();
				p.setBlue(s.getValue()/100f);
				p.repaint();
			}
		});
		
		// Now add them all to the panel andplace it at the bottom.
		JPanel sliders = new JPanel();
		sliders.setLayout(new BoxLayout(sliders, BoxLayout.Y_AXIS));
		add(sliders, BorderLayout.PAGE_END);
		
		sliders.add(opacity);
		sliders.add(red);
		sliders.add(green);
		sliders.add(blue);
		
		// initially, set all to 100%
		opacity.setValue(100);
		red.setValue(100);
		green.setValue(100);
		blue.setValue(100);
		
		// Standard JFrame things to do.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private class PicturePanel extends JPanel {

		private static final long serialVersionUID = -4848393552816821422L;

		// One image is changed, the other stays the same
		// the entire execution time.
		private BufferedImage resImage;
		private BufferedImage originalImage;
		
		public int x=0, y=0;

		// The first, second, and third are the RGB values,
		// but the fourth is the alpha layer. These values are 
		// what we are messing with today and are multiplied
		// against EACH pixel
		private float[] scales = { 1.0f, 1.0f, 1.0f, 1.0f };

		// These are offsets and default to 0. We will not change 
		// these values in this tutorial. However, for experimentation,
		// remember that these are added to EACH pixel.
		private float[] offsets = new float[4];

		// This is the Rescale filter.
		RescaleOp rescale;

		public PicturePanel() {
			// Load the image - again from my favorite website
			BufferedImage loadedImg = getImage("C:\\Users\\moh\\Desktop\\pic\\1.jpg");

			// Now, make a new BufferedImage that absolutely has an alpha layer
			resImage = new BufferedImage(loadedImg.getWidth(), loadedImg.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

			// Grab the graphics and paint the loaded image to it. 
			Graphics g = resImage.getGraphics();
			g.drawImage(loadedImg, 0, 0, null);
			
			// Save the original for purposes of resizing.
			originalImage = loadedImg;
			
			// Make this panel the normal size
			setPreferredSize(new Dimension(640, 480));
			
			// Set the image to fully opaque
			setOpacity(1.0f);
		}
		
		// Explained in detail in parts 2 and 3 - not going to mention it now. Do
		// note that we are painting to the resImage, not the panel...
		public void resizeImage() {
			Graphics g = resImage.getGraphics();			
			g.setColor(Color.WHITE);
			
			g.fillRect(0,0,resImage.getWidth(), resImage.getHeight());
			    
			// Scale it by width
			int scaledWidth = (int)((originalImage.getWidth() * getHeight()/
				originalImage.getHeight()));

			// If the image is not off the screen horizontally...
			if (scaledWidth < getWidth()) {
			    // Center the left and right destination x coordinates.
			    int leftOffset = getWidth()/2 - scaledWidth/2;
			    int rightOffset = getWidth()/2 + scaledWidth/2;
				
			    g.drawImage(originalImage, 
				    leftOffset, 0, rightOffset, getHeight(), 
				    0, 0, originalImage.getWidth(), originalImage.getHeight(), 
				    null);
			}

			// Otherwise, the image width is too much, even scaled
			// So we need to center it the other direction
			else {
			    int scaledHeight = (originalImage.getHeight()*getWidth())/
				originalImage.getWidth();
				
			    int topOffset = getHeight()/2 - scaledHeight/2;
			    int bottomOffset = getHeight()/2 + scaledHeight/2;
				
			    g.drawImage(originalImage,
				    0, topOffset, getWidth(), bottomOffset, 
				    0, 0, originalImage.getWidth(), originalImage.getHeight(), 
				    null);
			}
		}
		
		// These methods access the scales array and change the values,
		// See how we the arrays to the constructor for use? The null is
		// for the RenderingHints, which are not needed at this time. Just
		// look through the API if you have any questions.
		public void setOpacity(float o) {
			scales[3] = o;
			rescale = new RescaleOp(scales, offsets, null);
		}
		
		public void setRed(float r) {
			scales[0] = r;
			rescale = new RescaleOp(scales, offsets, null);
		}
		
		public void setGreen(float g) {
			scales[1] = g;
			rescale = new RescaleOp(scales, offsets, null);
		}
		
		public void setBlue(float B) {
			scales[2] = B;
			rescale = new RescaleOp(scales, offsets, null);
		}

		// Paints onto the panel
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// Make sure the image is the right size.
			resizeImage();
			
			// Using the Graphics2D class, you paint the image
			// with the RescaleOp class and the coordinates.
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(resImage, rescale, x, y);
			
		}

		// Discussed in the first part of my tutorial series.
		private BufferedImage getImage(String filename) {
			// This time, you can use an InputStream to load
			try {
				// Grab the URL for the image
				URL url = new URL(filename);

				// Then read it in.
				return ImageIO.read(url);
			} catch (IOException e) {
				System.out.println("The image was not loaded.");
				System.exit(1);
			}
			return null;
		}   
}
}
