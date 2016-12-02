package a8;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings(value = {"serial" })
public class PixelInspector extends JPanel implements MouseListener {
	
	private PictureView picture;
	
	private JPanel west;
	
	private JLabel x_label;
	private JLabel y_label;
	private JLabel red_label;
	private JLabel green_label;
	private JLabel blue_label;
	private JLabel brightness_label;
	
	
	public PixelInspector(Picture p) {
		setLayout(new BorderLayout());
		
		picture = new PictureView(p.createObservable());
		picture.addMouseListener(this);
		add(picture, BorderLayout.CENTER);
		
		
		west = new JPanel();
		add(west, BorderLayout.WEST);
		
		west.setLayout(new GridLayout(6, 1));
		
		x_label = new JLabel("X: ");
		y_label = new JLabel("Y: ");
		red_label = new JLabel("Red: ");
		green_label = new JLabel("Green: ");
		blue_label = new JLabel("Blue: ");
		brightness_label = new JLabel("Brightness: ");
		
		west.add(x_label);
		west.add(y_label);
		west.add(red_label);
		west.add(green_label);
		west.add(blue_label);
		west.add(brightness_label);
		
	
		
	}

	//shows RGB and intensity values of pixel clicked, along with x,y coordinates
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		Pixel p = picture.getPicture().getPixel(x, y);
		
		x_label.setText("X: " + x);
		y_label.setText("Y: " + y);
		red_label.setText("Red: " + Math.round(p.getRed()*100.00)/100.00);
		green_label.setText("Green: " + Math.round(p.getGreen()*100.00)/100.00);
		blue_label.setText("Blue: " + Math.round(p.getBlue()*100.00)/100.00);
		brightness_label.setText("Brightness: " + Math.round(p.getIntensity()*100.00)/100.00);

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	

}
