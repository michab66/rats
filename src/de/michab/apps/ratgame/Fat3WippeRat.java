/* $Id: Fat3WippeRat.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996,97 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Just a one step animation rat used for the transformation
 * from the slim rat through the Wippe.  After passing, the
 * Wippe has to be reconstructed with states switched and a rat
 * object has to be created.
 *
 * Only a horizontal transformation is possible.
 */
public class Fat3WippeRat
  extends
    RatLike
{
  private static String ourName = "Fat3WippeRat";
  private static boolean Debug = false;

  static private PictureStrip[] strip;




  public Fat3WippeRat( AnimationGrid ag, GridPosition p, int whatStrip )
  {
    super( ag, ourName );

    if ( null == strip || null == strip[ whatStrip ] )
    {
      System.out.println( "Fat3WippeRat: strip or strip idx invalid..." );
      return;
    }
    ag.addStrip( this, p, strip[ whatStrip ] );
  }
  public Fat3WippeRat( Applet app )
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

    // in case we collide, this is the one we hit
    Existence opponent = null;
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
      new FatRat( home, targetCell.gridPosition, stripIdx );
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
    new DragableWippe( null, home, ac.gridPosition,
                       s != strip[ STRIP_WO ] && s != strip[ STRIP_OO ]);
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
    String prefix = "images/xratf";

    DropColour dc = new DropColour( Color.white );

    // Array erzeugen...
    strip = new PictureStrip[ STRIP_USER ];

    // ...und Images laden
    (strip[ STRIP_OO ] = new PictureStrip( app, prefix + "oo.gif", "", dc ))
      .addSignal( 8, COLL_O );
    (strip[ STRIP_OW ] = new PictureStrip( app, prefix + "ow.gif", "", dc ))
      .addSignal( 4, COLL_W );
    (strip[ STRIP_WO ] = new PictureStrip( app, prefix + "wo.gif", "", dc ))
      .addSignal( 4, COLL_O );
    (strip[ STRIP_WW ] = new PictureStrip( app, prefix + "ww.gif", "", dc ))
      .addSignal( 8, COLL_W );
  }
}

