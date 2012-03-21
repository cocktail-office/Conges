package fr.univlr.cri.conges;

/*
 * Copyright 2003-2004 CRI, Universite de La Rochelle
 *
 * This file is part of CRIWebExt framework.
 *
 * CRIWebExt is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * CRIWebExt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import fr.univlr.cri.util.StringCtrl;
import fr.univlr.cri.webapp.CRIWebComponent;

/**
 * Ce composant permet d'afficher le contenu d'un display group
 * (<code>WODisplayGroup</code>) sur plusieurs pages. Il fournit
 * les elements graphiques permettant de naviguer entre differentes pages. 
 * 
 * <p><div align="center"><img src="../../../../doc-files/CRINavigationBar-1.gif" border="1"/></div>
 * 
 * <p>Le nombre des pages depend du nombre d'elements d'un display group et
 * du nombre d'elements affiches sur une page. Celui-ci est indique a l'aide
 * de la methode <code>setNumberOfObjectsPerBatch</code> de
 * <code>WODisplayGroup</code>.
 * <font class="importantNote">Vous devez definir cette valeur explicitement
 * car le composant <code>CRINavigationBar</code> ne possede pas d'attributs
 * permettant de le faire.</font>
 *
 *
 * <h3>Bindings</h3>
 * 
 * <p>
 * <table width="95%" border="0" cellspacing="2" cellpadding="3" align="center">
 * <tr align="center" class="paramBoxHeader">
 * <td>Connecteur</td><td>Definition</td><td>Valeur</td><td>Description</td>
 * </tr>
 * <tr align="center" valign="top" class="paramBoxContents">
 * <td><code>displayGroup</code></td>
 * <td>obligatoire</td>
 * <td><code>WODisplayGroup</code><br>[<i>null</i>]</td>
 * <td align="left">Indique le display group dont les elements seront
 *   affiches sur plusieurs pages.</td>
 * </tr>
 * <tr align="center" valign="top" class="paramBoxContents">
 * <td><code>shortPageList</code></td>
 * <td>optionnel</td>
 * <td><code>boolean</code><br>[<i>false</i>]</td>
 * <td align="left">Indique la maniere dans laquelle la liste des pages est
 *   representee. L'affichage peut avoir le format court (valeur
 *   <i>true</i>)&nbsp;: <code>x/n</code> (page <code>x</code> sur
 *   <code>n</code>). Le format peut etre long (valeur <i>false</i>)&nbsp;:
 *   <code>1 2 ... n</code>.</td>
 * </tr>
 * <tr align="center" valign="top" class="paramBoxContents">
 * <td><code>textMode</code></td>
 * <td>optionnel</td>
 * <td><code>boolean</code><br>[<i>false</i>]</td>
 * <td align="left">Indique si les controles de navigation entre les pages
 *   doivent etre affiches en format texte. Par defaut, les images sont
 *   utilises pour afficher les controles.</td>
 * </tr>
 * <tr align="center" valign="top" class="paramBoxContents">
 * <td><code>hideControls</code></td>
 * <td>optionnel</td>
 * <td><code>boolean</code><br>[<i>false</i>]</td>
 * <td align="left">Indique si les controles de navigation doivent etre
 *   caches. Par defaut, les controles sont affiches.</td>
 * </tr>
 * <tr align="center" valign="top" class="paramBoxContents">
 * <td><code>longListLimit</code></td>
 * <td>optionnel</td>
 * <td><code>int</code><br>[<i>0</i>]</td>
 * <td align="left">Le nombre maximal des pages affiche sous forme de la
 *   liste longue. Cette valeur est ignoree si le connecteur
 *   <code>shortPageList</code> est defini a <i>true</i>. Dans le cas
 *   contraire, si le nombre total des pages affichees depasse la valeur
 *   <code>longListLimit</code>, alors la liste des pages sera affichee
 *   comme une liste courte, meme si la valeur de <code>shortPageList</code>
 *   est false.</td>
 * </tr>
 * </table>
 * </p>
 */

/**
 * Version modifiée pour que le composant ne fasse pas de cache sur le displaygroup
 * et ainsi permet de changer son contenu via setObjectArray de façon dynamique
 */
public class CRINavigationBar extends CRIWebComponent {
  // Les noms des bindings du composant
  private static final String BND_SHORT_PAGE_LIST = "shortPageList";
  private static final String BND_TEXT_MODE = "textMode"; 
  private static final String BND_DISPLAY_GROUP = "displayGroup";
  private static final String BND_HIDE_CONTROLS = "hideControls";
  private static final String BND_LONG_LIST_LIMIT = "longListLimit";
  
  /**
   * Le composant retourne lorsque la page affichee est changee. 
   */
  private WOComponent returnPage;
  
