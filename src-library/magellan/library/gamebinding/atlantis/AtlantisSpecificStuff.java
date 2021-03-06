// class magellan.library.gamebinding.AtlantisSpecificStuff
// created on Apr 16, 2013
//
// Copyright 2003-2013 by magellan project team
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
package magellan.library.gamebinding.atlantis;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import magellan.library.CompleteData;
import magellan.library.Faction;
import magellan.library.GameData;
import magellan.library.LongID;
import magellan.library.Message;
import magellan.library.Region;
import magellan.library.RegionResource;
import magellan.library.Rules;
import magellan.library.Unit;
import magellan.library.completion.Completer;
import magellan.library.completion.CompleterSettingsProvider;
import magellan.library.completion.OrderParser;
import magellan.library.gamebinding.EresseaConstants;
import magellan.library.gamebinding.EresseaPostProcessor;
import magellan.library.gamebinding.GameSpecificOrderWriter;
import magellan.library.gamebinding.GameSpecificRules;
import magellan.library.gamebinding.GameSpecificStuff;
import magellan.library.gamebinding.MapMergeEvaluator;
import magellan.library.gamebinding.MapMetric;
import magellan.library.gamebinding.MessageRenderer;
import magellan.library.gamebinding.MovementEvaluator;
import magellan.library.gamebinding.OrderChanger;
import magellan.library.gamebinding.RelationFactory;
import magellan.library.gamebinding.SimpleOrderFactory;
import magellan.library.gamebinding.e3a.E3AMapMergeEvaluator;
import magellan.library.io.GameDataIO;
import magellan.library.io.ReportParser;
import magellan.library.io.RulesReader;
import magellan.library.io.cr.CRGameNameIO;
import magellan.library.io.cr.CRParser;
import magellan.library.io.file.FileType;
import magellan.library.io.json.JSONGameNameIO;
import magellan.library.io.json.JSONParser;
import magellan.library.io.nr.NRGameNameIO;
import magellan.library.io.nr.NRParser;
import magellan.library.utils.OrderReader;
import magellan.library.utils.RadixTreeImpl;
import magellan.library.utils.UserInterface;
import magellan.library.utils.transformation.ReportTransformer;
import magellan.library.utils.transformation.TransformerFinder;

/**
 * Atlantis specific stuff!
 */
public class AtlantisSpecificStuff implements GameSpecificStuff {
  private static final String name = "Atlantis";

  private Rules rules;
  private GameSpecificRules gameSpecificRules;
  private MapMergeEvaluator mapMergeEvaluator;

  private RelationFactory relationFactory;

  protected long resourceId;

  private MapMetric metric;

  /**
   * Returns the value of rules.
   *
   * @return Returns rules.
   */
  public Rules getRules() {
    return rules;
  }

  /**
   *
   */
  public AtlantisSpecificStuff() {
    rules = new RulesReader().readRules(getName());
    metric = new AtlantisMapMetric(rules);
  }

