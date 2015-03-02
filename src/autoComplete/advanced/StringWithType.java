package autoComplete.advanced;


public class StringWithType {
	public static final String TEXT = MyColors.Text.name();
	public static final String ATTRIBUTE = MyColors.Attribute.name();
	public static final String ATTRIBUTEVALUE= MyColors.AttributeValue.name();
	public static final String TAG = MyColors.Tag.name();
	public static final String OTHER = MyColors.Other.name();
	
	
	private String string;
	private String type;
	
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public StringWithType(String string, String type) {
		super();
		this.string = string;
		this.type = type;
	}
	
	public int length(){
		return string.length();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringWithType other = (StringWithType) obj;
		if (string == null) {
			if (other.string != null)
				return false;
		} else if (!string.equals(other.string))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
	
	
	

}
