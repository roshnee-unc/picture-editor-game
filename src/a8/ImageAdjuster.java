package a8;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings(value = {"serial" })
public class ImageAdjuster extends JPanel implements ChangeListener {

	private PictureView picture;
	private final Picture ORIGINAL_PICTURE;

	private JSlider blur_slider;
	private JSlider brightness_slider;
	private JSlider saturation_slider;


	public ImageAdjuster(Picture p) {

		picture = new PictureView(p.createObservable());

		setLayout(new BorderLayout());
		add(picture, BorderLayout.CENTER);

		JPanel slider_panel = new JPanel();
		slider_panel.setLayout(new GridLayout(3, 2));

		add(slider_panel, BorderLayout.SOUTH);

		JLabel blur_label = new JLabel("Blur: ");
		slider_panel.add(blur_label);

		blur_slider = new JSlider(JSlider.HORIZONTAL, 0, 5, 0);
		blur_slider.setMajorTickSpacing(1);
		blur_slider.setSnapToTicks(true);
		blur_slider.setPaintTicks(true);
		blur_slider.setPaintLabels(true);
		slider_panel.add(blur_slider);

		JLabel brightness_label = new JLabel("Brightness: ");
		slider_panel.add(brightness_label);

		brightness_slider = new JSlider(-100, 100, 0);
		brightness_slider.setMajorTickSpacing(25);
		brightness_slider.setPaintTicks(true);
		brightness_slider.setPaintLabels(true);
		slider_panel.add(brightness_slider);

		JLabel saturation_label = new JLabel("Saturation: ");
		slider_panel.add(saturation_label);

		saturation_slider = new JSlider(-100, 100, 0);
		saturation_slider.setMajorTickSpacing(25);
		saturation_slider.setPaintTicks(true);
		saturation_slider.setPaintLabels(true);
		slider_panel.add(saturation_slider);

		blur_slider.addChangeListener(this);
		brightness_slider.addChangeListener(this);
		saturation_slider.addChangeListener(this);


		ORIGINAL_PICTURE = new PictureImpl(picture.getWidth(), picture.getHeight());
		Pixel [] [] original_pixels = new Pixel [picture.getWidth()][picture.getHeight()];
		for (int i = 0; i < picture.getWidth(); i++) {
			for (int j = 0; j < picture.getHeight(); j++) {
				original_pixels[i][j] = picture.getPicture().getPixel(i, j);
				ORIGINAL_PICTURE.setPixel(i, j, original_pixels[i][j]);
			}
		}
	}