  /**
   * This is a callback interface to let the GameSpecificStuff create the GameData object.
   *
   * @param gameName The game name (like "Eressea", "E3", ...)
   */
  public GameData createGameData(String gameName) {
    return new CompleteData(getRules(), gameName);
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getGameDataIO()
   */
  public GameDataIO getGameDataIO() {
    return null;
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#postProcess(magellan.library.GameData)
   */
  public void postProcess(GameData data) {
    resourceId = 0;
    // enforce locale to be non-null
    if (data.getLocale() == null) {
      data.setLocale(Locale.ENGLISH);
    }
    for (Faction f : data.getFactions()) {
      if (f.getLocale() == null) {
        f.setLocale(Locale.ENGLISH);
      }
    }
    for (Region region : data.getRegions()) {
      if (region.getRegionType().getID().equals(AtlantisConstants.RT_PLAIN)) {

        RegionResource res =
            new RegionResource(LongID.create(++resourceId), getRules().getItemType(
                AtlantisConstants.I_RHORSE));
        res.setDate(data.getDate().getDate());
        res.setAmount(200);
        region.addResource(res);

        region.setWage(15);
      } else if (region.getRegionType().getID().equals(AtlantisConstants.RT_MOUNTAIN)) {
        RegionResource res =
            new RegionResource(LongID.create(++resourceId), getRules().getItemType(
                AtlantisConstants.I_RSTONE));
        res.setDate(data.getDate().getDate());
        res.setAmount(200);
        region.addResource(res);
        res =
            new RegionResource(LongID.create(++resourceId), getRules().getItemType(
                AtlantisConstants.I_RIRON));
        res.setDate(data.getDate().getDate());
        res.setAmount(200);
        region.addResource(res);

        region.setWage(12);
      } else if (region.getRegionType().getID().equals(AtlantisConstants.RT_FOREST)) {
        RegionResource res =
            new RegionResource(LongID.create(++resourceId), getRules().getItemType(
                AtlantisConstants.I_RTREES));
        res.setDate(data.getDate().getDate());
        res.setAmount(200);
        region.addResource(res);

        region.setWage(12);
      } else if (region.getRegionType().getID().equals(AtlantisConstants.RT_SWAMP)) {
        RegionResource res =
            new RegionResource(LongID.create(++resourceId), getRules().getItemType(
                AtlantisConstants.I_RTREES));
        res.setDate(data.getDate().getDate());
        res.setAmount(100);
        region.addResource(res);

        region.setWage(12);
      }
    }

    // // adding Default Translations to the translations
    // data.postProcessDefaultTranslations();

    data.postProcessUnknown();

    // remove double messages
    data.postProcessMessages();

    // postProcessMessages(data);

    // // remove double potions
    // data.postProcessPotions();

    EresseaPostProcessor.resolveWraparound(data);

    // cleanAstralSchemes(data);

    data.postProcessIslands();

    // adjustFogOfWar2Visibility(data);

    int sortIndex = data.getUnits().size();
    for (Unit unit : data.getUnits()) {
      sortIndex = unit.extractTempUnits(data, sortIndex);
    }

    // postProcessNullInformation(data);

  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#postProcessAfterTrustlevelChange(magellan.library.GameData)
   */
  public void postProcessAfterTrustlevelChange(GameData data) {
    // currently nothing to do
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getOrderChanger()
   */
  public OrderChanger getOrderChanger() {
    return new AtlantisOrderChanger(getRules(), new SimpleOrderFactory());
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getRelationFactory()
   */
  public RelationFactory getRelationFactory() {
    // TODO implement
    if (relationFactory == null) {
      relationFactory = new AtlantisRelationFactory(getRules());
    }
    return relationFactory;
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getMovementEvaluator()
   */
  public MovementEvaluator getMovementEvaluator() {
    return new AtlantisMovementEvaluator(getRules());
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getCompleter(magellan.library.GameData,
   *      magellan.library.completion.CompleterSettingsProvider)
   */
  public Completer getCompleter(GameData data, CompleterSettingsProvider csp) {
    AtlantisOrderCompleter completer = new AtlantisOrderCompleter(data, csp);
    OrderParser parser = getOrderParser(data, completer);
    completer.setParser(parser);
    return completer;

  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getOrderParser(magellan.library.GameData)
   */
  public OrderParser getOrderParser(GameData data) {
    return getOrderParser(data, null);
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getOrderParser(magellan.library.GameData)
   */
  public OrderParser getOrderParser(GameData data, AtlantisOrderCompleter completer) {
    return new AtlantisOrderParser(data, completer);
  }

  /**
   * Delivers the Eressea specific Message Renderer (as of CR VERSION 41)
   *
   * @param data - A GameData object to enrich the messages with names of units, regions ,...
   * @return the new EresseaMessageRenderer for rendering ONE message
   * @see magellan.library.gamebinding.GameSpecificStuff#getMessageRenderer(magellan.library.GameData)
   */
  public MessageRenderer getMessageRenderer(GameData data) {
    return new MessageRenderer() {

      public String renderMessage(Message msg) {
        return msg.getText();
      }
    };
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getMapMergeEvaluator()
   */
  public MapMergeEvaluator getMapMergeEvaluator() {
    if (mapMergeEvaluator == null) {
      mapMergeEvaluator = new E3AMapMergeEvaluator(rules);
    }

    return mapMergeEvaluator;
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getOrderWriter()
   */
  public GameSpecificOrderWriter getOrderWriter() {
    return new AtlantisOrderWriter();
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getGameSpecificRules()
   */
  public GameSpecificRules getGameSpecificRules() {
    if (gameSpecificRules == null) {
      gameSpecificRules = new AtlantisGameSpecificRules(rules);
    }
    return gameSpecificRules;
  }

  public String getName() {
    return name;
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getTransformers(magellan.library.GameData,
   *      magellan.library.GameData, magellan.library.utils.UserInterface, boolean)
   */
  public ReportTransformer[] getTransformers(GameData globalData, GameData addedData,
      UserInterface ui, boolean interactive) {

    return (new TransformerFinder(globalData, addedData, ui, interactive, true)).getTransformers();
  }

  private static final SortedMap<Integer, String> combatStates = new TreeMap<Integer, String>();

  static {
    combatStates.put(1, "unit.combatstatus.front");
    combatStates.put(2, "unit.combatstatus.back");
  }

  /**
   * @see magellan.library.gamebinding.GameSpecificStuff#getCombatStates()
   */
  public Map<Integer, String> getCombatStates() {
    return combatStates;
  }

  public CoordMapper getCoordMapper() {
    // return GameSpecificStuff.UNIT_MAPPER;
    return new CoordMapper() {
      public float getYY(int y) {
        return (1.0f * (y + 1.0f));
      }

      public float getXY(int x) {
        return -.5f * x;
      }

      public float getXX(int x) {
        return 1f * x;
      }

      public float getYX(int y) {
        return 0;
      }
    };
  }

  public ReportParser getParser(FileType aFileType) throws IOException {
    // FIXME code duplication, see Eressea/E3/AtlantisSpecificStuff
    if (aFileType.getInnerName().endsWith(FileType.CR))
      return new CRParser(null);
    if (aFileType.getInnerName().endsWith(FileType.JSON))
      return new JSONParser(null);

    try {
      if (new CRGameNameIO().getGameName(aFileType) != null)
        return new CRParser(null);
    } catch (IOException e) {
      // try something else
    }

    try {
      if (new JSONGameNameIO().getGameName(aFileType) != null)
        return new JSONParser(null);
    } catch (IOException e) {
      // try something else
    }

    try {
      if (new NRGameNameIO().getGameName(aFileType) != null)
        return new NRParser(null);
    } catch (IOException e) {
      // try something else
    }

    return null;
  }

  public OrderReader getOrderReader(final GameData data) {
    return new OrderReader(data) {
      public String getCheckerName() {
        return null;
      }

      @Override
      protected void initHandlers() {
        handlers = new RadixTreeImpl<OrderReader.LineHandler>();
        addHandler(data.getRules().getOrderfileStartingString(), new StartingHandler());
        // addHandler(getOrderTranslation(EresseaConstants.OC_REGION), new RegionHandler());
        addHandler(getOrderTranslation(EresseaConstants.OC_UNIT), new UnitHandler());
      }

      @Override
      protected void endHandler() {
        // no NEXT necessary
        data.postProcess();
      }

      @Override
      protected String normalize(String token) {
        return token.trim().toLowerCase();
      }

      @Override
      protected List<LineHandler> getHandlers(String token) {
        LineHandler found = handlers.find(normalize(token));
        if (found != null)
          return Collections.singletonList(found);
        else
          return Collections.emptyList();
      }

    };
  }

  public MapMetric getMapMetric() {
    return metric;
  }
}
