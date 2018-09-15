package net.hellopig.app.controller;

import net.hellopig.app.domain.SysUser;
import net.hellopig.app.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description：SysUserController
 *
 * @author CC
 * @date 2018/7/31 22:40    SysUserController.java
 */

@RestController
public class SysUserController {

    private final SysUserRepository sysUserRepository;

    @Autowired
    public SysUserController(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    /**
     * 获取系统用户列表
     * @return List<SysUser> 系统用户列表
     */
    @GetMapping(value = "/users")
    public List<SysUser> getSysUser() {
        return sysUserRepository.findAll();
    }

}
