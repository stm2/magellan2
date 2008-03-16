// class magellan.plugin.extendedcommands.ExtendedCommandsHelper
// created on 02.02.2008
//
// Copyright 2003-2008 by magellan project team
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
package magellan.plugin.extendedcommands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import magellan.library.GameData;
import magellan.library.ID;
import magellan.library.Item;
import magellan.library.LuxuryPrice;
import magellan.library.Region;
import magellan.library.Skill;
import magellan.library.StringID;
import magellan.library.Unit;
import magellan.library.UnitContainer;
import magellan.library.UnitID;
import magellan.library.rules.ItemCategory;
import magellan.library.rules.ItemType;
import magellan.library.utils.logging.Logger;

/**
 * A Helper class to have some shortcuts
 *
 * @author Thoralf Rickert
 * @version 1.0, 11.09.2007
 */
public class ExtendedCommandsHelper {
  
  private static final Logger log = Logger.getInstance(ExtendedCommandsHelper.class);

  private GameData world = null;
  private Unit unit = null;
  private UnitContainer container = null;
  
  public ExtendedCommandsHelper(GameData world) {
    this.world = world;
  }
  
  public ExtendedCommandsHelper(GameData world, Unit unit) {
    this.world = world;
    this.unit = unit;
  }
  
  public ExtendedCommandsHelper(GameData world, UnitContainer container) {
    this.world = world;
    this.container = container;
  }
  
  /**
   * Returns true, if the current unit is in the region with the
   * given name.
   */
  public boolean unitIsInRegion(String regionName) {
    return unit.getRegion().getName().equalsIgnoreCase(regionName);
  }
  
  /**
   * Returns the unit with the given Unit-ID in the current region.
   */
  public Unit getUnitInRegion(String unitId) {
    if (unit != null) {
      return unit.getRegion().getUnit(UnitID.createUnitID(unitId, world.base));
    } else {
      return world.getUnit(UnitID.createUnitID(unitId, world.base));
    }
  }
  
  /**
   * Returns the unit with the given Unit-ID in the current region.
   */
  public Unit getUnitInRegion(Region region, String unitId) {
    if (region != null) {
      return region.getUnit(UnitID.createUnitID(unitId, world.base));
    } else return null;
  }
  
  /**
   * Returns true, if the current unit sees another unit with
   * the given unit id.
   */
  public boolean unitSeesOtherUnit(String unitId) {
    Unit otherunit = getUnitInRegion(unitId);
    return otherunit != null;
  }
  
  /**
   * Returns the number of items of a unit with the given
   * item name. For example:
   * 
   *  int horses = getItemCount(unit,"Pferd")
   *  
   * returns the number of horses of the given unit.
   */
  public int getItemCount(Unit unit, String itemTypeName) {
    Collection<Item> items = unit.getItems();
    if (items != null) {
      for (Item item : items) {
        if (item.getItemType().getName().equalsIgnoreCase(itemTypeName)) {
          return item.getAmount();
        }
      }
    }
    return 0;
  }
  
  /**
   * Returns the luxury item for the given unit that you can
   * purchase.
   */
  public ItemType getRegionLuxuryItem(Region region) {
    if (region == null) return null;
    Map<ID,LuxuryPrice> prices = region.getPrices();
    for (LuxuryPrice price : prices.values()) {
      if (price.getPrice()<0) return price.getItemType();
    }
    return null;
  }
  
  /**
   * This method returns the amount of silver of the given
   * unit. This is a shortcut for
   *  
   *  getItemCount(unit,"Silber")
   */
  public int getSilver(Unit unit) {
    return getItemCount(unit, "Silber");
  }
  
  /**
   * Returns the number of persons in this unit.
   */
  public int getPersons(Unit unit) {
    return unit.getPersons();
  }
  
  /**
   * Returns the skill level of a unit. For example
   *  getLevel(unit,"Unterhaltung")
   */
  public int getLevel(Unit unit, String skillName) {
    Collection<Skill> skills = unit.getSkills();
    if (skills != null) {
      for (Skill skill : skills) {
        if (skill.getSkillType().getName().equalsIgnoreCase(skillName)) {
          return skill.getLevel();
        }
      }
    }
    return 0;
  }
  
  /**
   * Adds an order to the current unit.
   */
  public void addOrder(String order) {
    addOrder(unit,order);
  }
  
  /**
   * Adds an order to the given unit.
   */
  public void addOrder(Unit unit, String order) {
    if (unit == null) return;
    unit.addOrder(order, false, 0);
  }
  
  /**
   * Sets the command for the current unit and replaces all
   * given commands.
   */
  public void setOrder(String order) {
    setOrder(unit,order);
  }
  
  /**
   * Sets the command for the given unit and replaces all
   * given commands.
   */
  public void setOrder(Unit unit, String order) {
    if (unit == null) return;
    List<String> orders = new ArrayList<String>();
    orders.add(order);
    unit.setOrders(orders);
  }
  
  /**
   * This method tries to find out, if the current unit
   * has a weapon and a skill to use this weapon.
   */
  public boolean isSoldier() {
    Collection<Item> items = unit.getItems();
    ItemCategory weapons = world.rules.getItemCategory(StringID.create("weapons"), false);
    if (weapons == null) {
      // we don't know something about weapons.
      log.info("World has no weapons rules");
      return false;
    }
    
    for (Item item : items) {
      ItemCategory itemCategory = item.getItemType().getCategory();
      if (itemCategory == null) {
        
        continue;
      }
      if (itemCategory.equals(weapons)) {
        log.info("Unit has a weapon");
        // ah, a weapon...
        Skill useSkill = item.getItemType().getUseSkill();
        if (useSkill != null) {
          log.info("Skill needed "+useSkill.getName());
          // okay, has the unit the skill?
          for (Skill skill : unit.getSkills()) {
            log.info("Skill "+skill.getName());
            if (useSkill.getSkillType().equals(skill.getSkillType())) {
              log.info("Unit is a soldier.");
              return true;
            }
          }
        }
      }
    }
    log.info("Unit is not a soldier.");
    return false;
  }
  
  /**
   * This method returns a list of all regions in the given world.
   * It's a workaround for the complex iteration thru the map. 
   */
  public List<Region> getRegions(GameData world) {
    List<Region> regions = new ArrayList<Region>();
    
    for (Region region : world.regions().values()) {
      regions.add(region);
    }
    
    return regions;
  }
}
