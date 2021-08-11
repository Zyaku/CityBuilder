import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile {

    private double x;
    private double y;
    private double tileWidth;
    private double tileHeight;
    private Image image;

    public Tile(double x, double y, double tileWidth, double tileHeight){

        this.x = x ;
        this.y = y ;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }




    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTileHeight() {
        return tileHeight;
    }

    public double getTileWidth() {
        return tileWidth;
    }

    public Image getImage (){
        return this.image;
    }

    public void setTileType(Image image){
        this.image = image;
    }

}
