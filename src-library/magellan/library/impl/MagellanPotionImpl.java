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

package magellan.library.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import magellan.library.ID;
import magellan.library.Item;
import magellan.library.Potion;
import magellan.library.utils.OrderedHashtable;


/**
 * Container class for a potion based on its representation in a cr version >= 42.
 */
public class MagellanPotionImpl extends MagellanDescribedImpl implements Potion {
	private int level = -1;

	/** The ingredients needed for this potion. The list contains <tt>String</tt> objects. */
	private Map<ID,Item> ingredients = null;

	/**
	 * Constructs a new Potion object identified by id.
	 *
	 * 
	 */
	public MagellanPotionImpl(ID id) {
		super(id);
	}

	/**
	 * Sets the level of this Potion.
	 *
	 * 
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Returns the level of this Potion.
	 *
	 * 
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Returns the ingredients required for this potion. The elements are instances of class Item.
	 *
	 * 
	 */
	public Collection<Item> ingredients() {
    if (ingredients != null && ingredients.values() != null) {
      return Collections.unmodifiableCollection(ingredients.values());
    }
		return Collections.emptyList();
	}

	/**
	 * Returns a specific ingredient of this potion.
	 *
	 * @param key the item type id of the ingredient to be returned.
	 *
	 * 
	 */
	public Item getIngredient(ID key) {
		if(this.ingredients != null) {
			return this.ingredients.get(key);
		}

		return null;
	}

	/**
	 * Puts a new element into the list of ingredients required to brew this potion.
	 *
	 * 
	 *
	 * 
	 */
	public Item addIngredient(Item i) {
		if(this.ingredients == null) {
			this.ingredients = new OrderedHashtable<ID, Item>();
		}

		this.ingredients.put(i.getItemType().getID(), i);

		return i;
	}

	/**
	 * Removes an item from the list of ingredients required to brew this potion.
	 *
	 * @param key the id of the item's item type to be removed.
	 *
	 * 
	 */
	public Item removeIngredient(ID key) {
		if(this.ingredients != null) {
			return this.ingredients.remove(key);
		}

		return null;
	}

	/**
	 * Removes all ingredients of this potion.
	 */
	public void clearIngredients() {
		if(this.ingredients != null) {
			this.ingredients.clear();
			this.ingredients = null;
		}
	}
}
