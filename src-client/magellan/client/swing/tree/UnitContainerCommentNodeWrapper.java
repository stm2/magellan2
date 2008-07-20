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

package magellan.client.swing.tree;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import magellan.library.UnitContainer;


/**
 * DOCUMENT-ME
 *
 * @author $Author: $
 * @version $Revision: 171 $
 */
public class UnitContainerCommentNodeWrapper implements CellObject, SupportsClipboard {
	private String comment = null;
	private UnitContainer uc = null;
	private List<String> iconNames = null;

	/**
	 * Creates a new BorderNodeWrapper object.
	 *
	 * 
	 */
	public UnitContainerCommentNodeWrapper(UnitContainer uc,String comment) {
		this.comment = comment;
		this.uc=uc;
	}

	
	/**
	 * DOCUMENT-ME
	 *
	 * 
	 */
	@Override
  public String toString() {
		return this.comment;
	}

	/**
	 * DOCUMENT-ME
	 *
	 * 
	 */
	public List<String> getIconNames() {
		if(iconNames == null) {
			iconNames = Collections.singletonList("comment_marker");
		}

		return iconNames;
	}

	/**
	 * DOCUMENT-ME
	 *
	 * 
	 */
	public boolean emphasized() {
		return false;
	}

	/**
	 * DOCUMENT-ME
	 */
	public void propertiesChanged() {
	}

	/**
	 * DOCUMENT-ME
	 *
	 * 
	 * 
	 *
	 * 
	 */
	public NodeWrapperDrawPolicy init(Properties settings, NodeWrapperDrawPolicy adapter) {
		return null;
	}

	/**
	 * DOCUMENT-ME
	 *
	 * 
	 * 
	 * 
	 *
	 * 
	 */
	public NodeWrapperDrawPolicy init(Properties settings, String prefix,
									  NodeWrapperDrawPolicy adapter) {
		return null;
	}

	/**
	 * DOCUMENT-ME
	 *
	 * 
	 */
	public String getClipboardValue() {
		if(comment != null) {
			return comment;
		} else {
			return "";
		}
	}


	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}


	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}




	/**
	 * @return the uc
	 */
	public UnitContainer getUnitContainer() {
		return uc;
	}
}