  /**
   * La liste de numeros des toutes les pages.
   */
  private NSMutableArray pagesList;
  
  /**
   * Le numero de la page en cours
   */
  public String numPage;
  
  /**
   * Le display groupe dont les elements sont affiches
   * sur plusieurs pages.
   */
  /*private WODisplayGroup objectGroup;*/

  /**
   * Cree un nouveau composant dans le context donne. 
   */
  public CRINavigationBar(WOContext context) {
    super(context);
    pagesList = new NSMutableArray();
    // if (context().page() instanceof ScrollContainer) {
      // containerPage = (ScrollContainer)context().page();
      // containerPage.setScroll(this);
      // ((ScrollContainer)context().page()).setScroll(this);
    // }
    returnPage = context().page();
    //objectGroup = null;
  }

  /**
   * Renvoie la valeur <i>false</i>.
   * 
   * <p>Voir la definition de cette methode dans la documentation
   * de la classe <code>WOComponent</code> (WebObjects).
   */
  public boolean synchronizesVariablesWithBindings() {
    return false;
  }
/*
  public void awake() {
    displayGroup();
  }*/
  
  public boolean isTextMode() {
    if (hasBinding(BND_TEXT_MODE))
      return ((Boolean)valueForBinding(BND_TEXT_MODE)).booleanValue();
    return false;
  }

  public boolean isShortPageList() {
    boolean respShort = false;
    if (hasBinding(BND_SHORT_PAGE_LIST))
      respShort = ((Boolean)valueForBinding(BND_SHORT_PAGE_LIST)).booleanValue();
    if ((!respShort) && (longListLimit() > 0) && (longListLimit() <= pagesTotal()))
      respShort = true;
    return respShort;
  }

  public boolean isHideControls() {
    if (hasBinding(BND_HIDE_CONTROLS))
      return ((Boolean)valueForBinding(BND_HIDE_CONTROLS)).booleanValue();
    return false;
  }

  private int longListLimit() {
    if (hasBinding(BND_LONG_LIST_LIMIT))
      return ((Number)valueForBinding(BND_LONG_LIST_LIMIT)).intValue();
    return 0;
  }
  
  //public WODisplayGroup displayGroup() {
  public WODisplayGroup objectGroup() {
   /*  if (objectGroup == null) {
      if (hasBinding(BND_DISPLAY_GROUP)) {
        objectGroup = (WODisplayGroup)valueForBinding(BND_DISPLAY_GROUP);
        getPagesList();
      }
    }
    LRLog.log(">>"+objectGroup.allObjects().count());
    return objectGroup;*/
  	return (WODisplayGroup)valueForBinding(BND_DISPLAY_GROUP);
  }

  
  public void setReturnPage(WOComponent newReturnPage) {
    returnPage = newReturnPage;
  }

  public NSArray getPagesList() {
    if (pagesList.count() != objectGroup().batchCount()) {
      pagesList.removeAllObjects();
      for(int i=1; i<=objectGroup().batchCount(); i++)
        pagesList.addObject(String.valueOf(i));
    }
    return pagesList;
  }

  public boolean hasNextPage() {
    return (objectGroup().currentBatchIndex() < objectGroup().batchCount());
  }

  public boolean hasPreviousPage() {
    return (objectGroup().currentBatchIndex() > 1);
  }

  public boolean isCurrentPage() {
    return (StringCtrl.toInt(numPage, 1) == objectGroup().currentBatchIndex());
  }

  public WOComponent clickFormNumPage() {
    return returnPage;
  }
  
  public WOComponent clickNumPage() {
    setCurrentPageNum(numPage);
    return returnPage;
  }

  public WOComponent clickNextPage() {
    if (hasNextPage()) objectGroup().displayNextBatch();
    return returnPage;
  }

  public WOComponent clickPrevPage() {
    if (hasPreviousPage()) objectGroup().displayPreviousBatch();
    return returnPage;
  }

  public WOComponent clickFirstPage() {
    if (objectGroup().batchCount() > 0) objectGroup().setCurrentBatchIndex(1);
    return returnPage;
  }

  public WOComponent clickLastPage() {
    objectGroup().setCurrentBatchIndex(objectGroup().batchCount());
    return returnPage;
  }

  public String getCurrentPageNum() {
    return Integer.toString(objectGroup().currentBatchIndex());
  }

  public void setCurrentPageNum(String numPage) {
    int nPage = StringCtrl.toInt(numPage, -1); 
    if (nPage < 0) nPage = 1;
    if (nPage > pagesTotal()) nPage = pagesTotal();
    objectGroup().setCurrentBatchIndex(nPage);
  }
  
  public int pagesTotal() {
    return objectGroup().batchCount();
  }
}
