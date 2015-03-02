package searchComponent.impl;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.Timer;

public class ColorFader {
	
	private static final int refreshRate=33;
	JComponent comp;
	Timer timer;
	Color color;
	float[] currentColor = new float[3];
	float ds;

	
	public ColorFader(JComponent comp, long delayDuration, Color color) {
		this.comp = comp;
		this.color = color;
		getHSBColor(color);
		ds = currentColor[1]/(delayDuration/refreshRate);
		timer = new Timer(refreshRate,new TimerActionListener());
	}
	
	private void getHSBColor (Color color){
		int r = color.getRed();
		int b = color.getBlue();
		int g = color.getGreen();
		Color.RGBtoHSB(r, g, b, currentColor);
		Color.HSBtoRGB(currentColor[0], currentColor[1], currentColor[2]);
	}
	
	public void startFading(){
		getHSBColor(color);
		timer.start();
	}
	
	
	
	class TimerActionListener implements ActionListener {
				
		@Override
		public void actionPerformed(ActionEvent ev) {
			currentColor[1] = Math.max(currentColor[1] - ds, 0f);
			Color color = new Color(Color.HSBtoRGB(currentColor[0], currentColor[1], currentColor[2]));
			comp.setBackground(color);
			if (currentColor[1]==0f)timer.stop();
		}
	}
	
}
