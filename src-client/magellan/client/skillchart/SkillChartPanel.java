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

package magellan.client.skillchart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import com.jrefinery.chart.AxisNotCompatibleException;
import com.jrefinery.chart.DefaultCategoryDataSource;
import com.jrefinery.chart.HorizontalCategoryAxis;
import com.jrefinery.chart.JFreeChart;
import com.jrefinery.chart.Plot;
import com.jrefinery.chart.VerticalNumberAxis;

import magellan.client.event.EventDispatcher;
import magellan.client.event.SelectionEvent;
import magellan.client.event.SelectionListener;
import magellan.client.swing.InternationalizedDataPanel;
import magellan.library.Faction;
import magellan.library.GameData;
import magellan.library.ID;
import magellan.library.Region;
import magellan.library.Skill;
import magellan.library.Unit;
import magellan.library.rules.SkillType;
import magellan.library.utils.Resources;
import magellan.library.utils.SkillStats;
import magellan.library.utils.logging.Logger;

/**
 * A class painting barcharts out of skills of eressea units. The data with the units to be
 * considered is received solely by SelectionEvents.
 *
 * @author Ulrich K�ster
 */
public class SkillChartPanel extends InternationalizedDataPanel implements SelectionListener {
  private static final Logger log = Logger.getInstance(SkillChartPanel.class);
  private SkillChartJFreeChartPanel chartPanel;
  private JComboBox persons;
  private JComboBox totalSkillPoints;
  private JComboBox totalSkillLevel;
  private JComboBox skills;
  private SkillStats skillStats = new SkillStats();
  private Set<Region> regions;
  private Map<ID, Faction> factions = new Hashtable<ID, Faction>();
  private JCheckBox cumulative;

  /**
   * Creates a new SkillChartPanel. The data is received by SelectionEvents (where factions and
   * regions are considered). Despite of that an GameData-reference is necessary to set all regions as
   * data, if no region is selected
   */
  public SkillChartPanel(EventDispatcher ed, GameData data, Properties settings) {
    super(ed, data, settings);
    regions = new HashSet<Region>(data.getRegions());

    ed.addSelectionListener(this);

    // create axis, plot, chart
    HorizontalCategoryAxis xAxis =
        new HorizontalCategoryAxis(Resources
            .get("skillchart.skillchartpanel.labeltext.horizontalaxis"));
    VerticalNumberAxis yAxis =
        new VerticalNumberAxis(Resources.get("skillchart.skillchartpanel.labeltext.verticalaxis"));
    yAxis.setTickValue(1);
    yAxis.setAutoRangeIncludesZero(true);

    try {
      Plot verticalBarPlot = new VerticalBarPlot(null, xAxis, yAxis);
      DefaultCategoryDataSource dataSource = createDataSource(null);
      chartPanel =
          new SkillChartJFreeChartPanel(new JFreeChart("", new Font("Arial", Font.BOLD, 24),
              dataSource, verticalBarPlot), this);
    } catch (AxisNotCompatibleException e) { // work on this later...
      SkillChartPanel.log.warn(e);
    }

    JFreeChart chart = chartPanel.getChart();
    chart.setLegend(null);
    chart.setChartBackgroundPaint(new GradientPaint(0, 0, Color.white, 0, 1000, Color.blue));

    // chart.setChartBackgroundPaint(new GradientPaint(0, 0, Color.white, 0, 500, new Color(153,
    // 153, 204)));
    chart.getPlot().setInsets(new Insets(10, 20, 20, 40));

    // initialize the Panel with all the stuff
    GridBagLayout grid = new GridBagLayout();
    setLayout(grid);

    GridBagConstraints c =
        new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 2, 2);
    add(chartPanel, c);

