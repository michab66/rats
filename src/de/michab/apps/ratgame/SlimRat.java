/* $Id: SlimRat.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;



/**
 * Implementierung der Ratte
 */
public class SlimRat
  extends
    Rat
{
  static private String ourName = "SlimRat";
  static private String ourPrefix = "images/srat";

  // Array der PictureStrips
  static PictureStrip[] strip;



  public SlimRat( Applet app, AnimationGrid ag, GridPosition p, int startStrip )
  {
    super( ag, ourName );
    initImages( app );
    // Positionierung der Ratte
    ag.addStrip( this, p, strip[ startStrip ]  );
  }
  public SlimRat( AnimationGrid ag, GridPosition p, int whatStrip )
  {
    this( null, ag, p, whatStrip );
  }
  public SlimRat( Applet app )
  {
    super( null, ourName );
    initImages( app );
  }



  protected boolean preventCollision( Existence ex )
  {
    if ( null != ex )
       return ex instanceof RatLike;
    else
       return false;
  }



  protected boolean resolveCollision( Existence opponent,
                                   AnimatedCell targetCell,
                                   int stripIdx, int collDir )
  {
    if ( opponent instanceof Pill )
    {
      // die...
      home.removeStrip( targetCell.gridPosition );
      new Slim2SlimRat( home, targetCell.gridPosition, stripIdx );
    }
    else if ( opponent instanceof Cheese )
    {
      // become a fat one...
      home.removeStrip( targetCell.gridPosition );
      new Slim2FatRat( home, targetCell.gridPosition, stripIdx );
    }
    else if ( opponent instanceof Wippe )
    {
      // First fix the map entries...
      ((DragableWippe)opponent).fixMap();
      // ...kill the strip...
      home.removeStrip( targetCell.gridPosition );
      // ...and start the pass thru sequence
      new Slim3WippeRat( home, targetCell.gridPosition, stripIdx );
    }
    else if ( opponent instanceof StopTheFat )
    {
      // Kill the strip...
      home.removeStrip( targetCell.gridPosition );
      // ...and start the pass thru sequence
      new Slim3STFRat( home, targetCell.gridPosition, stripIdx );
    }
    else if ( opponent instanceof Rat )
      // turn
      return true;
    else
      return 0 != opponent.collisionRequest( this, targetCell, collDir );

    return false;
  }



  protected PictureStrip[] getStripArray()
  {
    return strip;
  }



  /**
   * Initialisierung des Strip Arrays
   */
  private void initImages( Applet app )
  {
    if ( null == strip )
      strip = loadImages( app, ourPrefix );
  }
}
