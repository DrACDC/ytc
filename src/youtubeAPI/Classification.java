package youtubeAPI;

public class Classification {
	
	private String classification;
	private ModuleType module;
	private String URL;
	
	public Classification(String classification, ModuleType module, String URL) {
		this.classification = classification;
		this.module = module;
		this.URL = URL;
	}
	
	public String getClassification() {
		return this.classification;
	}
	
	public ModuleType getModule() {
		return this.module;
	}
	
	public String getURL() {
		return this.URL;
	}
}
