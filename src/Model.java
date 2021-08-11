import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.File;
import java.util.HashMap;

public class Model {

    private double width ;
    private double height;
    private double tileWidth ;
    private double tileHeight ;
    private Tile [][] playingfield ;
    private HashMap <String, Image> assets ;
    private String assetFolder;

    public Model(double width, double height, double tileWidth, double tileHeight) {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.playingfield = new Tile [(int)width][ (int)height];
        this.assets = new HashMap <String, Image>();
        this.assetFolder = ".//Assets";
        initialize();
    }

    public void initialize(){
        readTiles();
        setUpTiles();
    }

    public void readTiles(){
        File folder = new File(this.assetFolder);

        for (File file : folder.listFiles()){

            Image image = new Image (file.toURI().toString());
            this.assets.put(file.getName().substring(0, file.getName().indexOf(".")), image);

        }
    }

    public void setUpTiles(){
        for ( int y = 0; y < this.height; y++){
            for(int x = 0; x < this.width; x++){

                this.playingfield[x][y] = new Tile(x,y,this.tileWidth,this.tileHeight);
                this.playingfield[x][y].setTileType(this.assets.get("Iso-Basic-64C"));

            }
        }

//        this.playingfield[5][5].setTileType(this.assets.get("Iso-Tree-64"));
    }

    public void update() {

    }


    public HashMap<String, Image> getAssets() {
        return assets;
    }

    public double getTileWidth() {
        return tileWidth;
    }

    public double getTileHeight() {
        return tileHeight;
    }

    public Tile[][] getPlayingfield() {
        return playingfield;
    }

    public Tile getTile(double x, double y){
        return playingfield[(int)x][(int)y];
    }

    public double getWidth(){
        return this.width;
    }

    public double getHeight(){
        return this.height;
    }

}
