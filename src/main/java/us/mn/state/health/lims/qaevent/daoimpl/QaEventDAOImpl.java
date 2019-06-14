/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
*
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
*
* The Original Code is OpenELIS code.
*
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/
package us.mn.state.health.lims.qaevent.daoimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.qaevent.dao.QaEventDAO;
import us.mn.state.health.lims.qaevent.valueholder.QaEvent;

/**
 * @author diane benz
 */
@Component
@Transactional
public class QaEventDAOImpl extends BaseDAOImpl<QaEvent, String> implements QaEventDAO {

	public QaEventDAOImpl() {
		super(QaEvent.class);
	}

//	@Override
//	public void deleteData(List qaEvents) throws LIMSRuntimeException {
//		// add to audit trail
//		try {
//
//			for (int i = 0; i < qaEvents.size(); i++) {
//				QaEvent data = (QaEvent) qaEvents.get(i);
//
//				QaEvent oldData = readQaEvent(data.getId());
//				QaEvent newData = new QaEvent();
//
//				String sysUserId = data.getSysUserId();
//				String event = IActionConstants.AUDIT_TRAIL_DELETE;
//				String tableName = "QA_EVENT";
//				auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
//			}
//		} catch (Exception e) {
//			// bugzilla 2154
//			LogEvent.logError("QaEventDAOImpl", "AuditTrail deleteData()", e.toString());
//			throw new LIMSRuntimeException("Error in QaEvent AuditTrail deleteData()", e);
//		}
//
//		try {
//			for (int i = 0; i < qaEvents.size(); i++) {
//				QaEvent data = (QaEvent) qaEvents.get(i);
//				QaEvent cloneData = readQaEvent(data.getId());
//
//				sessionFactory.getCurrentSession().merge(cloneData);
//				// sessionFactory.getCurrentSession().flush(); // CSL remove old
//				// sessionFactory.getCurrentSession().clear(); // CSL remove old
//				// sessionFactory.getCurrentSession().evict // CSL remove old(cloneData);
//				// sessionFactory.getCurrentSession().refresh // CSL remove old(cloneData);
//			}
//		} catch (Exception e) {
//			// bugzilla 2154
//			LogEvent.logError("QaEventDAOImpl", "deleteData()", e.toString());
//			throw new LIMSRuntimeException("Error in QaEvent deleteData()", e);
//		}
//	}

//	@Override
//	public boolean insertData(QaEvent qaEvent) throws LIMSRuntimeException {
//
//		try {
//			if (duplicateQaEventExists(qaEvent)) {
//				throw new LIMSDuplicateRecordException("Duplicate record exists for " + qaEvent.getQaEventName());
//			}
//
//			String id = (String) sessionFactory.getCurrentSession().save(qaEvent);
//			qaEvent.setId(id);
//
//			String sysUserId = qaEvent.getSysUserId();
//			String tableName = "QA_EVENT";
//			auditDAO.saveNewHistory(qaEvent, sysUserId, tableName);
//
//			// sessionFactory.getCurrentSession().flush(); // CSL remove old
//			// sessionFactory.getCurrentSession().clear(); // CSL remove old
//
//		} catch (Exception e) {
//			// bugzilla 2154
//			LogEvent.logError("QaEventDAOImpl", "insertData()", e.toString());
//			throw new LIMSRuntimeException("Error in QaEvent insertData()", e);
//		}
//
//		return true;
//	}

//	@Override
//	public void updateData(QaEvent qaEvent) throws LIMSRuntimeException {
//		try {
//			if (duplicateQaEventExists(qaEvent)) {
//				throw new LIMSDuplicateRecordException("Duplicate record exists for " + qaEvent.getQaEventName());
//			}
//		} catch (Exception e) {
//			// bugzilla 2154
//			LogEvent.logError("QaEventDAOImpl", "updateData()", e.toString());
//			throw new LIMSRuntimeException("Error in QaEvent updateData()", e);
//		}
//
//		QaEvent oldData = readQaEvent(qaEvent.getId());
//		QaEvent newData = qaEvent;
//
//		// add to audit trail
//		try {
//
//			String sysUserId = qaEvent.getSysUserId();
//			String event = IActionConstants.AUDIT_TRAIL_UPDATE;
//			String tableName = "QA_EVENT";
//			auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
//		} catch (Exception e) {
//			// bugzilla 2154
//			LogEvent.logError("QaEventDAOImpl", "AuditTrail updateData()", e.toString());
//			throw new LIMSRuntimeException("Error in QaEvent AuditTrail updateData()", e);
//		}
//
//		try {
//			sessionFactory.getCurrentSession().merge(qaEvent);
//			// sessionFactory.getCurrentSession().flush(); // CSL remove old
//			// sessionFactory.getCurrentSession().clear(); // CSL remove old
//			// sessionFactory.getCurrentSession().evict // CSL remove old(qaEvent);
//			// sessionFactory.getCurrentSession().refresh // CSL remove old(qaEvent);
//		} catch (Exception e) {
//			// bugzilla 2154
//			LogEvent.logError("QaEventDAOImpl", "updateData()", e.toString());
//			throw new LIMSRuntimeException("Error in QaEvent updateData()", e);
//		}
//	}

