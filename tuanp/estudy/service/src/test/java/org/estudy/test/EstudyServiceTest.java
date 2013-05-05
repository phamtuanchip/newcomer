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
import java.util.Arrays;
import java.util.Collection;

import javax.jcr.ItemExistsException;

import org.estudy.learning.Util;
import org.estudy.learning.model.ECategory;
import org.estudy.learning.model.EQuestion;
import org.estudy.learning.model.ESession;
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
		storage_.saveCategory(e, true);
		assertNotNull(storage_.getCategories());
		assertEquals(1, storage_.getCategories().size());

		e.setName("new name");
		storage_.saveCategory(e, false);
		assertEquals("new name", storage_.getCategory(e.getId()).getName());

		try {
			storage_.saveCategory(new ECategory("new name"), true);
		} catch (ItemExistsException e2) {
			log.info("category already exits!");
			assertTrue(true);
		}
		assertEquals(1, storage_.getCategories().size());

		storage_.removeCategory(e.getId());
		assertEquals(0, storage_.getCategories().size());


	}

	//mvn test -Dtest=EstudyServiceTest#testEQuestion
		public void testEQuestion() throws Exception {
			 
			String title = "what is <a> tag?";
			String[] answers = new String[]{"is html tag", "none of above", "a link tag", "a special char"};
			String[] correct = new String[]{"is html tag","a link tag"};
			String[] wrong1 = new String[]{"is html tag"};
			String[] wrong2 = new String[]{"is html tag","a link tag","a special char"};
			String[] wrong3 = new String[]{"is html tag","a special char"};
			long point = 2 ;
			EQuestion eq = new EQuestion();
			eq.setTitle(title);
			eq.setAnswers(Arrays.asList(answers));
			eq.setCorrect(Arrays.asList(correct));
			eq.setPoint(point);
			storage_.saveQuestion(eq, true);
			
			assertNotNull(storage_.getQuestions());
			assertEquals(1, storage_.getQuestions().size());
			assertEquals(title,  storage_.getQuestion(eq.getId()).getTitle());
			assertEquals(Arrays.asList(answers),  storage_.getQuestion(eq.getId()).getAnswers());
			String[] answered = wrong1 ;
			eq.setAnswered(Arrays.asList(answered));
			storage_.saveQuestion(eq, false);
			assertEquals(Arrays.asList(answered),  storage_.getQuestion(eq.getId()).getAnswered());
			assertEquals(0, storage_.getQuestion(eq.getId()).getPoint());
			
			answered = wrong2 ;
			eq.setAnswered(Arrays.asList(answered));
			storage_.saveQuestion(eq, false);
			assertEquals(Arrays.asList(answered),  storage_.getQuestion(eq.getId()).getAnswered());
			assertEquals(0, storage_.getQuestion(eq.getId()).getPoint());
			
			answered = wrong3 ;
			eq.setAnswered(Arrays.asList(answered));
			storage_.saveQuestion(eq, false);
			assertEquals(Arrays.asList(answered),  storage_.getQuestion(eq.getId()).getAnswered());
			assertEquals(0, storage_.getQuestion(eq.getId()).getPoint());
			
			answered = correct ;
			eq.setAnswered(Arrays.asList(answered));
			storage_.saveQuestion(eq, false);
			
			assertEquals(point, storage_.getQuestion(eq.getId()).getPoint());
			
			try {
				storage_.saveQuestion(new EQuestion(title), true);
			} catch (ItemExistsException e2) {
				log.info("question or answer already exits!");
				assertTrue(true);
			}
			assertEquals(1, storage_.getQuestions().size());

			storage_.removeQuestion(eq.getId());
			assertEquals(0, storage_.getQuestions().size());
		}

	
	//mvn test -Dtest=EstudyServiceTest#testESession
	public void testESession() throws Exception {
		ECategory e = new ECategory("Test category");
		storage_.saveCategory(e, true);
		ESession es = new ESession();
		String title = "study about HTML";
		es.setTitle(title);
		es.setCat(e.getId());
		es.setDec("some description");
		String question = "quest1"+Util.SEMI_COLON+"quest2";
		es.setQuest(question);
		es.setVlink("htt://youtube.com");
		es.setRflink("htt://w3chool.com");
		
		storage_.saveSession(es, true);
		
		assertNotNull(storage_.getSessions());
		assertEquals(1, storage_.getSessions().size());
		assertEquals(title,  storage_.getSession(es.getId()).getTitle());
		assertEquals(Arrays.asList(question.split(Util.SEMI_COLON)),  storage_.getSession(es.getId()).getQuest());

		try {
			storage_.saveSession(new ESession(title), true);
		} catch (ItemExistsException e2) {
			log.info("session already exits!");
			assertTrue(true);
		}
		assertEquals(1, storage_.getSessions().size());

		storage_.removeSession(es.getId());
		assertEquals(0, storage_.getSessions().size());
	}

}
