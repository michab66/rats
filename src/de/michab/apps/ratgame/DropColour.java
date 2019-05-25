/* $Id: DropColour.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.awt.Color;
import java.awt.image.RGBImageFilter;



/**
 * Unterklasse von RGBImageFilter.  Setzt die angegebene
 * Farbe im Image transparent.
 */
public class DropColour
  extends
    RGBImageFilter
{
  int dc,
      dc_transparent;


  DropColour( Color dropOutColour )
  {
    dc = dropOutColour.getRGB() | 0xFF000000;

    dc_transparent = dc & 0x00FFFFFF;

    canFilterIndexColorModel = true;
  }



  public int filterRGB( int x, int y, int rgb )
  {
    return ( rgb == dc ) ? dc_transparent : rgb;
  }
}
