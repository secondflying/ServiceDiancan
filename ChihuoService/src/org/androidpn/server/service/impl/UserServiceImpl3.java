///*
// * Copyright (C) 2010 Moduad Co., Ltd.
// * 
// * This program is free software; you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation; either version 2 of the License, or
// * (at your option) any later version.
// * 
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License along
// * with this program; if not, write to the Free Software Foundation, Inc.,
// * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
// */
//package org.androidpn.server.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicLong;
//
//
//import org.androidpn.server.model.User;
//import org.androidpn.server.service.UserExistsException;
//import org.androidpn.server.service.UserNotFoundException;
//import org.androidpn.server.service.UserService;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
///** 
// * This class is the implementation of UserService.
// *
// * @author Sehwan Noh (devnoh@gmail.com)
// */
//public class UserServiceImpl3 implements UserService {
//
//	//TODO 这里改为从数据库获取用户
//    protected final Log log = LogFactory.getLog(getClass());
//    
//    private final AtomicLong longCount = new AtomicLong(0);
//
//    private List<User> users = new ArrayList<User>();
//
//    private static UserServiceImpl3 instance;
//    private UserServiceImpl3() {
//    }
//
//    /**
//     * Returns the singleton instance of SessionManager.
//     * 
//     * @return the instance
//     */
//    public static UserServiceImpl3 getInstance() {
//        if (instance == null) {
//            synchronized (UserServiceImpl3.class) {
//                instance = new UserServiceImpl3();
//            }
//        }
//        return instance;
//    }
//    
//    public User getUser(String userId) {
//		for (int i = 0; i < users.size(); i++) {
//			if(new Long(userId) == users.get(i).getId()){
//				return users.get(i);
//			}
//		}
//		return null;
//	}
//    
//    public List<User> getUsers() {
//        return users;
//    }
//
//    public User saveUser(User user) throws UserExistsException {
//        if(user.getId() == null){
//        	user.setId(longCount.incrementAndGet());
//        	users.add(user);
//        	return user;
//        }else {
//        	for (int i = 0; i < users.size(); i++) {
//        		if(user.getId() == users.get(i).getId()){
//    				users.remove(users.get(i));
//    				users.add(user);
//    				return user;
//    			}
//    		}
//            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
//		}
//    }
//
//    public User getUserByUsername(String username) throws UserNotFoundException {
//        for (int i = 0; i < users.size(); i++) {
//			if(username.equals(users.get(i).getUsername())){
//				return users.get(i);
//			}
//		}
//        throw new UserNotFoundException("User '" + username + "' not found");
//    }
//
//    public void removeUser(Long userId) {
//        log.debug("removing user: " + userId);
//        for (int i = 0; i < users.size(); i++) {
//			if(userId == users.get(i).getId()){
//				users.remove(users.get(i));
//				break;
//			}
//		}
//    }
//    
//}
