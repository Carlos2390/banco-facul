package com.cgp.banco.dao.impl;

import com.cgp.banco.dao.LogDAO;
import com.cgp.banco.model.Log;
import org.springframework.stereotype.Repository;

@Repository
public class LogDAOImpl extends GenericDAOImpl<Log> implements LogDAO {

    public LogDAOImpl() {
        super(Log.class);
    }
}