	//blurs, brightens, and saturates picture based on sliders
	//cumulative -- checks to see all values consistently
	//BLUR: averages RGB values of 2*blur pixels perimeter
	//BRIGHTNESS: lightens or darkens picture by factor determined by slider
	//100 returns white, -100 returns black
	//SATURATION: creates more intense color
	//-100 returns no color, 100 returns less blended more filtered color
	@Override
	public void stateChanged(ChangeEvent e) {

		int blur = blur_slider.getValue();
		double saturation = saturation_slider.getValue();
		double brightness = brightness_slider.getValue();
		for (int i = 0; i < ORIGINAL_PICTURE.getWidth(); i++) {
			for (int j = 0; j < ORIGINAL_PICTURE.getHeight(); j++) {
				Pixel p = ORIGINAL_PICTURE.getPixel(i, j);

				Pixel tempPixelBlur = p;
				
				
				double sum_red = 0;
				double sum_green = 0;
				double sum_blue = 0;

				int x_range_min = i - blur;
				if (x_range_min < 0) {
					x_range_min = 0;
				}

				int x_range_max = i + blur;
				if (x_range_max >= picture.getPicture().getWidth()) {
					x_range_max = picture.getPicture().getWidth() - 1;
				}

				int y_range_min = j - blur;
				if (y_range_min < 0) {
					y_range_min = 0;
				}

				int y_range_max = j + blur;
				if (y_range_max >= picture.getPicture().getHeight()) {
					y_range_max = picture.getPicture().getHeight() - 1;
				}

				for (int k = x_range_min; k <= x_range_max; k++) {
					for (int l = y_range_min; l <= y_range_max; l++) {
						sum_red += ORIGINAL_PICTURE.getPixel(k,l).getRed();
						sum_green += ORIGINAL_PICTURE.getPixel(k,l).getGreen();
						sum_blue += ORIGINAL_PICTURE.getPixel(k,l).getBlue();
					}
				}

				double num_pixels = Math.pow(2 * blur + 1, 2);
				tempPixelBlur = new ColorPixel(sum_red / num_pixels, 
						sum_green / num_pixels, 
						sum_blue / num_pixels);
				picture.getPicture().setPixel(i, j, tempPixelBlur);
				

				
				Pixel tempPixelBrightness;
				
				if (brightness == 0) {
					picture.getPicture().setPixel(i, j, tempPixelBlur);
					tempPixelBrightness = tempPixelBlur;
				} else if (brightness < 0) {
					Pixel blackPixel = new ColorPixel(0,0,0);
					double weight = Math.abs(brightness/100);
					tempPixelBrightness = new ColorPixel(blackPixel.getRed()*weight + 
							tempPixelBlur.getRed()*(1.0-weight),
							blackPixel.getGreen()*weight + tempPixelBlur.getGreen()*(1.0-weight),
							blackPixel.getBlue()*weight + tempPixelBlur.getBlue()*(1.0-weight));
					picture.getPicture().setPixel(i, j, tempPixelBrightness);
				} else  {
					Pixel whitePixel = new ColorPixel(1,1,1);
					double weight = brightness/100;
					tempPixelBrightness = new ColorPixel(whitePixel.getRed()*weight + 
							tempPixelBlur.getRed()*(1.0-weight),
							whitePixel.getGreen()*weight + tempPixelBlur.getGreen()*(1.0-weight),
							whitePixel.getBlue()*weight + tempPixelBlur.getBlue()*(1.0-weight));
					picture.getPicture().setPixel(i, j, tempPixelBrightness);
				}
				

				
				if (saturation == 0) {
					picture.getPicture().setPixel(i, j, tempPixelBrightness);
					
				} else if (tempPixelBrightness.getIntensity() == 1) {
					picture.getPicture().setPixel(i, j, new ColorPixel(1,1,1));
				} else if (saturation < 0) {
					double new_red = tempPixelBrightness.getRed() * 
							(1.0 + (saturation / 100.0) ) 
							- (tempPixelBrightness.getIntensity() * saturation / 100.0);
					double new_blue = tempPixelBrightness.getBlue() * 
							(1.0 + (saturation / 100.0) ) 
							- (tempPixelBrightness.getIntensity() * saturation / 100.0);
					double new_green = tempPixelBrightness.getGreen() * 
							(1.0 + (saturation / 100.0) ) 
							- (tempPixelBrightness.getIntensity() * saturation / 100.0);
					picture.getPicture().setPixel(i, j, new ColorPixel(new_red, new_green, new_blue));
				} else {
					double max_value;
					if (tempPixelBrightness.getRed() >= tempPixelBrightness.getBlue() && 
							tempPixelBrightness.getRed() >= tempPixelBrightness.getGreen())
						max_value = tempPixelBrightness.getRed();
					else if (tempPixelBrightness.getBlue() >= tempPixelBrightness.getRed() && 
							tempPixelBrightness.getBlue() >= tempPixelBrightness.getGreen())
						max_value = tempPixelBrightness.getBlue();
					else 
						max_value = tempPixelBrightness.getGreen();
					double new_red = tempPixelBrightness.getRed() * 
							((max_value + ((1.0 - max_value) * (saturation / 100.0))) / max_value);
					double new_green = tempPixelBrightness.getGreen() * 
							((max_value + ((1.0 - max_value) * (saturation / 100.0))) / max_value);
					double new_blue = tempPixelBrightness.getBlue() * 
							((max_value + ((1.0 - max_value) * (saturation / 100.0))) / max_value);
					picture.getPicture().setPixel(i, j, new ColorPixel(new_red, new_green, new_blue));
				}

			}
		}
		
		
		
	}
	
	

}