    c =
        new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 2, 2);
    skills = new JComboBox();
    skills.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setSkillType();
      }
    });
    add(skills, c);

    persons =
        new JComboBox(new String[] { Resources
            .get("skillchart.skillchartpanel.labeltext.totalpersons") });
    c =
        new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 2, 2);
    add(persons, c);

    totalSkillPoints =
        new JComboBox(new String[] { Resources
            .get("skillchart.skillchartpanel.labeltext.totalskilllevel") });
    c =
        new GridBagConstraints(0, 2, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 2, 2);
    add(totalSkillPoints, c);

    if (data.noSkillPoints) {
      totalSkillPoints.setVisible(false);
    }

    totalSkillLevel =
        new JComboBox(new String[] { Resources
            .get("skillchart.skillchartpanel.labeltext.totalskillpoints") });
    c =
        new GridBagConstraints(1, 2, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 2, 2);
    add(totalSkillLevel, c);

    cumulative = new JCheckBox(Resources
        .get("skillchart.skillchartpanel.labeltext.cumulative"));
    cumulative.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setSkillType();
      }
    });
    c.gridx = 0;
    c.gridy = 3;
    add(cumulative, c);
  }

  protected void setSkillType() {
    SkillType skillType = (SkillType) skills.getSelectedItem();

    if (skillType != null) {
      chartPanel.getChart().setTitle(skillType.getName());
    } else {
      chartPanel.getChart().setTitle("");
    }

    setComboBoxes(skillType);
    chartPanel.getChart().setDataSource(createDataSource(skillType));
  }

  /**
   * just to reset the combo boxes
   */
  private void setComboBoxes(SkillType skillType) {
    if (skillType != null) {
      Vector<String> vPersons = new Vector<String>();
      Vector<String> vTotalSkillLevel = new Vector<String>();
      Vector<String> vTotalSkillPoints = new Vector<String>();
      vPersons.add(Resources.get("skillchart.skillchartpanel.labeltext.totalpersons")
          + skillStats.getPersonNumber(skillType));
      vTotalSkillLevel.add(Resources.get("skillchart.skillchartpanel.labeltext.totalskilllevel")
          + skillStats.getSkillLevelNumber(skillType));
      vTotalSkillPoints.add(Resources.get("skillchart.skillchartpanel.labeltext.totalskillpoints")
          + skillStats.getSkillPointsNumber(skillType));

      boolean isCumulative = cumulative.isSelected();
      String preLabel = isCumulative ? "T>=" : "T";
      int cumulativePersons = 0, cumulativeLevel = 0, cumulativePoints = 0;
      for (Skill skill : skillStats.getKnownSkills(skillType)) {
        int pNumber = skillStats.getPersonNumber(skill);
        if (isCumulative) {
          cumulativePersons += pNumber;
          pNumber = cumulativePersons;
        }
        vPersons.add(preLabel + skill.getLevel() + ": " + pNumber);

        int level = skillStats.getSkillLevelNumber(skill);
        if (isCumulative) {
          cumulativeLevel += level;
          level = cumulativeLevel;
        }
        vTotalSkillLevel.add(preLabel + skill.getLevel() + ": " + level);

        int points = skillStats.getSkillPointsNumber(skill);
        if (isCumulative) {
          cumulativePoints += points;
          points = cumulativePoints;
        }
        vTotalSkillPoints.add(preLabel + skill.getLevel() + ": "
            + points);
      }

      persons.setModel(new DefaultComboBoxModel(vPersons));
      totalSkillLevel.setModel(new DefaultComboBoxModel(vTotalSkillLevel));
      totalSkillPoints.setModel(new DefaultComboBoxModel(vTotalSkillPoints));
    } else {
      persons.setModel(new DefaultComboBoxModel(new String[] { Resources
          .get("skillchart.skillchartpanel.labeltext.totalpersons") }));
      totalSkillPoints.setModel(new DefaultComboBoxModel(new String[] { Resources
          .get("skillchart.skillchartpanel.labeltext.totalskilllevel") }));
      totalSkillLevel.setModel(new DefaultComboBoxModel(new String[] { Resources
          .get("skillchart.skillchartpanel.labeltext.totalskillpoints") }));
    }
  }

  /**
   * creates a DefaultCategoryDataSource as basis of a skillchart out of the SkillStats-Object of this
   * class and the specified skillType. If skillType is null, an empty datasource is created,
   * containing the single value (0,0)
   */
  private DefaultCategoryDataSource createDataSource(SkillType skillType) {
    Number dataArray[][];
    Vector<Object> names;

    if (skillType == null) {
      dataArray = new Number[][] { { Integer.valueOf(0) } };
      names = new Vector<Object>();
      names.add("");
    } else {
      int lowLevel = skillStats.getLowestKnownSkillLevel(skillType);
      int highLevel = skillStats.getHighestKnownSkillLevel(skillType);

      if (highLevel != 0) {
        highLevel++;
      }

      if (lowLevel != 0) {
        lowLevel--;
      }

      dataArray = new Number[1][highLevel - lowLevel + 1];
      names = new Vector<Object>();

      int loopCounter = 0;
      int cumulativeLevel = 0;

      for (int level = lowLevel; level <= highLevel; loopCounter++, level++) {
        names.add(Integer.valueOf(level));
      }
      for (int level = highLevel; level >= lowLevel; loopCounter++, level--) {
        Skill skill = new Skill(skillType, 1, level, 1, false);
        int skillLevel = skillStats.getPersonNumber(skill);
        if (cumulative.isSelected()) {
          cumulativeLevel += skillLevel;
          skillLevel = cumulativeLevel;
        }
        dataArray[0][level - lowLevel] = Integer.valueOf(skillLevel);
      }
    }

    return new DefaultCategoryDataSource(new Vector<Object>(), names, dataArray);
  }

  /**
   * Displays skill information only for selected objects (Factions or Regions).
   *
   * @see magellan.client.event.SelectionListener#selectionChanged(magellan.client.event.SelectionEvent)
   */
  public void selectionChanged(SelectionEvent e) {
    // we are only interested in the selectedObjects, not in activeObject
    // and there only in regions and factions
    if (e.getSelectedObjects() != null) {
      boolean modified = false;

      // empty Selection => set all region as data
      if (e.getSelectedObjects().isEmpty()) {
        regions.clear();

        if (getGameData() != null) {
          regions.addAll(getGameData().getRegions());
        }

        modified = true;
      } else {
        boolean rModified = false;
        boolean fModified = false;
        for (Object o : e.getSelectedObjects()) {
          if (o instanceof Region) {
            // some regions were selected:
            Region region = (Region) o;
            if (!rModified) {
              regions.clear();
            }
            rModified = true;

            regions.add(region);
          } else if (o instanceof Faction) {
            // a faction has been selected
            Faction faction = (Faction) o;
            if (!fModified) {
              factions.clear();
            }
            fModified = true;

            factions.put(faction.getID(), faction);
          }
        }
        modified = rModified || fModified;
      }

      if (modified) {
        // update the skillStats-Object to the new data (new factions or new regions)
        skillStats = new SkillStats();

        for (Region r : regions) {
          for (Unit u : r.units()) {
            if (factions.containsValue(u.getFaction())) {
              skillStats.addUnit(u);
            }
          }
        }

        skills.removeAllItems();

        for (SkillType skillType : skillStats.getKnownSkillTypes()) {
          skills.addItem(skillType);
        }

        if (skills.getItemCount() > 0) {
          skills.setSelectedIndex(0);
        }

        chartPanel.getChart().setDataSource(createDataSource((SkillType) skills.getSelectedItem()));
      }
    }
  }

  /**
   * @see magellan.client.swing.InternationalizedDataPanel#gameDataChanged(magellan.library.event.GameDataEvent)
   */
  @Override
  public void gameDataChanged(magellan.library.event.GameDataEvent e) {
    setGameData(e.getGameData());

    if (getGameData().noSkillPoints) {
      totalSkillPoints.setVisible(false);
    } else {
      totalSkillPoints.setVisible(true);
    }

    /**
     * Don't clear factions as the SelectionEvent of the updated List in FactionStatsDialog might be
     * processed befor the GameDataEvent
     */
    // factions.clear();
    // enforce refreshing of regions-table and redrawing of chart
    selectionChanged(SelectionEvent.create(this));
  }

  /**
   * returns a tooltip for the (i)th Bar of the chart
   */
  public String getToolTip(int i) {
    SkillType type = (SkillType) skills.getSelectedItem();

    if ((type == null) || (i >= skillStats.getKnownSkills(type).size()))
      return null;
    else {
      Skill skill = skillStats.getKnownSkills(type).get(i);
      String retVal =
          Resources.get("skillchart.skillchartpanel.labeltext.totalpersons")
              + skillStats.getPersonNumber(skill);
      retVal +=
          (", " + Resources.get("skillchart.skillchartpanel.labeltext.totalskilllevel") + skillStats
              .getSkillLevelNumber(skill));
      retVal +=
          (", " + Resources.get("skillchart.skillchartpanel.labeltext.totalskillpoints") + skillStats
              .getSkillPointsNumber(skill));

      return retVal;
    }
  }
}
