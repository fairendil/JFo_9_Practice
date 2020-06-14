package FindingCentralLocation;

import javafx.scene.control.ScrollBar;

public class PopulationChanger {
    private final ScrollBar dormPopulationScrollBar = new ScrollBar();
    private final int xBarWidth = 100;
    private final int xBarHeight = 15;

    /**
     * Constructor accepts dorm population
     *
     * @param population Dorm population value
     */
    public PopulationChanger(int population) {
        this.dormPopulationScrollBar.setMin(50.0D);
        this.dormPopulationScrollBar.setMax(400.0D);
        this.setValue(population);
        this.dormPopulationScrollBar.setMinSize(100.0D, 15.0D);
        this.dormPopulationScrollBar.setPrefSize(100.0D, 15.0D);
        this.dormPopulationScrollBar.setMaxSize(100.0D, 15.0D);
    }

    public ScrollBar getScrollBar() {
        return this.dormPopulationScrollBar;
    }

    public void setValue(int value) {
        this.dormPopulationScrollBar.setValue((double)value);
    }
}