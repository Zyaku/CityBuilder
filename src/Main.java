import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

        private static String title = "City Builder";

        private static double width = 40;
        private static double height = 40;

        private static double tileWidth = 64;
        private static double tileHeight = 32;

        private static double screenWidth = 900;
        private static double screenHeight = 600;


        public void start(Stage primaryStage) throws Exception {

            Model model = new Model(this.width, this.height, this.tileWidth, this.tileHeight );

            View view = new View(model,primaryStage, this.screenWidth, this.screenHeight);
            @SuppressWarnings("unused")
            Control control = new Control(model,view);

            AnimationTimer loop = new AnimationTimer(){																				// Animation Timer

                public void handle(long currentNanoTime){

                    control.update();

                }
            };

            loop.start();

            primaryStage.setTitle(title);
            primaryStage.show();

        }

        public static void main(String[] args) {
            launch(args);
        }


    }

