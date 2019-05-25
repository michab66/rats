/* $Id: Slim3STFRat.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Just a one step animation rat used for the transformation
 * from the slim rat through the STF.  After passing, the
 * STF has to be reconstructed with states switched and a rat
 * object has to be created.
 *
 * Only a horizontal transformation is possible.
 */
public class Slim3STFRat
  extends
    RatLike
{
  private static String ourName = "Slim3STFRat";
  private Applet app;
  private static boolean Debug = false;

  static private PictureStrip[] strip;




  public Slim3STFRat( AnimationGrid ag, GridPosition p, int whatStrip )
  {
    super( ag, ourName );

    if ( null == strip || null == strip[ whatStrip ] )
    {
      System.out.println( "Slim3STFRat: strip or strip idx invalid..." );
      return;
    }
    ag.addStrip( this, p, strip[ whatStrip ] );
  }
  public Slim3STFRat( Applet app )
  {
    super( null, ourName );

    if ( null == strip )
      initImages( app );
  }



  /**
   * signal
   *
   * Receives collision signals and contains navigation system.
   */
  public void signal( int sigval, AnimatedCell currentCell, PictureStrip ps )
  {
    if ( Debug )
      System.out.println( "Rat: Collision signal: " + sigval + " "
                          + ps.getName() );

    // we try to pass from currentCell to targetCell.
    AnimatedCell targetCell  = getFrontCell( sigval, currentCell ),
                 secTarget;

    // this is needed in case of an collision
    int stripIdx;

    // if our target cell is not reachable, do hard turn
    if ( null == targetCell || null != targetCell.getExistence() )
    {
      currentCell.addStrip( this, strip[ getIdxToBack( sigval ) ], 4, 12, true );
      // Since we forced the add no collision possible, bail out
      return;
    }
    // can we turn right?
    else if ( null != (secTarget = getRightCell( sigval, targetCell )) )
      stripIdx = getIdxToRight( sigval );
    // can we go straight?
    else if ( null != (secTarget = getFrontCell( sigval, targetCell )) )
      stripIdx = getIdxToFront( sigval );
    // or , at least, turn left?
    else if ( null != (secTarget = getLeftCell( sigval, targetCell )) )
      stripIdx = getIdxToLeft( sigval );
    // really no way out. Go straight and turn in next cycle
    else
      stripIdx = getIdxToFront( sigval );

    // if the target cell is empty...
    if ( null == targetCell.getExistence() )
      // ...create a new rat in it
      new SlimRat( home, targetCell.gridPosition, stripIdx );
    else
      // ...in the other case, turn
      currentCell.addStrip( this, strip[ getIdxToBack( sigval ) ], 4, 12, true );
  }


  /**
   * terminated
   *
   * The terminated-signal means we have passed the Wippe.  What has
   * to be done is to recreate the Wippe.
   */
  public void terminated( PictureStrip s, AnimatedCell ac )
  {
    new DragableStopTheFat( null, home, ac.gridPosition );
  }



  /**
   * collisionRequest
   *
   */
  public int collisionRequest( Existence identity, AnimatedCell idshome, int collDir )
  {
    return 1;
  }



  /**
   * initImages
   */
  private void initImages( Applet app )
  {
    String prefix = "images/srate";

    DropColour dc = new DropColour( Color.white );

    // create array...
    strip = new PictureStrip[ STRIP_USER ];

    // ...and load images
    (strip[ STRIP_OO ] = new PictureStrip( app, prefix + "oo.gif", "", dc ))
      .addSignal( 8, COLL_O );
    (strip[ STRIP_WW ] = new PictureStrip( app, prefix + "ww.gif", "", dc ))
      .addSignal( 8, COLL_W );
    (strip[ STRIP_OW ] = new PictureStrip( app, prefix + "ow.gif", "s2sow", dc ))
      .addSignal( 4, COLL_W );
    (strip[ STRIP_WO ] = new PictureStrip( app, prefix + "wo.gif", "s2swo", dc ))
      .addSignal( 4, COLL_O );
  }
}
