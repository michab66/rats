/* $Id: PictureStrip.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;



/**
 * A strip of images, playable in-line by the animation engine -- is it called
 * thumb cinema?  Responsible for creating the single images from one file-
 * supplied image by cropping this down.
 *
 * Note:  Method initFromVerticalImage() contains a workaround for a problem
 *        hopefully in the Java 2 system and not in my code.  See comment there.
 */
public class PictureStrip
{
  // Pixel size of the square-formed resulting images.
  int cellDimension;

  // The resulting images.
  Image[] Pics;

  // Signals being defined for each image. TODO: Better explanation needed.
  int[] signals;

  // Logical name of this picture strip.
  private final String name;

  final static private boolean Debug = false;



  /**
   * Creates the strip from a disk file.
   */
  public PictureStrip( Applet home,
                       String filename,
                       String name_,
                       ImageFilter filter )
  {
    if ( Debug )
      System.out.println( "PictureStrip: Loading " + filename );

    home.showStatus( "Loading " + filename );

    MediaTracker tracker = new MediaTracker( home );
    Image img = home.getImage( home.getDocumentBase(), filename );
    tracker.addImage( img, 1 );
    // wait until we really have the image
    try
    {
      tracker.waitForID( 1 );
      if ( Debug )
        System.out.println( "PictureStrip: Successfully read " + filename );
    }
    catch ( InterruptedException e )
    {
      System.out.println( "PictureStrip: Exception when loading " + filename );
    }

    // set name
    name = name_;

    // set cell size in pixels
    int pixWidth =  img.getWidth( null );
    int pixHeight = img.getHeight( null );

    if ( Debug )
      System.out.println( "PictureStrip: " + filename + " size in pixels: " + pixWidth + "x" + pixHeight );

    // if we want transparency
    if ( null != filter )
    {
      Image transpImage = home.createImage( new FilteredImageSource( img.getSource(),
                                                                    filter
                                                                  )
                                         );
      // It not entirely clear if attaching this image to the image
      // tracker and waiting is needed or not...
      img = transpImage;
    }

    // init image array
    if ( pixWidth > pixHeight )
      Pics = initFromHoricontalImage( img, pixHeight, pixWidth / pixHeight, home );
    else
      Pics = initFromVerticalImage( img, pixWidth, pixHeight / pixWidth, home );

    // take care on getting all images
    for ( int i = 0 ; i < Pics.length ; i++ )
      tracker.addImage( Pics[i], 2 );
    try
    {
      tracker.waitForID( 2 );
      if ( Debug )
        System.out.println( "PictureStrip: got array" );
    }
    catch ( InterruptedException e )
    {
      System.out.println( "PictureStrip: Exception while transforming" );
    }
  }
  public PictureStrip( Applet home, String filename, String name )
  {
    this( home, filename, name, null );
  }
  public PictureStrip( Applet home, String filename, ImageFilter filter )
  {
    this( home, filename, filename, filter );
  }
  public PictureStrip( Applet home, String filename )
  {
    this( home, filename, filename, null );
  }



  /**
   * Get the logical name of this PictureStrip.
   */
  public String getName()
  {
    return name;
  }



  /**
   * Defines a signal for a single image in this strip.  If this image is
   * displayed, the attached signal is sent to the strips controlling
   * Existence.
   *
   * @arg pict Index of the image the signal should be attached to.
   * @arg sigval An integer being known to the Existence.  Must not be 0.
   * @return true if the index was valid, else false.
   */
  public boolean addSignal( int pict, int sigval )
  {
    int sl = Pics.length;

    if ( pict < 0 || pict > sl )
      return false;

    if ( null == signals )
    {
      signals = new int[ sl ];
      // init the signal array
      for ( int i = 0 ; i < sl ; i++ )
        signals[i] = 0;
    }

    signals[ pict ] = sigval;

    return true;
  }



  /**
   * Check if there is a signal defined for the specified index.
   *
   * @arg pict The index to test.
   * @return The signals value or 0 if no signal is defined.
   */
  public int hasSignal( int pict )
  {
    if ( null == signals )
      return 0;
    else
      return signals[ pict ];
  }



  /**
   *
   */
  public int getCellDimension()
  {
    return cellDimension;
  }



  /**
   * Number of images contained in this strip.
   */
  public int getLength()
  {
    return Pics.length;
  }



  /**
   * Crop the passed image horizontally in smaller, square-formed images.
   */
  private Image[] initFromHoricontalImage( Image img, int cellsize, int numOfPics, Applet home )
  {
    Image[] pa = new Image[ numOfPics ];

    if ( Debug )
      System.out.println( "PictureStrip: Horz " + cellsize + " " + numOfPics );

    // Attribute initialisieren
    cellDimension = cellsize;

    // Aufteilen in Segmente
    for ( int pic = 0 ; pic < numOfPics ; pic++ )
    {
      pa[ pic ] =
          home.createImage(
            new FilteredImageSource( img.getSource(),
                                     new CropImageFilter( pic * cellsize,
                                                          0,
                                                          cellsize,
                                                          cellsize  ) ) );
    }

    return pa;
  }



  /**
   * Crop the passed image vertically in smaller, square-formed images.
   */
  private Image[] initFromVerticalImage( Image img, int cellsize, int numOfPics, Applet home )
  {
    Image[] pa = new Image[ numOfPics ];

    if ( Debug )
      System.out.println( "PictureStrip: Vert " + cellsize + " " + numOfPics );

    // Attribute initialisieren
    cellDimension = cellsize;

    // Aufteilen in Segmente
    for ( int pic = 0 ; pic < numOfPics ; pic++ )
    {
      pa[ pic ] =
          home.createImage(
            new FilteredImageSource( img.getSource(),
            /* TODO: The -1 in the code below is due to an not yet
             * solved problem with my code or with Java 2 ...
             * This is needed to work under the java 2 appletviewer, but
             * results in small display problems (vertical lines in
             * animated characters. */
                                     new CropImageFilter( 0,
                                                          pic * cellsize,
                                                          cellsize -1,
                                                          cellsize  ) ) );
    }

    return pa;
  }
}
