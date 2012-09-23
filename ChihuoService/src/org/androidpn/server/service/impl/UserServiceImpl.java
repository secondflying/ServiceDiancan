/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.service.impl;

import java.util.List;

import org.androidpn.server.service.UserExistsException;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chihuo.bussiness.Waiter;
import com.chihuo.dao.WaiterDao;

/** 
 * This class is the implementation of UserService.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class UserServiceImpl implements UserService {

    protected final Log log = LogFactory.getLog(getClass());

    private WaiterDao userDao;

    public UserServiceImpl(){
    	userDao  = new WaiterDao();
    }
    
    public List<Waiter> getUsers() {
        return userDao.findAll();
    }

    public Waiter saveUser(Waiter user) throws UserExistsException {
    	try {
			userDao.getSession().beginTransaction();
			Waiter w =  userDao.saveOrUpdate(user);
			userDao.getSession().getTransaction().commit();
			return w;
		} catch (Throwable ex) {
			try {
				if (userDao.getSession().getTransaction().isActive()) {
					userDao.getSession().getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				
			}
			throw new UserExistsException("User '" + user.getUsername()
                    + "' 错误!" + ex.getMessage());
		}
    }

    public Waiter getUserByUsername(String username) throws UserNotFoundException {
        try {
			userDao.getSession().beginTransaction();
			Waiter w =  (Waiter) userDao.getUserByUsername(username);;
			userDao.getSession().getTransaction().commit();
			return w;
		} catch (Throwable ex) {
			try {
				if (userDao.getSession().getTransaction().isActive()) {
					userDao.getSession().getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				
			}
			throw new UserNotFoundException("User '" + username
                    + "' not found!");
		}
    }

    public void removeUser(Integer userId) {
    	Waiter waiter = userDao.findById(userId);
    	userDao.delete(waiter);
    }

	public Waiter getUser(String userId) {
		try {
			userDao.getSession().beginTransaction();
			Waiter w =  userDao.findById(new Integer(userId));
			userDao.getSession().getTransaction().commit();
			return w;
		} catch (Throwable ex) {
			try {
				if (userDao.getSession().getTransaction().isActive()) {
					userDao.getSession().getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				
			}
			return null;
		}
	}

}
