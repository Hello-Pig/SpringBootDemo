package net.hellopig.app.repository;

import net.hellopig.app.domain.Pig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description：
 *
 * @author CC
 * <p>
 * {@link Pig}{@link Repository}
 * @date 2018/7/31 22:40    PigRepository.java
 */

@Repository
public interface PigRepository extends JpaRepository<Pig, String> {

    /**
     * 通过年龄查询
     * @param age 年龄
     * @return 该年龄的所有 Pig
     */
    public List<Pig> findPigsByAge(int age);

}
