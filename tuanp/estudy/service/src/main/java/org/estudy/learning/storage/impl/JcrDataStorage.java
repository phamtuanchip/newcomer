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

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;

import org.estudy.learning.Util;
import org.estudy.learning.storage.DataStorage;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

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

  public Node getElearningStorageHome() throws Exception {
    SessionProvider sProvider = createSessionProvider();
    Node publicApp = nodeHierarchyCreator_.getPublicApplicationNode(sProvider); 
    try {
      return publicApp.getNode(Util.ESTUDY_APP);
    } catch (PathNotFoundException e) {
      Node calendarApp = publicApp.addNode(Util.ESTUDY_APP, Util.NT_UNSTRUCTURED);
      publicApp.getSession().save();
      return calendarApp;
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
}
