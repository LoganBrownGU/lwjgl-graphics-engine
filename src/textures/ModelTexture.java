package textures;

public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	private boolean hasTransparency = false;
	private boolean isEmissive = false;
	
	public ModelTexture(int texture, boolean isEmissive){
		this.textureID = texture;
		this.isEmissive = isEmissive;
	}
	
	public int getID(){
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean hasTransparency() {
		return hasTransparency;
	}

	public void setTransparent(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public boolean isEmissive() {
		return isEmissive;
	}

	public void setEmissive(boolean emissive) {
		isEmissive = emissive;
	}
}
