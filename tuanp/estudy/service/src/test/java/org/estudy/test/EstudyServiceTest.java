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
package org.estudy.test;

import java.util.ArrayList;
import java.util.Collection;

import org.estudy.learning.Util;
import org.estudy.learning.model.ECategory;
import org.estudy.learning.storage.DataStorage;
import org.estudy.learning.storage.impl.JcrDataStorage;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.MembershipEntry;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * May 2, 2013  
 */
public class EstudyServiceTest extends BaseServiceTestCase {

  private RepositoryService repositoryService_ ;
  private DataStorage  storage_;
  private static String   username = "root";
  public Collection<MembershipEntry> membershipEntries = new ArrayList<MembershipEntry>();
  private OrganizationService organizationService_;

  public void setUp() throws Exception {
    super.setUp();
    repositoryService_ = getService(RepositoryService.class);
    organizationService_ = (OrganizationService) getService(OrganizationService.class);
    storage_ = getService(JcrDataStorage.class);
  }

  private void loginUser(String userId) {
    Identity identity = new Identity(userId, membershipEntries);
    ConversationState state = new ConversationState(identity);
    ConversationState.setCurrent(state);
  }
  //mvn test -Dtest=EstudyServiceTest#testInitServices
  public void testInitServices() throws Exception{

      assertNotNull(repositoryService_) ;
      assertEquals(repositoryService_.getDefaultRepository().getConfiguration().getName(), "repository");
      assertEquals(repositoryService_.getDefaultRepository().getConfiguration().getDefaultWorkspaceName(), "portal-test");
      assertNotNull(organizationService_) ;

      //assertEquals(organizationService_.getUserHandler().findAllUsers().getSize(), 8);

      assertNotNull(storage_);

  }
  //mvn test -Dtest=EstudyServiceTest#testEStoreHome
  public void testEStoreHome() throws Exception {
	  
	  assertNotNull(storage_.getEStorageHome());
	  
	  assertEquals(Util.E_STUDY_APP,storage_.getEStorageHome().getName());
  }
  //mvn test -Dtest=EstudyServiceTest#testECategory
  public void testECategory() throws Exception {
	  ECategory e = new ECategory("Test category");
	  storage_.saveCategory(e);
	  assertNotNull(storage_.getCategories());
	  assertEquals(1, storage_.getCategories().size());
	  
  }

}
