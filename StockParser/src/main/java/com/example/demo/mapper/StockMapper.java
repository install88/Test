package com.example.demo.mapper;

import java.util.List;

import com.example.demo.VO.StockVO;


public interface StockMapper{
    //insert訊息至資料庫
    public void insertStock(List<StockVO> stockList);
         
}
