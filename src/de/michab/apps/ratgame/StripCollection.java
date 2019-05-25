/* $Id: StripCollection.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1997 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;



/**
 * A StripCollection is a set of related bitmap files.  It is
 * able to load itself, providing a number of methods that
 * allow to customize the load process.
 */
public class StripCollection
{
  // Standard file name extension for images
  private static String stdSuffix = ".gif";

  // Only one of the two is valid
  private PictureStrip[] stripArray;
  private PictureStrip   stripOne;

  // Number of picture strips (e.g. 16 for a detailed object)
  private int numOfStrips;

  /* Table of direction suffixes, has to be in sync
   * with direction constants in Existence
   */
  static final String[] dirSuffix =
  {
    "wo", "ow",
    "ns", "sn",
    "wn", "nw",
    "no", "on",
    "os", "so",
    "sw", "ws",
    "ww", "nn",
    "oo", "ss"
  };

  /**
   * StripCollection
   */
  public StripCollection( Applet home, String base, int num )
  {
    if ( 0 < (numOfStrips = num) )
    {
      loadStrips( home, base );
    }
    else
      throw( new Error( "StripCollection '" + base + "' ctor" ) );
  }
  public StripCollection( Applet home, String base )
  {
    this( home, base, 1 );
  }



  /**
   * What should I say?  This is always the number defined with
   * the constructor.
   */
  public int getNumberOfStrips()
  {
    return numOfStrips;
  }



  /**
   * Get the strip array defined by this collection.  If this collection
   * holds only one strip an error is thrown.
   */
  public PictureStrip[] getStrips()
  {
    if ( numOfStrips > 1 )
      return stripArray;
    else
      throw( new Error( "StripCollection: no array for single strip" ) );
  }




  /**
   * Get the only strip in this collection.  If there is an array
   * contained in this collection an error is thrown.
   */
  public PictureStrip getStrip()
  {
    if ( numOfStrips == 1 )
      return stripOne;
    else
      throw( new Error( "StripCollection: no single strip for array" ) );
  }




  /**
   * Build a filename (without any .gif suffix!) for the strip
   * with the passed index.
   * This standard implementation connects the direction
   * suffix for the index to the basename.
   */
  protected String createBaseName( String name, int stripidx )
  {
    // just return name
    return name + dirSuffix[ stripidx ];
  }




  /**
   * Allows the user of this class to set signals as needed on
   * the given strip.
   * The default implementation doesn't set any signals.
   */
  protected void setSignals( int stripidx, PictureStrip strip )
  {
    return;
  }



  /**
   * Method controls the actual loading of the strips
   * and calls setSignals and buildBaseName while doing so.
   */
  private void loadStrips( Applet home, String base )
  {
    String builtName;

    // If we have only one strip, do it a little more efficient
    if ( numOfStrips == 1 )
    {
      System.out.println( "singleloading: " + base );
      // Load picture strip with specified filename
      stripOne = new PictureStrip( home, base + stdSuffix );
      // Give'em a chance to set their signals on this strip
      setSignals( 0, stripOne );
    }
    // They want to load several strips
    else
    {
      stripArray = new PictureStrip[ numOfStrips ];

      for ( int i = 0 ; i < numOfStrips ; i++ )
      {
        // If we got a name for this strip...
        if ( null != (builtName = createBaseName( base, i )) )
        {
          System.out.println( "multiloading: " + builtName );
          // ...load it, calling setSignals afterwards
          stripArray[ i ] = new PictureStrip( home, builtName + stdSuffix );
          setSignals( i, stripArray[ i ] );
        }
        else
          // set this array entry to null
          stripArray[ i ] = null;
      }
    }
  }
}
