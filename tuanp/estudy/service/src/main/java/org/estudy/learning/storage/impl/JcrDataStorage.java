/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.estudy.learning.storage.impl;

import java.util.ArrayList;
import java.util.Collection;

import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.estudy.learning.Util;
import org.estudy.learning.model.ECategory;
import org.estudy.learning.model.ESession;
import org.estudy.learning.model.ETesting;
import org.estudy.learning.storage.DataStorage;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationConfig.User;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * May 2, 2013  
 */
public class JcrDataStorage implements DataStorage {
  private NodeHierarchyCreator nodeHierarchyCreator_;
  private RepositoryService repoService_;
  private SessionProviderService sessionProviderService_;
  private static final Log       log                 = ExoLogger.getLogger("org.estudy.learning.storage.JcrDataStorage");
  public JcrDataStorage(NodeHierarchyCreator nodeHierarchyCreator, RepositoryService repoService){
    nodeHierarchyCreator_ = nodeHierarchyCreator;
    repoService_ = repoService;
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    sessionProviderService_ = (SessionProviderService) container.getComponentInstanceOfType(SessionProviderService.class);
  }

  public Node getEStorageHome() throws RepositoryException, Exception {
    SessionProvider sProvider = createSessionProvider();
    Node publicApp = nodeHierarchyCreator_.getPublicApplicationNode(sProvider); 
    try {
      return publicApp.getNode(Util.E_STUDY_APP);
    } catch (PathNotFoundException e) {
      Node eApp = publicApp.addNode(Util.E_STUDY_APP, Util.NT_UNSTRUCTURED);
      publicApp.getSession().save();
      return eApp;
    }
  }
  
  private Node getECategoryHome() throws RepositoryException, Exception {
	  Node eHome = getEStorageHome();
	  try {
		return  eHome.getNode(Util.E_STUDY_CAT);
	} catch (PathNotFoundException e) {
		eHome.addNode(Util.E_STUDY_CAT, Util.NT_UNSTRUCTURED);
		eHome.getSession().save();
		return eHome.getNode(Util.E_STUDY_CAT);
	}  
	 
	  
  }
  
  public SessionProvider createSessionProvider() {
    SessionProvider provider = sessionProviderService_.getSessionProvider(null);
    if (provider == null) {
      log.info("No user session provider was available, trying to use a system session provider");
      provider = sessionProviderService_.getSystemSessionProvider(null);
    }
    return SessionProvider.createSystemProvider();
  }

@Override
public void saveCategory(ECategory category) throws ItemExistsException{
	try {
		Node catHome = getECategoryHome();
		Node cat = catHome.addNode(category.getId(), ECategory.NT_NAME);
		cat.setProperty(ECategory.P_NAME, category.getName());
		cat.addMixin(Util.MIX_REFERENCEABLE);
		cat.save();
	} catch (ItemExistsException e) {
		throw e;
	} catch (Exception re) {
		re.printStackTrace();
	}
	
}

@Override
public Collection<ECategory> getCategories() throws Exception {
	try {
		Node catHome = getECategoryHome();
		NodeIterator it = catHome.getNodes();
		Collection<ECategory> list = new ArrayList<ECategory>();
		while (it.hasNext()) {
			list.add(new ECategory(it.nextNode()));
		}
		return list;
	} catch (Exception e){
		e.printStackTrace();
	}
	return null;
}

@Override
public ECategory getCategory(String name) throws ItemNotFoundException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void saveSession(ESession session) throws RepositoryException {
	// TODO Auto-generated method stub
	
}

@Override
public Collection<ESession> getSessions() throws RepositoryException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public ESession getSession(String id) throws ItemNotFoundException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void saveTesting(User user) throws RepositoryException {
	// TODO Auto-generated method stub
	
}

@Override
public ETesting getTestingScore(String uid) throws ItemNotFoundException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void removeCategory(String id) throws Exception {
	// TODO Auto-generated method stub
	
}
}
