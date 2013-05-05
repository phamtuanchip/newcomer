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
import java.util.Arrays;
import java.util.Collection;

import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.estudy.learning.Util;
import org.estudy.learning.model.ECategory;
import org.estudy.learning.model.EQuestion;
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


	private Node setSessionProp(ESession e, Node ses){
		try {
			ses.setProperty(ESession.P_TITLE, e.getTitle());
			ses.setProperty(ESession.P_CAT, e.getCat());
			ses.setProperty(ESession.P_QUEST, e.getQuest().toArray(new String[]{}));
			ses.setProperty(ESession.P_DEC, e.getDec());
			ses.setProperty(ESession.P_RFLINK, e.getRflink());
			ses.setProperty( ESession.P_VLINK, e.getVlink());
		} catch (Exception e2) {
			log.info(e2.getMessage());
			return null ;
		}
		return ses;
	}

	private Node setQuestionProp(EQuestion e, Node ses){
		try {
			ses.setProperty(EQuestion.P_TITLE, e.getTitle());
			ses.setProperty(EQuestion.P_ANSWER, e.getAnswers().toArray(new String[]{}));
			if(e.getAnswered() != null)
			ses.setProperty(EQuestion.P_ANSWERED, e.getAnswered().toArray(new String[]{}));
			ses.setProperty(EQuestion.P_CORRECT, e.getCorrect().toArray(new String[]{}));
			ses.setProperty(EQuestion.P_POINT, e.getPoint());
		} catch (Exception e2) {
			e2.printStackTrace();
			log.info(e2.getMessage());
			return null ;
		}
		return ses;
	}

	private EQuestion getQuestionProp(Node ses){
		EQuestion e = new EQuestion();
		try {
			e.setId(ses.getName());
			if(ses.hasProperty(EQuestion.P_TITLE)) 
				e.setTitle(ses.getProperty(EQuestion.P_TITLE).getString());
			if(ses.hasProperty(EQuestion.P_ANSWER)) 
			{
				Value[] values = ses.getProperty(EQuestion.P_ANSWER).getValues();
				e.setAnswers(valuesToString(values, Util.SEMI_COLON));
			}

			if(ses.hasProperty(EQuestion.P_ANSWERED)) 
			{
				Value[] values = ses.getProperty(EQuestion.P_ANSWERED).getValues();
				e.setAnswered(valuesToString(values, Util.SEMI_COLON));
			}
			
			if(ses.hasProperty(EQuestion.P_CORRECT)) 
			{
				Value[] values = ses.getProperty(EQuestion.P_CORRECT).getValues();
				e.setCorrect(valuesToString(values, Util.SEMI_COLON));
			}
			if(ses.hasProperty(EQuestion.P_POINT)) {
				if(e.getAnswered()!= null && e.getAnswered().size() == e.getCorrect().size() && e.getAnswered().containsAll(e.getCorrect()))  {
					log.info("val ===============" + ses.getProperty(EQuestion.P_POINT).getLong());
					
					e.setPoint(ses.getProperty(EQuestion.P_POINT).getLong());
				}
				else e.setPoint(0);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			log.info(e2.getMessage());
			return null ;
		}
		return e;
	}

	private ESession getSessionProp(Node ses){
		ESession e = new ESession();
		try {
			e.setId(ses.getName());
			if(ses.hasProperty(ESession.P_TITLE)) 
				e.setTitle(ses.getProperty(ESession.P_TITLE).getString());
			if(ses.hasProperty(ESession.P_CAT)) 
				e.setCat(ses.getProperty(ESession.P_CAT).getString());
			if(ses.hasProperty(ESession.P_QUEST)) 
			{
				Value[] values = ses.getProperty(ESession.P_QUEST).getValues();
				e.setQuest(valuesToString(values, Util.SEMI_COLON));
			}
			if(ses.hasProperty(ESession.P_DEC)) 
				e.setDec(ses.getProperty(ESession.P_DEC).getString());
			if(ses.hasProperty(ESession.P_RFLINK)) 
				e.setRflink(ses.getProperty(ESession.P_RFLINK).getString());
			if(ses.hasProperty(ESession.P_VLINK)) 
				e.setVlink(ses.getProperty(ESession.P_VLINK).getString());
		} catch (Exception e2) {
			log.info(e2.getMessage());
			return null ;
		}
		return e;
	}

	private Node setCategoryProp(ECategory c, Node cat){
		try {
			cat.setProperty(ECategory.P_NAME, c.getName());
		} catch (Exception e2) {
			log.info(e2.getMessage());
			return null ;
		}
		return cat;
	}

	private ECategory getCategoryProp(Node cat){
		ECategory c = new ECategory();
		try {
			c.setId(cat.getName());
			if(cat.hasProperty(ECategory.P_NAME)) 
				c.setName(cat.getProperty(ECategory.P_NAME).getString());
		} catch (Exception e2) {
			log.info(e2.getMessage());
			return null ;
		}
		return c;
	}

	private Node getESessionHome() throws Exception {
		Node eHome = getEStorageHome();
		try {
			return  eHome.getNode(Util.E_STUDY_SES);
		} catch (PathNotFoundException e) {
			eHome.addNode(Util.E_STUDY_SES, Util.NT_UNSTRUCTURED);
			eHome.getSession().save();
			return eHome.getNode(Util.E_STUDY_SES);
		}  
	}

	private boolean isExists(String nt, String proName, String value){
		try {
			QueryManager qm = getEStorageHome().getSession().getWorkspace().getQueryManager();
			Query q = qm.createQuery("SELECT "+proName+" FROM " + nt + " WHERE " + proName + " = '" + value +"'", Query.SQL);
			return q.execute().getRows().getSize() != 0;
		} catch (Exception e) {
			log.info(e.getMessage());
			return false ;
		}


	}


	private SessionProvider createSessionProvider() {
		SessionProvider provider = sessionProviderService_.getSessionProvider(null);
		if (provider == null) {
			//log.info("No user session provider was available, trying to use a system session provider");
			provider = sessionProviderService_.getSystemSessionProvider(null);
		}
		return SessionProvider.createSystemProvider();
	}

	private String valuesToString (Value[] values, String regex) throws Exception{
		StringBuffer sb = new StringBuffer();
		for (Value v : values){
			sb.append(regex).append(v.getString());
		}
		return sb.toString().replaceFirst(regex, "");
	}

	private Collection<String> valuesToStrings (Value[] values, String regex) throws Exception{
		StringBuffer sb = new StringBuffer();
		for (Value v : values){
			sb.append(regex).append(v.getString());
		}
		return Arrays.asList(sb.toString().replaceFirst(regex, "").split(regex));
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
	private Node getEQestionHome() throws RepositoryException, Exception {
		Node eHome = getEStorageHome();
		try {
			return  eHome.getNode(Util.E_STUDY_QUEST);
		} catch (PathNotFoundException e) {
			eHome.addNode(Util.E_STUDY_QUEST, Util.NT_UNSTRUCTURED);
			eHome.getSession().save();
			return eHome.getNode(Util.E_STUDY_QUEST);
		}  
	}


	@Override
	public void saveCategory(ECategory category, boolean isNew) throws ItemExistsException, Exception{
		Node catHome = getECategoryHome();
		Node cat;
		if(isNew){
			if(isExists(ECategory.NT_NAME, ECategory.P_NAME, category.getName())) throw new ItemExistsException();
			try {
				cat = setCategoryProp(category, catHome.addNode(category.getId(), ECategory.NT_NAME));
				cat.addMixin(Util.MIX_REFERENCEABLE);
				cat.getSession().save();
			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());
				throw e;
			}
		} else {
			cat = setCategoryProp(category, catHome.getNode(category.getId()));
			cat.save();
		}

	}

	@Override
	public Collection<ECategory> getCategories() throws Exception {
		try {
			Node catHome = getECategoryHome();
			NodeIterator it = catHome.getNodes();
			Collection<ECategory> list = new ArrayList<ECategory>();
			while (it.hasNext()) {
				list.add(getCategoryProp(it.nextNode()));
			}
			return list;
		} catch (Exception e){
			log.info(e.getMessage());
		}
		return null;
	}

	@Override
	public ECategory getCategory(String id) throws ItemNotFoundException {
		try {
			Node catHome = getECategoryHome();
			return getCategoryProp(catHome.getNode(id));
		} catch (PathNotFoundException e) {
			throw new ItemNotFoundException();
		} catch (Exception e) {
			log.info(e.getMessage());
			return null;
		}
	}

	@Override
	public void removeCategory(String id) throws Exception {
		Node catHome = getECategoryHome();
		try {
			Node cat = catHome.getNode(id);
			cat.remove();
			catHome.save();
		} catch (PathNotFoundException e) {
		} catch (Exception e) {
			log.info(e.getMessage());
			throw e;
		}

	}

	@Override
	public void saveSession(ESession session, boolean isNew) throws ItemExistsException, Exception {
		Node sesHome = getESessionHome();
		Node ses;
		if(isNew){
			if(isExists(ESession.NT_NAME, ESession.P_TITLE, session.getTitle())) throw new ItemExistsException();
			try {
				ses = setSessionProp(session, sesHome.addNode(session.getId(), ESession.NT_NAME));
				ses.getSession().save();
			} catch (Exception e) {
				log.info(e.getMessage());
				throw e;
			}
		} else {
			ses = setSessionProp(session, sesHome.getNode(session.getId()));
			ses.save();
		} 
	}


	@Override
	public Collection<ESession> getSessions() throws Exception {
		try {
			Node sesHome = getESessionHome();
			NodeIterator it = sesHome.getNodes();
			Collection<ESession> list = new ArrayList<ESession>();
			while (it.hasNext()) {
				list.add(getSessionProp(it.nextNode()));
			}
			return list;
		} catch (Exception e){
			log.info(e.getMessage());
		}
		return null;
	}

	@Override
	public ESession getSession(String id) throws ItemNotFoundException {
		try {
			Node sesHome = getESessionHome();
			return getSessionProp(sesHome.getNode(id));
		} catch (PathNotFoundException e) {
			throw new ItemNotFoundException();
		} catch (Exception e) {
			log.info(e.getMessage());
			return null;
		}
	}
	@Override
	public void removeSession(String id) throws Exception {
		Node sesHome = getESessionHome();
		try {
			Node ses = sesHome.getNode(id);
			ses.remove();
			sesHome.save();
		} catch (PathNotFoundException e) {
		} catch (Exception e) {
			log.info(e.getMessage());
			throw e;
		}
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
	public void saveQuestion(EQuestion qestion, boolean isNew) throws ItemExistsException,
	Exception {
		Node qHome = getEQestionHome();
		Node ques ;
		if(isNew){
			if(isExists(EQuestion.NT_NAME, EQuestion.P_TITLE, qestion.getTitle())) throw new ItemExistsException();
			try {
				ques = setQuestionProp(qestion, qHome.addNode(qestion.getId(), EQuestion.NT_NAME));
				ques.getSession().save();
			} catch (Exception e) {
				log.info(e.getMessage());
				throw e;
			}
		} else {
			ques = setQuestionProp(qestion, qHome.getNode(qestion.getId()));
			ques.save();
		} 

	}

	@Override
	public Collection<EQuestion> getQuestions() throws Exception {

		try {
			Node sesHome = getEQestionHome();
			NodeIterator it = sesHome.getNodes();
			Collection<EQuestion> list = new ArrayList<EQuestion>();
			while (it.hasNext()) {
				list.add(getQuestionProp(it.nextNode()));
			}
			return list;
		} catch (Exception e){
			log.info(e.getMessage());
		}
		return null;
	}

	@Override
	public EQuestion getQuestion(String id) throws ItemNotFoundException {
		try {
			Node sesHome = getEQestionHome();
			return getQuestionProp(sesHome.getNode(id));
		} catch (PathNotFoundException e) {
			throw new ItemNotFoundException();
		} catch (Exception e) {
			log.info(e.getMessage());
			return null;
		}
	}

	@Override
	public void removeQuestion(String id) throws Exception {
		Node sesHome = getEQestionHome();
		try {
			Node ses = sesHome.getNode(id);
			ses.remove();
			sesHome.save();
		} catch (PathNotFoundException e) {
		} catch (Exception e) {
			log.info(e.getMessage());
			throw e;
		}
	}
}
