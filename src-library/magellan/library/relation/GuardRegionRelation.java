package magellan.library.relation;

import magellan.library.Unit;

/**
 * A relation indicating that the source unit changes its battle status
 */
public class GuardRegionRelation extends UnitRelation {

  /** Status for BEWACHE NICHT */
  public static final int GUARD_NOT = 0;
  /** Status for BEWACHE */
  public static final int GUARD = 1;
  /**
   * The guard status.
   */
  public int guard = 0;

  /**
   * Creates a new CombatStatusRelation.
   * 
   * @param s The source unit
   * @param newStatus new guarding bevaviour (true or false)
   * @param line The line in the source's orders
   */
  public GuardRegionRelation(Unit s, int newStatus, int line) {
    super(s, line);
    guard = newStatus;
  }
}
