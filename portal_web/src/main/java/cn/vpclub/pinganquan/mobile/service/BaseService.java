package cn.vpclub.pinganquan.mobile.service;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;

public class BaseService
{
    @Autowired
    protected EntityManagerFactory emf;
}