	@Override
	@Transactional(readOnly = true)
	public void getData(QaEvent qaEvent) throws LIMSRuntimeException {
		try {
			QaEvent qaEv = sessionFactory.getCurrentSession().get(QaEvent.class, qaEvent.getId());
			// sessionFactory.getCurrentSession().flush(); // CSL remove old
			// sessionFactory.getCurrentSession().clear(); // CSL remove old
			if (qaEv != null) {
				PropertyUtils.copyProperties(qaEvent, qaEv);

			} else {
				qaEvent.setId(null);
			}
		} catch (Exception e) {
			// bugzilla 2154
			LogEvent.logError("QaEventDAOImpl", "getData()", e.toString());
			throw new LIMSRuntimeException("Error in QaEvent getData()", e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List getAllQaEvents() throws LIMSRuntimeException {
		List list;
		try {
			String sql = "from QaEvent qe order by qe.id";
			org.hibernate.Query query = sessionFactory.getCurrentSession().createQuery(sql);
			list = query.list();
			// sessionFactory.getCurrentSession().flush(); // CSL remove old
			// sessionFactory.getCurrentSession().clear(); // CSL remove old

		} catch (Exception e) {
			LogEvent.logError("QaEventDAOImpl", "getAllQaEvents()", e.toString());
			throw new LIMSRuntimeException("Error in QaEvent getAllQaEvents()", e);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List getPageOfQaEvents(int startingRecNo) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			// calculate maxRow to be one more than the page size
			int endingRecNo = startingRecNo + (SystemConfiguration.getInstance().getDefaultPageSize() + 1);

			String sql = "from QaEvent qe order by qe.qaEventName";
			org.hibernate.Query query = sessionFactory.getCurrentSession().createQuery(sql);
			query.setFirstResult(startingRecNo - 1);
			query.setMaxResults(endingRecNo - 1);

			list = query.list();
			// sessionFactory.getCurrentSession().flush(); // CSL remove old
			// sessionFactory.getCurrentSession().clear(); // CSL remove old

		} catch (Exception e) {
			// bugzilla 2154
			LogEvent.logError("QaEventDAOImpl", "getPageOfQaEvents()", e.toString());
			throw new LIMSRuntimeException("Error in QaEvent getPageOfQaEvents()", e);
		}

		return list;
	}

	public QaEvent readQaEvent(String idString) {
		QaEvent qaEvent = null;
		try {
			qaEvent = sessionFactory.getCurrentSession().get(QaEvent.class, idString);
			// sessionFactory.getCurrentSession().flush(); // CSL remove old
			// sessionFactory.getCurrentSession().clear(); // CSL remove old
		} catch (Exception e) {
			// bugzilla 2154
			LogEvent.logError("QaEventDAOImpl", "readQaEvent()", e.toString());
			throw new LIMSRuntimeException("Error in QaEvent readQaEvent()", e);
		}

		return qaEvent;
	}

	@Override
	@Transactional(readOnly = true)
	public List getNextQaEventRecord(String id) throws LIMSRuntimeException {

		return getNextRecord(id, "QaEvent", QaEvent.class);

	}

	@Override
	@Transactional(readOnly = true)
	public List getPreviousQaEventRecord(String id) throws LIMSRuntimeException {

		return getPreviousRecord(id, "QaEvent", QaEvent.class);
	}

	// this is for autocomplete
	@Override
	@Transactional(readOnly = true)
	public List getQaEvents(String filter) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			String sql = "from QaEvent qe where upper(qe.qaEventName) like upper(:param) order by upper(qe.qaEventName)";
			org.hibernate.Query query = sessionFactory.getCurrentSession().createQuery(sql);
			query.setParameter("param", filter + "%");

			list = query.list();
			// sessionFactory.getCurrentSession().flush(); // CSL remove old
			// sessionFactory.getCurrentSession().clear(); // CSL remove old
		} catch (Exception e) {
			// bugzilla 2154
			LogEvent.logError("QaEventDAOImpl", "getQaEvents()", e.toString());
			throw new LIMSRuntimeException("Error in QaEvent getQaEvents(String filter)", e);
		}
		return list;

	}

	@Override
	@Transactional(readOnly = true)
	public QaEvent getQaEventByName(QaEvent qaEvent) throws LIMSRuntimeException {
		try {
			String sql = "from QaEvent qe where qe.qaEventName = :param";
			org.hibernate.Query query = sessionFactory.getCurrentSession().createQuery(sql);
			query.setParameter("param", qaEvent.getQaEventName());

			List list = query.list();
			// sessionFactory.getCurrentSession().flush(); // CSL remove old
			// sessionFactory.getCurrentSession().clear(); // CSL remove old
			QaEvent qe = null;
			if (list.size() > 0) {
				qe = (QaEvent) list.get(0);
			}

			return qe;

		} catch (Exception e) {
			// bugzilla 2154
			LogEvent.logError("QaEventDAOImpl", "getQaEventByName()", e.toString());
			throw new LIMSRuntimeException("Error in QaEvent getQaEventByName()", e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getTotalQaEventCount() throws LIMSRuntimeException {
		return getTotalCount("QaEvent", QaEvent.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List getNextRecord(String id, String table, Class clazz) throws LIMSRuntimeException {

		List list = new Vector();
		try {
			String sql = "from " + table + " t where name >= " + enquote(id) + " order by t.qaEventName";
			org.hibernate.Query query = sessionFactory.getCurrentSession().createQuery(sql);
			query.setFirstResult(1);
			query.setMaxResults(2);

			list = query.list();

		} catch (Exception e) {
			// bugzilla 2154
			LogEvent.logError("QaEventDAOImpl", "getNextRecord()", e.toString());
			throw new LIMSRuntimeException("Error in getNextRecord() for " + table, e);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List getPreviousRecord(String id, String table, Class clazz) throws LIMSRuntimeException {

		List list = new Vector();
		try {
			String sql = "from " + table + " t order by t.qaEventName desc where name <= " + enquote(id);
			org.hibernate.Query query = sessionFactory.getCurrentSession().createQuery(sql);
			query.setFirstResult(1);
			query.setMaxResults(2);

			list = query.list();
		} catch (Exception e) {
			// bugzilla 2154
			LogEvent.logError("QaEventDAOImpl", "getPreviousRecord()", e.toString());
			throw new LIMSRuntimeException("Error in getPreviousRecord() for " + table, e);
		}

		return list;
	}

	@Override
	public boolean duplicateQaEventExists(QaEvent qaEvent) throws LIMSRuntimeException {
		try {

			List list = new ArrayList();

			// not case sensitive hemolysis and Hemolysis are considered
			// duplicates
			String sql = "from QaEvent t where "
					+ "((trim(lower(t.qaEventName)) = :param and trim(lower(t.type)) = :param3 and t.id != :param2) "
					+ "or "
					+ "(trim(lower(t.description)) = :param4 and trim(lower(t.type)) = :param3 and t.id != :param2)) ";

			org.hibernate.Query query = sessionFactory.getCurrentSession().createQuery(sql);
			query.setParameter("param", qaEvent.getQaEventName().toLowerCase().trim());
			query.setParameter("param3", qaEvent.getType());
			query.setParameter("param4", qaEvent.getDescription().toLowerCase().trim());

			// initialize with 0 (for new records where no id has been generated yet
			String qaEventId = "0";
			if (!StringUtil.isNullorNill(qaEvent.getId())) {
				qaEventId = qaEvent.getId();
			}
			query.setParameter("param2", qaEventId);

			list = query.list();
			// sessionFactory.getCurrentSession().flush(); // CSL remove old
			// sessionFactory.getCurrentSession().clear(); // CSL remove old

			if (list.size() > 0) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			// bugzilla 2154
			LogEvent.logError("QaEventDAOImpl", "duplicateQaEventExists()", e.toString());
			throw new LIMSRuntimeException("Error in duplicateQaEventExists()", e);
		}
	}

}