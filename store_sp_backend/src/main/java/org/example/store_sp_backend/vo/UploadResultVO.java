package org.example.store_sp_backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadResultVO {
    private String fileName;
    private String url;
}
