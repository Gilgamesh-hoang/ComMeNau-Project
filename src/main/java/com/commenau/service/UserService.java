package com.commenau.service;

import com.commenau.constant.SystemConstant;
import com.commenau.dao.RoleDAO;
import com.commenau.dao.UserDAO;
import com.commenau.model.Role;
import com.commenau.model.User;
import com.commenau.pagination.PageRequest;
import com.commenau.util.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    @Inject
    private UserDAO userDao;
    @Inject
    private RoleDAO roleDao;


    public boolean isUsernameExists(String username) {
        return userDao.getUserByUsername(username) != null;
    }

    public boolean isEmailExists(String email) {
        return userDao.getUserByEmail(email) != null;
    }

    public User signup(User user, String authentication) {
        // encrypt Password
        String encryptPassword = EncryptUtil.hashPassword(user.getPassword());
        user.setPassword(encryptPassword);
        // assign default roles
        Role role = roleDao.getRoleByName(SystemConstant.USER);
        user.setRoleId(role.getId());
        // assign default status
        user.setStatus(authentication);
        //signup user
        if (!userDao.insert(user))
            return null;
        user = userDao.getUserByUsername(user.getUsername());
        return user;
    }

    public User signin(User user) {
        if (user == null)
            return null;
        String passwd = user.getPassword();
        user = userDao.getUserByUsername(user.getUsername());
        boolean s = user.getStatus().equals(SystemConstant.ACTIVATED);
        for (int i = 0; i < user.getStatus().length(); i++) {
            char a = user.getStatus().charAt(i);
            char b = SystemConstant.ACTIVATED.charAt(i);
            if (a == b)
                System.out.println();
            else
                System.out.println();
        }
        if (user != null && BCrypt.checkpw(passwd, user.getPassword())&& user.getStatus().equals(SystemConstant.ACTIVATED))
            return user;
        else
            return null;
    }

    public Map<User, String> lostPassword(String email) {
        Map<User, String> result = null;
        User user = userDao.getUserByEmail(email);
        if (user != null) {
            //generate random password
            String newPassword = EncryptUtil.randomString();
            user.setPassword(EncryptUtil.hashPassword(newPassword));
            // update new passwd for user
            if (userDao.updatePassword(user)) {
                result = new HashMap<User, String>();
                result.put(user, newPassword);
                return result;
            }
        }
        return result;
    }

    public User changePassword(String newPassword, User user) {
        newPassword = EncryptUtil.hashPassword(newPassword);
        user.setPassword(newPassword);
        return userDao.updatePassword(user) ? user : null;
    }

    public boolean updateProfile(User user) {
        return userDao.updateProfile(user);
    }

    public User signinSocial(User userSocial) {
        User existingUser = userDao.getUserByEmail(userSocial.getEmail());
        if (existingUser != null)
            if (existingUser.getStatus().equals(SystemConstant.ACTIVATED))
                return existingUser;
            else return null;
        else {
            // auto signup for user
            //generate random password and username
            String newPassword = EncryptUtil.randomString();
            userSocial.setPassword(newPassword);
            userSocial.setUsername(EncryptUtil.randomString());
            signup(userSocial, SystemConstant.ACTIVATED);
            return userSocial;
        }
    }


    public boolean lockOrUnlock(Long userId) {
        User user = userDao.getUserById(userId);
        String status = user.getStatus();
        if (status.equals(SystemConstant.LOCKED)) {
            return userDao.lockOrUnlock(userId, SystemConstant.ACTIVATED);
        } else if (status.equals(SystemConstant.ACTIVATED)) {
            return userDao.lockOrUnlock(userId, SystemConstant.LOCKED);
        }
        return false;
    }

    public User getUserById(Long userId) {
        return userDao.getUserById(userId);
    }

    public int countAll() {
        return userDao.countAll();
    }

    public int countByKeyWord(String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return this.countAll();
        return userDao.countByKeyWord(keyWord);
    }

    public List<User> getByKeyWord(PageRequest pageRequest, String keyWord) {
        List<User> list = null;
        if (StringUtils.isBlank(keyWord)) {
            list = userDao.findAll(pageRequest);
        } else {
            list = userDao.findByKeyWord(pageRequest, keyWord.trim());
        }
        return list;
    }
}
