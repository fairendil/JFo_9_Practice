package FindingCentralLocation;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.io.*;
import java.util.Properties;

public class FindingCentralLocation extends Application {
    public static Group root = new Group();


    public void start(Stage primaryStage) {

        Properties properties = new Properties();
        double width = 800.0D;
        double height = 650.0D;
        try {
            InputStream in = new FileInputStream(new File( "stageConfigFile.properties"));
            properties.load(in);
            width = Double.parseDouble(properties.getProperty("width"));
            height = Double.parseDouble(properties.getProperty("height"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, width, height, Color.WHITE);
        primaryStage.setTitle("Campus");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(windowEvent -> ClosingHandler(windowEvent) );
        primaryStage.show();
        Image background = new Image(this.getClass().getResourceAsStream("Images/Campus.png"));
        ImageView backgroundView = new ImageView(background);
        backgroundView.setPreserveRatio(true);
        backgroundView.setFitWidth(width);
        root.getChildren().add(backgroundView);
        backgroundView.toBack();
    }


    public static void main(String[] args) {

        Properties properties = new Properties();
        Dorm[] dorms = new Dorm[0];
        try {
            InputStream in = new FileInputStream(new File( "dormsConfig.properties"));
            properties.load(in);
            var dormCount = Integer.parseInt(properties.getProperty("dormCount"));
            if(dormCount > 0){
                dorms = new Dorm[dormCount];
                for (int i = 0; i < dormCount; ++i) {

                    properties = new Properties();

                    try {
                        in = new FileInputStream(new File( "dorm"+i+"Config.properties"));
                        properties.load(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    var x = Integer.parseInt(properties.getProperty("x"));
                    var y = Integer.parseInt(properties.getProperty("y"));
                    var population = Integer.parseInt(properties.getProperty("population"));
                    var name = properties.getProperty("name");
                    var red = Double.parseDouble(properties.getProperty("colorRed"));
                    var green = Double.parseDouble(properties.getProperty("colorGreen"));
                    var blue = Double.parseDouble(properties.getProperty("colorBlue"));
                    dorms[i] = new Dorm(x, y, population, name, Color.color(red, green, blue, 0.5D));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (dorms.length == 0 ){
            dorms = new Dorm[6];
            dorms[0] = new Dorm(50, 50, 50 + (int)(Math.random() * 350.0D), "Dorm 1", Color.rgb(0, 0, 255, 0.5D));
            dorms[1] = new Dorm(100, 50, 50 + (int)(Math.random() * 350.0D), "Dorm 2", Color.rgb(0, 255, 0, 0.5D));
            dorms[2] = new Dorm(150, 50, 50 + (int)(Math.random() * 350.0D), "Dorm 3", Color.rgb(255, 0, 0, 0.5D));
            dorms[3] = new Dorm(200, 50, 50 + (int)(Math.random() * 350.0D), "Dorm 4", Color.rgb(255, 255, 0, 0.5D));
            dorms[4] = new Dorm(250, 50, 50 + (int)(Math.random() * 350.0D), "Dorm 5", Color.rgb(255, 0, 255, 0.5D));
            dorms[5] = new Dorm(300, 50, 50 + (int)(Math.random() * 350.0D), "Dorm 6", Color.rgb(128, 64, 64, 0.5D));
        }

        new CenterPoint("All Dorms", true, dorms);
        new CenterPoint("Study Group", false, dorms[0], dorms[0], dorms[2], dorms[2],dorms[2], dorms[2], dorms[3]);
        launch(args);
    }

    public static void ClosingHandler(WindowEvent event){

        Properties properties =new Properties();

        Window window = (Window) event.getSource();
        if (window == null) {
            return;
        }

        var width = window.getWidth();
        var height = window.getHeight();
        properties.setProperty("width", String.valueOf(width));
        properties.setProperty("height", String.valueOf(height));

        var stageConfigFile = new File("stageConfigFile.properties");
        try {
            stageConfigFile.createNewFile();
            WriteProperties(properties, stageConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CenterPoint centerPoint: CenterPoint.allPoints){
            if (centerPoint.isBuildings){
                properties = new Properties();
                properties.setProperty("dormCount", String.valueOf(centerPoint.dorms.length));
                var configFile = new File( "dormsConfig.properties");
                try {
                    configFile.createNewFile();
                    WriteProperties(properties, configFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < centerPoint.dorms.length; ++i) {
                    Dorm dorm = centerPoint.dorms[i];
                    properties = new Properties();
                    properties.setProperty("x", String.valueOf(dorm.xLocation));
                    properties.setProperty("y", String.valueOf(dorm.yLocation));
                    properties.setProperty("population", String.valueOf(dorm.population));
                    properties.setProperty("name", dorm.name);
                    properties.setProperty("colorRed", String.valueOf(dorm.fillColor.getRed()));
                    properties.setProperty("colorGreen", String.valueOf(dorm.fillColor.getGreen()));
                    properties.setProperty("colorBlue", String.valueOf(dorm.fillColor.getBlue()));
                    var dormFile = new File( "dorm"+i+"Config.properties");
                    try {
                        dormFile.createNewFile();
                        WriteProperties(properties, dormFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static void WriteProperties(Properties properties, File file){
        try (OutputStream output = new FileOutputStream(file)) {

            // save properties to project root folder
            properties.store(output, null);

        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

}
