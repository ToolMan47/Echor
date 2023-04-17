package com.toolman.linebot.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.toolman.linebot.dao.StoreDao;

@RestController
public class IndexController {
    
    @Autowired
    private StoreDao store;

    @GetMapping("/")
    public String getIndex() {
	
	
	return "Hi " + store.findByName("XXX").getName();
    }

    @PostMapping("/")
    public String postIndex() {
	return "Hi";
    }

    // 可使用預設路徑取得 /resource/static 底下的靜態檔案
    @GetMapping(value = "/image/{jpg}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String jpg) throws IOException {
	File file = ResourceUtils.getFile("classpath:static/store/" + jpg);

	if (file.exists()) {

	    System.out.println(file.toURI());

	    return IOUtils.toByteArray(FileUtils.openInputStream(file));
	}

	return null;
    }

}
