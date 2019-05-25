/* $Id: Fat2FatRat.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Just a one step animation rat used for the transformation
 * from the fat to fat which turns out to be fatal error for the
 * poor animal...
 *
 * Only a horizontal transformation is possible.
 */
public class Fat2FatRat
  extends
    FatRat
{
  private static String ourName = "Fat2FatRat";
  private static boolean Debug = false;

  static private PictureStrip[] strip;




  public Fat2FatRat( AnimationGrid ag, GridPosition p, int whatStrip )
  {
    super( ag, p, whatStrip );

    if ( null == strip || null == strip[ whatStrip ] )
    {
      System.out.println( "Fat2FatRat: strip or strip idx invalid..." );
      return;
    }
    // create our representation, kill our predecessor
    ag.addStrip( this, p, strip[ whatStrip ], true );
  }
  public Fat2FatRat( Applet app )
  {
    super( app );

    if ( null == strip )
      initImages( app );
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
   * terminated
   *
   * In case of this rat just loop on the last picture of the strip.
   * (death scene)
   */
  public void terminated( PictureStrip s, AnimatedCell p )
  {
    p.addStrip( this, s, 11, 11 );
  }



  /**
   * initImages
   */
  private void initImages( Applet app )
  {
    String prefix = "images/xratk";

    // get the picture strip array of our superclass...
    PictureStrip[] strip2clone = getStripArray();
    // create our own array
    strip = new PictureStrip[ strip2clone.length ];
    // clone the entries
    for ( int i = 0 ; i < strip.length ; i++ )
      strip[i] = strip2clone[i];

    DropColour dc = new DropColour( Color.white );

    // now overwrite entries we redefine
    strip[ STRIP_OW ] = new PictureStrip( app, prefix + "ow.gif", "s2sow", dc );
    strip[ STRIP_WO ] = new PictureStrip( app, prefix + "wo.gif", "s2swo", dc );
  }
}
