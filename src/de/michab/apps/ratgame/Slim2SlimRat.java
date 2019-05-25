/* $Id: Slim2SlimRat.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996,97 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Just a one step animation rat used for the transformation
 * from the slim to slim which is a fatal error for the
 * poor animal...
 *
 * Only a horizontal transformation is possible.
 */
public class Slim2SlimRat
  extends
    RatLike
{
  private static String ourName = "Slim2SlimRat";
  private static boolean Debug = false;

  static private PictureStrip[] strip;




  public Slim2SlimRat( AnimationGrid ag, GridPosition p, int whatStrip )
  {
    super( ag, ourName );

    if ( null == strip || null == strip[ whatStrip ] )
    {
      System.out.println( "Slim2SlimRat: strip or strip idx invalid..." );
      return;
    }
    ag.addStrip( this, p, strip[ whatStrip ] );
  }
  public Slim2SlimRat( Applet app )
  {
    super( null, ourName );

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
    p.addStrip( this, s, 10, 10 );
  }



  /**
   * initImages
   */
  private void initImages( Applet app )
  {
    String prefix = "images/sratt";

    DropColour dc = new DropColour( Color.white );

    // Array erzeugen...
    strip = new PictureStrip[ STRIP_USER ];

    // ...und Images laden
    strip[ STRIP_OW ] = new PictureStrip( app, prefix + "ow.gif", "s2sow", dc );
    strip[ STRIP_WO ] = new PictureStrip( app, prefix + "wo.gif", "s2swo", dc );
  }
}
