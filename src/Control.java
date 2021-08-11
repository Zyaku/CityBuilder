import javafx.scene.Scene;


public class Control {

    private Model model;
    private View view;
    private Scene scene;

    // Scene Coordinates
    private double mouseScreenPositionX;
    private double mouseScreenPositionY;
    private double xEntered, yEntered;
    private double xExited, yExited;

    // Playing field Coordinates
    private double relativeScreenX;
    private double relativeScreenY;
    private int realX;
    private int realY;

    // Boundary Check
    private boolean inBounds;
    private Entities placeAble;

    // Canvas Offsets
    private double screenOffSetX;
    private double screenOffSetY;
    private double zoomFactor;




    private enum Entities {
        NULL,ONE,TWO,THREE
    }

    public Control (Model model, View view){
        this.model = model;
        this.view = view;
        this.scene = view.getScene();
        this.placeAble = Entities.NULL;
        this.screenOffSetX = 0;
        this.screenOffSetY = 0;
        this.zoomFactor = 0.02;

        initialize();
    }

    public void initialize(){
        setDrag();
        setClick();
        setZoom();
        setHover();
        setKeys();
    }

    public void update (){

        model.update();
        //view.update(this.screenOffSetX, this.screenOffSetY);


    }

    public void setHover(){

        this.scene.setOnMouseMoved( hover ->{

            this.mouseScreenPositionX = hover.getSceneX();
            this.mouseScreenPositionY = hover.getSceneY();

            this.relativeScreenX = (-(this.view.getX() - this.mouseScreenPositionX) * (1/view.getZoom()) + this.screenOffSetX);    // mouse position on playing field relative to zooming and panning
            this.relativeScreenY = (-(this.view.getY() - this.mouseScreenPositionY) * (1/view.getZoom()) + this.screenOffSetY);

            this.realX = (int) (((relativeScreenX / ( model.getTileWidth()/2 ) ) + ( relativeScreenY /( model.getTileHeight()/2))) /2);     // mouse position on grid
            this.realY = (int) (((relativeScreenY / ( model.getTileHeight()/2) ) - ( relativeScreenX /( model.getTileWidth()/2 ))) /2);
//            System.out.println("X: " + Formating.round(relativeScreenX,2) + ", Y: " + Formating.round(relativeScreenY,2));

//            System.out.println("X: " + mouseScreenPositionX + ", Y: " + mouseScreenPositionY);

            this.inBounds = realX >= 0 && realX < model.getWidth() && realY >= 0 && realY < model.getHeight();

            if (inBounds) {
                view.updateHighlight(this.realX , this.realY);
            }
        });

    }


    public void setZoom(){

        this.scene.setOnScroll( scroll -> {
            if (scroll.getDeltaY() > 0 && view.getZoom() < 1.8) {
                for ( double i = this.zoomFactor / 5; i < this.zoomFactor ; i += this.zoomFactor /5 ) {         // zooming in. Incrementing in smaller steps for a smoother transition (optional)

                    view.setZoom(i);                                                                            // Setting zoom first is important. Rest of the calculation depends on it.
                    this.screenOffSetX = ((1 - (1 / view.getZoom())) * this.scene.getWidth() / 2);
                    this.screenOffSetY = ((1 - (1 / view.getZoom())) * this.scene.getHeight() / 2);

                    // align canvas to mouse cursor after zooming

                    double afterX = (-(this.view.getX() - this.mouseScreenPositionX) * (1 / view.getZoom()) + this.screenOffSetX);     // new relative mouse position
                    double afterY = (-(this.view.getY() - this.mouseScreenPositionY) * (1 / view.getZoom()) + this.screenOffSetY);

                    double offSetX = (afterX - this.relativeScreenX);                                                                   // difference to old relative mouse position
                    double offSetY = (afterY - this.relativeScreenY);

                    view.setCanvasPosition(offSetX, offSetY);                                                                           // moving the offset
                }

            }else if (scroll.getDeltaY() < 0 && view.getZoom() > 0.7 ){                                        // zooming out
                for ( double i = this.zoomFactor / 5; i < this.zoomFactor ; i += this.zoomFactor /5 ) {
                    view.setZoom(-i);
                    this.screenOffSetX = (((1 - (1 / view.getZoom())) * this.scene.getWidth() / 2));
                    this.screenOffSetY = (((1 - (1 / view.getZoom())) * this.scene.getHeight() / 2));

                    // align canvas to mouse cursor after zooming

                    double afterX = (-(this.view.getX() - this.mouseScreenPositionX) * (1 / view.getZoom()) + this.screenOffSetX);
                    double afterY = (-(this.view.getY() - this.mouseScreenPositionY) * (1 / view.getZoom()) + this.screenOffSetY);

                    double offSetX = (afterX - this.relativeScreenX);
                    double offSetY = (afterY - this.relativeScreenY);

                    view.setCanvasPosition(offSetX, offSetY);
                }

            }

        });

    }

    public void setDrag() {

        this.scene.setOnMousePressed( enter ->{
            this.xEntered = enter.getX() ;
            this.yEntered = enter.getY() ;
        });

        this.scene.setOnMouseDragged( drag -> {
            double dx = (drag.getX() - this.xEntered);
            double dy = (drag.getY() - this.yEntered);
            this.xEntered = this.xEntered + dx;
            this.yEntered = this.yEntered + dy;
            view.setCanvasPosition(dx,dy);
        });

    }

    public void setKeys(){
        this.scene.setOnKeyPressed( key -> {

            switch(key.getCode()){

                case DIGIT0:
                    this.placeAble = Entities.NULL;
                    break;
                case DIGIT1:
                    this.placeAble = Entities.ONE;
                    break;
                case DIGIT2:
                    this.placeAble = Entities.TWO;
                    break;
                case DIGIT3:
                    this.placeAble = Entities.THREE;
                    break;

            }
        });
    }

    public void setClick(){

        this.scene.setOnMouseClicked( click ->{

            switch(this.placeAble) {

                case NULL:
                    if (inBounds) {

                        System.out.println("X: " + this.realX + ", Y: " + this.realY);
//                        System.out.println("X: " + this.realX + ", Y: " + (int) Math.abs(this.realY - (model.getWidth() -1)) );       // actual coordinate // TODO : Retarded cheese tactic that would throw of models
                    }
                    break;

                case ONE:
                    if (inBounds) {
                        model.getTile(this.realX, this.realY).setTileType(model.getAssets().get("Iso-Wood-64C"));
                        view.updatePane(this.realX, this.realY);
                    }
                    break;

                case TWO:
                    if (inBounds) {
                        model.getTile(this.realX, this.realY).setTileType(model.getAssets().get("Iso-Street-NS-64C"));
                        view.updatePane(this.realX, this.realY);
                    }
                    break;

                case THREE:
                    if (inBounds) {
                        model.getTile(this.realX, this.realY).setTileType(model.getAssets().get("Iso-Basic-64C"));
                        view.updatePane(this.realX, this.realY);
                    }
                    break;
            }
        });

    }

}
