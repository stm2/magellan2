/*
 *  Copyright (C) 2000-2004 Roger Butenuth, Andreas Gampe,
 *                          Stefan Goetz, Sebastian Pappert,
 *                          Klaas Prause, Enno Rehling,
 *                          Sebastian Tusk, Ulrich Kuester,
 *                          Ilja Pavkovic
 *
 * This file is part of the Eressea Java Code Base, see the
 * file LICENSING for the licensing information applying to
 * this file.
 *
 */

package magellan.library.relation;

import magellan.library.Unit;

/**
 * A (possibly abstract) relation indicating between a source and a target unit.
 */
public class InterUnitRelation extends UnitRelation {
  /** The unit that is the target of the relation */
  public Unit target;

  /**
   * Creates a new InterUnitRelation object.
   * 
   * @param source The source unit
   * @param target The target unit
   * @param line The line in the source's orders
   */
  public InterUnitRelation(Unit source, Unit target, int line) {
    this(source, source, target, line);
  }

  /**
   * Creates a new InterUnitRelation object.
   * 
   * @param origin The origin unit
   * @param source The source unit
   * @param target The target unit
   * @param line The line in the source's orders
   */
  public InterUnitRelation(Unit origin, Unit source, Unit target, int line) {
    super(origin, source, line);
    this.target = target;
  }

  /*
   * (non-Javadoc)
   * @see com.eressea.relation.UnitRelation#toString()
   */
  @Override
  public String toString() {
    return super.toString() + "@TARGET=" + target;
  }

  /**
   * Attaches an order to all report objects it is relevant to. source and target.
   * 
   * @see magellan.library.relation.UnitRelation#add()
   */
  @Override
  public void add() {
    super.add();
    if (target != source && target != origin && target != null) {
      target.addRelation(this);
    }
  }

  /**
   * @return true if the object is source, origin or target
   * @see magellan.library.relation.UnitRelation#isRelated(java.lang.Object)
   */
  @Override
  public boolean isRelated(Object object) {
    return super.isRelated(object) || target == object;
  }

}
