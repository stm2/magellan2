// class magellan.library.gamebinding.GuardOrder
// created on Oct 12, 2010
//
// Copyright 2003-2010 by magellan project team
//
// Author : $Author: $
// $Id: $
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program (see doc/LICENCE.txt); if not, write to the
// Free Software Foundation, Inc.,
// 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
package magellan.library.gamebinding.e3a;

import java.util.List;

import magellan.library.EntityID;
import magellan.library.GameData;
import magellan.library.Unit;
import magellan.library.gamebinding.ExecutionState;
import magellan.library.gamebinding.SimpleOrder;
import magellan.library.utils.OrderToken;

/**
 * A guard order (BEWACHE)
 *
 * @author stm
 */
public class MaintainOrder extends SimpleOrder {

  private boolean not;
  private EntityID target;

  /**
   * @param tokens
   * @param text
   */
  public MaintainOrder(List<OrderToken> tokens, String text) {
    super(tokens, text);
  }

  @Override
  public void execute(ExecutionState state, GameData data, Unit unit, int line) {
    // no relation necessary so far
  }

  /**
   * Sets the value of not.
   *
   * @param not The value for not.
   */
  public void setNot(boolean not) {
    this.not = not;
  }

  /**
   * Returns the value of not.
   *
   * @return Returns not.
   */
  public boolean isNot() {
    return not;
  }

  public void setBuilding(EntityID target) {
    this.target = target;
  }

  /**
   * Returns the value of target.
   *
   * @return Returns target.
   */
  public EntityID getBuilding() {
    return target;
  }

}
