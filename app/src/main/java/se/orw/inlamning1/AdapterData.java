package se.orw.inlamning1;

/**
 * Created by Marcus Orwén on 2014-09-18.
 *
 * Describes an income or outcome.
 */
public class AdapterData {
    private String text = "";
    private int imageID = R.drawable.blank;

    /**
     * Sets the text of the income/outcome.
     *
     * @param text The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the text from the income/outcome.
     *
     * @return String text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the image id of the income/outcome.
     *
     * @param imageID int imageID
     */
    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    /**
     * Get the image id of the income/outcome.
     *
     * @return int imageID
     */
    public int getImageID() {
        return imageID;
    }
}