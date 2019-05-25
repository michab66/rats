/* $Id: EndRat.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996,97 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * This is the object displayed in the last line of the game
 */
public class EndRat
  extends
    Existence
{
  // The possible states for this object
  final public static int STATIC = 0,
                          HAPPY  = 1,
                          SAD    = 2;

  final public static int SLIM = 0,
                          FAT = 1;

  static private String ourName = "EndRat";

  // our image data
  static private PictureStrip[] strip;

  // the object's state
  private int crtState;
  // are we fat or slim?
  private int ourEgo;



  /**
   * ctor 1: Add EndRat to AnimationGrid
   * ctor 2: Only preload
   */
  public EndRat( Applet app,
                 AnimationGrid ag,
                 AnimatedCell p,
                 int state,
                 int ego )
  {
    super( ag, ourName );
    initGfx( app );

    crtState = state;
    ourEgo = ego;
    // go...
    showUp( p );
  }
  public EndRat( Applet app, AnimationGrid ag, AnimatedCell p, int state )
  {
    // our ego is created random
    this( app, ag, p, state, Math.random() > 0.5 ? FAT : SLIM );
  }
  public EndRat( Applet app, AnimationGrid ag, AnimatedCell p )
  {
    this( app, ag, p, STATIC );
  }
  // preload constructor
  public EndRat( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  /* This is called by the target object to get information, if we
   * want a fat or slim rat to be happy
   */
  public int collisionRequest( Existence a,
                               AnimatedCell b,
                               int c )
  {
    return ourEgo;
  }



  // We have to handle static and dynamic loopers
  public void terminated( PictureStrip s, AnimatedCell ourCell )
  {
    showUp( ourCell );
  }



  private void showUp( AnimatedCell tgt )
  {
    switch ( crtState )
    {
      case STATIC:
        tgt.addStrip( this, strip[ ourEgo ], 0, 0, true );
        break;

      case HAPPY:
        tgt.addStrip( this, strip[ ourEgo ], 1, 3, true );
        break;

      case SAD:
        tgt.addStrip( this, strip[ ourEgo ], 4, 6, true );
        break;

      default:
         // Maybe we should throw an exception
         System.out.println( "Unknown state in FatEndRat" );
    }
  }



  private void initGfx( Applet app )
  {
    if ( null == strip )
    {
      strip = new PictureStrip[2];

      strip[ FAT  ] = new PictureStrip( app, "images/xrat.gif", new DropColour( Color.white ) );
      strip[ SLIM ] = new PictureStrip( app, "images/srat.gif", new DropColour( Color.white ) );
    }
  }
}
