package com.example.buecher.observatoireapplication;

import android.os.Bundle;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

/**
 * Created by Nicolas Buecher on 26/01/2016.
 */
public class MainActivity extends CardboardActivity
{

    private CardboardOverlayView overlayView;
    private CardboardRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_ui);

        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRestoreGLStateEnabled(false);
        renderer = new CardboardRenderer(this);
        cardboardView.setRenderer(renderer);
        setCardboardView(cardboardView);

        overlayView = (CardboardOverlayView) findViewById(R.id.overlay);
        overlayView.show3DToast("Look around !");
    }

    /**
     * Called when the Cardboard trigger is pulled.
     */
    @Override
    public void onCardboardTrigger()
    {
        int trigger = renderer.switchTexture();

        switch (trigger)
        {
            case 0:
                overlayView.show3DToast("1 of 768 of the sky in one texture. (HealPIX).");
                break;
            case 1:
                overlayView.show3DToast("All sky in one texture. (HealPIX)");
                break;
            case 2:
                overlayView.show3DToast("All sky in one texture. (Other)");
                break;
            default:
                // Hold an error
                break;
        }


        overlayView.show3DToast("");
    }
}
