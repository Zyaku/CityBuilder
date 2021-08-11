import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;




public class View {

    private Model model;
    private Stage stage;
    private Pane root;
    private Scene scene;
    private Tile[][] playingfield;

    private double screenWidth;
    private double screenHeight;
    private double tileWidth ;
    private double tileHeight ;


    private double x;
    private double y;
    private double zoom;
    private double oldZoom;

    private Image highlight;
    private Pane highlightPane;
    private Pane [][] fieldPanes;




    public View( Model model , Stage  primaryStage, double screenWidth, double screenHeight){


        this.model = model;
        this.tileWidth = model.getTileWidth();
        this.tileHeight = model.getTileHeight();
        this.stage = primaryStage;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.root = new Pane();
        this.scene = new Scene(root);
        this.stage.setWidth(this.screenWidth);
        this.stage.setHeight(this.screenHeight);
        this.stage.setScene(scene);

        this.x = 0;
        this.y = 0;
        this.zoom = 1;
        this.oldZoom = 1;

        initialize();
    }

    public void initialize(){
        this.playingfield = this.model.getPlayingfield();
        this.fieldPanes = new Pane [(int) model.getWidth()][ (int) model.getHeight()];

        setup();
        setUpHighlight();

    }

    public void setup(){

        for (int y = 0; y < model.getHeight(); y++) {
            for (int x = 0; x < model.getWidth(); x++) {

                updatePane(x,y);

            }
        }
    }

    public void update( ) {



    }

    public void updatePane( int x , int y ){

        Tile tile = model.getTile(x, y);
        // Isometric calculation

        double isoX = getIsoX(tile.getX(), tile.getY());              // missing zoom factor ?
        double isoY = getIsoY(tile.getX() ,tile.getY());


        Image image = this.playingfield[x][y].getImage();
        Pane pane = new Pane(new ImageView(image));
        pane.setTranslateX(isoX - (tileWidth /2) );
        pane.setTranslateY(isoY - (tileHeight) );
        if (this.fieldPanes[x][y] == null) {
            this.fieldPanes[x][y] = pane;
            this.root.getChildren().add(pane);
        }else{
            this.root.getChildren().remove(this.fieldPanes[x][y]);
            this.fieldPanes[x][y] = pane;
            this.root.getChildren().add(pane);

        }



    }



    public void setUpHighlight(){
        this.highlight = model.getAssets().get("Iso-Highlight");
        this.highlightPane = new Pane(new ImageView(highlight));
        this.root.getChildren().add(highlightPane);
    }

    public void updateHighlight(int x , int y){

        double isoX = getIsoX(x,y);
        double isoY = getIsoY(x,y);

        this.highlightPane.setTranslateX(isoX - (tileWidth /2));
        this.highlightPane.setTranslateY(isoY - (tileHeight) );

    }



    public double getIsoX( double x , double y ){
        return  ( ( x - y) * (this.tileWidth /2));
    }

    public double getIsoY( double x , double y ){
        return  ( ( x + y) * (this.tileHeight /2));
    }


    public void setCanvasPosition( double dx , double dy){
        this.x = this.x + dx;
        this.y = this.y + dy;
        this.root.setTranslateX(this.x);
        this.root.setTranslateY(this.y);


    }

    public void setZoom( double scroll){

        this.zoom = this.zoom + scroll < 0.7 ? 0.7 : Formating.round(this.zoom + scroll,2) > 1.8 ? 1.8 : Formating.round(this.zoom + scroll,2);

        if( Math.abs(this.oldZoom - this.zoom) > 0 ) {

            double dx = ((oldZoom - zoom) * this.scene.getWidth() / 2);
            double dy = ((oldZoom - zoom) * this.scene.getHeight() / 2);

            this.x = (this.x - dx);
            this.y = (this.y - dy);
            this.root.setTranslateX(this.x);
            this.root.setTranslateY(this.y);
            this.oldZoom = this.zoom;

        }

        this.root.setScaleX(this.zoom);
        this.root.setScaleY(this.zoom);

    }


    public Scene getScene(){
        return this.scene;
    }

    public double getX (){
        return this.x;
    }

    public double getY (){
        return this.y;
    }

    public double getZoom(){
        return  this.zoom;
    }


}
