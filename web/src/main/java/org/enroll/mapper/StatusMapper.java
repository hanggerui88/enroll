package org.enroll.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface StatusMapper {

    void addLog(@Param("content") String content, @Param("status") int status);

    Integer getStatus();

}
