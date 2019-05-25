/* $Id: RatSource.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996,97 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;



/**
 * Class RatSource
 */
public class RatSource
  extends
    Existence
{
  static private String ourName = "RatSource";
  static private String ourFile = "images/zzkiste.gif";

  // image data.
  static private PictureStrip strip;

  // state of arm; false = not selected, true = selected
  boolean arm = false;

  // Rat creation position
  GridPosition ourPosition,
               creationPosition;
  Applet ourApp;

  // number of rats created
  private int nCreated = 0;



  /**
   * ctor 1: Add RatSource to AnimationGrid
   * ctor 2: Only preload
   */
  public RatSource( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( ag, ourName );
    initGfx( app );

    ourPosition = p;
    creationPosition = new GridPosition( p.x-1, p.y+1 );
    ourApp = app;

    // go...
    terminated( null, null );
  }
  public RatSource( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  public void terminated( PictureStrip dummy, AnimatedCell dummy1 )
  {
    showUp();
  }



  public boolean mouseDown( Event e, int x, int y, GridPosition p )
  {
    if ( nCreated < 5 )
    {
      arm = true;
      showUp();

      if ( null == home.getCell( creationPosition ).getExistence() )
      {
        if ( Math.random() > 0.5 )
          new FatRat( ourApp, home, creationPosition, Existence.STRIP_OW );
        else
          new SlimRat( ourApp, home, creationPosition, Existence.STRIP_OW );

        // create this only once
        if ( 0 == nCreated )
          new Target( home, new GridPosition( 2 + nCreated, 14 ) );

        // create a EndRat object
        new EndRat( ourApp,
                    home,
                    home.getCell( new GridPosition( 2 + nCreated++, 15 ) ) );
      }
    }

    return true;
  }



  public boolean mouseUp( Event e, int x, int y, GridPosition p )
  {
    arm = false;
    showUp();
    return true;
  }



  private void showUp()
  {
    /* The following is a quick hack to allow
     * selection of the lever on the real visible
     * position of the play field.
     * The target animated cell has to be set to
     * invisible to prevent double drawing.
     */
    GridPosition echo = new GridPosition( ourPosition.x,
                                          ourPosition.y + 1 );

    home.getCell( echo ).setVisible( false );
    if ( arm )
    {
      home.addStrip( this, ourPosition, strip, 3, 3, true );
      home.addStrip( this, echo,        strip, 3, 3, true );
    }
    else
    {
      home.addStrip( this, ourPosition, strip, 0, 2, true );
      home.addStrip( this, echo,        strip, 0, 2, true );
    }
  }



  private void initGfx( Applet app )
  {
    if ( null == strip )
      strip = new PictureStrip( app, ourFile, new DropColour( Color.white ) );
  }
}
