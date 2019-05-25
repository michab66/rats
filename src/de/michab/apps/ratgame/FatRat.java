/* $Id: FatRat.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;



/**
 * Implementierung der Ratte.
 */
public class FatRat
  extends
    Rat
{
  static private String ourName = "FatRat";
  static private String ourPrefix = "images/xrat";

  // Array der PictureStrips
  static PictureStrip[] strip;



  /**
   * ctor 1: All bangs and whizzles.
   * ctor 2: No public ctor!  Its only use is from the resolveCollision method.
   * ctor 3: Preload.
   */
  public FatRat( Applet app, AnimationGrid ag, GridPosition p, int whatStrip )
  {
    super( ag, ourName );

    // init image array
    if ( null == strip )
      initImages( app );

    ag.addStrip( this, p, strip[ whatStrip ] );
  }
  public FatRat( AnimationGrid ag, GridPosition p, int whatStrip )
  {
    this( null, ag, p, whatStrip );
  }
  public FatRat( AnimationGrid ag )
  {
    super( ag, ourName );
  }
  public FatRat( Applet app )
  {
    super( null, ourName );

    // init image array
    if ( null == strip )
      initImages( app );
  }



  protected boolean preventCollision( Existence ex )
  {
    if ( null != ex )
      return ex instanceof RatLike || ex instanceof StopTheFat;
    else
      return false;
  }



  protected boolean resolveCollision( Existence opponent,
                                      AnimatedCell targetCell,
                                      int stripIdx, int collDir )
  {
    if ( opponent instanceof Pill )
    {
      home.removeStrip( targetCell.gridPosition );
      new Fat2SlimRat( home, targetCell.gridPosition, stripIdx );
    }
    else if ( opponent instanceof Cheese )
    {
      // die...
      home.removeStrip( targetCell.gridPosition );
      new Fat2FatRat( home, targetCell.gridPosition, stripIdx );
    }
    else if ( opponent instanceof Wippe )
    {
      // First fix the map entries...
      ((DragableWippe)opponent).fixMap();
      // ...kill the strip...
      home.removeStrip( targetCell.gridPosition );
      // ...and start the pass thru sequence
      new Fat3WippeRat( home, targetCell.gridPosition, stripIdx );
    }
    else if ( opponent instanceof StopTheFat || opponent instanceof Rat )
      // turn
      return true;
    else
      return 0 != opponent.collisionRequest( this, targetCell, collDir );

    return false;
  }



  /**
   * getStripArray
   */
  protected PictureStrip[] getStripArray()
  {
    return strip;
  }



  /**
   * Initialisierung des Strip Arrays
   */
  private void initImages( Applet app )
  {
    strip = loadImages( app, ourPrefix );
  }
}
