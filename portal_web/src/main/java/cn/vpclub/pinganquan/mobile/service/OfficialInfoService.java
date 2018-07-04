package cn.vpclub.pinganquan.mobile.service;

import cn.vpclub.pinganquan.mobile.domain.OfficialInfo;
import cn.vpclub.pinganquan.mobile.repository.OfficialInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */
@Service
public class OfficialInfoService extends BaseService{

    @Autowired
    private OfficialInfoRepository officialInfoRepository;

    public OfficialInfo add(OfficialInfo entity) {
        return officialInfoRepository.save(entity);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "OfficialInfoService", keyGenerator = "wiselyKeyGenerator")
    public List<OfficialInfo> findByActivityId(String activityId) {
        return officialInfoRepository.findByActivityId(activityId);
    }


    @Transactional
    public void updateOfficialInfoById(String id)
    {
        OfficialInfo officialInfo = officialInfoRepository.findOne(id);
        officialInfo.setOfficialCo(officialInfo.getOfficialCo() + "888");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(officialInfo);
        em.flush();
        em.getTransaction().commit();
        emf.close();
    }
}
