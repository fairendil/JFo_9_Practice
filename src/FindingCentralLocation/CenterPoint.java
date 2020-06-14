package FindingCentralLocation;

import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CenterPoint {
    public static final ArrayList<CenterPoint> allPoints = new ArrayList();
    public boolean isBuildings = true;
    Dorm[] dorms;
    public int xLocation;
    public int yLocation;
    public String text;
    private int distance;
    private int distanceTarget = 0;
    public Circle point = new Circle();
    public Color fillColor;
    public Color outlineColor;
    public Text display;


    /**
     * Constructor that accepts a name of center point, is it calculated for buildings or group and dorm array.
     *
     * @param centerPointName  name of center point
     * @param isBuildings is it calculated for dorms?
     * @param dorms dorms array
     */
    public CenterPoint(String centerPointName, boolean isBuildings, Dorm... dorms) {
        this.fillColor = Color.BLACK;
        this.outlineColor = Color.YELLOW;
        this.display = new Text();
        this.text = centerPointName + "\nDistance: ";
        this.isBuildings = isBuildings;
        this.dorms = dorms;
        this.display.setFill(Color.RED);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(4.0D);
        dropShadow.setSpread(1.0D);
        dropShadow.setColor(Color.BLACK);
        this.display.setEffect(dropShadow);
        this.display.setFont(Font.font("Default", FontWeight.BOLD, 16.0D));
        FindingCentralLocation.root.getChildren().addAll(this.point, this.display);
        allPoints.add(this);
        this.calculateCenter();
        this.updateDrawing();
    }

    public void updateDrawing() {
        this.point.setCenterX(this.xLocation);
        this.point.setCenterY(this.yLocation);
        this.point.setRadius(10.0D);
        this.point.setFill(this.fillColor);
        this.point.setStroke(this.outlineColor);
        this.display.setText(this.text + this.distance);
        this.display.setX((double) (this.xLocation - 20));
        this.display.setY((double) this.yLocation + this.point.getRadius() + 15.0D);
    }

    public void calculateCenter() {
        if (this.isBuildings) {
            this.calculateDormCenter();
        } else {
            this.calculatePeopleCenter();
        }

        this.distance = this.calculateDistance(this.dorms[this.distanceTarget]);
    }

    private void calculateDormCenter() {
        int pop = 0;
        int xWeight = 0;
        int yWeight = 0;

        for (Dorm dorm : this.dorms) {
            xWeight += dorm.xLocation * dorm.population;
            yWeight += dorm.yLocation * dorm.population;
            pop += dorm.population;
        }

        this.xLocation = xWeight / pop;
        this.yLocation = yWeight / pop;
    }

    private void calculatePeopleCenter() {
        int xWeight = 0;
        int yWeight = 0;

        for (Dorm dorm : this.dorms) {
            xWeight += dorm.xLocation;
            yWeight += dorm.yLocation;
        }

        this.xLocation = xWeight / this.dorms.length;
        this.yLocation = yWeight / this.dorms.length;
    }

    private int calculateDistance(Dorm dorm) {
        return (int) Math.sqrt(Math.pow((dorm.xLocation - this.xLocation), 2.0D) + Math.pow((dorm.yLocation - this.yLocation), 2.0D));
    }

    public static final void updateAllPoint() {
        Iterator var0 = allPoints.iterator();

        while (var0.hasNext()) {
            CenterPoint point = (CenterPoint) var0.next();
            point.calculateCenter();
            point.updateDrawing();
            point.point.toFront();
            point.display.toFront();
        }
    }

    public static final void updateAllPointDistanceTarget(Dorm dorm) {
        Iterator var0 = allPoints.iterator();

        while (var0.hasNext()) {
            CenterPoint point = (CenterPoint) var0.next();
            if (java.util.Arrays.asList(point.dorms).contains(dorm)) {
                point.distanceTarget = java.util.Arrays.asList(point.dorms).indexOf(dorm);
                point.calculateCenter();
                point.updateDrawing();
                point.point.toFront();
                point.display.toFront();
            }
        }
    }
}
