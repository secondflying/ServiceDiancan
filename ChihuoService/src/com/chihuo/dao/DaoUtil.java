package com.chihuo.dao;

import java.util.Date;

import com.chihuo.bussiness.DataLog;
import com.chihuo.resource.MyConstants;

public class DaoUtil {

	public static void saveOrUpdateLog(String tableName, Integer id) {
		DataLogDao dao = new DataLogDao();
		DataLog log = dao.findByTableDid(tableName, id);
		if (log == null) {
			log = new DataLog();
			log.setDataTable(tableName);
			log.setDid(id);
			log.setAddTime(new Date());
		} else {
			log.setUpdateTime(new Date());
		}
		dao.saveOrUpdate(log);
	}

	public static void deleteLog(String tableName, Integer id) {
		DataLogDao dao = new DataLogDao();
		DataLog log = dao.findByTableDid(MyConstants.CATEGORY_TABLE, id);
		if (log != null) {
			log.setDeleteTime(new Date());
			dao.saveOrUpdate(log);
		}
	}
}
