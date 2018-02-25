package com.hendisantika.springbootmysqlreport.repository;

import com.hendisantika.springbootmysqlreport.domain.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-mysql-report
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 25/02/18
 * Time: 19.11
 * To change this template use File | Settings | File Templates.
 */
public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findByBrand(@Param("brand") String brand);

    List<Car> findByModel(@Param("model") String model);

    List<Car> findByFuel(@Param("fuel") String fuel);

    List<Car> findByColor(@Param("color") String color);

    List<Car> findByYear(@Param("year") int year);
}
