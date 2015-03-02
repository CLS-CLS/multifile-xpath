package autoComplete.advanced;

import java.awt.Color;   

public enum MyColors {
	
	Text(Color.BLACK), Tag(Color.BLUE),Attribute(Color.RED), AttributeValue (Color.DARK_GRAY),Other(Color.LIGHT_GRAY);
		
	private Color color;

	MyColors(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
}
