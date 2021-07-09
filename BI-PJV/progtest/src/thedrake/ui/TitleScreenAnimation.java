package thedrake.ui;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class TitleScreenAnimation extends Transition {

    // Weak reference to not force keeping FlowPane in memory after screen hiding
    private WeakReference<FlowPane> flowPane;

    // Create animation
    public TitleScreenAnimation ( Parent node ) {
        if ( ! ( node instanceof BorderPane ) || ! ( ((BorderPane) node).getCenter() instanceof FlowPane ) )
            return;

        this.flowPane = new WeakReference<>( (FlowPane) ((BorderPane) node).getCenter() );
        setCycleDuration(Duration.INDEFINITE);
        setInterpolator(Interpolator.EASE_OUT);
    }

    @Override
    protected void interpolate ( double x ) {
        // Calculate transition power
        double frac = ( System.currentTimeMillis() % 5000L ) / 5000D;
        if ( frac < 0.5D )
            frac *= 2D;
        else
            frac = (1D-frac)*2D;

        // Choose either blue or orange
        Color color = new Color(0.94 - frac * ( 0.74 ), 0.52 + frac * (0.29), 0.42 + frac * (0.55), 1);

        // If reference lost or not set, stop task
        try {
            Objects.requireNonNull(flowPane.get()).setBackground(
                    new Background( new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY) )
            );
        }
        catch ( NullPointerException exc ) {
            stop();
        }
    }

}
