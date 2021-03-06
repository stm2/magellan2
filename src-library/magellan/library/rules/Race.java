/*
 * Copyright (C) 2000-2004 Roger Butenuth, Andreas Gampe, Stefan Goetz, Sebastian Pappert, Klaas
 * Prause, Enno Rehling, Sebastian Tusk, Ulrich Kuester, Ilja Pavkovic This file is part of the
 * Eressea Java Code Base, see the file LICENSING for the licensing information applying to this
 * file.
 */

package magellan.library.rules;

import java.util.LinkedHashMap;
import java.util.Map;

import magellan.library.ID;
import magellan.library.StringID;

/**
 * Holds rule relevant information about a race.
 */
public class Race extends UnitContainerType {
  private int recruit = -1;
  private float weight = 0;
  private float capacity = 0;
  private Map<ID, Integer> skillBonuses = null;
  private Map<ID, Map<ID, Integer>> skillRegionBonuses = null;
  private int additiveShipBonus;
  private int maintenance = 10;
  private int recruitFactor = 1;

  /**
   * Creates a new Race object.
   */
  public Race(StringID id) {
    super(id);
  }

  /**
   * Sets the price in silver to recruit one person of this race.
   */
  public void setRecruitmentCosts(int r) {
    recruit = r;
  }

  /**
   * Returns the cost for creating one person of this race.
   */
  public int getRecruitmentCosts() {
    return recruit;
  }

  /**
   * Sets the number of persons that can be recruited for one peasant.
   */
  public void setRecruitmentFactor(int factor) {
    recruitFactor = factor;
  }

  /**
   * Returns the number of persons that can be recruited for one peasant.
   */
  public int getRecruitmentFactor() {
    return recruitFactor;
  }

  /**
   * Set the amount of silver needed to sustain one person of this race in one week.
   */
  public void setMaintenance(int maintenance) {
    this.maintenance = maintenance;
  }

  /**
   * Returns the amount of silver needed to sustain one person of this race in one week. The default
   * value is 10.
   */
  public int getMaintenance() {
    return maintenance;
  }

  /**
   * Set the weight of one person (in GE).
   */
  public void setWeight(float w) {
    weight = w;
  }

  /**
   * Returns the weight of one person (in GE).
   */
  public float getWeight() {
    return weight;
  }

  /**
   * Set the carrying capacity (in GE).
   */
  public void setCapacity(float c) {
    capacity = c;
  }

  /**
   * Returns the carrying capacity (in GE).
   */
  public float getCapacity() {
    return capacity;
  }

  /**
   * Returns the bonus this race has on the specified skill.
   * 
   * @return the bonus for the specified skill or 0, if no bonus-information is available for this
   *         skill.
   */
  public int getSkillBonus(SkillType skillType) {
    int bonus = 0;

    if (skillBonuses != null) {
      Integer i = skillBonuses.get(skillType.getID());

      if (i != null) {
        bonus = i.intValue();
      }
    }

    return bonus;
  }

  /**
   * Sets the bonus this race has on the specified skill.
   */
  public void setSkillBonus(SkillType skillType, int bonus) {
    if (skillBonuses == null) {
      skillBonuses = new LinkedHashMap<ID, Integer>();
    }

    skillBonuses.put(skillType.getID(), Integer.valueOf(bonus));
  }

  /**
   * Returns the bonus this race has in certain region terrains.
   */
  public int getSkillBonus(SkillType skillType, RegionType regionType) {
    int bonus = 0;

    if (skillRegionBonuses != null) {
      Map<ID, Integer> m = skillRegionBonuses.get(regionType.getID());

      if (m != null) {
        Integer i = m.get(skillType.getID());

        if (i != null) {
          bonus = i.intValue();
        }
      }
    }

    return bonus;
  }

  /**
   * Sets the bonus this race has in certain region terrains.
   */
  public void setSkillBonus(SkillType skillType, RegionType regionType, int bonus) {
    if (skillRegionBonuses == null) {
      skillRegionBonuses = new LinkedHashMap<ID, Map<ID, Integer>>();
    }

    Map<ID, Integer> m = skillRegionBonuses.get(regionType.getID());

    if (m == null) {
      m = new LinkedHashMap<ID, Integer>();
      skillRegionBonuses.put(regionType.getID(), m);
    }

    m.put(skillType.getID(), bonus);
  }

  /**
   * Returns the bonus that is added to the ship radius for this race.
   * 
   * @return The bonus
   */
  public int getAdditiveShipBonus() {
    return additiveShipBonus;
  }

  /**
   * Returns the bonus (or malus) that is added to the ship radius for this race.
   * 
   * @param bon
   */
  public void setAdditiveShipBonus(int bon) {
    additiveShipBonus = bon;
  }

  /**
   * Returns the id uniquely identifying this object.
   */
  @Override
  public StringID getID() {
    return (StringID) id;
  }

}
