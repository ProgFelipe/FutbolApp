package example.futbolapp.View;

/**
 * Created by Felipe on 06/11/2014.
 */
public class Item {

    private String title;
    private String description;

    public Item(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }
    // getters and setters...
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
}