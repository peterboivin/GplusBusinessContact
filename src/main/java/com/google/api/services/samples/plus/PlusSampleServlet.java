/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.plus;

import com.boivin.model.PeopleFeedBean;
import com.boivin.model.PersonBean;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.PeopleFeed;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry sevlet for the Plus App Engine Sample. Demonstrates how to make an authenticated API call
 * using OAuth2 helper classes.
 *
 * @author Nick Miceli
 */
public class PlusSampleServlet extends AbstractAppEngineAuthorizationCodeServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        // Get the stored credentials using the Authorization Flow
        AuthorizationCodeFlow authFlow = initializeFlow();
        Credential credential = authFlow.loadCredential(getUserId(req));
        // Build the Plus object using the credentials
        PeopleFeed peopleFeed = findPeople(resp, credential, "Lucky", 50L);

        List<PersonBean> personBeanList = new ArrayList<>();

        PeopleFeedBean peopleFeedBean = new PeopleFeedBean();
        peopleFeedBean.setId(peopleFeed.getTitle() + peopleFeed.getEtag());
        peopleFeedBean.setTitle(peopleFeed.getTitle());
        peopleFeedBean.setEtag(peopleFeed.getEtag());
        peopleFeedBean.setKind(peopleFeed.getKind());
        peopleFeedBean.setNextPageToken(peopleFeed.getNextPageToken());
        peopleFeedBean.setSelfLink(peopleFeed.getSelfLink());
        peopleFeed.setTotalItems(peopleFeed.getTotalItems());

        for (com.google.api.services.plus.model.Person person : peopleFeed.getItems()) {
            PersonBean personBean = new PersonBean();
            personBean.setAboutMe(person.getAboutMe());
            personBean.setKind(person.getKind());
            personBean.setEtag(person.getEtag());
            personBean.setBirthday(person.getBirthday());
            personBean.setBraggingRights(person.getBraggingRights());
            personBean.setCurrentLocation(person.getCurrentLocation());
            personBean.setDisplayName(person.getDisplayName());
            personBean.setGender(person.getGender());
            personBean.setNickname(person.getNickname());
            personBean.setOccupation(person.getOccupation());
            personBean.setRelationshipStatus(person.getRelationshipStatus());
            personBean.setSkills(person.getSkills());
            personBean.setAgeRange((PersonBean.AgeRange) person.get(person.getAgeRange()));
            personBean.setCircledByCount(person.getCircledByCount());
            personBean.setCover((PersonBean.Cover) person.get(person.getCover()));
            personBean.setDomain(person.getDomain());
            personBean.setEmails((List<PersonBean.Emails>) person.get(person.getEmails()));
            personBean.setId(person.getId());
            personBean.setImage((PersonBean.Image) person.get(person.getImage()));
            personBean.setIsPlusUser(person.getIsPlusUser());
            personBean.setObjectType(person.getObjectType());
            personBean.setLanguage(person.getLanguage());
            personBean.setPlacesLived((List<PersonBean.PlacesLived>) person.get(person.getPlacesLived()));
            personBean.setOrganizations((List<PersonBean.Organizations>) person.get(person.getOccupation()));
            personBean.setTagline(person.getTagline());
            personBean.setPlusOneCount(person.getPlusOneCount());
            personBean.setName((PersonBean.Name) person.get(person.getName()));
            personBean.setUrls((List<PersonBean.Urls>) person.get(person.getUrls()));
            personBean.setUrl(person.getUrl());
            personBean.setVerified(person.getVerified());

            personBeanList.add(personBean);
        }

        peopleFeedBean.setItems(personBeanList);

        Key<PeopleFeedBean> peopleFeedBeanKey = ObjectifyService.ofy().save().entity(peopleFeedBean).now();

        List<PeopleFeedBean> fetched = ObjectifyService.ofy().load().type(PeopleFeedBean.class).list();

        // Delete it
//        ObjectifyService.ofy().delete().entity(peopleFeedBean).now();
    }

    private PeopleFeed findPeople(HttpServletResponse resp, Credential credential, String searchTerm, Long maxResults) throws IOException {
        Plus plus = new Plus.Builder(
                Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, credential).setApplicationName("").build();
        // Make the API call
        PeopleFeed peopleFeed = plus.people().search(searchTerm).setMaxResults(maxResults).execute();
        // Send the results as the response
        PrintWriter respWriter = resp.getWriter();
        resp.setStatus(200);
        resp.setContentType("text/html");
//        respWriter.print("Title='" + peopleFeed.getTitle() + "'>");
//        respWriter.print("Etag='" + peopleFeed.getEtag() + "'>");
//        respWriter.print("Kind='" + peopleFeed.getKind() + "'>");
//        respWriter.print("NextPageToken='" + peopleFeed.getNextPageToken() + "'>");
//        respWriter.println("SelfLink='" + peopleFeed.getSelfLink() + "'>");
//        int count = 0;
//        for (com.google.api.services.plus.model.Person person : peopleFeed.getItems()) {
//            respWriter.print("Etag='" + person.getEtag() + "'>");
//            respWriter.print("Kind='" + person.getKind() + "'>");
//            respWriter.print("AboutMe='" + person.getAboutMe() + "'>");
//            respWriter.print("Birthday='" + person.getBirthday() + "'>");
//            respWriter.print("BraggingRights='" + person.getBraggingRights() + "'>");
//            respWriter.print("CurrentLocation='" + person.getCurrentLocation() + "'>");
//            respWriter.print("DisplayName='" + person.getDisplayName() + "'>");
//            respWriter.print("Gender='" + person.getGender() + "'>");
//            respWriter.print("Nickname='" + person.getNickname() + "'>");
//            respWriter.print("Occupation='" + person.getOccupation() + "'>");
//            respWriter.print("RelationshipStatus='" + person.getRelationshipStatus() + "'>");
//            respWriter.print("Skills='" + person.getSkills() + "'>");
//            respWriter.println("Count = " + count + "<-------------------------------------->");
//            count++;
//        }
        return peopleFeed;
    }

    @Override
    protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
        return Utils.initializeFlow();
    }

    @Override
    protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
        return Utils.getRedirectUri(req);
    }
}
