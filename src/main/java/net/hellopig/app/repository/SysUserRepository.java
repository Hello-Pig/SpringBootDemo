package net.hellopig.app.repository;

import net.hellopig.app.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description：
 *
 * @author CC
 * <p>
 * {@link SysUser}{@link Repository}
 * @date 2018/7/31 22:40    SysUserRepository.java
 */

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, String> {

}
