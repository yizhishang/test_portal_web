package cn.vpclub.pinganquan.mobile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.vpclub.pinganquan.mobile.domain.OfficialInfo;

/**
 * Created by Administrator on 2016/5/3.
 */
@Repository
public interface OfficialInfoRepository extends JpaRepository<OfficialInfo, String> {

    /**
     * 活动id和文案类型查找
     *
     * @return
     */
    List<OfficialInfo> findByActivityId(String activityId);

}

