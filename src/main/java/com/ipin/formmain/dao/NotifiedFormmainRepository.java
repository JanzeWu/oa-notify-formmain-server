package com.ipin.formmain.dao;

import com.ipin.formmain.bean.NotifiedFormmain;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by janze on 4/23/18.
 */
public interface NotifiedFormmainRepository extends MongoRepository<NotifiedFormmain, String > {

    Lock LOCK = new ReentrantLock();


    NotifiedFormmain findByFormmainId(long formmainId);

    void deleteByEndDateLessThan(long endDate);

    List<NotifiedFormmain> findByEndDateGreaterThan(long endDate);



}
