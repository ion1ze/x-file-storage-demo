package org.example.demo.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author admin
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/infra")
public class InfraFileController {
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam MultipartFile file) {
        String date = DateUtil.format(LocalDateTime.now(), "yyyy/MM/dd");

        try {
            String checksum = SecureUtil.md5().digestHex(file.getBytes());
            log.info("MultipartFile Md5: {}", checksum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileInfo fileInfo = this.fileStorageService.of(file)
                .setHashCalculatorMd5()
                .setPath(date)
                .upload();

        log.info("FileInfo Md5: {}", fileInfo.getHashInfo().getMd5());
        return  "上传成功";
    }
}
