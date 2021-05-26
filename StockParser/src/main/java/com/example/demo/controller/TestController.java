package com.example.demo.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.VO.JsonVO;
import com.example.demo.VO.StockVO;
import com.example.demo.mapper.StockMapper;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class TestController {
	
	@Autowired
    private StockMapper stockMapper;	

	@RequestMapping("/")
	public String index() {				
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream input;
		try {
			input = new FileInputStream("src/stock.json");
			JsonVO[] jsonVO_list = objectMapper.readValue(input, JsonVO[].class);
			ArrayList<StockVO> stockVO_list = new ArrayList();
			StockVO stockVO=null;
			for(JsonVO jsonVO : jsonVO_list) {
				stockVO = new StockVO();				
				stockVO.setStock_number(Integer.toString(jsonVO.getNo()));
				stockVO.setStock_name(jsonVO.getName());
				stockVO.setStock_mode(jsonVO.getMode());
				stockVO_list.add(stockVO);
			}
			stockMapper.insertStock(stockVO_list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "hello world";
	}
}
