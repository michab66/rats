/* $Id: Target.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 *
 */
public class Target
  extends
    Existence
{
  static private String ourName = "Target";
  static private String ourFile = "images/zzz.gif";

  // image data
  static private PictureStrip strip;

  private AnimatedCell ourCell;
  private GridPosition ourPosition;



  /**
   * ctor 1: insert Target into AnimationGrid.
   * ctor 2: preload
   */
  public Target( AnimationGrid ag, GridPosition p )
  {
    super( ag, ourName );
System.out.println( "target ctor" );

    if ( null == strip )
      System.out.println( ourName + "not preloaded..." );

    ourPosition = p;
    ourCell = ag.getCell( p );

    // go...
    ourCell.addStrip( this, strip, true );
  }
  public Target( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  public int collisionRequest( Existence identity, AnimatedCell idshome, int collDir )
  {
    System.out.println( "collision request" );
    /* We expect an EndRat object in the cell northern of
     * ours.  Get a reference to it.
     */
    Existence what = home.getExistence( new GridPosition( ourPosition.x,
                                                          ourPosition.y+1 ) );
    /* Create a new object of our type in the cell eastern of
     * ours. This will handle the next collision request
     */
    new Target( home, new GridPosition( ourPosition.x+1, ourPosition.y ) );

    if ( identity instanceof FatRat )
    {
      if ( 0 != what.collisionRequest( this, idshome, 0 ) )
         // FatRat had luck
        new EndRat( null, home, ourCell, EndRat.HAPPY, EndRat.FAT );
      else
        // FatRat had not so much luck
        new EndRat( null, home, ourCell, EndRat.SAD, EndRat.FAT );
    }
    else
    {
      if ( 0 == what.collisionRequest( this, idshome, 0 ) )
        new EndRat( null, home, ourCell, EndRat.HAPPY, EndRat.SLIM );
      else
        new EndRat( null, home, ourCell, EndRat.SAD, EndRat.SLIM );
    }

    return 0;
  }



  private void initGfx( Applet app )
  {
    if ( null == strip )
      strip = new PictureStrip( app, ourFile, new DropColour( Color.white ) );
  }
}
