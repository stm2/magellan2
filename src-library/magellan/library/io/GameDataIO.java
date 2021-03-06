/*
 * Copyright (C) 2000-2004 Roger Butenuth, Andreas Gampe, Stefan Goetz, Sebastian Pappert, Klaas
 * Prause, Enno Rehling, Sebastian Tusk, Ulrich Kuester, Ilja Pavkovic This file is part of the
 * Eressea Java Code Base, see the file LICENSING for the licensing information applying to this
 * file.
 */

package magellan.library.io;

import java.io.IOException;
import java.io.Reader;

import magellan.library.GameData;
import magellan.library.Rules;
import magellan.library.io.file.FileType;

/**
 * A type of reader for GameData.
 */
public interface GameDataIO {
  /**
   * Fills the existing GameData object <code>world</code> from an input Reader of a game data file.
   * 
   * @param in A Reader, initialized to an game data file.
   * @param world An existing GameData.
   * @return The (modified) <code>world</code>
   * @throws IOException If an I/O error occurs
   */
  public GameData read(Reader in, GameData world) throws IOException;

  /**
   * Reads a new GameData object from aFileType.
   * 
   * @param aFileType Provides the input file (reader).
   * @param rules This game is expected to be found.
   * @return a new GameData object, representing the input.
   * @throws IOException If an I/O error occurs
   */
  GameData read(FileType aFileType, Rules rules) throws IOException;

}
