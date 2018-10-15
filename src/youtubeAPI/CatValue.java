package youtubeAPI;

/**
 * Created by ericmilton on 3/22/17.
 */
public class CatValue {
    private String categoryName;
    private int categoryValue;

    public CatValue(String categoryName, int categoryValue) {
        this.categoryName = categoryName;
        this.categoryValue = categoryValue;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryValue() {
        return categoryValue;
    }
}
