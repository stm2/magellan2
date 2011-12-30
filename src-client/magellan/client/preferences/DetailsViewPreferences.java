// class magellan.client.preferences.DetailsViewPreferences
// created on 15.02.2008
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
package magellan.client.preferences;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import magellan.client.EMapDetailsPanel;
import magellan.client.EMapDetailsPanel.ShowItems;
import magellan.client.swing.preferences.ExtendedPreferencesAdapter;
import magellan.client.swing.preferences.PreferencesAdapter;
import magellan.library.utils.Resources;

public class DetailsViewPreferences extends JPanel implements ExtendedPreferencesAdapter {
  private EMapDetailsPanel source = null;
  private List<PreferencesAdapter> subAdapters;
  private PreferencesAdapter regionPref;
  private JCheckBox chkShowTagButtons;
  private JCheckBox chkAllowCustomIcons;
  private JCheckBox chkCompact;
  private JRadioButton rdbAllItems;
  private JRadioButton rdbFriendlyItems;
  private JRadioButton rdbMyItems;
  private JRadioButton rdbSomeItems;
  private JCheckBox chkShowPrivileged;

  /**
   * Creates a new EMapDetailsPreferences object.
   */
  public DetailsViewPreferences(EMapDetailsPanel source) {
    this.source = source;

    subAdapters = new ArrayList<PreferencesAdapter>(2);
    subAdapters.add(source.getEditor().getPreferencesAdapter());
    subAdapters.add(source.getOrders().getPreferencesAdapter());

    // layout this container
    setLayout(new BorderLayout());

    // data view panel
    JPanel help = getDataViewPanel();
    this.add(help, BorderLayout.NORTH);

    // region panel information
    regionPref = source.getRegionPanel().getPreferredAdapter();
    this.add(regionPref.getComponent(), BorderLayout.CENTER);
  }

  private JPanel getDataViewPanel() {
    JPanel outerPanel = new JPanel(new GridBagLayout());
    outerPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), Resources
        .get("emapdetailspanel.prefs.datadisplay")));

    GridBagConstraints c =
        new GridBagConstraints(0, 0, 2, 1, 1.0, 0, GridBagConstraints.NORTH,
            GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

    c.anchor = GridBagConstraints.WEST;
    c.gridx = 0;
    c.gridy = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.1;

    JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));

    chkShowTagButtons =
        new JCheckBox(Resources.get("emapdetailspanel.prefs.showTagButtons"), source
            .isShowingTagButtons());
    innerPanel.add(chkShowTagButtons);

    chkAllowCustomIcons =
        new JCheckBox(Resources.get("emapdetailspanel.prefs.allowCustomIcons"), source
            .isAllowingCustomIcons());
    innerPanel.add(chkAllowCustomIcons);

    chkCompact =
        new JCheckBox(Resources.get("emapdetailspanel.prefs.compact"), source.isCompactLayout());
    innerPanel.add(chkCompact);

    outerPanel.add(innerPanel, c);

    innerPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
    ButtonGroup showButtons = new ButtonGroup();
    rdbAllItems =
        new JRadioButton(Resources.get("emapdetailspanel.prefs.allitems"),
            source.getShowItems() == ShowItems.SHOW_ALL);
    rdbFriendlyItems =
        new JRadioButton(Resources.get("emapdetailspanel.prefs.friendlyitems"), source
            .getShowItems() == ShowItems.SHOW_PRIVILEGED_FACTIONS);
    rdbMyItems =
        new JRadioButton(Resources.get("emapdetailspanel.prefs.myitems"),
            source.getShowItems() == ShowItems.SHOW_MY_FACTION);
    rdbSomeItems =
        new JRadioButton(Resources.get("emapdetailspanel.prefs.someitems"),
            source.getShowItems() == ShowItems.SHOW_IN_REGION);
    showButtons.add(rdbAllItems);
    showButtons.add(rdbFriendlyItems);
    showButtons.add(rdbMyItems);
    showButtons.add(rdbSomeItems);
    innerPanel.add(rdbAllItems);
    innerPanel.add(rdbFriendlyItems);
    innerPanel.add(rdbMyItems);
    innerPanel.add(rdbSomeItems);

    c.gridy++;
    outerPanel.add(innerPanel, c);

    chkShowPrivileged =
        new JCheckBox(Resources.get("emapdetailspanel.prefs.showprivileged"),
            source.getShowItems() == ShowItems.SHOW_PRIVILEGED_FACTIONS);

    c.gridy++;
    outerPanel.add(chkShowPrivileged, c);

    return outerPanel;
  }

  /**
   * @see magellan.client.swing.preferences.ExtendedPreferencesAdapter#getChildren()
   */
  public List<PreferencesAdapter> getChildren() {
    return subAdapters;
  }

  /**
   * @deprecated not implemented?
   * @see magellan.client.swing.preferences.PreferencesAdapter#initPreferences()
   */
  @Deprecated
  public void initPreferences() {
    chkShowTagButtons.setSelected(source.isShowingTagButtons());
    chkAllowCustomIcons.setSelected(source.isAllowingCustomIcons());
    chkCompact.setSelected(source.isCompactLayout());
    chkShowPrivileged.setSelected(source.getShowItems() == ShowItems.SHOW_PRIVILEGED_FACTIONS);
    if (source.getShowItems() == ShowItems.SHOW_ALL) {
      rdbAllItems.setSelected(true);
    }
    if (source.getShowItems() == ShowItems.SHOW_IN_REGION) {
      rdbSomeItems.setSelected(true);
    }
    if (source.getShowItems() == ShowItems.SHOW_MY_FACTION) {
      rdbMyItems.setSelected(true);
    }
    if (source.getShowItems() == ShowItems.SHOW_PRIVILEGED_FACTIONS) {
      rdbFriendlyItems.setSelected(true);
    }
    regionPref.initPreferences();
  }

  /**
   * preferences adapter code:
   * 
   * @see magellan.client.swing.preferences.PreferencesAdapter#applyPreferences()
   */
  public void applyPreferences() {
    source.setShowTagButtons(chkShowTagButtons.isSelected());
    source.setAllowCustomIcons(chkAllowCustomIcons.isSelected());
    source.setCompactLayout(chkCompact.isSelected());
    if (rdbAllItems.isSelected()) {
      source.setShowItems(ShowItems.SHOW_ALL);
    } else if (rdbSomeItems.isSelected()) {
      source.setShowItems(ShowItems.SHOW_IN_REGION);
    } else if (rdbMyItems.isSelected()) {
      source.setShowItems(ShowItems.SHOW_MY_FACTION);
    } else {
      source.setShowItems(ShowItems.SHOW_PRIVILEGED_FACTIONS);
    }
    regionPref.applyPreferences();
  }

  /**
   * @see magellan.client.swing.preferences.PreferencesAdapter#getComponent()
   */
  public Component getComponent() {
    return this;
  }

  /**
   * @see magellan.client.swing.preferences.PreferencesAdapter#getTitle()
   */
  public String getTitle() {
    return Resources.get("emapdetailspanel.prefs.title");
  }
}
