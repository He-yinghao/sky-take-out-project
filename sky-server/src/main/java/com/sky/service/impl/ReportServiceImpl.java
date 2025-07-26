package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final WorkSpaceService workSpaceService;

    public ReportServiceImpl(OrderMapper orderMapper, UserMapper userMapper, WorkSpaceService workSpaceService) {
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
        this.workSpaceService = workSpaceService;
    }

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 当前集合存放begin到end内每天的日期
        List<LocalDate> dateList = getLocalDates(begin, end);
        // 封装turnoverList集合，用于存放日期对应的营业额数据
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 查询date日期对应的营业额数据，营业额是指：状态为“已完成”的订单金额合计   使用的是下单时间，也可使用结账时间
            // 创建开始时间，设定为指定日期的零点
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            // 创建结束时间，设定为指定日期的23:59:59.999
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // select sum(amount) from orders where status = 5 and order_time>beginTime and order_time<endTime
            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnOver = orderMapper.sumTurnoverByMap(map);
            System.out.println(turnOver);
            turnoverList.add(turnOver == null ? 0.0 : turnOver);
        }
        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 当前集合存放begin到end内每天的日期
        List<LocalDate> dateList = getLocalDates(begin, end);
        // 计算新增用户列表与总用户列表
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // select count(id) from user where create_time >= begin and create_time < end
            // select count(id) from user where create_time < end
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("end", endTime);
            totalUserList.add(userMapper.countByMap(map));
            map.put("begin", beginTime);
            newUserList.add(userMapper.countByMap(map));
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getLocalDates(begin, end);
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        // 遍历dateList集合，获取每天的有效订单数和订单总数
        for (LocalDate date : dateList) {
            // 查询每天的订单总数 select count(id) from orders where order_time >= begin and order_time < end
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            orderCountList.add(getOrderCount(beginTime, endTime, null));
            // 查询有效订单数 select count(id) from orders status = 5 and where order_time >= begin and order_time < end and status = 5
            validOrderCountList.add(getOrderCount(beginTime, endTime, Orders.COMPLETED));
        }
        // 计算时间区间内的订单总数
        int totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        // 计算时间区间内的有效订单数
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        // 计算订单完成率
        double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).toList();
        String nameList = StringUtils.join(names, ",");
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).toList();
        String numberList = StringUtils.join(numbers, ",");
        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    @Override
    public void exportBusinessData(HttpServletResponse response) {
        // 1.查询数据库获取营业数据---查询最近30天的数据
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        // 查询概览数据
        BusinessDataVO businessDataVO = workSpaceService.getBusinessData(
                LocalDateTime.of(begin, LocalTime.MIN),
                LocalDateTime.of(end, LocalTime.MAX));

        // 2.通过Approach POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        // 基于模板文件创建一个新的Excel文件
        try {
            XSSFWorkbook excel = new XSSFWorkbook(Objects.requireNonNull(in));
            // 获取报表文件的Sheet页
            XSSFSheet sheet1 = excel.getSheet("Sheet1");
            // 填充数据--时间
            sheet1.getRow(1).getCell(1).setCellValue("时间：" + begin + "至" + end);
            // 填充数据--概览数据：30天所有数据的总和
            XSSFRow row = sheet1.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());
            row = sheet1.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());
            // 填充数据--明细数据：每天对应的概览数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                BusinessDataVO businessData = workSpaceService.getBusinessData(
                        LocalDateTime.of(date, LocalTime.MIN),
                        LocalDateTime.of(date, LocalTime.MAX)
                );
                // 获取当前行
                row = sheet1.getRow(7 + i);
                // 日期列（保持原逻辑）
                row.getCell(1).setCellValue(date.toString());
                // 数值列处理（空值设为0）
                row.getCell(2).setCellValue(businessData.getTurnover() != null ? businessData.getTurnover() : 0.0);
                row.getCell(3).setCellValue(businessData.getValidOrderCount() != null ? businessData.getValidOrderCount() : 0);
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate() != null ? businessData.getOrderCompletionRate() : 0.0);
                row.getCell(5).setCellValue(businessData.getUnitPrice() != null ? businessData.getUnitPrice() : 0.0);
                row.getCell(6).setCellValue(businessData.getNewUsers() != null ? businessData.getNewUsers() : 0);
            }
            // 3.通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            // 关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<LocalDate> getLocalDates(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            // 日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }

    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return orderMapper.countOrdersByMap(map);
    }
}
