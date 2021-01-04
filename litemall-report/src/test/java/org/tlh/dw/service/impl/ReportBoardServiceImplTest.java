package org.tlh.dw.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tlh.dw.service.ReportBoardService;
import org.tlh.dw.vo.RegionOrderVo;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 离歌笑
 * @desc
 * @date 2021-01-04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ReportBoardServiceImplTest {

    @Autowired
    private ReportBoardService reportBoardService;

    @Test
    public void regionOrder() {
        List<RegionOrderVo> regionOrderVos = this.reportBoardService.regionOrder(null, 1,"重庆");
        System.out.println(regionOrderVos.size());
    }
}