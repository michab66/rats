/* $Id: RatLike.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;



/**
 * Temporary helper class to quick fix the inheritance
 * hierarchy.  Should be removed as soon as possible.
 */
public class RatLike
  extends
    Existence
{
  /**
   * RatLike
   *
   * Simple ctor, just save the args.
   */
  public RatLike( AnimationGrid g, String n )
  {
    super( g, n );
  }
}
