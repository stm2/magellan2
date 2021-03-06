// class magellan.library.gamebinding.LeaveOrderTest
// created on Jan 18, 2020
//
// Copyright 2003-2020 by magellan project team
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
package magellan.library.gamebinding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import magellan.library.Building;
import magellan.library.Faction;
import magellan.library.GameData;
import magellan.library.Region;
import magellan.library.Unit;
import magellan.test.GameDataBuilder;
import magellan.test.MagellanTestWithResources;

public class LeaveOrderTest extends MagellanTestWithResources {

  private GameDataBuilder builder;
  private GameData data;
  private Unit unit;
  private Region region0;
  private EresseaRelationFactory relationFactory;
  private Faction faction0;

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    builder = new GameDataBuilder();
    data = builder.createSimpleGameData();
    unit = data.getUnits().iterator().next();
    faction0 = unit.getFaction();
    region0 = data.getRegions().iterator().next();
    relationFactory = ((EresseaRelationFactory) data.getGameSpecificStuff().getRelationFactory());
    relationFactory.stopUpdating();
  }

  /**
   * Test basic operation
   */
  @Test
  public void testLeave() {
    Building b1 = builder.addBuilding(data, region0, "b1", "Burg", "Burg 1", 10);
    unit.addOrder("VERLASSE");
    addToContainer(unit, b1);

    EresseaRelationFactory executor = new EresseaRelationFactory(data.rules);
    executor.processOrders(region0);

    assertEquals(null, unit.getModifiedBuilding());
    assertEquals(b1, unit.getBuilding());
  }

  @Test
  public void testLeaveNone() {
    unit.clearOrders();
    unit.addOrder("VERLASSE");

    EresseaRelationFactory executor = new EresseaRelationFactory(data.rules);
    executor.processOrders(region0);

    assertEquals(null, unit.getModifiedBuilding());
    assertNotNull(unit.getOrders2().get(0).getProblem());
  }

  @Test
  public void testCommand() {
    Unit unit2 = builder.addUnit(data, "Unit2", region0);
    Faction faction2 = builder.addFaction(data, "foes", "Foes", "Menschen", 1);
    Unit unit3 = builder.addUnit(data, "foe", "Foe", faction2, region0);

    builder.addRegion(data, "1 0", "Bla", "Ebene", 1);

    Building b1 = builder.addBuilding(data, region0, "b1", "Burg", "Burg 1", 10);
    unit.addOrder("Nach o");
    addToContainer(unit, b1);
    addToContainer(unit3, b1);
    addToContainer(unit2, b1);

    EresseaRelationFactory executor = new EresseaRelationFactory(data.rules);
    executor.processOrders(region0);

    assertEquals(null, unit.getModifiedBuilding());
    assertEquals(unit2, b1.getModifiedOwnerUnit());
  }

  @Test
  public void testCommandForeign() {
    Faction faction2 = builder.addFaction(data, "foes", "Foes", "Menschen", 1);
    Unit unit3 = builder.addUnit(data, "foe", "Foe", faction2, region0);

    Building b1 = builder.addBuilding(data, region0, "b1", "Burg", "Burg 1", 10);
    unit.addOrder("VERLASSE");
    addToContainer(unit, b1);
    addToContainer(unit3, b1);

    EresseaRelationFactory executor = new EresseaRelationFactory(data.rules);
    executor.processOrders(region0);

    assertEquals(null, unit.getModifiedBuilding());
    assertEquals(unit3, b1.getModifiedOwnerUnit());
  }

  /**
   * Test basic operation
   */
  @Test
  public void testEnterLeave() {
    Building b1 = builder.addBuilding(data, region0, "b1", "Burg", "Burg 1", 10);
    Building b2 = builder.addBuilding(data, region0, "b2", "Burg", "Burg 2", 10);
    unit.addOrder("BETRETE BURG b2");
    unit.addOrder("VERLASSE");
    addToContainer(unit, b1);

    EresseaRelationFactory executor = new EresseaRelationFactory(data.rules);
    executor.processOrders(region0);

    // LEAVE should ignore the building we just entered
    assertEquals(b2, unit.getModifiedBuilding());
    assertEquals(b1, unit.getBuilding());
  }

  private void addToContainer(Unit unit0, Building ship) {
    unit0.setBuilding(ship);
    if (ship.units().size() == 1) {
      ship.setOwner(unit0);
      ship.setOwnerUnit(unit0);
    }
  }

}
