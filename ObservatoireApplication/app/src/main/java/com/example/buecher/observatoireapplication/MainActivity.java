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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_ui);

        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRestoreGLStateEnabled(false);
        cardboardView.setRenderer(new CardboardRenderer());
        setCardboardView(cardboardView);

        overlayView = (CardboardOverlayView) findViewById(R.id.overlay);
    }
}
