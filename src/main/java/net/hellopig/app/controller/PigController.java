package net.hellopig.app.controller;

import net.hellopig.app.domain.Pig;
import net.hellopig.app.repository.PigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Description：pigs controller
 *
 * @author CC
 * @date 2018/8/5 13:54    PigController.java
 */
@RestController
public class PigController {

    private final PigRepository pigRepository;

    @Autowired
    public PigController(PigRepository pigRepository) {
        this.pigRepository = pigRepository;
    }

    /**
     * 获取所有 pig 用户列表
     * @return List<Pig> pig用户列表
     */
    @GetMapping(value = "/pigs")
    public List<Pig> getAllPigs() {
        return pigRepository.findAll();
    }

    /**
     * 根据 ID 查询 pig
     *
     * @return Pig用户
     */
    @GetMapping(value = "/pigs/id/{id}")
    public Pig findPigById(@PathVariable(value = "id") String id) {
        Optional<Pig> pigs = pigRepository.findById(id);
        return pigs.orElse(null);
    }

    /**
     * 通过年龄查询 pig
     *
     * @return 所有符合该年龄的pig
     */
    @GetMapping(value = "/pigs/age/{age}")
    public List<Pig> findPigsByAge(@PathVariable(value = "age") int age) {
        return pigRepository.findPigsByAge(age);
    }

    /**
     * 根据 ID 更新 pig 用户
     *
     * @return Pig用户
     */
    @PutMapping(value = "/pigs/{id}")
    public Pig updatePig(@PathVariable(value = "id") String id, @RequestParam("name") String name,
                         @RequestParam(name = "phone") Integer phone, @RequestParam("email") String email,
                         @RequestParam("password") String password, @RequestParam("sex") String sex,
                         @RequestParam("age") int age, @RequestParam("age") String address) {
        Pig pig = new Pig();
        pig.setId(id);
        pig.setName(name);
        pig.setPhoneNumber(phone);
        pig.seteMail(email);
        pig.setPassword(password);
        pig.setSex(sex);
        pig.setAge(age);
        pig.setAddress(address);
        return pigRepository.save(pig);
    }

    /**
     * 根据 ID 删除 pig 用户
     *
     * @return List<SysUser> pig用户列表
     */
    @DeleteMapping(value = "/pigs/{id}")
    public String delectSysUser(@PathVariable(name = "id") String id) {
        pigRepository.deleteById(id);
        return "id = " + id + "已删除！";
    }

    /**
     * 新增 pig
     *
     * @param name 姓名
     * @return pig 对象
     */
    @PostMapping(value = "/pig")
    public Pig addPig(@RequestParam("name") String name, @RequestParam(name = "phone") Integer phone,
                         @RequestParam("email") String email, @RequestParam("password") String passsword,
                         @RequestParam("sex") String sex, @RequestParam("age") int age,
                         @RequestParam("age") String address) {
        Pig pig = new Pig();
        pig.setName(name);
        pig.setPhoneNumber(phone);
        pig.seteMail(email);
        pig.setPassword(passsword);
        pig.setSex(sex);
        pig.setAge(age);
        pig.setAddress(address);
        return pigRepository.save(pig);
    }

}
