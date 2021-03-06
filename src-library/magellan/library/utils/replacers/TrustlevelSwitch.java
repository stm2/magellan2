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

package magellan.library.utils.replacers;

import magellan.library.utils.Resources;
import magellan.library.utils.filters.UnitFactionTLFilter;

/**
 * Selects unit with given trust level
 * 
 * @author Andreas
 * @version 1.0
 */
public class TrustlevelSwitch extends AbstractParameterReplacer implements EnvironmentDependent,
    SwitchOnly {
  protected ReplacerEnvironment environment;

  /**
   * Creates new FactionSwitch.
   * 
   * @param mode if 0: only min level, if 1: min and max level as parameters
   */
  public TrustlevelSwitch(int mode) {
    super((mode == 0) ? 1 : 2);
  }

  /**
   * Returns EMPTY and sets filter to units with given trust level.
   * 
   * @see magellan.library.utils.replacers.Replacer#getReplacement(java.lang.Object)
   */
  public Object getReplacement(Object src) {
    try {
      Object param1 = getParameter(0, src);
      Object param2 = null;
      int min = Integer.MIN_VALUE;
      int max = Integer.MAX_VALUE;

      if (getParameterCount() > 1) {
        param2 = getParameter(1, src);
        max = Integer.parseInt(param2.toString());
      }

      try {
        min = Integer.parseInt(param1.toString());
      } catch (NumberFormatException nfe) {
        if (param1.toString().equals(Replacer.CLEAR)) {
          ((UnitSelection) environment.getPart(ReplacerEnvironment.UNITSELECTION_PART))
              .removeFilters(UnitFactionTLFilter.class);

          return Replacer.EMPTY;
        }
      }

      ((UnitSelection) environment.getPart(ReplacerEnvironment.UNITSELECTION_PART))
          .addFilter(new UnitFactionTLFilter(min, max));
    } catch (RuntimeException npe) {
      // do not filter on error
    }

    return Replacer.EMPTY;
  }

  /**
   * @see magellan.library.utils.replacers.EnvironmentDependent#setEnvironment(magellan.library.utils.replacers.ReplacerEnvironment)
   */
  public void setEnvironment(ReplacerEnvironment env) {
    environment = env;
  }

  /**
   * @see magellan.library.utils.replacers.Replacer#getDescription()
   */
  @Override
  public String getDescription() {
    return Resources
        .get("util.replacers.trustlevelswitch.description." + (getParameterCount() - 1))
        + "\n\n" + super.getDescription();
  }
}
