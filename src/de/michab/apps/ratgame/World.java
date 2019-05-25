/* $Id: World.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;



/**
 * Loads and manages the background image for one level. Cuts
 * it down to one-cell-parts, provides an initialised off screen
 * image. Allows access on the per-cell background images on a
 * one by one base.
 * The sound facilities which up to this version were in this
 * class have been removed. They never worked good and we have to
 * get this thing simpler for these wimpy Windows computers.
 */

public class World
{
  public static final int WEST = 1;
  public static final int NORTH = 2;
  public static final int EAST = 4;
  public static final int SOUTH = 8;

  public static final int INVISIBLE = 16;


  // background image
  private final Image imgBack;

  // playground offset
  public int pixOffsetToCellsX;
  public int pixOffsetToCellsY;

  public byte[][] connectionMap;

  // image dimensions in pixels
  private final int pixWidth,
              pixHeight;

  // playground dimensions in cells
  int cellWidth, cellHeight;
  // cell dimension in pixels
  public int cellDimension;

  // needed for data access
  private final Applet home;

  private final static boolean Debug = false;



  /**
   * World
   *
   * Creates the world for one game level.
   */
  public World( Applet h,
                int offset2cellsX,
                int offset2cellsY,
                int gridDimensions,
                String backImageName )
  {
    /* init attributes */
    pixOffsetToCellsX = offset2cellsX;
    pixOffsetToCellsY = offset2cellsY;
    cellDimension = gridDimensions;
    home = h;

    /* read background images */
    debugOut( "Loading: " + backImageName );

    MediaTracker tracker = new MediaTracker( home );
    imgBack = home.getImage( home.getDocumentBase(), backImageName + ".gif" );
    tracker.addImage( imgBack, 1 );

    // wait till we have the complete image data
    try
    {
      tracker.waitForID( 1 );
    }
    catch ( InterruptedException e )
    {
      debugOut( "  error" );
    }

    // set image size
    pixWidth =  imgBack.getWidth( null );
    pixHeight = imgBack.getHeight( null );
    debugOut( "  size in pixels: " + pixWidth + "x" + pixHeight );


    // compute array size for cells
    cellWidth  = (pixWidth - pixOffsetToCellsX) / cellDimension;
    cellHeight = (pixHeight - pixOffsetToCellsY) / cellDimension;
    debugOut( "  size in cells: " + cellWidth + "x" + cellHeight );

    connectionMap = readConnectionMap( backImageName + ".con",
                                       cellWidth, cellHeight );
  }



  /**
   * getCellImage
   *
   * Computes background image for one cell.  Takes care
   * of actually finishing the calculation before returning
   * the image.
   *
   * Tough it seems dreadful inefficient to create a
   * media tracker for each call, we really experienced
   * a major performance gain from adding this code.
   * (possibly by not spawning one thread per cell but
   * by serialising the computation).
   */
  public Image getCellImage( int x, int y )
  {
    MediaTracker t = new MediaTracker( home );

    Image ci = home.createImage(
      new FilteredImageSource( imgBack.getSource(),
                               new CropImageFilter( x * cellDimension + pixOffsetToCellsX,
                                                    y * cellDimension + pixOffsetToCellsY,
                                                    cellDimension,
                                                    cellDimension  ) ) );

    t.addImage( ci, 0 );
    try
    {
      t.waitForID( 0 );
    }
    catch ( InterruptedException e ){};

    return ci;
  }



  /**
   * hasCellConnections
   */
  public boolean hasCellConnections()
  {
    return null != connectionMap;
  }



  /**
   * getCellConnections
   *
   */
  public byte getCellConnections( int x, int y )
  {
    return connectionMap[ x ][ y ];
  }



  /**
   * Returns this World's background image.  Drawing's not allowed on this.
   */
  public Image getBackground()
  {
    return imgBack;
  }



  /**
   * Returns a reference to an offscreen image.  This is initialised with the
   * background defined for this world.  One is allowed to draw on the returned
   * object and use it freely.
   */
  public Image getOffscreenImage()
  {
    Image imgOffScreen = home.createImage( pixWidth, pixHeight );

    imgOffScreen.getGraphics().drawImage( imgBack, 0, 0, null );

    return imgOffScreen;
  }



  /**
   * getOverallDimension
   *
   * Size of the image.  Includes the space surrounding the
   * game cells.
   */
  public Dimension getDimensionInPixels()
  {
    return new Dimension( pixWidth, pixHeight );
  }



  /**
   * transPixelToCellCoo
   *
   * Transform pixel coordinate to cell coordinate.
   */
  public GridPosition transPixelToCell( int x, int y )
  {
    int cX = ( x - pixOffsetToCellsX ) / cellDimension;
    int cY = ( y - pixOffsetToCellsY ) / cellDimension;

    if ( cX >= 0 && cX < cellWidth &&
         cY >= 0 && cY < cellHeight )
      return new GridPosition( cX, cY );
    else
      return null;
  }



  /**
   * dimensionInCells
   *
   */
  public Dimension getDimensionInCells()
  {
    return new Dimension( cellWidth, cellHeight );
  }



  /*
   * readConnectionMap
   *
   * Fills the passed array with the values specified in the
   * connection map file.  If the file can't be opened the
   * method silently returns.
   */
  private byte[][] readConnectionMap( String mapname, int w, int h )
  {
    byte[][] map;

    // read connection map
    try
    {
      debugOut( "Trying to read connection map" );

      // Get a stream from an URL
      URL mapURL = new URL( home.getDocumentBase(), mapname );

      debugOut( "Got doc URL: " + mapURL );

      // open the stream for the move map
      DataInputStream mapFile = new DataInputStream(
                                       mapURL.openStream() );

      // create an array for the map
      map = new byte[w][h];

      // read map file line by line
      for ( int i = 0 ; i < cellHeight ; i++ )
      {
        String line = mapFile.readLine();

        if ( line != null )
        {
          StringTokenizer st = new StringTokenizer( line );

          for ( int col = 0 ; col < cellWidth ; col++ )
          {
            // init entry in connection map
            if ( st.hasMoreElements() )
              map[ col ][ i ] = (byte)Integer.parseInt( (String)st.nextElement() );
            else
              break;
          }
        }
        else
          break;
      }
    }
    catch ( FileNotFoundException e )
    {
      debugOut( "  no connections" );
      return null;
    }
    catch ( IOException e )
    {
      debugOut( "  IOException" );
      return null;
    }
    catch ( NumberFormatException e )
    {
      debugOut( "  NumberFormatException" );
      return null;
    }

    // Map seems to be valid, so return it
    return map;
  }



  private void debugOut( String err, boolean force )
  {
    if ( Debug || force )
      System.out.println( err );
  }
  private void debugOut( String err )
  {
    debugOut( err, false );
  }
}


