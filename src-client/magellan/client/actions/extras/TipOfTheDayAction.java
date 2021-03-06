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

package magellan.client.actions.extras;

import java.awt.event.ActionEvent;

import magellan.client.Client;
import magellan.client.actions.MenuAction;
import magellan.client.swing.TipOfTheDay;
import magellan.library.utils.Resources;

/**
 * DOCUMENT ME!
 * 
 * @author Andreas
 * @version 1.0
 */
public class TipOfTheDayAction extends MenuAction {

  /**
   * Creates a new TipOfTheDayAction object.
   * 
   * @param client
   */
  public TipOfTheDayAction(Client client) {
    super(client);
  }

  /**
   * DOCUMENT-ME
   */
  @Override
  public void menuActionPerformed(ActionEvent e) {
    if (!TipOfTheDay.active) {
      TipOfTheDay totd = new TipOfTheDay(client, client.getProperties());
      totd.showTipDialog();
      totd.showNextTip();
    }
  }

  /**
   * @see magellan.client.actions.MenuAction#getAcceleratorTranslated()
   */
  @Override
  protected String getAcceleratorTranslated() {
    return Resources.get("actions.tipofthedayaction.accelerator", false);
  }

  /**
   * @see magellan.client.actions.MenuAction#getMnemonicTranslated()
   */
  @Override
  protected String getMnemonicTranslated() {
    return Resources.get("actions.tipofthedayaction.mnemonic", false);
  }

  /**
   * @see magellan.client.actions.MenuAction#getNameTranslated()
   */
  @Override
  protected String getNameTranslated() {
    return Resources.get("actions.tipofthedayaction.name");
  }

  @Override
  protected String getTooltipTranslated() {
    return Resources.get("actions.tipofthedayaction.tooltip", false);
  }
}
