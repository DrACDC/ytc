package youtubeAPI;

/**
 * Created by ericmilton on 3/22/17.
 */
public class Category {
    private String primaryCategory;
    private String secondCategory;
    private String thirdCategory;
    private int primaryScore;
    private int secondScore;
    private int thirdScore;
    private int totalScore;

    Category(String primaryCategory, int primaryScore, int totalScore){
        this.primaryCategory = primaryCategory;
        this.primaryScore = primaryScore;
        secondCategory = "";
        secondScore = 0;
        thirdCategory = "";
        thirdScore = 0;
        this.totalScore = totalScore;
    }

    Category(String primaryCategory, int primaryScore, String secondaryCategory, int secondaryScore, int totalScore){
        this.primaryCategory = primaryCategory;
        this.primaryScore = primaryScore;
        this.secondCategory = secondaryCategory;
        this.secondScore = secondaryScore;
        thirdCategory = "";
        thirdScore = 0;
        this.totalScore = totalScore;
    }

    public Category(String primaryCategory, int primaryScore, String secondaryCategory,
                    int secondaryScore, String thirdCategory, int thirdScore, int totalScore) {
        this.primaryCategory = primaryCategory;
        this.secondCategory = secondaryCategory;
        this.thirdCategory = thirdCategory;
        this.primaryScore = primaryScore;
        this.secondScore = secondaryScore;
        this.thirdScore = thirdScore;
        this.totalScore = totalScore;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public String getSecondCategory() {
        return secondCategory;
    }

    public int getPrimaryScore() {
        return primaryScore;
    }

    public int getSecondScore() {
        return secondScore;
    }

    public String getThirdCategory() {
        return thirdCategory;
    }

    public int getThirdScore() {
        return thirdScore;
    }

    public void print(){
        System.out.println("Primary Category: " + primaryCategory + ", Score of " + primaryScore);
        if(secondScore > 0){
            System.out.println("Secondary Category: " + secondCategory + ", Score of " + secondScore);
            if(thirdScore > 0){
                System.out.println("Terciary Category: " + thirdCategory + ", Score of " + thirdScore);
            }
        }
        System.out.println("Total Possible Score: " + totalScore);
    }
}
