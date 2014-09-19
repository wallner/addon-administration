package org.osiam.addons.administration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.AccessToken;
import org.osiam.client.query.Query;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.UpdateGroup;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

	@Mock
	OsiamConnector connector;

	@Mock
	AccessToken accessToken;

	@Spy
	@InjectMocks
	GeneralSessionData sessionData = new GeneralSessionData();

	@Spy
	@InjectMocks
	GroupService toTestSpy = new GroupService();

	@Test
	public void searchGroup_advanced() {
		final String query = "testQuery";
		final Integer limit = 13;
		final Long offset = 12L;
		final String orderBy = "orderby";
		final Boolean asc = false; // desc

		toTestSpy.searchGroup(query, limit, offset, orderBy, asc);

		ArgumentCaptor<Query> cap = ArgumentCaptor.forClass(Query.class);

		verify(connector).searchGroups(cap.capture(), same(accessToken));

		Query usedQuery = cap.getValue();
		assertEquals(query, usedQuery.getFilter());
		assertTrue(limit == usedQuery.getCount());
		assertTrue(offset == usedQuery.getStartIndex());
		assertEquals(orderBy, usedQuery.getSortBy());
		assertEquals("descending", usedQuery.getSortOrder());
	}

	@Test
	public void deleteGroup() {
		final String id = "groupID";

		toTestSpy.deleteGroup(id);

		verify(connector).deleteGroup(eq(id), same(accessToken));
	}

	@Test
	public void getGroup() {
		String id = "groupID";

		Group group = mock(Group.class);
		doReturn(group).when(connector).getGroup(eq(id), same(accessToken));

		Group result = toTestSpy.getGroup(id);
		assertEquals(group, result);
	}

	@Test
	public void updateGroup() {
		UpdateGroup updateGroup = new UpdateGroup.Builder().build();
		String id = "user ID";

		toTestSpy.updateGroup(id, updateGroup);
		verify(connector, times(1)).updateGroup(eq(id), eq(updateGroup), same(accessToken));
	}
}
